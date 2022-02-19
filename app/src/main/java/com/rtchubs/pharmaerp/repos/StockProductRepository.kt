package com.rtchubs.pharmaerp.repos

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.rtchubs.pharmaerp.api.ApiService
import com.rtchubs.pharmaerp.models.product_stock.*
import com.rtchubs.pharmaerp.models.purchase_list.PurchaseListResponse
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
class StockProductRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getStockProduct(token: String): Response<StockProductsResponse> {
        val jsonObject = JsonObject().apply {
            addProperty("token", token)
        }
        return withContext(Dispatchers.IO) {
            apiService.getStockProduct("1", jsonObject)
        }
    }

    suspend fun getStockProductDetails(product_detail_id: Int): Response<List<StockProductsDetails>> {
        val jsonObject = JsonObject().apply {
            addProperty("product_detail_id", product_detail_id)
        }
        return withContext(Dispatchers.IO) {
            apiService.getStockProductDetails("all", jsonObject)
        }
    }

    suspend fun uploadReceiveProductImages(user_token: String?, save_modal: String?, return_var: String?,
                                           type: String?, imagePaths: ArrayList<String>): Response<ReceiveProductImageUploadResponse> {

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart("user_token", user_token ?: "")
            addFormDataPart("save_modal", save_modal ?: "")
            addFormDataPart("return_var", return_var ?: "")
            addFormDataPart("type", type ?: "")

            imagePaths.forEachIndexed { index, imagePath ->
                val imageFile = File(imagePath)
                val fileRequestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("filename[$index]", imageFile.name, fileRequestBody)
            }
        }.build()

        return withContext(Dispatchers.IO) {
            apiService.uploadReceiveProductImages(requestBody)
        }
    }

    suspend fun storeReceivedProduct(receiveProductStoreBody: ReceiveProductStoreBody): Response<ReceiveProductResponse> {
        val jsonString = Gson().toJson(receiveProductStoreBody)
        val jsonObject = JsonParser().parse(jsonString).asJsonObject
        val jsonArray = JsonArray().apply {
            add(jsonObject)
        }
        return withContext(Dispatchers.IO) {
            apiService.storeReceivedProduct(jsonArray)
        }
    }

    suspend fun getPurchaseList(page: Int?, token: String?): Response<PurchaseListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getPurchaseList(page, token)
        }
    }
}