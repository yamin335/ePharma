package com.rtchubs.pharmaerp.ui.add_payment_methods

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.payment_account_models.BankOrCardListResponse
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.repos.HomeRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddPaymentMethodsViewModel @Inject constructor(
    private val preferencesHelper: PreferencesHelper,
    private val application: Application,
    private val repository: HomeRepository
) : BaseViewModel(application) {
    val bankOrCardListResponse: MutableLiveData<BankOrCardListResponse> by lazy {
        MutableLiveData<BankOrCardListResponse>()
    }

    fun requestBankList(type:String) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse =
                    ApiResponse.create(repository.requestBankListRepo(type, preferencesHelper.getAccessTokenHeader()))) {
                    is ApiSuccessResponse -> {
                        bankOrCardListResponse.postValue(apiResponse.body)
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        bankOrCardListResponse.postValue(
                            Gson().fromJson(
                                apiResponse.errorMessage,
                                BankOrCardListResponse::class.java
                            )
                        )
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
    }
}