package com.rtchubs.pharmaerp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class AllProductResponse(val code: Int?, val status: String?, val message: String?, val data: List<Product>?)

data class ProductCategory(val id: Int?, val name: String?, val description: String?, val merchant_id: Int?,
                           val created_at: String?, val updated_at: String?, val attributes: List<Attribute>?) : Serializable

@Entity(tableName = "favorite")
data class Product(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String?,
    val barcode: String?,
    val description: String?,
    val buying_price: Double?,
    val selling_price: Double?,
    val mrp: Double?,
    val expired_date: String?,
    val thumbnail: String?,
    val product_image1: String?,
    val product_image2: String?,
    val product_image3: String?,
    val product_image4: String?,
    val product_image5: String?,
    val category_id: Int?,
    val merchant_id: Int?,
    var available_qty: Int? = 1,
    var orderQuantity: Int? = 1,
    val created_at: String?,
    val updated_at: String?,
    val category: ProductCategory?,
    val attributes: List<Attribute>?): Serializable

data class Attribute(val id: Int?, val product_category_id: Int?, val merchant_id: Int?,
                     val attribute_id: Int?, val attribute_name: String?, val stock_barcode_id: Int?,
                     var attrribute_value: String?, val created_at: String?, val updated_at: String?): Serializable

data class ProductDetails(val id: Int?, val product_id: Int?, val name: String?,
                          val qty: Int?, val mrp: Int?, val buying_price: Int?,
                          val selling_price: Int?, val lot: String?, val branch_id: Any?,
                          val warehouse_id: Any?, val expire_date: String?,
                          val created_at: String?, val updated_at: String?,
                          val merchant_id: Int?, val is_opening_stock: Int?)