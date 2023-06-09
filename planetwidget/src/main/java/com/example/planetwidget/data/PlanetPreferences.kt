package com.example.planetwidget.data

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

    fun deleteDataForWidget(widgetId: Int) {
        preferences
            .edit()
            .remove(getKey(widgetId))
            .apply()
    }

    private fun getKey(widgetId: Int): String = widgetId.toString() + POSTFIX_CURRENT_PLANET

    companion object {
        private const val PLANET_PREFERENCES = "planet_preferences"
        private const val POSTFIX_CURRENT_PLANET = "_CURRENT_PLANET"
    }
}
