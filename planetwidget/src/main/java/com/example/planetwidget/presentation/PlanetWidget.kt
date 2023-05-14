package com.example.planetwidget.presentation

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.example.planetwidget.R
import com.ponykamni.astronomy.api.domain.Planet


class PlanetWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_NEXT_PLANET) {

            val appWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, -1)
            if (appWidgetId == -1) return

            incrementCurrentPlanet(appWidgetId, context)
            updateWidget(context, appWidgetId)
        }
    }

    private fun incrementCurrentPlanet(widgetId: Int, context: Context) {
        val oldPlanet = getCurrentPlanet(widgetId, context)
        val newPlanet = Planet.values()[(oldPlanet.ordinal + 1) % Planet.values().size]

        updateCurrentPlanet(widgetId, newPlanet, context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)

        val minHeight = newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) ?: return
        val maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

        val heightInCells = CellsCalculator().getWidgetHeightInCells(minHeight, maxHeight) ?: 1

        updateHeight(appWidgetId, heightInCells, context ?: return)

        updateWidget(context, appWidgetId)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val heightInCells = getHeight(appWidgetId, context)

        val views = createViews(
            context,
            appWidgetId,
            heightInCells,
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun createViews(
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
                setTextViewText(R.id.mars_distance, Planet.MARS.name)
                setTextViewText(R.id.venus_distance, Planet.VENUS.name)
                setTextViewText(R.id.mercury_distance, Planet.MERCURY.name)
            }
    }

    private fun configureSmallView(context: Context, widgetId: Int): RemoteViews {
        val pendingIntent = createPendingClickIntent(context, widgetId)

        val currentPlanet = getCurrentPlanet(widgetId, context)

        val distance = currentPlanet.name

        return RemoteViews(context.packageName, R.layout.planets_widget_small)
            .apply {
                setTextViewText(R.id.planet_name, distance)
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

    private fun getCurrentPlanet(widgetId: Int, context: Context): Planet {
        return PlanetPreferences(context).getCurrentPlanet(widgetId)
    }

    private fun updateCurrentPlanet(widgetId: Int, planet: Planet, context: Context) {
        PlanetPreferences(context).updateCurrentPlanet(widgetId, planet)
    }

    private fun getHeight(widgetId: Int, context: Context): Int {
        return WidgetSizePreferences(context).getHeight(widgetId)
    }

    private fun updateHeight(widgetId: Int, height: Int, context: Context) {
        WidgetSizePreferences(context).updateHeight(widgetId, height)
    }

    companion object {

        fun updateWidget(context: Context, id: Int) {
            val intent = createUpdateIntent(context, id)

            context.sendBroadcast(intent)
        }

        private fun createUpdateIntent(context: Context, widgetId: Int): Intent {
            val intent = Intent(context, PlanetWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId))

            return intent
        }
    }
}

private const val ACTION_NEXT_PLANET = "ACTION_NEXT_PLANET"
private const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"
