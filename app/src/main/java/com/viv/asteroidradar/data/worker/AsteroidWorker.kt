package com.viv.asteroidradar.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.viv.asteroidradar.data.repository.repository
import com.viv.asteroidradar.domain.usecase.LoadAsteroidsUseCase


class AsteroidWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val loadAsteroidsUseCase by lazy {
        LoadAsteroidsUseCase(
            repository = repository
        )
    }

    override suspend fun doWork(): Result {
        return try {
            loadAsteroidsUseCase.execute()
            Result.success()
        } catch (ex: Exception) {
            Log.e("AsteroidWorker", ex.message, ex)
            Result.failure()
        }
    }
}