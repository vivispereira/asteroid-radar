package com.viv.asteroidradar.data.api

import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
class AsteroidParserTest {

    private lateinit var testJsonObject: JSONObject

    @Before
    fun setUp() {
        // Create a sample JSON response similar to NASA's API
        testJsonObject = JSONObject(
            """
            {
                "near_earth_objects": {
                    "2026-04-01": [
                        {
                            "id": "2000433",
                            "name": "Eros (433)",
                            "absolute_magnitude_h": 10.4,
                            "is_potentially_hazardous_asteroid": false,
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_max": 34.05
                                }
                            },
                            "close_approach_data": [
                                {
                                    "relative_velocity": {
                                        "kilometers_per_second": 12.5
                                    },
                                    "miss_distance": {
                                        "astronomical": 0.25
                                    }
                                }
                            ]
                        }
                    ],
                    "2026-04-02": [
                        {
                            "id": "2000719",
                            "name": "Albert (719)",
                            "absolute_magnitude_h": 15.94,
                            "is_potentially_hazardous_asteroid": true,
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_max": 2.26
                                }
                            },
                            "close_approach_data": [
                                {
                                    "relative_velocity": {
                                        "kilometers_per_second": 18.75
                                    },
                                    "miss_distance": {
                                        "astronomical": 0.15
                                    }
                                }
                            ]
                        },
                        {
                            "id": "2163249",
                            "name": "(163249)",
                            "absolute_magnitude_h": 20.5,
                            "is_potentially_hazardous_asteroid": false,
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_max": 0.22
                                }
                            },
                            "close_approach_data": [
                                {
                                    "relative_velocity": {
                                        "kilometers_per_second": 5.25
                                    },
                                    "miss_distance": {
                                        "astronomical": 0.08
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()
        )
    }

    @Test
    fun parseAsteroidsJsonResult_ValidJson_ReturnsList() {
        val result = parseAsteroidsJsonResult(testJsonObject)

        assertNotNull(result)
        assertFalse(result.isEmpty())
    }

    @Test
    fun parseAsteroidsJsonResult_ValidJson_ReturnsCorrectNumberOfAsteroids() {
        val result = parseAsteroidsJsonResult(testJsonObject)

        // Should have 3 asteroids total (1 on 04-01, 2 on 04-02)
        assertEquals(3, result.size)
    }

    @Test
    fun parseAsteroidsJsonResult_ValidJson_ParsesFirstAsteroidCorrectly() {
        val result = parseAsteroidsJsonResult(testJsonObject)
        val firstAsteroid = result[0]

        assertEquals(2000433L, firstAsteroid.id)
        assertEquals("Eros (433)", firstAsteroid.codename)
        assertEquals(10.4, firstAsteroid.absoluteMagnitude, 0.0)
        assertEquals(34.05, firstAsteroid.estimatedDiameter, 0.01)
        assertEquals(12.5, firstAsteroid.relativeVelocity, 0.0)
        assertEquals(0.25, firstAsteroid.distanceFromEarth, 0.0)
        assertFalse(firstAsteroid.isPotentiallyHazardous)
    }

    @Test
    fun parseAsteroidsJsonResult_ValidJson_ParsesHazardousAsteroidCorrectly() {
        val result = parseAsteroidsJsonResult(testJsonObject)
        val hazardousAsteroid = result[1]

        assertEquals(2000719L, hazardousAsteroid.id)
        assertEquals("Albert (719)", hazardousAsteroid.codename)
        assertEquals(15.94, hazardousAsteroid.absoluteMagnitude, 0.0)
        assertEquals(2.26, hazardousAsteroid.estimatedDiameter, 0.01)
        assertEquals(18.75, hazardousAsteroid.relativeVelocity, 0.0)
        assertEquals(0.15, hazardousAsteroid.distanceFromEarth, 0.0)
        assertTrue(hazardousAsteroid.isPotentiallyHazardous)
    }

    @Test
    fun parseAsteroidsJsonResult_ValidJson_ParsesMultipleAsteroidsOnSameDay() {
        val result = parseAsteroidsJsonResult(testJsonObject)

        // Check that both asteroids on 2026-04-02 are present
        val asteroidsOnSecondDay = result.filter { it.closeApproachDate == "2026-04-02" }
        assertEquals(2, asteroidsOnSecondDay.size)
    }

    @Test
    fun parseAsteroidsJsonResult_EmptyNearEarthObjects_ReturnsEmptyList() {
        val emptyJson = JSONObject(
            """
            {
                "near_earth_objects": {}
            }
        """.trimIndent()
        )

        val result = parseAsteroidsJsonResult(emptyJson)

        assertNotNull(result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun parseAsteroidsJsonResult_NoMatchingDates_ReturnsEmptyList() {
        val jsonWithUnmatchedDates = JSONObject(
            """
            {
                "near_earth_objects": {
                    "2025-01-01": [
                        {
                            "id": "123456",
                            "name": "Test Asteroid",
                            "absolute_magnitude_h": 10.0,
                            "is_potentially_hazardous_asteroid": false,
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_max": 5.0
                                }
                            },
                            "close_approach_data": [
                                {
                                    "relative_velocity": {
                                        "kilometers_per_second": 10.0
                                    },
                                    "miss_distance": {
                                        "astronomical": 0.5
                                    }
                                }
                            ]
                        }
                    ]
                }
            }
        """.trimIndent()
        )

        val result = parseAsteroidsJsonResult(jsonWithUnmatchedDates)

        assertTrue(result.isEmpty())
    }

    @Test
    fun parseAsteroidsJsonResult_MultipleAsteroids_ParsesAllValues() {
        val result = parseAsteroidsJsonResult(testJsonObject)

        // Verify all asteroids have required fields populated
        for (asteroid in result) {
            assertNotEquals(0L, asteroid.id)
            assertNotNull(asteroid.codename)
            assertFalse(asteroid.codename.isEmpty())
            assertNotNull(asteroid.closeApproachDate)
            assertFalse(asteroid.closeApproachDate.isEmpty())
            assertNotEquals(0.0, asteroid.absoluteMagnitude)
            assertNotEquals(0.0, asteroid.estimatedDiameter)
            assertNotEquals(0.0, asteroid.relativeVelocity)
            assertNotEquals(0.0, asteroid.distanceFromEarth)
        }
    }

    @Test
    fun getNextSevenDaysFormattedDates_ReturnsList() {
        val result = getNextSevenDaysFormattedDates()

        assertNotNull(result)
        assertFalse(result.isEmpty())
    }

    @Test
    fun getNextSevenDaysFormattedDates_ReturnsCorrectNumberOfDates() {
        val result = getNextSevenDaysFormattedDates()

        // Should return 8 dates (today + 7 days)
        assertEquals(8, result.size)
    }

    @Test
    fun getNextSevenDaysFormattedDates_ReturnsFormattedDates() {
        val result = getNextSevenDaysFormattedDates()

        // Check that all dates follow the correct format (yyyy-MM-dd)
        val datePattern = Regex("\\d{4}-\\d{2}-\\d{2}")
        for (date in result) {
            assertTrue("Date $date does not match format yyyy-MM-dd", datePattern.matches(date))
        }
    }

    @Test
    fun getNextSevenDaysFormattedDates_DatesAreInSequence() {
        val result = getNextSevenDaysFormattedDates()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dates = mutableListOf<Long>()

        for (dateString in result) {
            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(dateString)!!
            // Get the time in milliseconds (midnight UTC)
            dates.add(calendar.timeInMillis)
        }

        // Verify each date is 24 hours after the previous
        for (i in 1 until dates.size) {
            val diff = dates[i] - dates[i - 1]
            val diffInDays = diff / (1000 * 60 * 60 * 24)

            assertEquals(
                "Date at index $i is not one day after previous date",
                1L,
                diffInDays
            )
        }
    }

    @Test
    fun getNextSevenDaysFormattedDates_StartsWithTodayOrNear() {
        val result = getNextSevenDaysFormattedDates()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val firstDate = dateFormat.parse(result[0])!!
        val today = Calendar.getInstance().time

        val diffInDays = Math.abs((firstDate.time - today.time) / (1000 * 60 * 60 * 24))

        // First date should be today or very close to it (within 1 day)
        assertTrue("First date is not within 1 day of today", diffInDays <= 1)
    }

    @Test
    fun getNextSevenDaysFormattedDates_ReturnsConsistentResults() {
        val result1 = getNextSevenDaysFormattedDates()
        val result2 = getNextSevenDaysFormattedDates()

        // Both calls should return the same number of dates
        assertEquals(result1.size, result2.size)

        // All corresponding dates should be the same
        for (i in result1.indices) {
            assertEquals(result1[i], result2[i])
        }
    }
}

