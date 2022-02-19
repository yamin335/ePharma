package com.rtchubs.pharmaerp.repos

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.rtchubs.pharmaerp.api.ApiService
import com.rtchubs.pharmaerp.models.MPOSOrderProduct
import com.rtchubs.pharmaerp.models.MPOSOrderProductsRequestBody
import com.rtchubs.pharmaerp.models.order.GetOrderListRequestBody
import com.rtchubs.pharmaerp.models.order.OrderListResponse
import com.rtchubs.pharmaerp.models.order.OrderStoreBody
import com.rtchubs.pharmaerp.models.order.OrderStoreResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getOrderList(getOrderListRequestBody: GetOrderListRequestBody?, page: Int): Response<OrderListResponse> {
        val jsonString = Gson().toJson(getOrderListRequestBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.getOrderList(jsonObject, page)
        }
    }

    suspend fun placeOrder(orderStoreBody: OrderStoreBody?): Response<OrderStoreResponse> {
        val jsonString = Gson().toJson(orderStoreBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.placeOrder(jsonObject)
        }
    }

    suspend fun getProductsByBarcodes(requestBody: MPOSOrderProductsRequestBody?): Response<List<MPOSOrderProduct>> {
        val jsonString = Gson().toJson(requestBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.getProductsByBarcodes(jsonObject)
        }
    }
}