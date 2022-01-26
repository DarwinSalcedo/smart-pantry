package com.smart.pantry.ui.shopping_list.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.base.NavigationCommand
import com.smart.pantry.data.ItemDataSource
import com.smart.pantry.data.ShoppingListDataSource
import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.ShoppingListWithProduct
import com.smart.pantry.ui.product.ProductDataItem
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import kotlinx.coroutines.launch

class DetailShoppingListViewModel(
    val app: Application,
    val dataSource: ShoppingListDataSource,
    private val itemsSource: ItemDataSource
) :
    BaseViewModel(app) {

    private val _shoppingList = MutableLiveData<ShoppingListDataItem>()
    val shoppingList: LiveData<ShoppingListDataItem> = _shoppingList

    val listResult = MutableLiveData<List<ProductDataItem>>()

    fun start(value: ShoppingListDataItem) {
        showLoading.value = true
        _shoppingList.value = value
        
        viewModelScope.launch {
            val result = itemsSource.getProductsForShoppingList(value.id)
            showLoading.postValue(false)

            when (result) {
                is Result.Success<ShoppingListWithProduct> -> {
                    val dataList = ArrayList<ProductDataItem>()

                    dataList.addAll((result.data).products.map { data ->
                        ProductDataItem(data.product.name, data.product.productId)
                    })

                    listResult.value = dataList
                }
                is Result.Error -> showSnackBar.value = result.message
            }
        }


    }

    fun deleteShoppingList() = viewModelScope.launch {
        dataSource.deleteAllShoppingList()
        navigationCommand.postValue(NavigationCommand.Back)
        showSnackBar.postValue("It's been deleted")
    }

}

