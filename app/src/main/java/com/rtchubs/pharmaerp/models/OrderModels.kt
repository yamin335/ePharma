package com.rtchubs.pharmaerp.models

import java.io.Serializable

data class Order(val customer_id: Int?, val merchant_id: Int?, val purchase_number: String?,
                 val invoice_number: String?, val date_of_issue: String?, val amount_are: String?,
                 val customer_note: String?, val sub_total: Int?, val tax_type_total: Int?,
                 val discount_total: Int?, val total: Int?, val amount_paid: Int?,
                 val amount_due: Int?, val list: List<OrderItem>?): Serializable

data class OrderItem(val product_id: Int?, val description: String?, val unitType: String?,
                     val unitValue: Int?, val unitPrice: Int?, val taxType: Int?,
                     val taxTypeValue: String?, val discount: Int?, val discountAmount: String?,
                     val amount: Int?, val product: OrderProduct?, val vatAndTax: Any?): Serializable

data class OrderProduct(val id: Int?, val name: String?, val barcode: String?,
                        val description: String?, val buying_price: Int?, val selling_price: Int?,
                        val mrp: Int?, val expired_date: String?, val thumbnail: String?,
                        val product_image1: String?, val product_image2: String?,
                        val product_image3: String?, val product_image4: String?,
                        val product_image5: String?, val category_id: Int?, val merchant_id: Int?,
                        val created_at: String?, val updated_at: String?,
                        val merchant: OrderMerchant?, val category: ProductCategory?): Serializable

data class OrderMerchant(val id: Int?, val name: String?, val user_name: String?,
                         val password: String?, val shop_name: String?, val mobile: String?,
                         val lat: Double?, val long: Double?, val whatsApp: String?,
                         val email: String?, val address: String?, val shop_address: String?,
                         val shop_logo: String?, val thumbnail: String?, val isActive: Int?,
                         val shopping_mall_id: Int?, val shopping_mall_level_id: Int?,
                         val created_at: String?, val updated_at: String?): Serializable