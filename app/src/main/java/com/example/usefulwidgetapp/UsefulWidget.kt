package com.example.usefulwidgetapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.ponykamni.astronomy.api.domain.Planet


class UsefulWidget : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_NEXT_PLANET) {

            val appWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, -1)
            if (appWidgetId == -1) return

            incrementCurrentPlanet(appWidgetId, context)

            context.sendBroadcast(createUpdateIntent(context, appWidgetId))
        }
    }

    private fun incrementCurrentPlanet(widgetId: Int, context: Context) {
        val oldPlanet = getCurrentPlanet(widgetId, context)
        val newPlanet = Planet.values()[(oldPlanet.ordinal + 1) % Planet.values().size]

        updateCurrentPlanet(widgetId, newPlanet, context)
    }

    private fun createUpdateIntent(context: Context, widgetId: Int): Intent {
        val intent = Intent(context, UsefulWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetId))

        return intent
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

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val pendingIntent = createPendingClickIntent(context, appWidgetId)

        val currentPlanet = getCurrentPlanet(appWidgetId, context)

        val views = createViews(
            context,
            currentPlanet,
            currentPlanet.name,
            pendingIntent,
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun createViews(
        context: Context,
        planet: Planet,
        distance: String,
        pendingIntent: PendingIntent,
    ): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.planets_widget_small)
        views.setTextViewText(R.id.planet_distance, distance)
        views.setOnClickPendingIntent(R.id.arrow_next, pendingIntent)
        views.setImageViewResource(R.id.planet_icon, planet.getIcon())

        return views
    }

    private fun createPendingClickIntent(context: Context, widgetId: Int): PendingIntent {
        val intent = Intent(context, UsefulWidget::class.java)
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
}

private const val ACTION_NEXT_PLANET = "ACTION_NEXT_PLANET"
private const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"