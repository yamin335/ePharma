package com.rtchubs.pharmaerp.ui.live_chat

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.rtchubs.pharmaerp.ui.common.BaseViewModel
import javax.inject.Inject

class LiveChatViewModel @Inject constructor(private val application: Application) : BaseViewModel(application) {

    val newMessage: MutableLiveData<String> = MutableLiveData()

}