package com.viv.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.viv.asteroidradar.Asteroid
import com.viv.asteroidradar.PictureOfDay
import com.viv.asteroidradar.R
import com.viv.asteroidradar.api.AsteroidApi
import com.viv.asteroidradar.database.getDatabase
import com.viv.asteroidradar.repository.AsteroidsRepository
import com.viv.asteroidradar.worker.AsteroidWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(application: Application) : AndroidViewModel(application) {

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

    private val database = application.getDatabase()

    private val repository = AsteroidsRepository(
        asteroidApi = AsteroidApi,
        dao = database.asteroidDao
    )
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
                val asteroids = repository.getAsteroids()
                if (asteroids.isEmpty()) {
                    repository.loadAsteroids()
                    _asteroids.postValue(repository.getAsteroids())
                } else {
                    _asteroids.postValue(asteroids)
                }
            } catch (ex: Exception) {
                _error.postValue(R.string.error_asteroid_list)
            }
            try {
                _picture.postValue(repository.getDailyPicture())
            } catch (ex: Exception) {
                _error.postValue(R.string.error_image_of_day)
            }
            _loading.postValue(false)
        }
    }

    fun showAsteroidDetails() {
        _asteroids.value = null
    }
}