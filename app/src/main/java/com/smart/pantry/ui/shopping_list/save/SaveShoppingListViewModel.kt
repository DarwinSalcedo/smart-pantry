package com.smart.pantry.ui.shopping_list.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.R
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.dto.ShoppingList
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import com.smart.pantry.utils.Event
import com.smart.pantry.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class SaveShoppingListViewModel(val app: Application, val shoppingListDataSource: ShoppingListDataSource) :
    BaseViewModel(app) {

    val navigate: SingleLiveEvent<Boolean>  =   SingleLiveEvent()

    private val _shoppingList = MutableLiveData<ShoppingList>()

    fun getShoppingList() : ShoppingListDataItem? {
        _shoppingList.value?.let {
            return ShoppingListDataItem(
                 it.title,
                it.isCompleted,
            it.creationDate,
            it.id,
            )

        }
        return null
    }

    fun saveShoppingList(_name: String) {
        val shoppingList = ShoppingList(
            title = _name
        )
        if(validateEnteredData(shoppingList))
        viewModelScope.launch {
            shoppingListDataSource.saveShoppingList(
                shoppingList
            )
            _shoppingList.postValue(shoppingList)
            navigate.postValue(true)
        }
    }

    private fun validateEnteredData(shoppingList: ShoppingList): Boolean {
        if (shoppingList.title.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.error_enter_title
            return false
        }
        return true
    }

    fun resetNavigate() {
        navigate.value = false
    }

}