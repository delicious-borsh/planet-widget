package com.example.planetwidget.presentation

import android.content.Context
import javax.inject.Inject

class WidgetSizePreferences @Inject constructor(context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(WIDGET_SIZE_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun updateHeight(widgetId: Int, heightInCells: Int) {
        preferences
            .edit()
            .putInt(getKey(widgetId), heightInCells)
            .apply()
    }

    fun getHeight(widgetId: Int): Int = preferences.getInt(getKey(widgetId), 1)

    private fun getKey(widgetId: Int): String = widgetId.toString() + POSTFIX_HEIGHT_IN_CELLS
}

private const val WIDGET_SIZE_PREFERENCES = "WIDGET_SIZE_PREFERENCES"
private const val POSTFIX_HEIGHT_IN_CELLS = "_HEIGHT_IN_CELLS"