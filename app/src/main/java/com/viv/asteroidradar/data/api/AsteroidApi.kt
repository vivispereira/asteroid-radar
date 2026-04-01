package com.viv.asteroidradar.data.api

import retrofit2.Retrofit

class AsteroidApi(retrofit: Retrofit) {

    val service: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}