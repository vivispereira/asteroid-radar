package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import com.viv.asteroidradar.domain.PictureOfDay
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetDailyPictureUseCaseTest {

    private lateinit var repository: AsteroidsRepository
    private lateinit var useCase: GetDailyPictureUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = GetDailyPictureUseCase(repository)
    }

    @Test
    fun `execute returns image from repository when mediaType is image`() = runTest {
        val picture = PictureOfDay(
            mediaType = "image",
            title = "Nebula",
            url = "https://apod.nasa.gov/apod/image/nebula.jpg"
        )
        coEvery { repository.getDailyPicture() } returns picture

        val result = useCase.execute()

        assertEquals(picture, result)
        coVerify(exactly = 1) { repository.getDailyPicture() }
    }

    @Test
    fun `execute returns fallback image when mediaType is video`() = runTest {
        val videoPicture = PictureOfDay(
            mediaType = "video",
            title = "Some Video",
            url = "https://youtube.com/some-video"
        )
        coEvery { repository.getDailyPicture() } returns videoPicture

        val result = useCase.execute()

        assertEquals("image", result.mediaType)
        assertEquals("A Starburst Spiral Galaxy", result.title)
        assertEquals("https://apod.nasa.gov/apod/image/2604/ngc3310_gemini_1837.jpg", result.url)
    }

    @Test
    fun `execute returns fallback image when mediaType is unknown`() = runTest {
        val unknownMedia = PictureOfDay(
            mediaType = "audio",
            title = "Space Sounds",
            url = "https://example.com/audio.mp3"
        )
        coEvery { repository.getDailyPicture() } returns unknownMedia

        val result = useCase.execute()

        assertEquals("image", result.mediaType)
        assertEquals("A Starburst Spiral Galaxy", result.title)
        assertEquals("https://apod.nasa.gov/apod/image/2604/ngc3310_gemini_1837.jpg", result.url)
    }

    @Test(expected = Exception::class)
    fun `execute propagates exception when repository fails`() = runTest {
        coEvery { repository.getDailyPicture() } throws Exception("Network error")

        useCase.execute()
    }
}

