package com.rtchubs.pharmaerp.models

data class OfferProductMerchant(val id: Int?, val name: String?, val user_name: String?,
                    val shop_name: String?, val mobile: String?, val lat: Double?,
                    val long: Double?, val whatsApp: String?, val email: String?,
                    val address: String?, val shop_address: String?, val shop_logo: String?,
                    val thumbnail: String?, val isActive: Int?, val shopping_mall_id: Int?,
                    val shopping_mall_level_id: Int?, val rent_date: String?,
                    val monthly_rent: Any?, val advance_pament: Any?, val advance_payment_date: String?,
                    val agreement_duration: Any?, val created_at: String?, val updated_at: String?,
                    val type: String?, val offer_discount_type: Any?, val offer_discount_percent: Any?,
                    val offer_banner: String?, val offer_valid_from: String?, val offer_valid_to: String?)

// result generated from /json

data class OfferProductListResponse(val code: Int?, val status: String?, val message: String?, val data: OfferProductResponseData?, val merchant: OfferProductMerchant?)

data class OfferProductResponseData(val current_page: Int?, val data: List<OfferItem>?, val first_page_url: String?,
                                    val from: Int?, val last_page: Int?, val last_page_url: String?,
                                    val next_page_url: String?, val path: String?, val per_page: Int?,
                                    val prev_page_url: String?, val to: Int?, val total: Int?)

data class OfferItem(val id: Int?, val created_at: String?, val updated_at: String?,
                     val product_title: String?, val product_descriptin: String?,
                     val product_thumbnail: String?, val offer_description: String?,
                     val discount_percent: Int?, val valid_from: String?, val valid_to: String?,
                     val shopping_mall_id: Int?, val shopping_mall_level_id: Int?,
                     val merchant_id: Int?, val product_id: Int?, val is_active: Int?)


data class OfferAddResponse(val code: Int?, val status: String?, val message: String?,
                            val data: OfferAddResponseData?, val merchant: Merchant?)

data class OfferAddResponseData(val is_active: Boolean?, val updated_at: String?, val created_at: String?, val id: Int?,
                val offer_description: String?, val discount_percent: String?, val valid_from: String?,
                val valid_to: String?, val product_id: Int?, val product_thumbnail: String?,
                val product_title: String?, val product_descriptin: String?)

data class OfferStoreBody(val offer_description: String?, val discount_percent: String?, val valid_from: String?,
                val valid_to: String?, val product_id: List<Int>, val token: String?, val merchant_id: Int?)
