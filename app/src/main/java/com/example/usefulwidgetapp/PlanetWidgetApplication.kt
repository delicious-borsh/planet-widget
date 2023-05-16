package com.example.usefulwidgetapp

import android.app.Application
import com.example.planetwidget.di.PlanetWidgetFeatureImpl
import com.example.usefulwidgetapp.di.DaggerApplicationComponent
import com.ponykamni.astronomy.di.AstronomyFeatureImpl

class PlanetWidgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val astronomyFeature = AstronomyFeatureImpl()
        val planetWidgetFeature = PlanetWidgetFeatureImpl(this, astronomyFeature)

        DaggerApplicationComponent.factory().create(
            astronomyFeature, planetWidgetFeature
        )
    }
}