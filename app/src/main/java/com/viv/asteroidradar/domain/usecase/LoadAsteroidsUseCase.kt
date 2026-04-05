package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository

class LoadAsteroidsUseCase(
    private val repository: AsteroidsRepository
) {
    suspend fun execute() {
        repository.loadAsteroids()
    }
}