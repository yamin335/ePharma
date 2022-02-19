package com.rtchubs.pharmaerp.ui.cart

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.rtchubs.pharmaerp.local_db.dao.CartDao
import com.rtchubs.pharmaerp.local_db.dbo.CartItem
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(private val application: Application, private val cartDao: CartDao) : BaseViewModel(application) {

    val cartItems: LiveData<List<CartItem>> = liveData {
        cartDao.getCartItems().collect { list ->
            emit(list)
        }
    }

    fun deleteCartItem(item: CartItem) {
        val handler = CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }

        viewModelScope.launch(handler) {
            cartDao.deleteCartItem(item)
        }
    }
}