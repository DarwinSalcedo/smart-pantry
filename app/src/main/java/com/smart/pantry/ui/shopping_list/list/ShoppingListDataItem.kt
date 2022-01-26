package com.smart.pantry.ui.shopping_list.list

import java.io.Serializable
import java.util.*


data class ShoppingListDataItem(
    var name: String?,
    var isCompleted: Boolean?,
    var creationDate: Date?,
    val id: String = UUID.randomUUID().toString()
) : Serializable {
    fun clone() = ShoppingListDataItem(
        name = this.name,
        isCompleted = this.isCompleted,
        creationDate = this.creationDate,
        id = this.id
    )
}