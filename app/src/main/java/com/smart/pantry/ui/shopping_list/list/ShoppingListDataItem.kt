package com.smart.pantry.ui.shopping_list.list

import java.io.Serializable
import java.util.*


/**
 * data class acts as a data mapper between the DB and the UI
 */
data class ShoppingListDataItem(
    var name: String?,
    var description: String?,
    var isCompleted: Boolean?,
    var creationDate: Date?,
    val id: String = UUID.randomUUID().toString()
) : Serializable {
    fun clone() = ShoppingListDataItem(
        name = this.name,
        description = this.description,
        isCompleted = this.isCompleted,
        creationDate = this.creationDate,
        id = this.id
    )
}