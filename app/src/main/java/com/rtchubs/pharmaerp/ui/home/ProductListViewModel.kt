package com.rtchubs.pharmaerp.ui.home

import android.app.Application
import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.local_db.dao.CartDao
import com.rtchubs.pharmaerp.local_db.dao.FavoriteDao
import com.rtchubs.pharmaerp.local_db.dbo.CartItem
import com.rtchubs.pharmaerp.models.AllProductResponse
import com.rtchubs.pharmaerp.models.OrderProduct
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProductListResponse
import com.rtchubs.pharmaerp.repos.HomeRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import com.rtchubs.pharmaerp.util.AppConstants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    private val application: Application,
    private val repository: HomeRepository,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) : BaseViewModel(application) {

    val productListResponse: MutableLiveData<PharmaProductListResponse> by lazy {
        MutableLiveData<PharmaProductListResponse>()
    }

    val cartItemCount: LiveData<Int> = liveData {
        cartDao.getCartItemsCount().collect { count ->
            emit(count)
        }
    }

    fun addToFavorite(product: Product) {
        try {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
            }

            viewModelScope.launch(handler) {
                val response = favoriteDao.addItemToFavorite(product)
                if (response == -1L) {
                    toastWarning.postValue("Already added to favorite!")
                } else {
                    toastSuccess.postValue("Added to favorite")
                }
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun addToCart(product: OrderProduct, quantity: Int) {
        try {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
            }

            viewModelScope.launch(handler) {
                val response = cartDao.addItemToCart(CartItem(product.id ?: 0, product, quantity))
                if (response == -1L) {
                    toastWarning.postValue("Already added to cart!")
                } else {
                    toastSuccess.postValue("Added to cart")
                }
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    fun getProductList(page: Int, search: String, branchId: Int) {
        if (checkNetworkStatus()) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                apiCallStatus.postValue(ApiCallStatus.ERROR)
                toastError.postValue(AppConstants.serverConnectionErrorMessage)
            }

            apiCallStatus.postValue(ApiCallStatus.LOADING)
            viewModelScope.launch(handler) {
                when (val apiResponse = ApiResponse.create(repository.getAllProductsRepo(page, search, branchId))) {
                    is ApiSuccessResponse -> {
                        apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                        productListResponse.postValue(apiResponse.body)
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