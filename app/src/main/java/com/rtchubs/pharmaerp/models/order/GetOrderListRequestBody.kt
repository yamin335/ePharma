package com.rtchubs.pharmaerp.models.order

data class GetOrderListRequestBody(val column: String?, val data: String?, val filterDate: FilterDate?, val isDue: Boolean?)

data class FilterDate(val start_date: String?, val end_date: String?)
