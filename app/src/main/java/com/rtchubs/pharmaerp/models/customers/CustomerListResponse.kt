package com.rtchubs.pharmaerp.models.customers
import java.io.Serializable

data class AddCustomerResponse(val code: String?, val status: String?, val message: String?, val data: AddCustomerResponseData?)
data class AddCustomerResponseData(val customer: Customer?)

data class CustomerListResponse(val code: String?, val status: String?, val message: String?, val data: CustomerListData?)

data class CustomerList(val current_page: Int?, val data: List<Customer>?, val first_page_url: String?,
                        val from: Int?, val last_page: Int?, val last_page_url: String?,
                        val next_page_url: String?, val path: String?, val per_page: Int?, val prev_page_url: Any?,
                        val to: Int?, val total: Int?)

data class CustomerListData(val customer: CustomerList?)

data class Customer(val id: Int?, val name: String?, val address: String?, val city: String?, val state: String?,
                    val zipcode: String?, val phone: String?, val email: String?, val giveDiscount: String?,
                    val discountBy: String?, val discountAmount: Int?, val contact_person: String?,
                    val customer_type_id: Int?, val created_at: String?, val updated_at: String?, val type: CustomerType?): Serializable

data class CustomerType(val id: Int?, val name: String?, val description: String?, val created_at: String?, val updated_at: String?)