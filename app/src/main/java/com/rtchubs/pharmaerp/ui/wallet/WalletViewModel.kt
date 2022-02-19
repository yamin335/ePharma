package com.rtchubs.pharmaerp.ui.wallet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.local_db.dao.CartDao
import com.rtchubs.pharmaerp.models.PaymentMethod
import com.rtchubs.pharmaerp.repos.GiftPointRepository
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class WalletViewModel @Inject constructor(
    private val application: Application,
    private val giftPointRepository: GiftPointRepository,
    private val cartDao: CartDao
) : BaseViewModel(application) {

    val cartItemCount: LiveData<Int> = liveData {
        cartDao.getCartItemsCount().collect { count ->
            emit(count)
        }
    }

    val paymentMethodList: List<PaymentMethod>
        get() = listOf(
            /* PaymentMethod(
                 "0",
                 "\u2022\u2022\u2022\u2022 4122",R.drawable.maestro

             ),
             PaymentMethod(
                 "1",
                 "\u2022\u2022\u2022\u2022 9120",R.drawable.visa
             ),*/
            PaymentMethod(
                "-1",
                "Add Payment Method", R.drawable.plus
            )
        )



//    val slideDataList = listOf(
//        SlideData(R.drawable.slider_image_1, "Ads1", "Easy, Fast and Secure Way"),
//        SlideData(R.drawable.slider_image_1, "Ads2", "Easy, Fast and Secure Way"),
//        SlideData(R.drawable.slider_image_1, "Ads3", "Easy, Fast and Secure Way"),
//        SlideData(R.drawable.slider_image_1, "Ads4", "Easy, Fast and Secure Way"),
//        SlideData(R.drawable.slider_image_1, "Ads5", "Easy, Fast and Secure Way")
//    )
//
//    inner class SlideData(
//        var slideImage: Int,
//        var textTitle: String,
//        var descText: String
//    )

}