package com.rtchubs.pharmaerp.ui.order

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.customers.Customer
import com.rtchubs.pharmaerp.models.order.OrderStoreBody
import com.rtchubs.pharmaerp.models.order.OrderStoreResponse
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.repos.OrderRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.security.SecureRandom
import javax.inject.Inject

class CreateOrderViewModel @Inject constructor(
    private val application: Application,
    private val orderRepository: OrderRepository
) : BaseViewModel(application) {

    val invoiceNumber: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val selectedCustomer: MutableLiveData<Customer> by lazy {
        MutableLiveData<Customer>()
    }

    val orderItems: MutableLiveData<MutableList<PharmaProduct>> by lazy {
        MutableLiveData<MutableList<PharmaProduct>>()
    }

    val orderPlaceResponse: MutableLiveData<OrderStoreResponse> by lazy {
        MutableLiveData<OrderStoreResponse>()
    }

    fun incrementOrderItemQuantity(id: Int) {
        val items = orderItems.value ?: mutableListOf()
//        val tempItems = items.map { if (it.id == id && it.available_qty != null) { it.available_qty = it.available_qty!! + 1 }
//            it}.toMutableList()
//        orderItems.postValue(tempItems)
    }

    fun decrementOrderItemQuantity(id: Int) {
        val items = orderItems.value ?: mutableListOf()
//        val tempItems = items.map { if (it.id == id && it.available_qty != null) { it.available_qty = it.available_qty!! - 1 }
//            it}.toMutableList()
//        orderItems.postValue(tempItems)
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

    fun generateInvoiceID() {
        val random1 = "${1 + SecureRandom().nextInt(9999999)}"
        val random2 = "${1 + SecureRandom().nextInt(999999)}"
        invoiceNumber.postValue("SO-${random1}${random2}")
    }

}