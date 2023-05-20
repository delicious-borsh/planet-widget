package com.example.planetwidget.di

import com.example.planetwidget.UpdateDistancesWorker

interface PlanetWidgetFeature {

    fun getUpdateDistancesWorkerFactory(): UpdateDistancesWorker.UpdateDistancesWorkerFactory
}