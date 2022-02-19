package com.rtchubs.pharmaerp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.SplashActivityBinding
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.ui.splash.SplashViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferenceHelper: PreferencesHelper

    private val viewModel: SplashViewModel by viewModels {
        // Get the ViewModel.
        viewModelFactory
    }

    private lateinit var binding: SplashActivityBinding
    private lateinit var animation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the status bar.
        hideSystemUI()


        //binding = DataBindingUtil.setContentView(this@SplashActivity, R.layout.activity_splash)
        binding.lifecycleOwner = this

        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                runBlocking {
                    launch {
                        delay(1200L)
                    }

                    if (preferenceHelper.isLoggedIn) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        this@SplashActivity.finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out)
                    } else {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        this@SplashActivity.finish()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out)
                    }
                }
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

        binding.logo.startAnimation(animation)
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}