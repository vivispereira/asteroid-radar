package com.viv.asteroidradar.domain.usecase

import com.viv.asteroidradar.data.repository.AsteroidsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoadAsteroidsUseCaseTest {

    private lateinit var repository: AsteroidsRepository
    private lateinit var useCase: LoadAsteroidsUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = LoadAsteroidsUseCase(repository)
    }

    @Test
    fun `execute delegates to repository loadAsteroids`() = runTest {
        useCase.execute()

        coVerify(exactly = 1) { repository.loadAsteroids() }
    }

    @Test(expected = Exception::class)
    fun `execute propagates exception when repository fails`() = runTest {
        coEvery { repository.loadAsteroids() } throws Exception("Network error")

        useCase.execute()
    }
}

