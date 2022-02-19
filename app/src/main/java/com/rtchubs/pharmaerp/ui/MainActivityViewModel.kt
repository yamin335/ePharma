package com.rtchubs.pharmaerp.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestResponse
import com.rtchubs.pharmaerp.repos.AttendanceRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val application: Application,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel(application) {
    val checkInOutResponse: MutableLiveData<Pair<String, CheckInOutRequestResponse>> by lazy {
        MutableLiveData<Pair<String, CheckInOutRequestResponse>>()
    }

    fun checkInOut(checkInOutRequestBody: CheckInOutRequestBody, attendanceType: String) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(attendanceRepository.checkInOut(checkInOutRequestBody))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        checkInOutResponse.postValue(Pair(attendanceType, apiResponse.body))
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