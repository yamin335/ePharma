package com.rtchubs.pharmaerp.ui.stock_product

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.ProductDetailsResponse
import com.rtchubs.pharmaerp.models.product_stock.ReceiveProductResponse
import com.rtchubs.pharmaerp.models.product_stock.ReceiveProductStoreBody
import com.rtchubs.pharmaerp.repos.HomeRepository
import com.rtchubs.pharmaerp.repos.StockProductRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReceiveProductViewModel @Inject constructor(
    private val application: Application,
    private val stockProductRepository: StockProductRepository,
    private val homeRepository: HomeRepository
) : BaseViewModel(application) {

    val selectedProduct: MutableLiveData<MutableList<Product>> by lazy {
        MutableLiveData<MutableList<Product>>()
    }
    val buyingPrice: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val sellingPrice: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val expiredDate: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val description: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val imageUploadResponse: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    fun uploadReceiveProductImages(user_token: String?, save_modal: String?, return_var: String?,
                   type: String?, imagePaths: ArrayList<String>) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(
                    stockProductRepository.uploadReceiveProductImages(
                        user_token, save_modal,
                        return_var, type, imagePaths
                    )
                )) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        imageUploadResponse.postValue(apiResponse.body.data?.filelists)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
    }

    fun getProductDetails(id: Int?): LiveData<ProductDetailsResponse> {
        val productDetails: MutableLiveData<ProductDetailsResponse> = MutableLiveData()
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(homeRepository.getProductDetailsRepo(id))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        productDetails.postValue(apiResponse.body)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
        return productDetails
    }

    fun storeReceivedProduct(receiveProductStoreBody: ReceiveProductStoreBody): LiveData<ReceiveProductResponse> {
        val productDetails: MutableLiveData<ReceiveProductResponse> = MutableLiveData()
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(stockProductRepository.storeReceivedProduct(receiveProductStoreBody))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        productDetails.postValue(apiResponse.body)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
        return productDetails
    }

}