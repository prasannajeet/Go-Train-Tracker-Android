package com.p2.apps.mygotraintracker

import android.app.Application
import com.p2.apps.mygotraintracker.di.apiClientModule
import com.p2.apps.mygotraintracker.di.apiKeyModule
import com.p2.apps.mygotraintracker.di.connectivityManagerModule
import com.p2.apps.mygotraintracker.di.databaseModule
import com.p2.apps.mygotraintracker.di.dataSourceModule
import com.p2.apps.mygotraintracker.di.loggerModule
import com.p2.apps.mygotraintracker.di.networkModule
import com.p2.apps.mygotraintracker.di.repositoryModule
import com.p2.apps.mygotraintracker.di.useCaseModule
import com.p2.apps.mygotraintracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    loggerModule,
                    apiKeyModule,
                    networkModule,
                    databaseModule,
                    dataSourceModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    connectivityManagerModule,
                    apiClientModule
                )
            )
        }
    }
}