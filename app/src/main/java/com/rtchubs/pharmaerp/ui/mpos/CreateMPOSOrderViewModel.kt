package com.rtchubs.pharmaerp.ui.mpos

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.MPOSOrderProduct
import com.rtchubs.pharmaerp.models.MPOSOrderProductsRequestBody
import com.rtchubs.pharmaerp.models.customers.Customer
import com.rtchubs.pharmaerp.models.order.OrderStoreProduct
import com.rtchubs.pharmaerp.models.order.OrderStoreBody
import com.rtchubs.pharmaerp.models.order.OrderStoreResponse
import com.rtchubs.pharmaerp.repos.OrderRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.security.SecureRandom
import javax.inject.Inject

class CreateMPOSOrderViewModel @Inject constructor(
    private val application: Application,
    private val orderRepository: OrderRepository
) : BaseViewModel(application) {

    val invoiceNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val selectedCustomer: MutableLiveData<Customer> by lazy {
        MutableLiveData<Customer>()
    }

    val orderItems: MutableLiveData<MutableList<OrderStoreProduct>> by lazy {
        MutableLiveData<MutableList<OrderStoreProduct>>()
    }

    val orderProducts: MutableLiveData<List<MPOSOrderProduct>> by lazy {
        MutableLiveData<List<MPOSOrderProduct>>()
    }

    val orderPlaceResponse: MutableLiveData<OrderStoreResponse> by lazy {
        MutableLiveData<OrderStoreResponse>()
    }

    fun incrementOrderItemQuantity(id: Int) {
        val items = orderItems.value ?: mutableListOf()
        val tempItems = items.map { if (it.product_id == id && it.quantity != null) {
            it.quantity = it.quantity!! + 1
            val qty = it.quantity ?: 1
            val price = it.price ?: 0
            it.amount = qty * price
        }
            it}.toMutableList()
        orderItems.postValue(tempItems)
    }

    fun decrementOrderItemQuantity(id: Int) {
        val items = orderItems.value ?: mutableListOf()
        val tempItems = items.map { if (it.product_id == id && it.quantity != null) {
            it.quantity = it.quantity!! - 1
            val qty = it.quantity ?: 1
            val price = it.price ?: 0
            it.amount = qty * price
        }
            it}.toMutableList()
        orderItems.postValue(tempItems)
    }

    fun placeOrder(orderStoreBody: OrderStoreBody) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(orderRepository.placeOrder(orderStoreBody))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        orderPlaceResponse.postValue(apiResponse.body)
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

    fun getProductsByBarcodes(requestBody: MPOSOrderProductsRequestBody) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(orderRepository.getProductsByBarcodes(requestBody))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        orderProducts.postValue(apiResponse.body)
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

    fun generateInvoiceID() {
        val random1 = "${1 + SecureRandom().nextInt(9999999)}"
        val random2 = "${1 + SecureRandom().nextInt(999999)}"
        invoiceNumber.postValue("SO-${random1}${random2}")
    }

}