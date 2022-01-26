package com.smart.pantry.ui.product

import java.io.Serializable
import java.util.*


data class ProductDataItem(
    var name: String?,
    val id: String = UUID.randomUUID().toString()
) : Serializable {
    fun clone() = ProductDataItem(
        name = this.name,
        id = this.id
    )
}