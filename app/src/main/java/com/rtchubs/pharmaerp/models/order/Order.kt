package com.rtchubs.pharmaerp.models.order

import com.rtchubs.pharmaerp.models.customers.Customer
import java.io.Serializable

data class OrderListResponse(val code: String?, val status: String?, val message: String?, val data: OrderListData?)
data class OrderListData(val sales: Sales?, val total: Total?, val branches: List<Branch>?): Serializable
data class Sales(val current_page: Number?, val data: List<SalesData>?, val first_page_url: String?,
                 val from: Number?, val last_page: Number?, val last_page_url: String?,
                 val next_page_url: Any?, val path: String?, val per_page: Number?,
                 val prev_page_url: Any?, val to: Number?, val total: Number?): Serializable

data class Branch(val id: Number?, val name: String?, val description: String?,
                  val address: String?, val city: String?, val state: String?,
                  val zipcode: String?, val phone: String?, val email: String?,
                  val contact_person: String?, val is_deleted: Number?,
                  val created_at: String?, val updated_at: String?): Serializable

data class Customer(val id: Number?, val name: String?, val address: String?, val city: String?,
                    val state: String?, val zipcode: String?, val phone: String?, val email: String?,
                    val giveDiscount: String?, val discountBy: String?, val discountAmount: Number?,
                    val contact_person: String?, val customer_type_id: Number?, val created_at: String?,
                    val updated_at: String?): Serializable

data class SalesData(val id: Number?, val customer_id: Number?, val salesman_id: Number?, val date: String?,
                val status: String?, val YourReference: Any?, val OurReference: String?, val branch_id: Number?,
                val cart_total: Number?, val discount_amount: Number?, val discount: Number?,
                val discount_type: String?, val tax: Number?, val tax_amount: Any?, val vat: Number?,
                val vat_amount: Any?, val grand_total: Number?, val buying_total: Number?, val paid_amount: Number?,
                val due_amount: Number?, val delivered_at: Any?, val paid_at: Any?, val created_at: String?,
                val updated_at: String?, val details: List<SalesDetails>?, val customer: Customer?, val branch: Branch?): Serializable

data class SalesDetails(val id: Number?, val sale_id: Number?, val product_id: Number?, val qty: Number?,
                       val total_received: Number?, val return_qty: Number?, val unit_price: Number?,
                       val sub_total: Number?, val type: Any?, val created_at: String?,
                        val updated_at: String?, val product: OrderProduct?): Serializable

data class OrderProduct(val id: Number?, val name: String?, val barcode: String?, val description: String?,
                   val buying_price: Number?, val selling_price: Number?, val mrp: Number?,
                   val product_type_id: Number?, val expired_date: String?, val re_order_level: Number?,
                   val available_qty: Number?, val actual_qty: Number?, val category_id: Number?,
                   val unit_of_measure_id: Number?, val branch_id: Number?, val currency_id: Any?,
                   val warehouse_id: Number?, val is_deleted: Number?, val created_at: String?, val updated_at: String?): Serializable

data class Total(val amount: Number?, val vat: Number?, val discount: Number?, val total: Number?): Serializable




