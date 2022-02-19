package com.rtchubs.pharmaerp.models.order

data class OrderStoreResponse(val code: String?, val status: String?, val message: String?, val data: OrderStoreResponseData?)

data class OrderStoreResponseData(val sale: OrderStoreSale?)

data class OrderStoreSale(val customer_id: String?, val date: String?, val status: String?,
                val salesman_id: String?, val branch_id: String?, val cart_total: String?,
                val YourReference: Any?, val OurReference: String?, val discount_amount: String?,
                val discount: String?, val discount_type: String?, val tax: String?,
                val tax_amount: Any?, val vat: String?, val grand_total: String?,
                val buying_total: Int?, val paid_amount: String?, val due_amount: String?,
                val updated_at: String?, val created_at: String?, val id: Int?)