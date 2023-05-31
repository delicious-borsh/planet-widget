package com.example.planetwidget.presentation

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.planetwidget.UpdateDistancesWorker
import com.example.planetwidget.di.PlanetWidgetInjector
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class PlanetWidget : AppWidgetProvider() {

    @Inject
    lateinit var controller: PlanetWidgetController

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("PlanetWidget", "Some error occured", throwable)
    }

    private val remoteViewsFactory by lazy {
        RemoteViewsFactory(
            controller::getDistance,
            controller::getCurrentPlanet,
        )
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)

        doAsync { controller.updatePlanetDistances() }

        UpdateDistancesWorker.startWorker(context ?: return)
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

        val views = remoteViewsFactory.createViews(
            context,
            appWidgetId,
            heightInCells,
        )
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun doAsync(block: suspend () -> Unit) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + exceptionHandler).launch {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onDisabled(context: Context?) {

        UpdateDistancesWorker.stopWorker(context ?: return)

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

const val ACTION_NEXT_PLANET = "ACTION_NEXT_PLANET"
const val EXTRA_WIDGET_ID = "EXTRA_WIDGET_ID"
