package com.rtchubs.pharmaerp.repos

import com.google.gson.JsonObject
import com.rtchubs.pharmaerp.api.ApiService
import com.rtchubs.pharmaerp.models.AllMerchantResponse
import com.rtchubs.pharmaerp.models.AllProductResponse
import com.rtchubs.pharmaerp.models.AllShoppingMallResponse
import com.rtchubs.pharmaerp.models.ProductDetailsResponse
import com.rtchubs.pharmaerp.models.add_product.AddProductResponse
import com.rtchubs.pharmaerp.models.customers.AddCustomerResponse
import com.rtchubs.pharmaerp.models.customers.CustomerListResponse
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
class HomeRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun requestBankListRepo(type:String,token:String): Response<BankOrCardListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.requestBankList(type,token)
        }
    }

    suspend fun addBankRepo(bankId: Int, accountNumber: String, token: String): Response<AddCardOrBankResponse> {
        val jsonObjectBody = JsonObject().apply {
            addProperty("bankId", bankId)
            addProperty("accountNumber", accountNumber)
        }

        return withContext(Dispatchers.IO) {
            apiService.addBankAccount(jsonObjectBody, token)
        }
    }

    suspend fun addCardRepo(bankId: Int, cardNumber: String, expireMonth: Int, expireYear: Int, token: String): Response<AddCardOrBankResponse> {
        val jsonObjectBody = JsonObject().apply {
            addProperty("bankId", bankId)
            addProperty("cardNumber", cardNumber)
            addProperty("expireMonth", expireMonth)
            addProperty("expireYear", expireYear)
        }

        return withContext(Dispatchers.IO) {
            apiService.addCardAccount(jsonObjectBody, token)
        }
    }

    // eDokanPat
    suspend fun getAllMallsRepo(): Response<AllShoppingMallResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getAllMalls()
        }
    }

    suspend fun getAllMerchantsRepo(): Response<AllMerchantResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getAllMerchants()
        }
    }

    suspend fun getAllProductsRepo(page: Int, search: String, branchId: Int): Response<PharmaProductListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getAllProducts(page, search, branchId)
        }
    }

    suspend fun allCustomers(page: Int, search: String): Response<CustomerListResponse> {
        return withContext(Dispatchers.IO) {
            apiService.allCustomers(page, search)
        }
    }

    suspend fun addCustomer(name: String, phone: String, email: String, contact_person: String,
                            discountAmount: String, city: String, state: String, zipcode: String,
                            address: String, merchant_id: Int): Response<AddCustomerResponse> {
        val jsonObjectBody = JsonObject().apply {
            addProperty("name", name)
            addProperty("phone", phone)
            addProperty("email", email)
            addProperty("contact_person", contact_person)
            addProperty("discountAmount", discountAmount)
            addProperty("city", city)
            addProperty("state", state)
            addProperty("zipcode", zipcode)
            addProperty("address", address)
            addProperty("merchant_id", merchant_id)
            addProperty("token", email)
        }

        return withContext(Dispatchers.IO) {
            apiService.addCustomer(jsonObjectBody)
        }
    }

    suspend fun addProduct(thumbnail: String?, product_image1: String?, product_image2: String?,
                           product_image3: String?, product_image4: String?, product_image5: String?, name: String?, barcode: String?,
                           description: String?, buying_price: String?, selling_price: String?, mrp: String?,
                           expired_date: String?, category_id: Int?, merchant_id: Int?, token: String?): Response<AddProductResponse> {

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart("name", name ?: "")
            addFormDataPart("barcode", barcode ?: "")
            addFormDataPart("description", description ?: "")
            addFormDataPart("buying_price", buying_price ?: "")
            addFormDataPart("selling_price", selling_price ?: "")
            addFormDataPart("mrp", mrp ?: "")
            addFormDataPart("expired_date", expired_date ?: "")
            addFormDataPart("category_id", category_id?.toString() ?: "")
            addFormDataPart("merchant_id", merchant_id?.toString() ?: "")
            addFormDataPart("token", token ?: "")

            thumbnail?.let {
                val thumbnailFile = File(it)
                val thumbFileRequestBody = thumbnailFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("thumbnail", thumbnailFile.name, thumbFileRequestBody)
            }

            product_image1?.let {
                val image1File = File(it)
                val image1FileRequestBody = image1File.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("product_image1", image1File.name, image1FileRequestBody)
            }

            product_image2?.let {
                val image2File = File(it)
                val image2FileRequestBody = image2File.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("product_image2", image2File.name, image2FileRequestBody)
            }

            product_image3?.let {
                val image3File = File(it)
                val image3FileRequestBody = image3File.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("product_image3", image3File.name, image3FileRequestBody)
            }

            product_image4?.let {
                val image4File = File(it)
                val image4FileRequestBody = image4File.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("product_image4", image4File.name, image4FileRequestBody)
            }

            product_image5?.let {
                val image5File = File(it)
                val image5FileRequestBody = image5File.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                addFormDataPart("product_image5", image5File.name, image5FileRequestBody)
            }

//            tileList.forEachIndexed { index, tile ->
//                val posterior = if (tile.frame == R.drawable.bold_frame) "bold" else "edge"
//                val imageFile = File(product_image ?: "")
//                val fileRequestBody = imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull()) ?: return@forEachIndexed
//                addFormDataPart("files[]", "${index}_$posterior.jpg", fileRequestBody)
////                val fileRequestBody = tile.bitmap?.toFile(context, posterior)?.asRequestBody("multipart/form-data".toMediaTypeOrNull()) ?: return@forEachIndexed
////                addFormDataPart("files[]", "$index.jpg", fileRequestBody)
//            }
        }.build()

        return withContext(Dispatchers.IO) {
            apiService.addProduct(requestBody)
        }
    }

    suspend fun getProductDetailsRepo(id: Int?): Response<ProductDetailsResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getProductDetails(id)
        }
    }
}