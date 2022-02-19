package com.rtchubs.pharmaerp.models.login

//data class LoginResponse(val success: Boolean?, val response: Merchant?)

data class Merchant(val id: Int?, val name: String?, val user_name: String?,
                             val password: String?, val shop_name: String?, val mobile: String?,
                             val lat: Double?, val long: Double?, val whatsApp: String?,
                             val email: String?, val address: String?, val shop_address: String?,
                             val shop_logo: String?, val thumbnail: String?, val isActive: Int?,
                             val shopping_mall_id: Int?, val shopping_mall_level_id: Int?,
                             val created_at: String?, val updated_at: String?, val type: String?,
                             val token: String?)

data class LoginResponse(val success: Boolean?, val response: PharmaUser?)

data class PharmaUser(val id: Int?, val name: String?, val employee_id: String?,
                    val token: String?, val email: String?, val email_verified_at: Any?,
                    val branch_id: Int?, val created_at: String?, val updated_at: String?)
