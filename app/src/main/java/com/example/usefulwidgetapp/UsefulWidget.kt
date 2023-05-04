package com.example.usefulwidgetapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.ponykamni.astronomy.api.domain.Planet
import com.ponykamni.astronomy.di.AstronomyFeatureImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UsefulWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val getDistanceFromEarthUseCase = AstronomyFeatureImpl().getGetDistanceFromEarthUseCase()

    val views = RemoteViews(context.packageName, R.layout.useful_widget)

    CoroutineScope(Dispatchers.IO).launch {
        val marsDistance = getDistanceFromEarthUseCase(Planet.MARS)
        val venusDistance = getDistanceFromEarthUseCase(Planet.VENUS)
        val mercuryDistance = getDistanceFromEarthUseCase(Planet.MERCURY)

        withContext(Dispatchers.Main) {
            views.setTextViewText(R.id.mars_distance, marsDistance.toString())
            views.setTextViewText(R.id.venus_distance, venusDistance.toString())
            views.setTextViewText(R.id.mercury_distance, mercuryDistance.toString())
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}