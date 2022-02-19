package com.rtchubs.pharmaerp.ui.offers

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.OfferAddResponse
import com.rtchubs.pharmaerp.models.OfferStoreBody
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.repos.OfferRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateOfferViewModel @Inject constructor(
    private val application: Application,
    private val offerRepository: OfferRepository
) : BaseViewModel(application) {

    val offerNote: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val offerPercent: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val offerItems: MutableLiveData<MutableList<PharmaProduct>> by lazy {
        MutableLiveData<MutableList<PharmaProduct>>()
    }

    val newOfferResponse: MutableLiveData<OfferAddResponse> by lazy {
        MutableLiveData<OfferAddResponse>()
    }

    fun addNewOffer(offerStoreBody: OfferStoreBody) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(offerRepository.addNewOffer(offerStoreBody))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        newOfferResponse.postValue(apiResponse.body)
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