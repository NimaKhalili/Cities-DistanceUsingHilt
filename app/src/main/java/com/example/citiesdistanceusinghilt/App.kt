package com.example.citiesdistanceusinghilt

import android.app.Application
import com.example.citiesdistanceusinghilt.data.repo.DistanceRepository
import com.example.citiesdistanceusinghilt.data.repo.DistanceRepositoryImpl
import com.example.citiesdistanceusinghilt.data.repo.source.DistanceLocalDataSource
import com.example.citiesdistanceusinghilt.data.repo.source.DistanceRemoteDataSource
import com.example.citiesdistance.feature.distance.DistanceViewModel
import com.example.citiesdistanceusinghilt.feature.home.HomeViewModel
import com.example.citiesdistanceusinghilt.feature.main.MainViewModel
import com.example.citiesdistanceusinghilt.http.ApiService
import com.example.citiesdistanceusinghilt.http.createApiServiceInstance
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val myModules = module {
            single<ApiService> { createApiServiceInstance() }
            factory<DistanceRepository> { DistanceRepositoryImpl(DistanceRemoteDataSource(get()), DistanceLocalDataSource()) }
            viewModel { MainViewModel(get()) }
            viewModel { HomeViewModel(get()) }
            viewModel { DistanceViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModules)
        }
    }
}