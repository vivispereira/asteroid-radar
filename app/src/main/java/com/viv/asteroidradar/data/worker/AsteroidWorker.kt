package com.viv.asteroidradar.data.worker

import android.content.Context
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.viv.asteroidradar.data.AsteroidsRepository
import com.viv.asteroidradar.data.api.getAsteroidApi
import com.viv.asteroidradar.data.database.getDatabase


class AsteroidWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    private val repository = AsteroidsRepository(
        asteroidApi = getAsteroidApi(),
        dao = context.getDatabase().asteroidDao
    )

    override suspend fun doWork(): Result {
        return if (isWifiConnect() && isCharging()) {
            try {
                repository.loadAsteroids()
            } catch (ex: Exception) {
                Log.e("AsteroidWorker", ex.message, ex)
                Result.failure()
            }
            Result.success()
        } else {
            Result.success()
        }
    }

    private fun isCharging(): Boolean {
        val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        return batteryManager?.isCharging ?: false
    }

    private fun isWifiConnect(): Boolean {
        val wifiMgr =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        return if (wifiMgr?.isWifiEnabled == true) { // Wi-Fi adapter is ON
            val wifiInfo = wifiMgr.connectionInfo
            wifiInfo.networkId != -1 // it is always - 1 in emulator, works with real network
        } else {
            false
        }
    }


}