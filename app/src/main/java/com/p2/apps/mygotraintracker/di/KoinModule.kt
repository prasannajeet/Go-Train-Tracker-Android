package com.p2.apps.mygotraintracker.di

import androidx.room.Room
import com.p2.apps.mygotraintracker.BuildConfig
import com.p2.apps.mygotraintracker.contract.ConnectivityManager
import com.p2.apps.mygotraintracker.contract.ILocalDataSource
import com.p2.apps.mygotraintracker.contract.ILogger
import com.p2.apps.mygotraintracker.contract.IRemoteDataSource
import com.p2.apps.mygotraintracker.data.api.HttpWebServiceHandler
import com.p2.apps.mygotraintracker.data.db.AppDatabase
import com.p2.apps.mygotraintracker.data.db.DatabaseMigrations
import com.p2.apps.mygotraintracker.data.local.MyGoTrainTrackerLocalDataSource
import com.p2.apps.mygotraintracker.data.remote.MyGoTrainTrackerRemoteDataSource
import com.p2.apps.mygotraintracker.domain.repository.StationRepository
import com.p2.apps.mygotraintracker.domain.usecase.GetAllGoTrainStationsUseCase
import com.p2.apps.mygotraintracker.domain.usecase.GetScheduleBetweenStopsUseCase
import com.p2.apps.mygotraintracker.domain.usecase.ManageHomeStationUseCase
import com.p2.apps.mygotraintracker.presentation.feature.trains.TrainScheduleViewModel
import com.p2.apps.mygotraintracker.presentation.feature.onboarding.OnboardingViewModel
import com.p2.apps.mygotraintracker.presentation.utils.AppLogger
import com.p2.apps.mygotraintracker.utils.ConnectivityManagerImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    single<ILocalDataSource> {
        MyGoTrainTrackerLocalDataSource(get())
    }
    
    single<IRemoteDataSource> {
        MyGoTrainTrackerRemoteDataSource(get(), get())
    }
}

val repositoryModule = module {
    single { StationRepository(get()) }
}

val useCaseModule = module {
    factory { GetAllGoTrainStationsUseCase(get(), get()) }
    factory { GetScheduleBetweenStopsUseCase(get(), get()) }
    factory { ManageHomeStationUseCase(get()) }
}

val viewModelModule = module {
    viewModel { parameters -> 
        TrainScheduleViewModel(
            fromStationCode = parameters.get(),
            toStationCode = parameters.get(),
            scheduleUseCase = get()
        ) 
    }
    viewModel { OnboardingViewModel(get(), get()) }
}

val loggerModule = module {
    single<ILogger> {
        AppLogger()
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "go_train_tracker_db"
        )
        .addMigrations(*DatabaseMigrations.ALL_MIGRATIONS)
        .fallbackToDestructiveMigration()
        .build()
    }
    
    single { get<AppDatabase>().stationDao() }
    single { get<AppDatabase>().userPreferencesDao() }
}

val apiKeyModule = module {
    single(named("API_KEY")) { BuildConfig.API_KEY }
}

val networkModule = module {
    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        get<ILogger>().logInfo(message)
                    }
                }
                level = LogLevel.ALL
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 15000
                connectTimeoutMillis = 15000
                requestTimeoutMillis = 15000
            }

            // Configure the default request with the base URL
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.openmetrolinx.com/OpenDataAPI/api/V1"
                }
            }

            install(apiKeyPlugin(get(named("API_KEY"))))
            expectSuccess = false
        }
    }
}

val connectivityManagerModule = module {
    single<ConnectivityManager> {
        ConnectivityManagerImpl(
            androidContext()
        )
    }
}

val apiClientModule = module {
    single {
        HttpWebServiceHandler(get(), get<ConnectivityManager>())
    }
}

fun apiKeyPlugin(apiKey: String) = createClientPlugin("ApiKeyPlugin") {
    onRequest { request, _ ->
        request.url.parameters.append("key", apiKey)
    }
}