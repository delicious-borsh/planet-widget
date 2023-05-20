package com.example.planetwidget.presentation

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.example.planetwidget.R
import com.example.planetwidget.di.PlanetWidgetInjector
import com.ponykamni.astronomy.api.domain.Planet.MARS
import com.ponykamni.astronomy.api.domain.Planet.MERCURY
import com.ponykamni.astronomy.api.domain.Planet.VENUS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class PlanetWidget : AppWidgetProvider() {

    @Inject
    lateinit var controller: PlanetWidgetController

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        doAsync { controller.updatePlanetDistances() }
    }

    override fun onReceive(context: Context, intent: Intent) {
        PlanetWidgetInjector.component?.inject(this)

        super.onReceive(context, intent)

        if (intent.action == ACTION_NEXT_PLANET) {

            val appWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, -1)
            if (appWidgetId == -1) return

            controller.incrementCurrentPlanet(appWidgetId)
            updateWidget(context, intArrayOf(appWidgetId))
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("AAAAA", "received update intent with ids ${appWidgetIds.joinToString()}")
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

        controller.updateHeight(appWidgetId, heightInCells)

        updateWidget(context ?: return, intArrayOf(appWidgetId))
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        controller.onWidgetUpdatedOrCreated(appWidgetId)

        val heightInCells = controller.getHeight(appWidgetId)

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
                setTextViewText(
                    R.id.mars_distance,
                    controller.getDistance(MARS).toFormattedString()
                )
                setTextViewText(
                    R.id.venus_distance,
                    controller.getDistance(VENUS).toFormattedString()
                )
                setTextViewText(
                    R.id.mercury_distance,
                    controller.getDistance(MERCURY).toFormattedString()
                )
            }
    }

    private fun configureSmallView(context: Context, widgetId: Int): RemoteViews {
        val pendingIntent = createPendingClickIntent(context, widgetId)

        val currentPlanet = controller.getCurrentPlanet(widgetId)

        val distance = controller.getDistance(currentPlanet).toFormattedString()

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

    private fun doAsync(block: suspend () -> Unit) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob()).launch {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)

    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)

        for (id in appWidgetIds ?: return) {
            controller.onWidgetRemoved(id)
        }
    }

    companion object {

        fun updateWidget(context: Context, appWidgetIds: IntArray) {
            Log.d("AAAAA", "sending update intent")
            val intent = createUpdateIntent(context, appWidgetIds)
            context.sendBroadcast(intent)
        }

        private fun createUpdateIntent(context: Context, appWidgetIds: IntArray): Intent =
            Intent(context, PlanetWidget::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            }
    }
}

private const val ACTION_NEXT_PLANET = "ACTION_NEXT_PLANET"
private const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"
