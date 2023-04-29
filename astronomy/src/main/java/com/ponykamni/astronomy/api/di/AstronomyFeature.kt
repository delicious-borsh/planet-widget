package com.ponykamni.astronomy.api.di

import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase

interface AstronomyFeature {

    fun getGetDistanceFromEarthUseCase(): GetDistanceFromEarthUseCase
}