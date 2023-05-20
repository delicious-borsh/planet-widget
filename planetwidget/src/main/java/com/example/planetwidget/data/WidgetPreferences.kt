package com.example.planetwidget.data

import android.content.Context
import javax.inject.Inject

class WidgetPreferences @Inject constructor(context: Context) {

    private val preferences by lazy {
        context.getSharedPreferences(WIDGET_SIZE_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun addWidgetIfNotPresent(widgetId: Int) {
        val widgetIdSet = preferences.getStringSet(KEY_WIDGETS_ID_SET, emptySet()) ?: emptySet()
        if (widgetIdSet.contains(widgetId.toString())) return

        val mutableWidgetSet = HashSet<String>(widgetIdSet)
        mutableWidgetSet.add(widgetId.toString())

        preferences
            .edit()
            .putStringSet(KEY_WIDGETS_ID_SET, mutableWidgetSet)
            .apply()
    }

    fun removeWidget(widgetId: Int) {
        val widgetIdSet = preferences.getStringSet(KEY_WIDGETS_ID_SET, null) ?: return
        val mutableWidgetSet = HashSet<String>(widgetIdSet)
        mutableWidgetSet.remove(widgetId.toString())

        preferences
            .edit()
            .putStringSet(KEY_WIDGETS_ID_SET, mutableWidgetSet)
            .apply()
    }

    fun getWidgetList(): Set<Int> =
        preferences.getStringSet(KEY_WIDGETS_ID_SET, emptySet())
            ?.map { it.toInt() }
            ?.toSet() ?: emptySet()

    fun updateHeight(widgetId: Int, heightInCells: Int) {
        preferences
            .edit()
            .putInt(getKey(widgetId), heightInCells)
            .apply()
    }

    fun getHeight(widgetId: Int): Int = preferences.getInt(getKey(widgetId), 1)

    private fun getKey(widgetId: Int): String = widgetId.toString() + POSTFIX_HEIGHT_IN_CELLS

    companion object {
        private const val WIDGET_SIZE_PREFERENCES = "WIDGET_SIZE_PREFERENCES"
        private const val POSTFIX_HEIGHT_IN_CELLS = "_HEIGHT_IN_CELLS"

        private const val KEY_WIDGETS_ID_SET = "KEY_WIDGETS_ID_SET"
    }
}
