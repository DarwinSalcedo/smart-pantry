package com.smart.pantry.ui.shopping_list.list

import com.smart.pantry.R
import com.smart.pantry.utils.BaseRecyclerViewAdapter

class ShoppingListAdapter(callBack: (dataItem: ShoppingListDataItem) -> Unit) :
    BaseRecyclerViewAdapter<ShoppingListDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.item_shopping_list
}