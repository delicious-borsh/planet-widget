package com.example.planetwidget

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.planetwidget.presentation.PlanetWidgetController
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateDistancesWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val planetWidgetController: PlanetWidgetController,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            planetWidgetController.updatePlanetDistances()
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

    companion object {

        private const val WORKER_NAME = "UpdateDistancesWorker"
        private const val REPEAT_INTERVAL_MINUTES = 15L

        fun startWorker(context: Context) {
            val updateRequest =
                PeriodicWorkRequestBuilder<UpdateDistancesWorker>(
                    REPEAT_INTERVAL_MINUTES,
                    TimeUnit.MINUTES
                )
                    .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORKER_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    updateRequest
                )
        }

        fun stopWorker(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork(WORKER_NAME)
        }
    }
}