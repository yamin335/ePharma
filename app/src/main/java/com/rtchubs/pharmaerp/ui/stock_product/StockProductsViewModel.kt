package com.rtchubs.pharmaerp.ui.stock_product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.product_stock.StockProductWithDetails
import com.rtchubs.pharmaerp.repos.StockProductRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class StockProductsViewModel @Inject constructor(
    private val application: Application,
    private val stockProductRepository: StockProductRepository
) : BaseViewModel(application) {

    val stockProductsList: MutableLiveData<List<StockProductWithDetails>> by lazy {
        MutableLiveData<List<StockProductWithDetails>>()
    }

    fun getStockProduct(token: String) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(stockProductRepository.getStockProduct(token))) {
                    is ApiSuccessResponse -> {
                        stockProductsList.postValue(apiResponse.body.data?.stockwithdetails?.data)
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