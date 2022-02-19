package com.rtchubs.pharmaerp.ui.stock_product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.product_stock.StockProductsDetails
import com.rtchubs.pharmaerp.repos.StockProductRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class StockProductDetailsViewModel @Inject constructor(
    private val application: Application,
    private val stockProductRepository: StockProductRepository
) : BaseViewModel(application) {

    val stockProductsList: MutableLiveData<List<StockProductsDetails>> by lazy {
        MutableLiveData<List<StockProductsDetails>>()
    }

    fun getStockProductDetails(id: Int) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(stockProductRepository.getStockProductDetails(id))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        stockProductsList.postValue(apiResponse.body)
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
}