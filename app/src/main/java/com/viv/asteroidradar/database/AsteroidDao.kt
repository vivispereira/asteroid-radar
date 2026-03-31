package com.viv.asteroidradar.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase

@Dao
interface AsteroidDao {
    @Query("select * from AsteroidDBItem where date(closeApproachDate) >= date(:date) order by closeApproachDate asc")
    fun getAsteroids(date: String): List<AsteroidDBItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidDBItems: AsteroidDBItem)
}

@Database(entities = [AsteroidDBItem::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}