package com.viv.asteroidradar.database

import android.content.Context
import androidx.room.Room

private lateinit var instance: AsteroidDatabase

fun Context.getDatabase(): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::instance.isInitialized) {
            instance = Room.databaseBuilder(
                applicationContext,
                AsteroidDatabase::class.java,
                "asteroid"
            ).build()
        }
    }
    return instance
}
