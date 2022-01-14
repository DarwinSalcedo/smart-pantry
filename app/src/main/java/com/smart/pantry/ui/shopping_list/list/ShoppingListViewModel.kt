package com.smart.pantry.ui.shopping_list.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.ShoppingList
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    app: Application,
    private val dataSource: ShoppingListDataSource
) : BaseViewModel(app) {

    val shoppingListResult = MutableLiveData<List<ShoppingListDataItem>>()

    fun loadShoppingList() {
        showLoading.value = true

        viewModelScope.launch {
            val result = dataSource.getShoppingList()
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<ShoppingListDataItem>()
                    dataList.addAll((result.data as List<ShoppingList>).map { data ->
                        ShoppingListDataItem(
                            data.name,
                            data.description,
                            data.isCompleted,
                            data.creationDate,
                            data.id
                        )
                    })
                    shoppingListResult.value = dataList
                }
                is Result.Error -> showSnackBar.value = result.message
            }

            invalidateShowNoData()
        }
    }


    private fun invalidateShowNoData() {
        showNoData.value = shoppingListResult.value == null || shoppingListResult.value!!.isEmpty()
    }
}