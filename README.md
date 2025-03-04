# GO Train Tracker Android

A modern Android application that displays real-time GO Train information, built with Jetpack Compose and following clean architecture principles. This is the Android version of the [GO Train Tracker iOS](https://github.com/prasannajeet/Go-Train-Tracker-iOS) app.

## Features

### Current Features
* **Train Schedule Viewing**
  * View real-time train schedules between stations
  * Filter trains by status (On-time, Delayed, Cancelled)
  * Refresh schedules manually
  * Beautiful Material Design 3 UI with animations

* **Station Management**
  * View all GO Train stations
  * Set home station preferences
  * Station search and filtering
  * Local storage of station data

* **Union Station Integration**
  * Real-time Union Station departures
  * Platform information
  * Train status updates

* **Settings & Preferences**
  * Customize app appearance
  * Manage notifications
  * Set default stations
  * Data refresh preferences

### Upcoming Features
* Real-time train tracking with GPS
* Custom notifications for specific trains
* Journey planning with multiple stops
* Offline mode support
* Widget support for quick schedule viewing
* Dark mode support
* Accessibility improvements

## Technical Implementation

### Architecture
The app follows clean architecture principles with distinct layers:

* **Data Layer**
  * Ktor-based API client for Metrolinx API integration
  * Room database for local storage
  * DTOs for API responses
  * Repository implementations

* **Domain Layer**
  * Domain models (`TrainTrip`, `GOTrainStation`)
  * Use cases for business logic
  * Repository interfaces
  * Error handling with Arrow.kt

* **Presentation Layer**
  * MVVM architecture with ViewModels
  * Jetpack Compose UI components
  * Reusable UI components
  * State management with Kotlin Flow

### Key Components

* **ViewModels**
  * `TrainScheduleViewModel`: Manages train schedule data
  * `OnboardingViewModel`: Handles first-time setup
  * `MainTabViewModel`: Manages bottom navigation state

* **Screens**
  * `MainTabScreen`: Main screen with bottom navigation
  * `TrainScheduleScreen`: Train schedule display
  * `TrainsFromStationScreen`: Station-specific departures
  * `UnionStationDeparturesScreen`: Union Station arrivals/departures
  * `SettingsScreen`: App configuration

* **Use Cases**
  * `GetAllGoTrainStationsUseCase`: Fetches all GO Train stations
  * `GetScheduleBetweenStopsUseCase`: Gets train schedules
  * `ManageHomeStationUseCase`: Handles home station preferences

### Dependencies
* Jetpack Compose for modern UI
* Koin for dependency injection
* Ktor for networking
* Arrow.kt for functional programming
* Kotlinx.datetime for date/time handling
* Room for local storage
* Kotlin Coroutines for async operations
* Material Design 3 for UI components

## Project Structure

```
app/
├── data/
│   ├── api/         # API client and DTOs
│   ├── db/          # Room database
│   ├── local/       # Local data source
│   └── remote/      # Remote data source
├── domain/
│   ├── model/       # Domain models
│   ├── repository/  # Repository interfaces
│   └── usecase/     # Use cases
├── presentation/
│   ├── components/  # Reusable UI components
│   ├── feature/     # Feature-specific screens
│   ├── navigation/  # Navigation setup
│   ├── theme/       # App theme and styling
│   └── utils/       # Utility classes
└── di/              # Dependency injection
```

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Create a `local.properties` file with your GO Train API key:
```properties
MY_GO_TRAIN_API=your_api_key_here
```
4. Build and run the app

## Contributing

Please read our [Contributing Guidelines](CONTRIBUTING.md) and [Pull Request Rules](PULL_REQUEST_TEMPLATE.md) before submitting any changes.

## License

MIT License with Additional Restrictions

Copyright (c) 2024

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software, including without limitation the rights to use, copy, modify, merge, publish, distribute, and/or sublicense copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
2. Commercial use, distribution, or publication of the Software or any derivative works is strictly prohibited without explicit written permission from the copyright holder.
3. The Software is provided for educational and personal use only.
4. Any derivative works must maintain the same license and restrictions.
5. The Software may not be used in any commercial applications or services.
6. The Software may not be redistributed or resold in any form.
7. The Software may not be used to create competing products or services.
8. The Software may not be used in any way that could damage the reputation of the original authors or the GO Train service.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

### Topics

android kotlin jetpack-compose metrolinx clean-architecture go-train transit 