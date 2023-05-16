package com.example.planetwidget.data

import android.content.Context
import com.ponykamni.astronomy.api.domain.Planet
import javax.inject.Inject

class PlanetDataPreferences @Inject constructor(context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(PLANET_DATA_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun updateDistance(planet: Planet, distance: Long) {
        preferences
            .edit()
            .putLong(getKey(planet), distance)
            .apply()
    }

    fun getDistance(planet: Planet): Long {
        return preferences.getLong(getKey(planet), 0)
    }

    private fun getKey(planet: Planet): String = planet.name + POSTFIX_DISTANCE

    companion object {
        private const val PLANET_DATA_PREFERENCES = "PLANET_DATA_PREFERENCES"
        private const val POSTFIX_DISTANCE = "_CURRENT_PLANET"
    }
}
