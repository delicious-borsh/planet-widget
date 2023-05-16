package com.example.planetwidget.presentation

import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase
import com.ponykamni.astronomy.api.domain.Planet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanetWidgetPresenter @Inject constructor(
    private val getDistanceFromEarthUseCase: GetDistanceFromEarthUseCase,
    private val planetPreferences: PlanetPreferences,
    private val widgetSizePreferences: WidgetSizePreferences,
) {

    suspend fun onWidgetHeightUpdate(widgetId: Int, newHeight: Int) {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun onWidgetPlanetUpdate(widgetId: Int) {
        withContext(Dispatchers.IO) {

        }
    }

    fun incrementCurrentPlanet(widgetId: Int) {
        val oldPlanet = getCurrentPlanet(widgetId)
        val newPlanet = Planet.values()[(oldPlanet.ordinal + 1) % Planet.values().size]

        updateCurrentPlanet(widgetId, newPlanet)
    }

    private fun updateCurrentPlanet(widgetId: Int, planet: Planet) {
        planetPreferences.updateCurrentPlanet(widgetId, planet)
    }

    fun getCurrentPlanet(widgetId: Int): Planet {
        return planetPreferences.getCurrentPlanet(widgetId)
    }

    fun getHeight(widgetId: Int): Int {
        return widgetSizePreferences.getHeight(widgetId)
    }

    fun updateHeight(widgetId: Int, height: Int) {
        widgetSizePreferences.updateHeight(widgetId, height)
    }
}