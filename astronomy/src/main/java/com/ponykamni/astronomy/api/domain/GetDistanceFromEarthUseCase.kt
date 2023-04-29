package com.ponykamni.astronomy.api.domain

interface GetDistanceFromEarthUseCase {

    suspend operator fun invoke(planet: Planet): Long
}