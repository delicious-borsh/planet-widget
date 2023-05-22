package com.example.planetwidget.presentation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.planetwidget.R
import com.ponykamni.astronomy.api.domain.Planet

class RemoteViewsFactory(
    private val getDistance: (Planet) -> Long,
    private val getCurrentPlanet: (Int) -> Planet,
) {

    fun createViews(
        context: Context,
        widgetId: Int,
        heightInCells: Int,
    ): RemoteViews {
        return when {
            heightInCells > 2 -> configureLargeView(context)
            else -> configureSmallView(context, widgetId)
        }
    }

    private fun configureLargeView(context: Context): RemoteViews {
        return RemoteViews(context.packageName, R.layout.planets_widget_large)
            .apply {
                setTextViewText(
                    R.id.mars_distance,
                    getDistance(Planet.MARS).toFormattedString()
                )
                setTextViewText(
                    R.id.venus_distance,
                    getDistance(Planet.VENUS).toFormattedString()
                )
                setTextViewText(
                    R.id.mercury_distance,
                    getDistance(Planet.MERCURY).toFormattedString()
                )
            }
    }

    private fun configureSmallView(context: Context, widgetId: Int): RemoteViews {
        val pendingIntent = createPendingClickIntent(context, widgetId)

        val currentPlanet = getCurrentPlanet(widgetId)

        val distance = getDistance(currentPlanet).toFormattedString()

        return RemoteViews(context.packageName, R.layout.planets_widget_small)
            .apply {
                setTextViewText(R.id.planet_name, currentPlanet.name)
                setTextViewText(R.id.planet_distance, distance)
                setOnClickPendingIntent(R.id.arrow_next, pendingIntent)
                setImageViewResource(R.id.planet_icon, currentPlanet.getIcon())
            }
    }

    private fun createPendingClickIntent(context: Context, widgetId: Int): PendingIntent {
        val intent = Intent(context, PlanetWidget::class.java)
        intent.action = ACTION_NEXT_PLANET
        intent.putExtra(EXTRA_WIDGET_ID, widgetId)

        return PendingIntent.getBroadcast(
            context, widgetId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE,
        )
    }
}