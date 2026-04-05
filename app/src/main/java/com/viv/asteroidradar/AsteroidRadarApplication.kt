package com.viv.asteroidradar

import android.app.Application
import androidx.room.Room
import com.viv.asteroidradar.data.database.AsteroidDatabase

class AsteroidRadarApplication : Application() {

    lateinit var database: AsteroidDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        synchronized(AsteroidDatabase::class.java) {
            if (!::database.isInitialized) {
                database = Room.databaseBuilder(
                    applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroid"
                ).build()
            }
        }
    }

    companion object {
        private lateinit var instance: AsteroidRadarApplication

        fun getInstance(): AsteroidRadarApplication = instance
    }
}
