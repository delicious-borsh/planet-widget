package com.example.planetwidget.presentation

import android.content.Context
import com.ponykamni.astronomy.api.domain.Planet
import javax.inject.Inject

class PlanetPreferences @Inject constructor(context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(PLANET_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun updateCurrentPlanet(widgetId: Int, planet: Planet) {
        preferences
            .edit()
            .putString(getKey(widgetId), planet.name)
            .apply()
    }

    fun getCurrentPlanet(widgetId: Int): Planet {
        val planetString = preferences.getString(getKey(widgetId), "")
            .let {
                if (it.isNullOrBlank()) {
                    Planet.MARS.name
                } else {
                    it
                }
            }

        return Planet.valueOf(planetString)
    }

    private fun getKey(widgetId: Int): String = widgetId.toString() + POSTFIX_CURRENT_PLANET
}

private const val PLANET_PREFERENCES = "PLANET_PREFERENCES"
private const val POSTFIX_CURRENT_PLANET = "_CURRENT_PLANET"