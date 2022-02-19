package com.rtchubs.pharmaerp.di

import com.rtchubs.pharmaerp.worker.DaggerWorkerFactory
import com.rtchubs.pharmaerp.worker.TokenRefreshWorker
import com.rtchubs.pharmaerp.worker.WorkerKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class WorkerBindingModule {

    /*injector for DaggerWorkerFactory*/
    @Binds
    @IntoMap
    @WorkerKey(TokenRefreshWorker::class)
    abstract fun bindTokenRefreshWorker(factory: TokenRefreshWorker.Factory):
            DaggerWorkerFactory.ChildWorkerFactory


}
