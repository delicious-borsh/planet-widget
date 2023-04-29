package com.ponykamni.astronomy.domain.usecase

import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase
import com.ponykamni.astronomy.api.domain.Planet
import com.ponykamni.astronomy.domain.repository.PlanetRepository
import javax.inject.Inject

internal class GetDistanceFromEarthUseCaseImpl @Inject constructor(
    private val repository: PlanetRepository,
) : GetDistanceFromEarthUseCase {

    override suspend operator fun invoke(planet: Planet): Long = repository.getDistanceFromEarth(planet)
}