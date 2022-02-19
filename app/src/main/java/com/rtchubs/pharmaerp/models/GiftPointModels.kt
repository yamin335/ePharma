package com.rtchubs.pharmaerp.models

import java.io.Serializable

data class GiftPointRequestListResponse(val code: Int?, val message: String?, val data: GiftPointRequestListResponseData?)

data class GiftPointRequestListResponseData(val rewards: List<GiftPointRequest>?, val total_reward: Int?)

data class GiftPointRequest(val id: Int?, val created_at: String?, val updated_at: String?,
                            val customer_id: Int?, val merchant_id: Int?, val reward_setting_id: Any?,
                            val customer_mobile: String?, val reward: Int?, val remarks: String?,
                            val is_approved: Int?, val name: String?, val address: String?, 
                            val city: String?, val state: Any?, val zipcode: String?, val phone: String?,
                            val password: String?, val email: String?, val discountAmount: Int?, 
                            val contact_person: String?, val total_rewards: Int?): Serializable

data class GiftPointHistoryDetailsItem(val id: Int?, val title: String?, val description: String?, val point: Int?)

data class GiftPointStoreBody(var merchant_id: Int?, var customer_id: Int?, var remarks: String?)

data class GiftPointStoreResponse(val code: Int?, val message: String?, val data: GiftPointStoreResponseData?)

data class GiftPointRewards(val merchant_id: Int?, val customer_id: Int?, val reward: Int?,
                            val remarks: String?, val updated_at: String?, val created_at: String?, val id: Int?)

data class GiftPointStoreResponseData(val reward: String?)

data class ShopWiseGiftPointResponse(val code: Int?, val message: String?, val data: ShopWiseGiftPointData?)

data class ShopWiseGiftPointData(val rewards: List<ShopWiseGiftPointRewards>?, val total_reward: Int?)

data class ShopWiseGiftPointRewards(val id: Int?, val created_at: String?, val updated_at: String?,
                                    val customer_id: Int?, val merchant_id: Int?, val reward_setting_id: Any?,
                                    val customer_mobile: Any?, val reward: Int?, val remarks: String?, val name: String?,
                                    val user_name: String?, val password: String?, val shop_name: String?, val mobile: String?,
                                    val lat: Double?, val long: Double?, val whatsApp: String?, val email: String?,
                                    val address: String?, val shop_address: String?, val shop_logo: String?, val thumbnail: String?,
                                    val isActive: Int?, val shopping_mall_id: Int?, val shopping_mall_level_id: Int?,
                                    val rent_date: Any?, val monthly_rent: Any?, val advance_pament: Any?, val advance_payment_date: Any?,
                                    val agreement_duration: Any?, val type: String?, val offer_discount_type: Any?,
                                    val offer_discount_percent: Any?, val offer_banner: Any?, val offer_valid_from: Any?,
                                    val offer_valid_to: Any?, val total_rewards: Int?)

data class GiftPointsHistoryDetailsResponse(val code: Int?, val message: String?, val data: GiftPointsHistoryDetailsResponseData?)

data class GiftPointsHistoryDetailsResponseData(val rewards: List<GiftPointsHistoryDetailsRewards>?, val total_reward: Int?)

data class GiftPointsHistoryDetailsRewards(val id: Int?, val created_at: String?, val updated_at: String?,
                                           val customer_id: Int?, val merchant_id: Int?,
                                           val reward_setting_id: Int?, val customer_mobile: String?,
                                           val reward: Int?, val remarks: String?, val is_approved: Int?)