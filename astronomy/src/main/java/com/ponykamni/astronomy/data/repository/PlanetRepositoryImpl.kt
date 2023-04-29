package com.ponykamni.astronomy.data.repository

import com.ponykamni.astronomy.api.domain.Planet
import com.ponykamni.astronomy.data.mapper.PlanetIdMapper
import com.ponykamni.astronomy.data.source.NetworkDataSource
import com.ponykamni.astronomy.domain.repository.PlanetRepository
import javax.inject.Inject

internal class PlanetRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val planetIdMapper: PlanetIdMapper
) : PlanetRepository {

    override suspend fun getDistanceFromEarth(planet: Planet): Long {
        val id = planetIdMapper.toPlanetId(planet)

        return networkDataSource.getPlanetInfo(id).getDistance(id)
    }
}