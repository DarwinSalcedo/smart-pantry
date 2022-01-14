package com.smart.pantry.data.dto

import androidx.room.*
import java.util.*

@Entity(tableName = "shopping_list")
data class ShoppingList(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "isCompleted") val isCompleted: Boolean = false,
    @ColumnInfo(name = "creation_date") val creationDate: Date = Date(),
    @PrimaryKey @ColumnInfo(name = "entry_id") val id: String = UUID.randomUUID().toString()
)


@Entity(tableName = "product",
    foreignKeys = [ForeignKey(entity = ShoppingList::class,
        parentColumns = arrayOf("entry_id"),
        childColumns = arrayOf("related_id"),
        onDelete = ForeignKey.CASCADE)]
)
data class Product(@PrimaryKey val petId: String,
                   @ColumnInfo(name = "name")  val name: String?,
                   @ColumnInfo(name = "isCompleted") val isCompleted: Boolean,
                   @ColumnInfo(name = "related_id")  val related_id: String)