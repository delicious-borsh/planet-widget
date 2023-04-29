package com.ponykamni.astronomy.di.module;

import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase
import com.ponykamni.astronomy.data.repository.PlanetRepositoryImpl
import com.ponykamni.astronomy.domain.repository.PlanetRepository
import com.ponykamni.astronomy.domain.usecase.GetDistanceFromEarthUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface AstronomyModule {

    @Binds
    fun bindPlanetRepository(impl: PlanetRepositoryImpl): PlanetRepository

    @Binds
    fun bindGetDistanceFromEarthUseCase(impl: GetDistanceFromEarthUseCaseImpl): GetDistanceFromEarthUseCase
}