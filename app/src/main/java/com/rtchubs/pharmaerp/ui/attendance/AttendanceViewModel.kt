package com.rtchubs.pharmaerp.ui.attendance

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.AllProductResponse
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestResponse
import com.rtchubs.pharmaerp.models.order.GetOrderListRequestBody
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.repos.AttendanceRepository
import com.rtchubs.pharmaerp.repos.HomeRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class AttendanceViewModel @Inject constructor(
    private val application: Application
) : BaseViewModel(application) {

}