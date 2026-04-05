package com.viv.asteroidradar.data.repository

import com.viv.asteroidradar.AsteroidRadarApplication
import com.viv.asteroidradar.data.api.getAsteroidApi

val repository by lazy {
    AsteroidsRepository(
        asteroidApi = getAsteroidApi(),
        dao = AsteroidRadarApplication.getInstance().database.asteroidDao
    )
}