package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import com.viv.asteroidradar.domain.PictureOfDay

class GetDailyPictureUseCase(
    private val repository: AsteroidsRepository
) {
    suspend fun execute(): PictureOfDay {
        val image = repository.getDailyPicture()
        return if (image.mediaType != "image") {
            PictureOfDay(
                mediaType = "image",
                title = "A Starburst Spiral Galaxy",
                url = "https://apod.nasa.gov/apod/image/2604/ngc3310_gemini_1837.jpg"
            )
        } else {
            image
        }
    }
}