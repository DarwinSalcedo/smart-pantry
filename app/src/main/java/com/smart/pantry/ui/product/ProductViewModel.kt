package com.smart.pantry.ui.product

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smart.pantry.base.BaseViewModel
import com.smart.pantry.data.ProductDataSource
import com.smart.pantry.data.dto.Product
import com.smart.pantry.data.dto.Result
import com.smart.pantry.data.dto.ShoppingList
import kotlinx.coroutines.launch

class ProductViewModel(
    app: Application,
    private val dataSource: ProductDataSource
) : BaseViewModel(app) {

    val listResult = MutableLiveData<List<ProductDataItem>>()


    fun loadProducts() {
        showLoading.value = true

        viewModelScope.launch {
            val result = dataSource.getProducts()
            showLoading.postValue(false)
            when (result) {
                is Result.Success<*> -> {
                    val dataList = ArrayList<ProductDataItem>()
                    dataList.addAll((result.data as List<Product>).map { data ->
                        ProductDataItem(
                            data.name,
                            data.productId
                        )
                    })
                    listResult.value = dataList
                }
                is Result.Error -> showSnackBar.value = result.message
            }

            invalidateShowNoData()
        }
    }


    private fun invalidateShowNoData() {
        showNoData.value = listResult.value == null || listResult.value!!.isEmpty()
    }
}