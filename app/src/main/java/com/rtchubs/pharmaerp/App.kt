package com.rtchubs.pharmaerp

import android.app.Service
import androidx.databinding.DataBindingUtil
import androidx.work.Configuration
import androidx.work.WorkManager
import com.rtchubs.pharmaerp.binding.FragmentDataBindingComponent
import com.rtchubs.pharmaerp.di.DaggerAppComponent
import com.rtchubs.pharmaerp.util.AppConstants.downloadedPdfFiles
import com.rtchubs.pharmaerp.util.FileUtils
import com.rtchubs.pharmaerp.worker.DaggerWorkerFactory
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerApplication
import javax.inject.Inject
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector







class App : DaggerApplication() {

    private val applicationInjector = DaggerAppComponent.builder()
        .application(this)
        .build()

    @Inject
    lateinit var workerFactory: DaggerWorkerFactory

    @Inject
    lateinit var picasso: Picasso

    override fun applicationInjector() = applicationInjector

    override fun onCreate() {
        super.onCreate()
        FileUtils.makeEmptyFolderIntoExternalStorageWithTitle(this, downloadedPdfFiles)
        // Inject this class's @Inject-annotated members.
        applicationInjector.inject(this)
        /*if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }*/

        // Fabric.with(this, Crashlytics())


        //set picasso to support http protocol
        Picasso.setSingletonInstance(picasso)

        DataBindingUtil.setDefaultComponent(FragmentDataBindingComponent())

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }

    companion object {
        private const val TAG = "App"
    }
}