package com.smart.pantry.ui.shopping_list.list

import com.smart.pantry.R
import com.smart.pantry.utils.BaseRecyclerViewAdapter


//Use data binding to show the shopping List on the item
class ShoppingListAdapter(callBack: (dataItem: ShoppingListDataItem) -> Unit) :
    BaseRecyclerViewAdapter<ShoppingListDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.item_shopping_list
}