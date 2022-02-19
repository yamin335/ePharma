package com.rtchubs.pharmaerp.models.products

import java.io.Serializable

data class PharmaProductListResponse(val code: String?, val status: String?, val message: String?,
                                     val data: PharmaProductListData?, val extra: Extra?)

data class PharmaProductListData(val products: ProductsList?)

data class ProductsList(val current_page: Int?, val first_page_url: String?, val next_page_url: String?,
                        val last_page: Int?, val last_page_url: String?, val total: Int?, val per_page: Int?,
                        val prev_page_url: Any?, val count: Int?, val product: List<PharmaProduct>?)

data class PharmaProduct(val id: Int?, val name: String?, val barcode: String?, val description: String?,
                         val productDescription: String?, val buying_price: Double?, val total_buying_price: Double?,
                         val selling_price: Int?, val mrp: Int?, val expired_date: String?, val re_order_level: Int?,
                         val available_qty: Int?, val actual_qty: Int?, val product_type: PharmaProductType?,
                         val product_type_id: Int?, val category: Category?, val unit_of_measure: UnitOfMeasure?,
                         val unit_of_measure_id: Int?, val branch: Branch?, val branch_id: Int?, val currency: Any?,
                         val currency_id: Any?, val warehouse: PharmaWarehouse?, val warehouse_id: Int?,
                         val details: List<PharmaProductDetails>?):Serializable

data class PharmaProductType(val id: Int?, val name: String?, val description: String?,
                             val created_at: String?, val updated_at: String?)

data class UnitOfMeasure(val id: Int?, val name: String?, val description: String?,
                         val is_deleted: Int?, val created_at: String?, val updated_at: String?)

data class PharmaWarehouse(val id: Int?, val name: String?, val description: String?,
                           val phone: String?, val mobile: String?, val address_1: String?,
                           val address_2: String?, val city: String?, val state: String?, val pin: Int?,
                           val is_deleted: Int?, val created_at: String?, val updated_at: String?)


data class Branch(val id: Int?, val name: String?, val description: String?, val address: String?,
                  val city: String?, val state: String?, val zipcode: String?, val phone: String?,
                  val email: String?, val contact_person: String?, val is_deleted: Int?, 
                  val created_at: String?, val updated_at: String?)

data class Category(val id: Int?, val name: String?, val description: String?, val is_deleted: Int?,
                    val created_at: String?, val updated_at: String?)



data class PharmaProductDetails(val id: Int?, val product_id: Int?, val name: String?, val qty: Int?,
                             val mrp: Int?, val buying_price: Double?, val selling_price: Int?,
                             val lot: String?, val branch_id: Int?, val warehouse_id: Int?,
                             val expire_date: String?, val created_at: String?, val updated_at: String?)

data class Extra(val total_buying_price: Double?, val total_selling_price: Double?, val total_mrp: Int?)