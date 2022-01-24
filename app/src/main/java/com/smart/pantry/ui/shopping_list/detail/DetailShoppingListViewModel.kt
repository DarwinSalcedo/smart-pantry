package com.smart.pantry.ui.shopping_list.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.base.NavigationCommand
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import kotlinx.coroutines.launch

class DetailShoppingListViewModel(val app: Application, val dataSource: ShoppingListDataSource) :
    BaseViewModel(app) {

    private val _shoppingList = MutableLiveData<ShoppingListDataItem>()
    val shoppingList: LiveData<ShoppingListDataItem> = _shoppingList

    fun start(value: ShoppingListDataItem) {
        showLoading.value = true
        _shoppingList.value = value
        showLoading.value = false
    }

    fun deleteShoppingList() = viewModelScope.launch {
        dataSource.deleteAllShoppingList()
        navigationCommand.postValue(NavigationCommand.Back)
        showSnackBar.postValue("It's been deleted")
    }

}
