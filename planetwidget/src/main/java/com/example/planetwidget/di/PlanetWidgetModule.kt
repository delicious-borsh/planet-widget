package com.example.planetwidget.di

import com.ponykamni.astronomy.api.di.AstronomyFeature
import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase
import dagger.Module
import dagger.Provides

@Module
object PlanetWidgetModule {

    @Provides
    fun provideGetDistanceFromEarthUseCase(
        astronomyFeature: AstronomyFeature
    ): GetDistanceFromEarthUseCase = astronomyFeature.getGetDistanceFromEarthUseCase()
}