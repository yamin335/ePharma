package com.rtchubs.pharmaerp.ui.live_chat

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.LiveChatActivityBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LiveChatActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: LiveChatViewModel by viewModels {
        // Get the ViewModel.
        viewModelFactory
    }

    lateinit var binding: LiveChatActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@LiveChatActivity, R.layout.activity_live_chat)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        CoroutineScope(Dispatchers.Main.immediate).launch {
            delay(500)
            binding.btnSend.isEnabled = false
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
            //overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
        }

        return super.onOptionsItemSelected(item)
    }
}