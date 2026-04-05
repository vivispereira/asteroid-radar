package com.viv.asteroidradar.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.viv.asteroidradar.R
import com.viv.asteroidradar.data.repository.repository
import com.viv.asteroidradar.data.worker.AsteroidWorker
import com.viv.asteroidradar.domain.Asteroid
import com.viv.asteroidradar.domain.PictureOfDay
import com.viv.asteroidradar.domain.usecase.GetAsteroidsUseCase
import com.viv.asteroidradar.domain.usecase.GetDailyPictureUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

public class MainViewModel(application: Application) : AndroidViewModel(application) {

    init {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueue(
            PeriodicWorkRequest.Builder(
                AsteroidWorker::class.java,
                1,
                TimeUnit.DAYS
            ).build()
        )
    }

    private val getAsteroidsUseCase = GetAsteroidsUseCase(repository)
    private val getDailyPictureUseCase = GetDailyPictureUseCase(repository)
    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int>
        get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _picture = MutableLiveData<PictureOfDay>()
    val picture: LiveData<PictureOfDay>
        get() = _picture

    fun loadAsteroids() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val asteroids = getAsteroidsUseCase.execute()
                _asteroids.postValue(asteroids)
            } catch (ex: Exception) {
                _error.postValue(R.string.error_asteroid_list)
            }
            try {
                _picture.postValue(getDailyPictureUseCase.execute())
            } catch (ex: Exception) {
                _error.postValue(R.string.error_image_of_day)
            }
            _loading.postValue(false)
        }
    }

    fun showAsteroidDetails() {
        _asteroids.value = emptyList()
    }
}