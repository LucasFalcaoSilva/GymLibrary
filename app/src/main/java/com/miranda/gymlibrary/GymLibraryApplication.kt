package com.miranda.gymlibrary

import android.app.Application
import com.miranda.gymlibrary.core.di.dataModule
import com.miranda.gymlibrary.core.di.domainModule
import com.miranda.gymlibrary.core.di.networkModule
import com.miranda.gymlibrary.core.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GymLibraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GymLibraryApplication)
            modules(
                networkModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
