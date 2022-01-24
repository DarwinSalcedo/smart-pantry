package com.smart.pantry.ui.shopping_list.save

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.smart.pantry.R
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.base.NavigationCommand
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.dto.ShoppingList
import kotlinx.coroutines.launch

class SaveShoppingListViewModel(val app: Application, val dataSource: ShoppingListDataSource) :
    BaseViewModel(app) {

    fun saveShoppingList(_name: String,_description: String) {
        val shoppingList = ShoppingList(
            name = _name,
            description = _description
        )
        if(validateEnteredData(shoppingList))
        viewModelScope.launch {
            dataSource.saveShoppingList(
                shoppingList
            )
            navigationCommand.value = NavigationCommand.Back
        }
    }

    private fun validateEnteredData(shoppingList: ShoppingList): Boolean {
        if (shoppingList.name.isNullOrEmpty()) {
            showSnackBarInt.value = R.string.error_enter_title
            return false
        }
        return true
    }

}