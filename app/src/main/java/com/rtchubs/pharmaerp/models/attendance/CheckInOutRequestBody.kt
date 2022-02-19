package com.rtchubs.pharmaerp.models.attendance

data class CheckInOutRequestBody(val employee_id: String?, val inout: String?, val lng: String?,
                                 val lat: String?, val type: String?, val note: String?)