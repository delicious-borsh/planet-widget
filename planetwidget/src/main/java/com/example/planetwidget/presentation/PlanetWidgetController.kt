package com.example.planetwidget.presentation

import android.content.Context
import android.util.Log
import com.example.planetwidget.data.PlanetDataPreferences
import com.example.planetwidget.data.PlanetPreferences
import com.example.planetwidget.data.WidgetPreferences
import com.ponykamni.astronomy.api.domain.GetDistanceFromEarthUseCase
import com.ponykamni.astronomy.api.domain.Planet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanetWidgetController @Inject constructor(
    private val getDistanceFromEarthUseCase: GetDistanceFromEarthUseCase,
    private val planetPreferences: PlanetPreferences,
    private val widgetPreferences: WidgetPreferences,
    private val planetDataPreferences: PlanetDataPreferences,
) {

    fun onDataUpdated(context: Context) {
        val currentWidgets = widgetPreferences.getWidgetList().toIntArray()
        PlanetWidget.updateWidget(context, currentWidgets)
    }

    fun onWidgetUpdatedOrCreated(widgetId: Int) {
        widgetPreferences.addWidgetIfNotPresent(widgetId)
    }

    fun onWidgetRemoved(widgetId: Int) {
        widgetPreferences.removeWidget(widgetId)
        planetPreferences.deleteDataForWidget(widgetId)
    }

    suspend fun updatePlanetDistances() {
        withContext(Dispatchers.IO) {
            Log.d("AAAAA", "updating planets")
            for (planet in Planet.values()) {
                Log.d("AAAAA", "updating for $planet")
                val distance = getDistanceFromEarthUseCase(planet)
                Log.d("AAAAA", "distance is $distance")
                updatePlanetDistance(planet, distance)
                Log.d("AAAAA", "writing...")
                Log.d("AAAAA", "new distance is ${getDistance(planet)}...")
            }
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
        return widgetPreferences.getHeight(widgetId)
    }

    fun updateHeight(widgetId: Int, height: Int) {
        widgetPreferences.updateHeight(widgetId, height)
    }

    fun getDistance(planet: Planet): Long {
        return planetDataPreferences.getDistance(planet)
    }

    fun updatePlanetDistance(planet: Planet, distance: Long) {
        planetDataPreferences.updateDistance(planet, distance)
    }
}