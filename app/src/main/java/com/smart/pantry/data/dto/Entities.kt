package com.smart.pantry.data.dto

import androidx.room.*
import java.util.*

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
    @ColumnInfo(name = "creation_date") val creationDate: Date = Date(),
    @PrimaryKey @ColumnInfo(name = "shopping_list_id") val id: String = UUID.randomUUID().toString(),
)


@Entity(tableName = "product")
data class Product(@PrimaryKey @ColumnInfo(name = "product_id") val productId: String = UUID.randomUUID().toString(),
                   @ColumnInfo(name = "name")  val name: String?,
                   @ColumnInfo(name = "code")  val code: String,
                   @ColumnInfo(name = "isFavorite") val isFav: Boolean = false)


@Entity(tableName = "item")
data class Item(@PrimaryKey @ColumnInfo(name = "item_id") val itemId: String = UUID.randomUUID().toString(),
                   @ColumnInfo(name = "is_buyed") val isBought: Boolean = false,
                   @ColumnInfo(name = "product_creator_id")  val productCreatorId: String,
                 @ColumnInfo(name = "shopping_list_creator_id")  val shoppingListCreatorId: String)

data class ProductAndItem(
    @Embedded val product: Product,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "product_creator_id"
    )
    val item: Item
)

data class ShoppingListWithItem(
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "shopping_list_id",
        entityColumn = "shopping_list_creator_id"
    )
    val products: List<Item>
)

data class ShoppingListWithProduct(
     val shoppingList: ShoppingList,
     val products: List<ProductAndItem>
)
