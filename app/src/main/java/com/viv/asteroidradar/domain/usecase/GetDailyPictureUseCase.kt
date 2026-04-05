package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import com.viv.asteroidradar.domain.PictureOfDay

class GetDailyPictureUseCase(
    private val repository: AsteroidsRepository
) {
    suspend fun execute(): PictureOfDay {
        return repository.getDailyPicture()
    }
}