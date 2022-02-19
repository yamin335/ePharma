package com.rtchubs.pharmaerp.models.order

data class OrderTrackHistory(val id: Int, val date: String, val time: String, val action: String, val currentLocation: String, val isActive: Boolean)
