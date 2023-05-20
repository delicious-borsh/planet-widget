package com.example.usefulwidgetapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.planetwidget.UpdateDistancesWorker
import com.example.planetwidget.di.PlanetWidgetFeatureImpl
import com.example.usefulwidgetapp.di.DaggerApplicationComponent
import com.ponykamni.astronomy.di.AstronomyFeatureImpl
import java.util.concurrent.TimeUnit

class PlanetWidgetApplication : Application(), Configuration.Provider {

    lateinit var updateDistancesWorkerFactory: UpdateDistancesWorker.UpdateDistancesWorkerFactory

    override fun onCreate() {
        super.onCreate()

        val astronomyFeature = AstronomyFeatureImpl()
        val planetWidgetFeature = PlanetWidgetFeatureImpl(this, astronomyFeature)

        DaggerApplicationComponent.factory().create(
            astronomyFeature, planetWidgetFeature
        )

        updateDistancesWorkerFactory = planetWidgetFeature.getUpdateDistancesWorkerFactory()

        val updateRequest =
            PeriodicWorkRequestBuilder<UpdateDistancesWorker>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("update", ExistingPeriodicWorkPolicy.KEEP, updateRequest)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.INFO)
            .setWorkerFactory(updateDistancesWorkerFactory)
            .build()
}