package com.rtchubs.pharmaerp.di

import com.rtchubs.pharmaerp.ui.attendance.AttendanceLocationUpdatesService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ServiceBuilderModule {

    // for my case, the service class which needs injection is MyFirebaseMessagingService
    @ContributesAndroidInjector
    abstract fun contributeAttendanceLocationUpdatesService(): AttendanceLocationUpdatesService

}