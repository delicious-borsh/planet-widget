package com.ponykamni.astronomy.domain.repository

import com.ponykamni.astronomy.api.domain.Planet

internal interface PlanetRepository {

    suspend fun getDistanceFromEarth(planet: Planet): Long
}