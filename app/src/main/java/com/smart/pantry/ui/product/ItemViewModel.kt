package com.smart.pantry.ui.product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.data.ItemDataSource
import com.smart.pantry.data.dto.*
import com.smart.pantry.ui.shopping_list.list.ShoppingListDataItem
import kotlinx.coroutines.launch

class ItemViewModel(
    app: Application,
    private val dataSource: ItemDataSource
) : BaseViewModel(app) {

    val listResult = MutableLiveData<List<ProductDataItem>>()
    private lateinit var _shoppingList: ShoppingListDataItem


    fun loadItems() {
        showLoading.value = true

        viewModelScope.launch {
            val result = dataSource.getProductsForShoppingList(_shoppingList.id)
            showLoading.postValue(false)
            when (result) {
                is Result.Success<ShoppingListWithProduct> -> {

                    val dataList = ArrayList<ProductDataItem>()
                    dataList.addAll(result.data.products.map { data ->
                        ProductDataItem(
                            data.product.name,
                            data.product.productId
                        )
                    })
                    listResult.value = dataList
                }
                is Result.Error -> showSnackBar.value = result.message
            }

            invalidateShowNoData()
        }
    }

    fun saveItem(productDataItem: ProductDataItem, callback: () -> Unit) {
        viewModelScope.launch {
            val item = Item(productCreatorId = productDataItem.id, shoppingListCreatorId = _shoppingList.id)
            dataSource.saveItem(item)
            callback.invoke()
        }
    }

    private fun invalidateShowNoData() {
        showNoData.value = listResult.value == null || listResult.value!!.isEmpty()
    }

    fun setShoppingList(data: ShoppingListDataItem) {
        _shoppingList = data
    }

    fun getShoppingListTitle() : String{
       return _shoppingList.name?:"No name"
    }
}