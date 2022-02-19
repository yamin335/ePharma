package com.rtchubs.pharmaerp.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rtchubs.pharmaerp.api.Api.ContentType
import com.rtchubs.pharmaerp.models.*
import com.rtchubs.pharmaerp.models.add_product.AddProductResponse
import com.rtchubs.pharmaerp.models.attendance.AttendanceLocationUpdateResponse
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestResponse
import com.rtchubs.pharmaerp.models.common.MyAccountListResponse
import com.rtchubs.pharmaerp.models.customers.AddCustomerResponse
import com.rtchubs.pharmaerp.models.customers.CustomerListResponse
import com.rtchubs.pharmaerp.models.login.LoginResponse
import com.rtchubs.pharmaerp.models.order.OrderListResponse
import com.rtchubs.pharmaerp.models.order.OrderStoreResponse
import com.rtchubs.pharmaerp.models.payment_account_models.AddCardOrBankResponse
import com.rtchubs.pharmaerp.models.payment_account_models.BankOrCardListResponse
import com.rtchubs.pharmaerp.models.product_stock.ReceiveProductImageUploadResponse
import com.rtchubs.pharmaerp.models.product_stock.ReceiveProductResponse
import com.rtchubs.pharmaerp.models.product_stock.StockProductsDetails
import com.rtchubs.pharmaerp.models.product_stock.StockProductsResponse
import com.rtchubs.pharmaerp.models.products.PharmaProductListResponse
import com.rtchubs.pharmaerp.models.purchase_list.PurchaseListResponse
import com.rtchubs.pharmaerp.models.registration.InquiryResponse
import com.rtchubs.pharmaerp.models.registration.DefaultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * REST API access points
 */
interface ApiService {

    @Multipart
    @POST(ApiEndPoint.INQUIRE)
    suspend fun inquire(
        @Part("PhoneNumber") mobileNumber: RequestBody,
        @Part("DeviceId") deviceId: RequestBody
    ): Response<InquiryResponse>

    @Multipart
    @POST(ApiEndPoint.REQUESTOTP)
    suspend fun requestOTP(
        @Part("PhoneNumber") mobileNumber: RequestBody,
        @Part("HasGivenConsent") hasGivenConsent: RequestBody
    ): Response<DefaultResponse>

    @Multipart
    @POST(ApiEndPoint.REGISTRATION)
    suspend fun registration(
        @Part("MobileNumber") mobileNumber: RequestBody,
        @Part("Otp") otp: RequestBody,
        @Part("Pin") password: RequestBody,
        @Part("FullName") fullName: RequestBody,
        @Part("MobileOperator") mobileOperator: RequestBody,
        @Part("DeviceId") deviceId: RequestBody,
        @Part("DeviceName") deviceName: RequestBody,
        @Part("DeviceModel") deviceModel: RequestBody,
        @Part userImage: MultipartBody.Part?,
        @Part("NidNumber") nidNumber: RequestBody,
        @Part nidFrontImage: MultipartBody.Part?,
        @Part nidBackImage: MultipartBody.Part?
    ): Response<DefaultResponse>


    @FormUrlEncoded
    @POST(ApiEndPoint.CONNECT_TOKEN)
    suspend fun connectToken(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String,
        @Field("scope") scope: String,
        @Field("device_id") deviceID: String,
        @Field("device_name") deviceName: String,
        @Field("device_model") deviceModel: String,
        @Field("client_id") clientID: String,
        @Field("client_secret") clientSecret: String,
        @Field("otp") otp: String
    ): Response<String>


    @GET(ApiEndPoint.GET_BANK_LIST)
    suspend fun requestBankList(
        @Query("type") type: String,
        @Header("Authorization") token: String
    ): Response<BankOrCardListResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ADD_CARD)
    suspend fun addBankAccount(
        @Body jsonObject: JsonObject,
        @Header("Authorization") token: String
    ): Response<AddCardOrBankResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ADD_BANK)
    suspend fun addCardAccount(
        @Body jsonObject: JsonObject,
        @Header("Authorization") token: String
    ): Response<AddCardOrBankResponse>

    @GET(ApiEndPoint.MY_ACCOUNT_LIST)
    suspend fun myAccountList(
        @Header("Authorization") token: String
    ): Response<MyAccountListResponse>


    // eDokanPat
    @Headers(ContentType)
    @POST(ApiEndPoint.LOGIN)
    suspend fun userLogin(
        @Body jsonObject: JsonObject
    ): Response<LoginResponse>

    @GET(ApiEndPoint.ALL_MALL)
    suspend fun getAllMalls(): Response<AllShoppingMallResponse>

    @GET(ApiEndPoint.ALL_MERCHANTS)
    suspend fun getAllMerchants(): Response<AllMerchantResponse>

    @GET(ApiEndPoint.PHARMA_PRODUCTS)
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("search") search: String,
        @Query("branch_id") branch_id: Int
    ): Response<PharmaProductListResponse>

    @Headers(ContentType)
    @GET(ApiEndPoint.CUSTOMERS)
    suspend fun allCustomers(
        @Query("page") page: Int,
        @Query("search") search: String
    ): Response<CustomerListResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ADD_CUSTOMER)
    suspend fun addCustomer(
        @Body jsonObject: JsonObject
    ): Response<AddCustomerResponse>

    @POST(ApiEndPoint.ADD_PRODUCT)
    suspend fun addProduct(@Body partFormData: RequestBody): Response<AddProductResponse>

    @POST(ApiEndPoint.ORDER_LIST)
    suspend fun getOrderList(
        @Body jsonObject: JsonObject,
        @Query("page") page: Int
    ): Response<OrderListResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.SALE)
    suspend fun placeOrder(
        @Body jsonObject: JsonObject
    ): Response<OrderStoreResponse>

    @POST(ApiEndPoint.OFFER_LIST)
    suspend fun getOfferList(
        @Query("page") page: Int?,
        @Query("token") token: String?): Response<OfferProductListResponse>

    @GET(ApiEndPoint.PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Path("id") type: Int?
    ): Response<ProductDetailsResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ADD_OFFER)
    suspend fun addNewOffer(
        @Body jsonObject: JsonObject
    ): Response<OfferAddResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.GIFT_POINT_REQUESTS)
    suspend fun getGiftPointRequestList(
        @Query("page") page: Int?,
        @Body jsonObject: JsonObject
    ): Response<GiftPointRequestListResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.GIFT_POINT_HISTORY)
    suspend fun getGiftPointHistory(
        @Query("page") page: Int?,
        @Body jsonObject: JsonObject
    ): Response<ShopWiseGiftPointResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.GIFT_POINT_HISTORY)
    suspend fun getGiftPointHistoryDetails(
        @Query("page") page: Int?,
        @Body jsonObject: JsonObject
    ): Response<GiftPointsHistoryDetailsResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.BARCODE_PRODUCTS)
    suspend fun getProductsByBarcodes(
        @Body jsonObject: JsonObject
    ): Response<List<MPOSOrderProduct>>

    @Headers(ContentType)
    @POST(ApiEndPoint.STOCK_PRODUCTS)
    suspend fun getStockProduct(
        @Query("page") page: String?,
        @Body jsonObject: JsonObject
    ): Response<StockProductsResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.STOCK_PRODUCTS_DETAILS)
    suspend fun getStockProductDetails(
        @Query("page") page: String?,
        @Body jsonObject: JsonObject
    ): Response<List<StockProductsDetails>>

    @POST(ApiEndPoint.RECEIVE_PRODUCT_IMAGE_UPLOAD)
    suspend fun uploadReceiveProductImages(@Body partFormData: RequestBody): Response<ReceiveProductImageUploadResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.RECEIVE_PRODUCT)
    suspend fun storeReceivedProduct(
        @Body jsonArray: JsonArray
    ): Response<ReceiveProductResponse>

    @GET(ApiEndPoint.PURCHASE_LIST)
    suspend fun getPurchaseList(
        @Query("page") page: Int?,
        @Query("token") token: String?): Response<PurchaseListResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ATTENDANCE_UPDATE)
    suspend fun updateCurrentLocation(
        @Body jsonObject: JsonObject
    ): Response<AttendanceLocationUpdateResponse>

    @Headers(ContentType)
    @POST(ApiEndPoint.ATTENDANCE_CHECK_IN_OUT)
    suspend fun checkInOut(
        @Body jsonObject: JsonObject
    ): Response<CheckInOutRequestResponse>
}
