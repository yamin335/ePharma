package com.rtchubs.pharmaerp.models.product_stock

import com.rtchubs.pharmaerp.models.Attribute
import com.rtchubs.pharmaerp.models.Product

data class StockProductsResponse(val code: String?, val status: String?, val message: String?, val data: StockProductsResponseData?)

data class StockProductsResponseData(val productDetails: StockProductDetailsData?, val stockwithdetails: StockWithDetails?)

data class StockProductDetailsData(val current_page: Int?, val data: List<StockProductData>?,
                          val first_page_url: String?, val from: Int?, val last_page: Int?,
                          val last_page_url: String?, val next_page_url: String?, val path: String?,
                          val per_page: Int?, val prev_page_url: String?, val to: Int?, val total: Int?)

data class StockProductData(val id: Int?, val product_id: Int?, val name: String?, val qty: Int?, val mrp: Int?,
                            val buying_price: Int?, val selling_price: Int?, val lot: String?, val branch_id: Int?,
                            val warehouse_id: Any?, val expire_date: String?, val created_at: String?,
                            val updated_at: String?, val merchant_id: Int?, val is_opening_stock: Int?,
                            val product: Product?, val stock_barcode: List<StockBarcode>?)

data class StockBarcode(val id: Int?, val product_id: Int?, val product_detail_id: Int?,
                        val merchant_id: Int?, val status: Int?, val salse_date: String?,
                        val receive_date: String?, val barcode: String?, val created_at: String?,
                        val updated_at: String?, val image: String?, val selling_price: Int?,
                        val buying_price: Int?, val attributes: List<Attribute>?)

data class StockWithDetails(val current_page: Int?, val data: List<StockProductWithDetails>?, val first_page_url: String?,
                            val from: Int?, val last_page: Int?, val last_page_url: String?,
                            val next_page_url: String?, val path: String?, val per_page: Int?,
                            val prev_page_url: String?, val to: Int?, val total: Int?)

data class StockProductWithDetails(val product: Product?, val details: List<StockProductDetail>?, val product_id: Int?,
                                   val qty: Int?, var isExpanded: Boolean = false)

data class StockProductDetail(val lot: String?, val qty: Int?, val is_opening_stock: Int?, val id: Int?, val created_at: String?)

data class StockProductsDetails(val id: Int?, val product_id: Int?, val product_detail_id: Int?, 
                          val merchant_id: Int?, val status: Int?, val salse_date: String?,
                          val receive_date: String?, val barcode: String?, val created_at: String?, 
                          val updated_at: String?, val image: String?, val selling_price: Int?,
                          val buying_price: Int?, val attributes: List<Attribute>?)

data class ReceiveProductImageUploadResponse(val code: Int?, val data: ReceiveProductImageUploadResponseData?,
                                             val files: List<Any>?, val path: String?)

data class ReceiveProductImageUploadResponseData(val filelists: List<String>?)

// result generated from /json

data class ReceiveProductResponse(val code: Int?, val status: String?, val message: String?, val data: ReceiveProductResponseData?,
                val barcode: Long?, val detail: ReceiveProductDetailData?, val pd: ReceiveProductPd?)

data class ReceiveProductResponseData(val id: Int?, val product_id: Int?, val name: String?, val qty: Int?,
                val mrp: Int?, val buying_price: String?, val selling_price: String?,
                val lot: String?, val branch_id: Int?, val warehouse_id: Any?,
                val expire_date: String?, val created_at: String?, val updated_at: String?,
                val merchant_id: Int?, val is_opening_stock: Int?)

data class ReceiveProductDetailData(val id: Int?, val purchase_id: Int?, val product_id: Int?,
                  val description: String?, val linkTo: Any?, val unitType: String?,
                  val qty: Int?, val total_received: Int?, val return_qty: Int?,
                  val unit_price: Int?, val sub_total: Int?, val taxType: String?,
                  val taxTypeValue: Int?, val discountType: String?, val discountTypeValue: Int?,
                  val type: Any?, val created_at: String?, val updated_at: String?, val merchant_id: Int?)

data class ReceiveProductPd(val id: Int?, val merchant_id: Int?, val vendor_id: Int?, val salesman_id: Any?,
              val branch_id: Int?, val date: String?, val YourReference: String?, val OurReference: String?,
              val status: String?, val amount_are: Any?, val customer_note: Any?, val cart_total: Int?,
              val discount_amount: Any?, val discount: Any?, val discount_type: String?, val file_name: String?,
              val tax: Int?, val tax_amount: Any?, val vat: Int?, val vat_amount: Any?,
              val tax_type_total: Double?, val discount_total: Double?, val grand_total: Double?,
              val paid_amount: Int?, val due_amount: Int?, val created_at: String?,
              val updated_at: String?, val received_status: Int?)

data class ReceiveProductStoreBody(
    var id: Int?, var purchase_id: Int?, var product_id: Int?,
    var description: String?, val linkTo: Any?, var unitType: String?,
    var qty: Int?, var total_received: Int?, val return_qty: Int?,
    var unit_price: Int?, var sub_total: Int?, val taxType: String?,
    val taxTypeValue: Int?, val discountType: String?, val discountTypeValue: Int?,
    val type: Any?, val created_at: String?, val updated_at: String?,
    var merchant_id: Int?, var product: Product?, var buying_price: String?,
    var selling_price: String?, var expire_date: String?, val receive: Int?,
    var attributes: List<Attribute>?, var images: String?)
