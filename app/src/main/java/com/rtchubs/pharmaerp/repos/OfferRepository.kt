package com.rtchubs.pharmaerp.repos

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.rtchubs.pharmaerp.api.ApiService
import com.rtchubs.pharmaerp.models.OfferAddResponse
import com.rtchubs.pharmaerp.models.OfferProductListResponse
import com.rtchubs.pharmaerp.models.OfferStoreBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfferRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getOfferList(page: Int?, token: String?): Response<OfferProductListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getOfferList(page, token)
        }
    }

    suspend fun addNewOffer(offerStoreBody: OfferStoreBody): Response<OfferAddResponse> {
        val jsonString = Gson().toJson(offerStoreBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.addNewOffer(jsonObject)
        }
    }
}