package com.ponykamni.astronomy.data.mapper

import com.ponykamni.astronomy.api.domain.Planet
import javax.inject.Inject

internal class PlanetIdMapper @Inject constructor() {

    fun toPlanetId(planet: Planet): String = planet.name.lowercase()
}