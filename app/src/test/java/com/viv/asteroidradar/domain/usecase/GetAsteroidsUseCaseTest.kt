package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import com.viv.asteroidradar.domain.Asteroid
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAsteroidsUseCaseTest {

    private lateinit var repository: AsteroidsRepository
    private lateinit var useCase: GetAsteroidsUseCase

    private val sampleAsteroids = listOf(
        Asteroid(
            id = 1L,
            codename = "Eros",
            closeApproachDate = "2026-04-07",
            absoluteMagnitude = 10.4,
            estimatedDiameter = 34.05,
            relativeVelocity = 12.5,
            distanceFromEarth = 0.25,
            isPotentiallyHazardous = false
        ),
        Asteroid(
            id = 2L,
            codename = "Albert",
            closeApproachDate = "2026-04-08",
            absoluteMagnitude = 15.94,
            estimatedDiameter = 2.26,
            relativeVelocity = 18.75,
            distanceFromEarth = 0.15,
            isPotentiallyHazardous = true
        )
    )

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = GetAsteroidsUseCase(repository)
    }

    @Test
    fun `execute returns cached asteroids when available`() = runTest {
        every { repository.getAsteroids() } returns sampleAsteroids

        val result = useCase.execute()

        assertEquals(sampleAsteroids, result)
        assertEquals(2, result.size)
        verify(exactly = 1) { repository.getAsteroids() }
        coVerify(exactly = 0) { repository.loadAsteroids() }
    }

    @Test
    fun `execute loads from network when cache is empty`() = runTest {
        every { repository.getAsteroids() } returns emptyList() andThen sampleAsteroids

        val result = useCase.execute()

        assertEquals(sampleAsteroids, result)
        coVerify(exactly = 1) { repository.loadAsteroids() }
        verify(exactly = 2) { repository.getAsteroids() }
    }

    @Test
    fun `execute returns empty list when both cache and network have no data`() = runTest {
        every { repository.getAsteroids() } returns emptyList()

        val result = useCase.execute()

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.loadAsteroids() }
        verify(exactly = 2) { repository.getAsteroids() }
    }

    @Test
    fun `execute returns correct asteroid properties`() = runTest {
        every { repository.getAsteroids() } returns sampleAsteroids

        val result = useCase.execute()

        val hazardous = result.first { it.isPotentiallyHazardous }
        assertEquals("Albert", hazardous.codename)
        assertEquals(2L, hazardous.id)

        val nonHazardous = result.first { !it.isPotentiallyHazardous }
        assertEquals("Eros", nonHazardous.codename)
        assertEquals(1L, nonHazardous.id)
    }

    @Test(expected = Exception::class)
    fun `execute propagates exception when load fails`() = runTest {
        every { repository.getAsteroids() } returns emptyList()
        coEvery { repository.loadAsteroids() } throws Exception("Network error")

        useCase.execute()
    }
}
