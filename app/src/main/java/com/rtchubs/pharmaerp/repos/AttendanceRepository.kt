package com.rtchubs.pharmaerp.repos

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rtchubs.pharmaerp.api.ApiService
import com.rtchubs.pharmaerp.models.AllMerchantResponse
import com.rtchubs.pharmaerp.models.AllProductResponse
import com.rtchubs.pharmaerp.models.AllShoppingMallResponse
import com.rtchubs.pharmaerp.models.ProductDetailsResponse
import com.rtchubs.pharmaerp.models.add_product.AddProductResponse
import com.rtchubs.pharmaerp.models.attendance.AttendanceLocationUpdateRequestBody
import com.rtchubs.pharmaerp.models.attendance.AttendanceLocationUpdateResponse
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestResponse
import com.rtchubs.pharmaerp.models.customers.AddCustomerResponse
import com.rtchubs.pharmaerp.models.customers.CustomerListResponse
import com.rtchubs.pharmaerp.models.order.OrderStoreBody
import com.rtchubs.pharmaerp.models.order.OrderStoreResponse
import com.rtchubs.pharmaerp.models.payment_account_models.AddCardOrBankResponse
import com.rtchubs.pharmaerp.models.payment_account_models.BankOrCardListResponse
import com.rtchubs.pharmaerp.models.products.PharmaProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun updateCurrentLocation(attendanceLocationUpdateRequestBody: AttendanceLocationUpdateRequestBody): Response<AttendanceLocationUpdateResponse> {
        val jsonString = Gson().toJson(attendanceLocationUpdateRequestBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.updateCurrentLocation(jsonObject)
        }
    }

    suspend fun checkInOut(checkInOutRequestBody: CheckInOutRequestBody): Response<CheckInOutRequestResponse> {
        val jsonString = Gson().toJson(checkInOutRequestBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        return withContext(Dispatchers.IO) {
            apiService.checkInOut(jsonObject)
        }
    }
}