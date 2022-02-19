package com.rtchubs.pharmaerp.ui.topup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.common.MyAccountListResponse
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.repos.TopUpRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopUpBankCardViewModel @Inject constructor(private val application: Application, private val repository: TopUpRepository, private val preferencesHelper: PreferencesHelper) : BaseViewModel(application) {

    val myAccountList: MutableLiveData<MyAccountListResponse> by lazy {
        MutableLiveData<MyAccountListResponse>()
    }

    fun getMyAccountList() {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse =
                    ApiResponse.create(repository.myAccountListRepo(preferencesHelper.getAccessTokenHeader()))) {
                    is ApiSuccessResponse -> {
                        myAccountList.postValue(apiResponse.body)
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                    }
                    is ApiEmptyResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.EMPTY)
                    }
                    is ApiErrorResponse -> {
                        myAccountList.postValue(
                            Gson().fromJson(
                                apiResponse.errorMessage,
                                MyAccountListResponse::class.java
                            )
                        )
                        apiCallStatus.postValue(ApiCallStatus.ERROR)
                    }
                }
            }
        }
    }
}