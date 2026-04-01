package com.viv.asteroidradar.data.api

import com.viv.asteroidradar.domain.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query


interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("api_key") key: String
    ): String

    @GET("planetary/apod")
    suspend fun getDailyPicture(
        @Query("api_key") key: String
    ): PictureOfDay
}

