package com.rtchubs.pharmaerp.di

import android.app.Application
import android.content.Context
import com.rtchubs.pharmaerp.local_db.dao.CartDao
import com.rtchubs.pharmaerp.local_db.dao.FavoriteDao
import com.rtchubs.pharmaerp.local_db.db.AppDatabase
import com.rtchubs.pharmaerp.prefs.AppPreferencesHelper
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.util.AppConstants
import com.squareup.picasso.BuildConfig
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
        WorkerBindingModule::class,
        ServiceBuilderModule::class
    ]
)
class AppModule {

    @Singleton
    @Provides
    fun providePicasso(app: Application, okHttpClient: OkHttpClient) = Picasso.Builder(app)
        .downloader(OkHttp3Downloader(okHttpClient))
        .listener { _, _, e -> if (BuildConfig.DEBUG) e.printStackTrace() }
        .loggingEnabled(BuildConfig.DEBUG)
        .build()

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideCartDao(db: AppDatabase): CartDao {
        return db.cartDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao {
        return db.favoriteDao()
    }

    @Singleton
    @Provides
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }
}