package com.viv.asteroidradar.ui

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest

/**
 * Helper class to set up and manage MockWebServer for API mocking in Espresso tests
 */
object MockApiHelper {

    private lateinit var mockWebServer: MockWebServer

    /**
     * Start the MockWebServer and return the base URL
     */
    fun startMockServer(): String {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        return mockWebServer.url("").toString()
    }

    /**
     * Stop the MockWebServer
     */
    fun stopMockServer() {
        if (::mockWebServer.isInitialized) {
            mockWebServer.shutdown()
        }
    }

    /**
     * Enqueue a mock response for asteroid API
     */
    fun enqueueMockAsteroidResponse() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getMockAsteroidJson())

        mockWebServer.enqueue(mockResponse)
    }

    /**
     * Enqueue a mock response for picture of the day API
     */
    fun enqueueMockPictureOfDayResponse() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getMockPictureOfDayJson())

        mockWebServer.enqueue(mockResponse)
    }

    /**
     * Get the last recorded request (useful for debugging)
     */
    fun getLastRequest(): RecordedRequest? {
        return try {
            mockWebServer.takeRequest()
        } catch (e: InterruptedException) {
            null
        }
    }

    /**
     * Mock JSON response for asteroids
     */
    private fun getMockAsteroidJson(): String {
        return """
            {
                "near_earth_objects": {
                    "2026-04-01": [
                        {
                            "id": "2000433",
                            "name": "433 Eros (A898 PA)",
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_min": 16.026341043,
                                    "estimated_diameter_max": 35.850838572
                                }
                            },
                            "is_potentially_hazardous_asteroid": false,
                            "close_approach_data": [
                                {
                                    "close_approach_date": "2026-04-01",
                                    "relative_velocity": {
                                        "kilometers_per_second": "12.5"
                                    },
                                    "miss_distance": {
                                        "astronomical": "0.25"
                                    }
                                }
                            ],
                            "absolute_magnitude_h": 10.4
                        },
                        {
                            "id": "2000719",
                            "name": "719 Albert (A911 SB)",
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_min": 1.13,
                                    "estimated_diameter_max": 2.26
                                }
                            },
                            "is_potentially_hazardous_asteroid": true,
                            "close_approach_data": [
                                {
                                    "close_approach_date": "2026-04-01",
                                    "relative_velocity": {
                                        "kilometers_per_second": "18.75"
                                    },
                                    "miss_distance": {
                                        "astronomical": "0.15"
                                    }
                                }
                            ],
                            "absolute_magnitude_h": 15.94
                        },
                        {
                            "id": "2163249",
                            "name": "163249",
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_min": 0.11,
                                    "estimated_diameter_max": 0.22
                                }
                            },
                            "is_potentially_hazardous_asteroid": false,
                            "close_approach_data": [
                                {
                                    "close_approach_date": "2026-04-01",
                                    "relative_velocity": {
                                        "kilometers_per_second": "5.25"
                                    },
                                    "miss_distance": {
                                        "astronomical": "0.08"
                                    }
                                }
                            ],
                            "absolute_magnitude_h": 20.5
                        }
                    ],
                    "2026-04-02": [
                        {
                            "id": "2000001",
                            "name": "1 Ceres",
                            "estimated_diameter": {
                                "kilometers": {
                                    "estimated_diameter_min": 930,
                                    "estimated_diameter_max": 975
                                }
                            },
                            "is_potentially_hazardous_asteroid": false,
                            "close_approach_data": [
                                {
                                    "close_approach_date": "2026-04-02",
                                    "relative_velocity": {
                                        "kilometers_per_second": "22.5"
                                    },
                                    "miss_distance": {
                                        "astronomical": "0.35"
                                    }
                                }
                            ],
                            "absolute_magnitude_h": 3.34
                        }
                    ]
                }
            }
        """.trimIndent()
    }

    /**
     * Mock JSON response for picture of the day
     */
    private fun getMockPictureOfDayJson(): String {
        return """
            {
                "copyright": "Test Copyright",
                "date": "2026-04-01",
                "explanation": "This is a mock astronomy picture of the day for testing purposes.",
                "hdurl": "https://apod.nasa.gov/apod/image/2604/test_image_hd.jpg",
                "media_type": "image",
                "service_version": "v1",
                "title": "Test Astronomy Picture",
                "url": "https://apod.nasa.gov/apod/image/2604/test_image.jpg"
            }
        """.trimIndent()
    }
}

