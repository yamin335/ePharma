package com.rtchubs.pharmaerp.ui.purchase_list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.purchase_list.PurchaseListResponseData
import com.rtchubs.pharmaerp.repos.StockProductRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class PurchaseListFragmentViewModel @Inject constructor(
    private val application: Application,
    private val stockProductRepository: StockProductRepository
) : BaseViewModel(application) {

    val purchaseList: MutableLiveData<List<PurchaseListResponseData>> by lazy {
        MutableLiveData<List<PurchaseListResponseData>>()
    }

    fun getPurchaseList(page: Int, token: String) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(stockProductRepository.getPurchaseList(page, token))) {
                    is ApiSuccessResponse -> {
                        purchaseList.postValue(apiResponse.body.data)
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