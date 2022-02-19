package com.rtchubs.pharmaerp.di

import com.rtchubs.pharmaerp.ui.LoginActivity
import com.rtchubs.pharmaerp.ui.MainActivity
import com.rtchubs.pharmaerp.ui.SplashActivity
import com.rtchubs.pharmaerp.ui.live_chat.LiveChatActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSplashActivity(): SplashActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLiveChatActivity(): LiveChatActivity

}