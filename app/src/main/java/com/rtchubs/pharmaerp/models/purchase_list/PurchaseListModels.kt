package com.rtchubs.pharmaerp.models.purchase_list

import com.rtchubs.pharmaerp.models.Product
import java.io.Serializable

data class PurchaseListResponse(val data: List<PurchaseListResponseData>?, val links: Links?,
                                val meta: Meta?, val total: Total?, val branches: Any?)

data class PurchaseListResponseData(val id: Int?, val merchant_id: Int?, val vendor_id: Int?,
                          val salesman_id: Any?, val branch_id: Int?, val date: String?,
                          val YourReference: String?, val OurReference: String?, val status: String?,
                          val amount_are: Any?, val customer_note: Any?, val cart_total: Double?,
                          val discount_amount: Any?, val discount: Any?, val discount_type: String?,
                          val file_name: String?, val tax: Double?, val tax_amount: Any?, val vat: Double?,
                          val vat_amount: Any?, val tax_type_total: Double?, val discount_total: Double?,
                          val grand_total: Double?, val paid_amount: Double?, val due_amount: Double?,
                          val created_at: String?, val updated_at: String?, val received_status: Int?,
                          val details: List<PurchaseListDetails>?, val vendor: Vendor?, var isExpanded: Boolean = false): Serializable

data class PurchaseListDetails(val id: Int?, val purchase_id: Int?, val product_id: Int?,
                             val description: String?, val linkTo: Any?, val unitType: String?,
                             val qty: Int?, val total_received: Double?, val return_qty: Int?,
                             val unit_price: Double?, val sub_total: Double?, val taxType: String?,
                             val taxTypeValue: Double?, val discountType: String?,
                             val discountTypeValue: Double?, val type: Any?, val created_at: String?,
                             val updated_at: String?, val merchant_id: Int?, val product: Product?): Serializable

data class Links(val first: String?, val last: String?, val prev: Any?, val next: Any?): Serializable

data class Meta(val current_page: Int?, val from: Int?, val last_page: Int?, val path: String?,
                val per_page: Int?, val to: Int?, val total: Int?): Serializable

data class Total(val amount: Double?, val vat: Double?, val discount: Double?, val total: Double?): Serializable

data class Vendor(val id: Int?, val name: String?, val address: String?, val city: String?,
                  val state: String?, val zipcode: String?, val phone: String?, val email: String?,
                  val contact_person: String?, val vendor_type_id: Int?, val merchant_id: Int?,
                  val created_at: String?, val updated_at: String?): Serializable