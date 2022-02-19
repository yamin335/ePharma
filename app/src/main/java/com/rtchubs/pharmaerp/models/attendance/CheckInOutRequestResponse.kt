package com.rtchubs.pharmaerp.models.attendance

data class CheckInOutRequestResponse(val code: Int?, val message: String?, val data: CheckInOutRequestResponseData?)

data class CheckInOutRequestResponseData(val lat: String?, val lng: String?, val note: String?, val type: String?,
                val employee_id: String?, val out_time: String?, val in_time: String?,
                val updated_at: String?, val created_at: String?, val id: Int?)
