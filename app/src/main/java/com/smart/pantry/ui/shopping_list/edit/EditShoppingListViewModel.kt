package com.smart.pantry.ui.shopping_list.edit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.R
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.base.NavigationCommand
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.ShoppingList
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import kotlinx.coroutines.launch

class EditShoppingListViewModel(val app: Application, val dataSource: ShoppingListDataSource) :
    BaseViewModel(app) {
    private val _shoppingList = MutableLiveData<ShoppingList>()
    val shoppingList: LiveData<ShoppingList> = _shoppingList

    fun editShoppingList(_name: String, _description: String) {
        _shoppingList.value?.apply {
            this.name = _name
            this.description = _description
        }
        if (validateEnteredData(_shoppingList.value?.name!!))
            viewModelScope.launch {
                dataSource.saveShoppingList(
                    _shoppingList?.value!!
                )
                navigationCommand.value = NavigationCommand.Back
            }
    }

    private fun validateEnteredData(name: String): Boolean {
        if (name.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.error_enter_title
            return false
        }
        return true
    }

    fun start(value: ShoppingListDataItem) {
        showLoading.value = true
        viewModelScope.launch {
            val result = dataSource.getShoppingList(value.id)
            showLoading.value = false
            when(result){
             is Result.Error -> showSnackBarInt.postValue( R.string.error_load_data)
             is Result.Success ->  _shoppingList.postValue(result.data)
         }
        }

    }
}