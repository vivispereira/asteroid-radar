package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import com.viv.asteroidradar.domain.Asteroid

class GetAsteroidsUseCase(
    private val repository: AsteroidsRepository
) {
    private val loadAsteroidsUseCase: LoadAsteroidsUseCase = LoadAsteroidsUseCase(repository)

    suspend fun execute(): List<Asteroid> {
        var asteroids = repository.getAsteroids()
        if (asteroids.isEmpty()) {
            loadAsteroidsUseCase.execute()
            asteroids = repository.getAsteroids()
        }
        return asteroids
    }
}