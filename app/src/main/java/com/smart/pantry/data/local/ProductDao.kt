package com.smart.pantry.data.local

import androidx.room.*
import com.smart.pantry.data.dto.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM product ")
    fun getProducts(): List<Product>


    @Query("SELECT * FROM product where product_id = :id")
    fun getProductById(id: String): Product?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product: Product)


    @Query("DELETE FROM product")
    fun deleteAllProducts()

}