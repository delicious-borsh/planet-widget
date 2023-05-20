package com.example.planetwidget

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.planetwidget.presentation.PlanetWidgetController
import javax.inject.Inject

class UpdateDistancesWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val planetWidgetController: PlanetWidgetController,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("AAAAA", "WORKKKKKKK")
        try {
            Log.d("AAAAA", "updating distances")
            planetWidgetController.updatePlanetDistances()
            Log.d("AAAAA", "updating widget")
            planetWidgetController.onDataUpdated(appContext)
        } catch (ex: Exception) {
            return Result.failure()

        }

        return Result.success()
    }

    class UpdateDistancesWorkerFactory @Inject constructor(
        private val planetWidgetController: PlanetWidgetController
    ) : WorkerFactory() {

        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return UpdateDistancesWorker(appContext, workerParameters, planetWidgetController)
        }
    }
}