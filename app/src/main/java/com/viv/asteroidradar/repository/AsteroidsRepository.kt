package com.viv.asteroidradar.repository

import com.viv.asteroidradar.Asteroid
import com.viv.asteroidradar.BuildConfig
import com.viv.asteroidradar.Constants
import com.viv.asteroidradar.PictureOfDay
import com.viv.asteroidradar.api.AsteroidApi
import com.viv.asteroidradar.api.parseAsteroidsJsonResult
import com.viv.asteroidradar.database.AsteroidDao
import com.viv.asteroidradar.database.AsteroidDBItem
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidsRepository(
    private val asteroidApi: AsteroidApi,
    private val dao: AsteroidDao
) {

    suspend fun loadAsteroids() {
        val today = Date()

        val apiResponse = asteroidApi.service.getAsteroids(today.formatDate(), BuildConfig.NASA_API_KEY)
        val json = JSONObject(apiResponse)
        val asteroids = parseAsteroidsJsonResult(json)
        dao.insertAll(*asteroids.map {
            AsteroidDBItem(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }.toTypedArray())
    }

    private fun Date.formatDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        return formatter.format(this)
    }

    fun getAsteroids(): List<Asteroid> {
        val today = Date()
        return dao.getAsteroids(today.formatDate()).map {
            Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }
    }

    suspend fun getDailyPicture(): PictureOfDay {
        return asteroidApi.service.getDailyPicture(BuildConfig.NASA_API_KEY)
    }

}