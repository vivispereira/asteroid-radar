# Asteroid Radar App

A modern Android application built with Kotlin and Compose that displays real-time asteroid data from NASA's Near-Earth Object API. The app provides users with information about asteroids passing near Earth, including detailed asteroid properties and the Astronomy Picture of the Day.

## Table of Contents

- [Features](#features)
- [Project Setup](#project-setup)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [API Configuration](#api-configuration)
- [Project Structure](#project-structure)
- [Building and Running](#building-and-running)
- [Key Components](#key-components)

## Features

- 📱 Display a list of nearby asteroids
- 🔍 View detailed asteroid information
- 🖼️ Astronomy Picture of the Day feature
- 🌐 Real-time data from NASA API
- 💾 Local caching with Room database
- 🔄 Background data synchronization with WorkManager
- 🎨 Modern UI built with Jetpack Compose and Material Design 3

## Project Setup

### Prerequisites

- Android Studio (latest version recommended)
- JDK 11 or higher
- Gradle 6.1.1 or higher (automatically managed by Gradle wrapper)
- An internet connection for API calls

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd asteroid-radar
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run on an emulator or device**
   ```bash
   ./gradlew installDebug
   ```

### Android Requirements

- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 35 (Android 15)
- **Compile SDK**: API 35
- **JVM Target**: Java 11

## Architecture

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:

```
┌─────────────────────────────────────────┐
│          UI Layer (Compose)             │
│  (MainActivity, DetailScreen, etc.)     │
└────────────────┬────────────────────────┘
                 │
┌─────────────────▼────────────────────────┐
│        ViewModel Layer                   │
│  (LiveData/StateFlow management)        │
└────────────────┬────────────────────────┘
                 │
┌─────────────────▼────────────────────────┐
│      Repository Layer                    │
│  (AsteroidsRepository)                  │
│  Abstracts data sources                 │
└────────────────┬────────────────────────┘
                 │
     ┌───────────┴───────────┐
     │                       │
┌────▼──────────┐  ┌────────▼──────────┐
│  API Layer    │  │  Database Layer   │
│  (Retrofit)   │  │  (Room)           │
└───────────────┘  │  Local cache      │
                   └───────────────────┘
```

### Layer Responsibilities

- **UI Layer**: Handles user interactions and displays data using Jetpack Compose
- **ViewModel Layer**: Manages UI state and business logic, survives configuration changes
- **Repository Layer**: Provides a single source of truth for data, handles caching strategy
- **API Layer**: Manages HTTP requests to NASA's API using Retrofit
- **Database Layer**: Handles local data persistence using Room

## Tech Stack

### Android Framework & UI

| Component | Version | Purpose |
|-----------|---------|---------|
| Jetpack Compose | 2024.09.00 | Modern declarative UI |
| Material Design 3 | 1.13.0 | Material Design components |
| AppCompat | 1.7.1 | Compatibility library |
| ConstraintLayout | 2.2.1 | Layout management |

### Networking

| Component | Version | Purpose |
|-----------|---------|---------|
| Retrofit | 2.11.0 | HTTP client |
| Moshi | 1.15.0 | JSON serialization/deserialization |
| OkHttp | (via Retrofit) | HTTP interceptor |

### Data Persistence

| Component | Version | Purpose |
|-----------|---------|---------|
| Room | 2.8.4 | Local database |
| Kotlin Serialization | - | Data class support |

### Lifecycle & Architecture

| Component | Version | Purpose |
|-----------|---------|---------|
| Lifecycle Runtime | 2.6.1 | Lifecycle awareness |
| ViewModel | 2.10.0 | State management |
| LiveData | 2.10.0 | Observable data |
| Navigation | 2.9.7 | Fragment navigation |
| WorkManager | 2.11.2 | Background tasks |

### Utilities

| Component | Version | Purpose |
|-----------|---------|---------|
| RecyclerView | 1.4.0 | List displays |
| Picasso | 2.8 | Image loading |
| Kotlin Coroutines | (via adapter) | Asynchronous programming |

### Testing

| Component | Version | Purpose |
|-----------|---------|---------|
| JUnit | 4.13.2 | Unit testing |
| AndroidX Test | 1.1.5 | Android testing |
| Espresso | 3.5.1 | UI testing |

## API Configuration

### NASA Near-Earth Object API

The app integrates with NASA's publicly available API for asteroid data.

**Base URL**: `https://api.nasa.gov/`

**API Key**: Configured in `app/build.gradle.kts`
```gradle
buildConfigField("String", "NASA_API_KEY", "\"your-api-key-here\"")
```

### Permissions

The app requires the following permissions (declared in `AndroidManifest.xml`):

- `android.permission.INTERNET` - For API calls
- `android.permission.ACCESS_WIFI_STATE` - For network state checking

## Project Structure

```
app/src/main/java/com/viv/asteroidradar/
├── MainActivity.kt                    # Entry point
├── Asteroid.kt                        # Asteroid data model
├── PictureOfDay.kt                   # APOD model
├── Constants.kt                       # App constants
├── BindingAdapters.kt                # Data binding adapters
│
├── api/                              # Networking layer
│   ├── AsteroidApi.kt               # API response models
│   ├── AsteroidApiService.kt        # Retrofit service interface
│   └── NetworkUtils.kt              # Network utility functions
│
├── database/                         # Local persistence layer
│   ├── AsteroidDao.kt               # Data Access Object
│   ├── AsteroidEntities.kt          # Room entity classes
│   └── Room.kt                      # Database configuration
│
├── repository/                       # Data management layer
│   └── AsteroidsRepository.kt       # Single source of truth
│
├── main/                            # Main screen (list)
│   ├── MainFragment.kt
│   ├── MainViewModel.kt
│   └── AsteroidAdapter.kt
│
├── detail/                          # Detail screen
│   ├── DetailFragment.kt
│   └── DetailViewModel.kt
│
└── worker/                          # Background tasks
    └── RefreshDataWorker.kt         # WorkManager tasks
```

## Building and Running

### Using Android Studio

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Select a device or emulator
4. Click **Run** (Shift + F10) or use the menu: **Run > Run 'app'**

### Using Command Line

**Debug Build**:
```bash
./gradlew assembleDebug
./gradlew installDebug
```

**Release Build**:
```bash
./gradlew assembleRelease
```

**Run Tests**:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

**Clean Build**:
```bash
./gradlew clean build
```

## Key Components

### MainActivity

Entry point of the application that hosts fragments and manages navigation.

### Repository Pattern

`AsteroidsRepository` provides a single source of truth for data:
- Fetches from API if data is not cached
- Stores data in local database for offline access
- Manages refresh strategy (7-day default window)

### ViewModel

Manages UI state and handles communication between views and repository:
- Uses LiveData for reactive updates
- Survives configuration changes
- Handles coroutines for async operations

### Room Database

Local SQLite database for:
- Caching asteroid data
- Reducing API calls
- Providing offline functionality

### Retrofit Service

HTTP client configuration:
- Base URL: NASA API endpoint
- JSON parsing with Moshi
- Coroutines adapter for async requests

### WorkManager

Background synchronization:
- Periodic data refresh
- Network-aware scheduling
- Device resource-aware execution

## Notes

- The app uses **Gradle Version Catalog** (`gradle/libs.versions.toml`) for dependency management
- **Kotlin Compose** is the primary UI framework
- **Data Binding** is enabled for view-model binding
- **Navigation Safe Args** is used for type-safe fragment navigation
- **KSP** (Kotlin Symbol Processing) is enabled for faster annotation processing

