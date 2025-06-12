Okay, this is an excellent plan for a multi-module Android project! Breaking it down feature-wise is a great strategy for scalability and maintainability.

Here's a detailed file tree structure for your project, incorporating all your requirements and following common MVVM and Koin best practices.

```
├── .gradle/
├── .idea/
├── app/
│   ├── build.gradle.kts
│   └── src/main/java/com/yourpackage/universityapp/
│       ├── UniversityApp.kt                   // Koin Application initialization
│       ├── MainActivity.kt                    // Main entry point, sets up navigation graph
│       └── navigation/
│           └── AppNavHost.kt                  // Centralizes app's navigation graph
│           └── AppDestinations.kt             // Sealed class/object for navigation routes
│           └── AppTopLevelDestination.kt      // Data class for bottom nav bar items
├── build-logic/                               // Optional: For custom Gradle plugins/conventions
│   └── src/main/kotlin/
│       ├── build-logic-common-plugins.gradle.kts
│       └── build-logic-feature-plugins.gradle.kts
├── build.gradle.kts                           // Root project build.gradle.kts
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── libs.versions.toml                         // Gradle Version Catalogs for dependencies
├── settings.gradle.kts                        // Declares all modules
├── core/
│   ├── build.gradle.kts
│   ├── core-common/                           // Common utilities, extensions, constants
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/common/
│   │       ├── utils/
│   │       │   └── Resource.kt                // Sealed class for network/data state (Loading, Success, Error)
│   │       │   └── DateUtils.kt
│   │       │   └── Extensions.kt
│   │       └── constants/
│   │           └── AppConstants.kt
│   ├── core-ui/                               // Reusable UI components for Jetpack Compose
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/ui/
│   │       ├── theme/
│   │       │   ├── Theme.kt
│   │       │   ├── Color.kt
│   │       │   └── Type.kt
│   │       ├── components/
│   │       │   ├── CustomTopAppBar.kt         // Top App Bar composable
│   │       │   ├── CustomBottomNavigationBar.kt // Bottom Navigation Bar composable
│   │       │   ├── LoadingIndicator.kt
│   │       │   └── ErrorScreen.kt
│   │       └── preview/
│   │           └── ThemePreview.kt
│   ├── core-data/                             // Base interfaces/models for data layer (e.g., common DTOs)
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/data/
│   │       └── models/
│   │           └── BaseResponse.kt
│   ├── core-domain/                           // Base interfaces/models for domain layer (e.g., common use cases)
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/domain/
│   │       └── models/
│   │           └── BaseModel.kt
│   │       └── usecase/
│   │           └── BaseUseCase.kt
│   ├── core-di/                               // Koin modules for core dependencies
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/di/
│   │       └── CoreModule.kt                  // Provides CoroutineDispatchers, etc.
│   ├── core-navigation/                       // Centralized navigation logic/routes
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/navigation/
│   │       └── NavigationRoute.kt             // Sealed class for routes across features
│   │       └── NavHostExtensions.kt           // Extensions for NavHost setup
│   └── core-network/                          // Base Retrofit setup, OkHttp client, interceptors
│       ├── build.gradle.kts
│       └── src/main/java/com/yourpackage/core/network/
│           ├── RetrofitBuilder.kt
│           ├── NetworkClient.kt
│           └── interceptor/
│               └── AuthInterceptor.kt
├── feature/
│   ├── feature-notice/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/notice/
│   │       ├── data/
│   │       │   ├── NoticeRepository.kt        // Interface
│   │   	│   ├── NoticeRepositoryImpl.kt    // Implements NoticeRepository, uses web scraper
│   │   	│   ├── NoticeScraper.kt           // Handles HTMLUnit logic for scraping
│   │   	│   └── models/
│   │   	│       ├── NoticeDto.kt             // Data Transfer Object
│   │   	│       └── NoticeEntity.kt          // Room Entity (if storing locally)
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetNoticesUseCase.kt
│   │       │   └── models/
│   │       │       └── Notice.kt              // Domain model
│   │       ├── presentation/
│   │       │   ├── NoticeViewModel.kt
│   │       │   ├── NoticeScreen.kt            // Compose UI
│   │       │   └── components/
│   │       │       └── NoticeListItem.kt
│   │       └── di/
│   │           └── NoticeModule.kt            // Koin module for feature-notice
│   ├── feature-attendance/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/attendance/
│   │       ├── data/
│   │       │   ├── AttendanceRepository.kt    // Interface
│   │       │   ├── AttendanceRepositoryImpl.kt// Implements AttendanceRepository
│   │       │   ├── location/
│   │       │   │   └── LocationDataSource.kt  // Provides GPS data, interacts with Google Maps API
│   │       │   │   └── MapUtils.kt            // Utility for geofencing, distance calculation
│   │       │   └── local/
│   │       │       └── AttendanceDao.kt       // Room DAO for attendance records
│   │       │       └── AttendanceEntity.kt    // Room Entity
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── MarkAttendanceUseCase.kt
│   │       │   │   └── GetClassLocationUseCase.kt
│   │       │   └── models/
│   │       │       └── ClassLocation.kt
│   │       │       └── AttendanceRecord.kt
│   │       ├── presentation/
│   │       │   ├── AttendanceViewModel.kt
│   │       │   ├── AttendanceScreen.kt        // Compose UI (may include MapView)
│   │       │   └── components/
│   │       │       └── LocationPermissionRequest.kt
│   │       └── di/
│   │           └── AttendanceModule.kt        // Koin module for feature-attendance
│   ├── feature-notifications/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/notifications/
│   │       ├── data/
│   │       │   ├── NotificationRepository.kt    // Interface
│   │       │   ├── NotificationRepositoryImpl.kt// Implements, uses Room & WorkManager
│   │       │   ├── local/
│   │       │   │   └── NotificationDao.kt       // Room DAO
│   │       │   │   └── NotificationEntity.kt    // Room Entity
│   │       │   └── worker/
│   │       │       └── NotificationSyncWorker.kt// WorkManager Worker for periodic fetching
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetNotificationsUseCase.kt
│   │       │   │   └── MarkNotificationAsReadUseCase.kt
│   │       │   │   └── ScheduleNotificationSyncUseCase.kt // To enqueue WorkManager
│   │       │   └── models/
│   │       │       └── Notification.kt
│   │       ├── presentation/
│   │       │   ├── NotificationsViewModel.kt
│   │       │   ├── NotificationsScreen.kt
│   │       │   └── components/
│   │       │       └── NotificationItem.kt
│   │       └── di/
│   │           └── NotificationsModule.kt     // Koin module for feature-notifications
│   ├── feature-chatroom/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/chatroom/
│   │       ├── data/
│   │       │   ├── ChatRepository.kt          // Interface
│   │       │   ├── ChatRepositoryImpl.kt      // Implements, uses Firebase
│   │       │   ├── remote/
│   │       │   │   └── FirebaseChatDataSource.kt// Firebase integration
│   │       │   └── models/
│   │       │       └── MessageDto.kt
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── SendMessageUseCase.kt
│   │       │   │   └── GetChatMessagesUseCase.kt
│   │       │   └── models/
│   │       │       └── Message.kt
│   │       ├── presentation/
│   │       │   ├── ChatroomViewModel.kt
│   │       │   ├── ChatroomScreen.kt
│   │       │   └── components/
│   │       │       └── MessageBubble.kt
│   │       └── di/
│   │           └── ChatroomModule.kt          // Koin module for feature-chatroom
│   ├── feature-result/
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/result/
│   │       ├── data/
│   │       │   ├── ResultRepository.kt        // Interface
│   │       │   ├── ResultRepositoryImpl.kt    // Implements, uses web scraper & ML Kit
│   │       │   ├── remote/
│   │       │   │   └── JamiaResultScraper.kt  // HTMLUnit for scraping
│   │       │   │   └── CaptchaSolver.kt       // Uses Google ML Kit for captcha solving
│   │       │   └── models/
│   │       │       └── ResultDto.kt
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetStudentResultUseCase.kt
│   │       │   │   └── SolveCaptchaUseCase.kt
│   │       │   └── models/
│   │       │       └── StudentResult.kt
│   │       ├── presentation/
│   │       │   ├── ResultViewModel.kt
│   │       │   ├── ResultScreen.kt
│   │       │   └── components/
│   │       │       └── ResultDisplay.kt
│   │       └── di/
│   │           └── ResultModule.kt            // Koin module for feature-result
├── data/                                      // Global data layer (e.g., Room database definitions)
│   ├── build.gradle.kts
│   └── src/main/java/com/yourpackage/data/
│       ├── local/
│       │   ├── AppDatabase.kt                 // Room database class
│       │   └── converters/
│       │       └── Converters.kt              // For custom types in Room
│       └── remote/
│           └── WebClientProvider.kt           // Provides shared HTMLUnit client instance
├── gradle.properties
└── README.md
```

**Explanation and Key Aspects:**

1.  **`app/` module:**
    * This is the entry point. It knows about all feature modules and orchestrates the main navigation.
    * `UniversityApp.kt`: Your `Application` class where `startKoin` will be called, loading all necessary Koin modules from the core and feature modules.
    * `MainActivity.kt`: Sets up the Jetpack Compose `NavHost` and the `CustomBottomNavigationBar`.
    * `navigation/`: Central place for app-wide navigation routes and setup.

2.  **`core/` modules:**
    * These modules contain truly shared and foundational code that no other module should have to duplicate. They are generally independent of other application-specific modules (features or `data/`).
    * `core-common`: Basic utilities, constants, sealed classes like `Resource.kt` for consistent data state handling.
    * `core-ui`: Contains all generic UI composables that can be reused across any screen (e.g., your `CustomTopAppBar`, `CustomBottomNavigationBar`). It defines the app's visual theme.
    * `core-data` & `core-domain`: Might hold very generic interfaces or base classes if there are common patterns across data and domain layers of different features.
    * `core-di`: Central Koin definitions that are fundamental to the app (e.g., providing `CoroutineDispatcher` instances).
    * `core-navigation`: Defines common navigation routes that the `app` module's `NavHost` will use.

3.  **`feature/` modules:**
    * Each folder (`feature-notice`, `feature-attendance`, etc.) represents a self-contained feature.
    * **Structure within a feature:** Each feature module follows a mini-MVVM architecture:
        * `data/`: Contains the feature's repository interface and its implementation. This is where the specific web scraping logic (e.g., `NoticeScraper.kt`, `JamiaResultScraper.kt` using HTMLUnit), location data source (`LocationDataSource.kt` for GPS/Maps), or Firebase data source (`FirebaseChatDataSource.kt`) resides. It also includes Room `Dao`s and `Entity` classes specific to that feature (if it needs local data).
        * `domain/`: Contains the feature's specific use cases (interactors) and domain models (plain data classes representing the business entities). Use cases orchestrate data flow between the presentation and data layers.
        * `presentation/`: Contains the `ViewModel`s and Jetpack Compose UI screens for that feature.
        * `di/`: A dedicated Koin module for defining and providing the dependencies *within that feature* (e.g., its ViewModel, Repository, Data Sources, Use Cases).

4.  **`data/` (Global Database/Web Client):**
    * This module is specifically for shared database infrastructure (like the main `AppDatabase.kt` for Room) and possibly a shared web client provider if multiple features need it.
    * `WebClientProvider.kt`: If HTMLUnit needs some shared configuration or instance across features, you can provide it here. Otherwise, the HTMLUnit dependency and specific scraper logic can live within the feature's data layer.

5.  **Koin Integration:**
    * Each `feature/X/di/XModule.kt` file will define the Koin modules for that feature.
    * In your `app/UniversityApp.kt`, you'll collect and load all these feature modules (and core modules) into Koin.
    * Dependencies between modules: A feature's `domain` module might depend on an interface from its `data` module, which is then implemented in the `data` module and provided by Koin. Koin handles the resolution across modules.

6.  **Navigation (`core-navigation` and `app/navigation`):**
    * `core-navigation`: Defines abstract navigation targets (e.g., `object NoticeRoute : NavigationRoute`).
    * `app/navigation`: The `AppNavHost` will use these routes and the `feature` modules' composables to build the actual navigation graph. `AppTopLevelDestination` will define the bottom navigation bar items, linking them to specific feature routes.

7.  **`libs.versions.toml`:** Crucial for managing all your dependencies (Jetpack Compose, Koin, WorkManager, Firebase, HTMLUnit, Google Maps, ML Kit) in a centralized and type-safe manner.

This detailed structure provides a robust foundation for your complex Android application, making it easier to manage, scale, and collaborate on. Remember to define your dependencies clearly in each module's `build.gradle.kts` and use `api` vs. `implementation` appropriately for visibility.

------------------------------------------------------ claude -------------------------------

# Feature-wise Modular Android App Structure

## Root Project Structure
```
JamiaApp/
├── app/                                    # Main app module
├── core/                                   # Core modules
│   ├── common/                            # Common utilities and extensions
│   ├── network/                           # Network layer (Retrofit, OkHttp)
│   ├── database/                          # Local database (Room)
│   ├── datastore/                         # DataStore preferences
│   └── ui/                                # UI components and theme
├── feature/                               # Feature modules
│   ├── notice/                            # Notice feature (web scraping)
│   ├── attendance/                        # Attendance feature (GPS/Maps)
│   ├── chatroom/                          # Chat feature (Firebase)
│   ├── result/                            # Result feature (web scraping + ML Kit)
│   └── notifications/                     # Notifications feature
├── data/                                  # Data layer modules
│   ├── repository/                        # Repository implementations
│   └── remote/                            # Remote data sources
└── build-logic/                           # Build configuration
```

## Detailed File Structure

### 1. App Module (`/app`)
```
app/
├── src/
│   └── main/
│       ├── java/com/jamiaapp/
│       │   ├── JamiaApplication.kt              # Application class with Koin setup
│       │   ├── MainActivity.kt                  # Main activity with navigation
│       │   ├── di/
│       │   │   └── AppModule.kt                 # Main DI module
│       │   ├── navigation/
│       │   │   ├── MainNavigation.kt            # Main navigation setup
│       │   │   ├── BottomNavigation.kt          # Bottom navigation component
│       │   │   └── TopAppBarSetup.kt            # Top app bar configuration
│       │   └── worker/
│       │       └── WorkManagerSetup.kt          # WorkManager initialization
│       ├── res/
│       │   ├── layout/
│       │   │   ├── activity_main.xml            # Main activity layout
│       │   │   ├── bottom_navigation.xml        # Bottom navigation layout
│       │   │   └── top_app_bar.xml              # Top app bar layout
│       │   ├── menu/
│       │   │   └── bottom_navigation_menu.xml   # Bottom navigation menu items
│       │   ├── values/
│       │   │   ├── colors.xml                   # App colors
│       │   │   ├── strings.xml                  # App strings
│       │   │   ├── themes.xml                   # App themes
│       │   │   └── dimens.xml                   # Dimension values
│       │   └── drawable/                        # App icons and drawables
│       └── AndroidManifest.xml                  # Main manifest with permissions
├── build.gradle.kts                             # App build configuration
└── proguard-rules.pro                          # ProGuard rules
```

### 2. Core Modules

#### Core Common (`/core/common`)
```
core/common/
├── src/main/java/com/jamiaapp/core/common/
│   ├── utils/
│   │   ├── Constants.kt                         # App constants
│   │   ├── DateUtils.kt                         # Date utility functions
│   │   ├── NetworkUtils.kt                      # Network utility functions
│   │   ├── LocationUtils.kt                     # Location utility functions
│   │   └── ValidationUtils.kt                   # Input validation utilities
│   ├── extensions/
│   │   ├── ContextExtensions.kt                 # Context extension functions
│   │   ├── ViewExtensions.kt                    # View extension functions
│   │   ├── StringExtensions.kt                  # String extension functions
│   │   └── DateExtensions.kt                    # Date extension functions
│   ├── result/
│   │   ├── Result.kt                            # Sealed class for API results
│   │   └── NetworkResult.kt                     # Network result wrapper
│   └── di/
│       └── CommonModule.kt                      # Common DI module
└── build.gradle.kts                             # Common module build config
```

#### Core Network (`/core/network`)
```
core/network/
├── src/main/java/com/jamiaapp/core/network/
│   ├── api/
│   │   ├── ApiService.kt                        # Main API service interface
│   │   └── WebScrapingService.kt                # Web scraping service
│   ├── interceptor/
│   │   ├── AuthInterceptor.kt                   # Authentication interceptor
│   │   ├── LoggingInterceptor.kt                # Logging interceptor
│   │   └── NetworkConnectionInterceptor.kt      # Network connection check
│   ├── adapter/
│   │   └── NetworkResponseAdapter.kt            # Network response adapter
│   ├── webscraping/
│   │   ├── HtmlUnitClient.kt                    # HTML Unit client setup
│   │   ├── JamiaWebScraper.kt                   # Jamia website scraper
│   │   └── NoticeWebScraper.kt                  # Notice specific scraper
│   └── di/
│       └── NetworkModule.kt                     # Network DI module
└── build.gradle.kts                             # Network module build config
```

#### Core Database (`/core/database`)
```
core/database/
├── src/main/java/com/jamiaapp/core/database/
│   ├── entities/
│   │   ├── NoticeEntity.kt                      # Notice database entity
│   │   ├── AttendanceEntity.kt                  # Attendance database entity
│   │   ├── ResultEntity.kt                      # Result database entity
│   │   └── UserEntity.kt                        # User database entity
│   ├── dao/
│   │   ├── NoticeDao.kt                         # Notice DAO
│   │   ├── AttendanceDao.kt                     # Attendance DAO
│   │   ├── ResultDao.kt                         # Result DAO
│   │   └── UserDao.kt                           # User DAO
│   ├── converters/
│   │   ├── DateConverter.kt                     # Date type converter
│   │   └── ListConverter.kt                     # List type converter
│   ├── JamiaDatabase.kt                         # Room database setup
│   └── di/
│       └── DatabaseModule.kt                    # Database DI module
└── build.gradle.kts                             # Database module build config
```

#### Core UI (`/core/ui`)
```
core/ui/
├── src/main/java/com/jamiaapp/core/ui/
│   ├── theme/
│   │   ├── Color.kt                             # App color scheme
│   │   ├── Typography.kt                        # Typography definitions
│   │   ├── Theme.kt                             # App theme setup
│   │   └── Shapes.kt                            # Shape definitions
│   ├── components/
│   │   ├── LoadingDialog.kt                     # Loading dialog component
│   │   ├── ErrorDialog.kt                       # Error dialog component
│   │   ├── CustomButton.kt                      # Custom button component
│   │   ├── CustomTextField.kt                   # Custom text field component
│   │   └── EmptyStateView.kt                    # Empty state view component
│   ├── utils/
│   │   ├── UiUtils.kt                           # UI utility functions
│   │   └── PermissionUtils.kt                   # Permission handling utilities
│   └── di/
│       └── UiModule.kt                          # UI DI module
└── build.gradle.kts                             # UI module build config
```

### 3. Feature Modules

#### Notice Feature (`/feature/notice`)
```
feature/notice/
├── src/main/java/com/jamiaapp/feature/notice/
│   ├── data/
│   │   ├── repository/
│   │   │   └── NoticeRepositoryImpl.kt          # Notice repository implementation
│   │   ├── datasource/
│   │   │   ├── NoticeRemoteDataSource.kt        # Remote data source
│   │   │   └── NoticeLocalDataSource.kt         # Local data source
│   │   └── mapper/
│   │       └── NoticeMapper.kt                  # Data to domain mapping
│   ├── domain/
│   │   ├── model/
│   │   │   └── Notice.kt                        # Notice domain model
│   │   ├── repository/
│   │   │   └── NoticeRepository.kt              # Notice repository interface
│   │   └── usecase/
│   │       ├── GetNoticesUseCase.kt             # Get notices use case
│   │       ├── RefreshNoticesUseCase.kt         # Refresh notices use case
│   │       └── SearchNoticesUseCase.kt          # Search notices use case
│   ├── presentation/
│   │   ├── viewmodel/
│   │   │   └── NoticeViewModel.kt               # Notice ViewModel
│   │   ├── ui/
│   │   │   ├── NoticeListFragment.kt            # Notice list UI
│   │   │   ├── NoticeDetailFragment.kt          # Notice detail UI
│   │   │   └── NoticeSearchFragment.kt          # Notice search UI
│   │   ├── adapter/
│   │   │   └── NoticeAdapter.kt                 # RecyclerView adapter
│   │   └── state/
│   │       └── NoticeUiState.kt                 # UI state data class
│   ├── worker/
│   │   └── NoticeRefreshWorker.kt               # Background notice refresh
│   └── di/
│       └── NoticeModule.kt                      # Notice DI module
├── src/main/res/
│   └── layout/
│       ├── fragment_notice_list.xml             # Notice list layout
│       ├── fragment_notice_detail.xml           # Notice detail layout
│       ├── item_notice.xml                      # Notice item layout
│       └── fragment_notice_search.xml           # Notice search layout
└── build.gradle.kts                             # Notice module build config
```

#### Attendance Feature (`/feature/attendance`)
```
feature/attendance/
├── src/main/java/com/jamiaapp/feature/attendance/
│   ├── data/
│   │   ├── repository/
│   │   │   └── AttendanceRepositoryImpl.kt      # Attendance repository implementation
│   │   ├── datasource/
│   │   │   ├── LocationDataSource.kt            # GPS location data source
│   │   │   ├── AttendanceRemoteDataSource.kt    # Remote data source
│   │   │   └── AttendanceLocalDataSource.kt     # Local data source
│   │   └── mapper/
│   │       └── AttendanceMapper.kt              # Data to domain mapping
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Attendance.kt                    # Attendance domain model
│   │   │   ├── Location.kt                      # Location domain model
│   │   │   └── ClassSchedule.kt                 # Class schedule model
│   │   ├── repository/
│   │   │   └── AttendanceRepository.kt          # Attendance repository interface
│   │   └── usecase/
│   │       ├── MarkAttendanceUseCase.kt         # Mark attendance use case
│   │       ├── GetAttendanceHistoryUseCase.kt   # Get attendance history
│   │       ├── CheckLocationUseCase.kt          # Check location use case
│   │       └── GetClassScheduleUseCase.kt       # Get class schedule
│   ├── presentation/
│   │   ├── viewmodel/
│   │   │   ├── AttendanceViewModel.kt           # Attendance ViewModel
│   │   │   └── LocationViewModel.kt             # Location ViewModel
│   │   ├── ui/
│   │   │   ├── AttendanceFragment.kt            # Main attendance UI
│   │   │   ├── AttendanceHistoryFragment.kt     # Attendance history UI
│   │   │   ├── LocationPermissionFragment.kt    # Location permission UI
│   │   │   └── MapFragment.kt                   # Map view for attendance
│   │   ├── adapter/
│   │   │   └── AttendanceHistoryAdapter.kt      # History RecyclerView adapter
│   │   └── state/
│   │       ├── AttendanceUiState.kt             # Attendance UI state
│   │       └── LocationUiState.kt               # Location UI state
│   ├── service/
│   │   ├── LocationService.kt                   # Background location service
│   │   └── GeofenceService.kt                   # Geofence monitoring service
│   ├── worker/
│   │   └── AttendanceReminderWorker.kt          # Attendance reminder worker
│   └── di/
│       └── AttendanceModule.kt                  # Attendance DI module
├── src/main/res/
│   └── layout/
│       ├── fragment_attendance.xml              # Main attendance layout
│       ├── fragment_attendance_history.xml      # History layout
│       ├── fragment_map.xml                     # Map layout
│       └── item_attendance_history.xml          # History item layout
└── build.gradle.kts                             # Attendance module build config
```

#### Chatroom Feature (`/feature/chatroom`)
```
feature/chatroom/
├── src/main/java/com/jamiaapp/feature/chatroom/
│   ├── data/
│   │   ├── repository/
│   │   │   └── ChatRepositoryImpl.kt            # Chat repository implementation
│   │   ├── datasource/
│   │   │   ├── FirebaseChatDataSource.kt        # Firebase Firestore data source
│   │   │   └── ChatLocalDataSource.kt           # Local cache data source
│   │   └── mapper/
│   │       ├── MessageMapper.kt                 # Message data mapping
│   │       └── ChatRoomMapper.kt                # Chat room data mapping
│   ├── domain/
│   │   ├── model/
│   │   │   ├── ChatRoom.kt                      # Chat room domain model
│   │   │   ├── Message.kt                       # Message domain model
│   │   │   └── User.kt                          # User domain model
│   │   ├── repository/
│   │   │   └── ChatRepository.kt                # Chat repository interface
│   │   └── usecase/
│   │       ├── GetChatRoomsUseCase.kt           # Get chat rooms use case
│   │       ├── SendMessageUseCase.kt            # Send message use case
│   │       ├── GetMessagesUseCase.kt            # Get messages use case
│   │       └── CreateChatRoomUseCase.kt         # Create chat room use case
│   ├── presentation/
│   │   ├── viewmodel/
│   │   │   ├── ChatRoomListViewModel.kt         # Chat room list ViewModel
│   │   │   └── ChatViewModel.kt                 # Individual chat ViewModel
│   │   ├── ui/
│   │   │   ├── ChatRoomListFragment.kt          # Chat room list UI
│   │   │   ├── ChatFragment.kt                  # Individual chat UI
│   │   │   └── CreateChatRoomFragment.kt        # Create chat room UI
│   │   ├── adapter/
│   │   │   ├── ChatRoomAdapter.kt               # Chat room list adapter
│   │   │   └── MessageAdapter.kt                # Messages adapter
│   │   └── state/
│   │       ├── ChatRoomUiState.kt               # Chat room UI state
│   │       └── ChatUiState.kt                   # Chat UI state
│   ├── firebase/
│   │   ├── FirebaseManager.kt                   # Firebase initialization
│   │   ├── FirestoreHelper.kt                   # Firestore helper functions
│   │   └── FCMService.kt                        # Firebase Cloud Messaging
│   └── di/
│       └── ChatModule.kt                        # Chat DI module
├── src/main/res/
│   └── layout/
│       ├── fragment_chat_room_list.xml          # Chat room list layout
│       ├── fragment_chat.xml                    # Chat layout
│       ├── item_chat_room.xml                   # Chat room item layout
│       ├── item_message_sent.xml                # Sent message layout
│       └── item_message_received.xml            # Received message layout
└── build.gradle.kts                             # Chat module build config
```

#### Result Feature (`/feature/result`)
```
feature/result/
├── src/main/java/com/jamiaapp/feature/result/
│   ├── data/
│   │   ├── repository/
│   │   │   └── ResultRepositoryImpl.kt          # Result repository implementation
│   │   ├── datasource/
│   │   │   ├── ResultRemoteDataSource.kt        # Web scraping data source
│   │   │   └── ResultLocalDataSource.kt         # Local cache data source
│   │   └── mapper/
│   │       └── ResultMapper.kt                  # Result data mapping
│   ├── domain/
│   │   ├── model/
│   │   │   ├── StudentResult.kt                 # Student result domain model
│   │   │   ├── Subject.kt                       # Subject domain model
│   │   │   └── Semester.kt                      # Semester domain model
│   │   ├── repository/
│   │   │   └── ResultRepository.kt              # Result repository interface
│   │   └── usecase/
│   │       ├── GetStudentResultUseCase.kt       # Get student result use case
│   │       ├── SolveCaptchaUseCase.kt           # Solve captcha use case
│   │       └── RefreshResultUseCase.kt          # Refresh result use case
│   ├── presentation/
│   │   ├── viewmodel/
│   │   │   └── ResultViewModel.kt               # Result ViewModel
│   │   ├── ui/
│   │   │   ├── ResultLoginFragment.kt           # Result login UI
│   │   │   ├── ResultDisplayFragment.kt         # Result display UI
│   │   │   └── CaptchaFragment.kt               # Captcha solving UI
│   │   ├── adapter/
│   │   │   └── SubjectResultAdapter.kt          # Subject results adapter
│   │   └── state/
│   │       └── ResultUiState.kt                 # Result UI state
│   ├── mlkit/
│   │   ├── CaptchaSolver.kt                     # ML Kit captcha solver
│   │   ├── TextRecognitionHelper.kt             # Text recognition helper
│   │   └── ImagePreprocessor.kt                 # Image preprocessing for ML
│   ├── webscraping/
│   │   ├── JamiaResultScraper.kt                # Jamia result website scraper
│   │   ├── CaptchaHandler.kt                    # Captcha handling logic
│   │   └── LoginHandler.kt                      # Login form handler
│   └── di/
│       └── ResultModule.kt                      # Result DI module
├── src/main/res/
│   └── layout/
│       ├── fragment_result_login.xml            # Result login layout
│       ├── fragment_result_display.xml          # Result display layout
│       ├── fragment_captcha.xml                 # Captcha layout
│       └── item_subject_result.xml              # Subject result item layout
└── build.gradle.kts                             # Result module build config
```

#### Notifications Feature (`/feature/notifications`)
```
feature/notifications/
├── src/main/java/com/jamiaapp/feature/notifications/
│   ├── data/
│   │   ├── repository/
│   │   │   └── NotificationRepositoryImpl.kt    # Notification repository implementation
│   │   ├── datasource/
│   │   │   ├── NotificationRemoteDataSource.kt  # Remote notification data
│   │   │   └── NotificationLocalDataSource.kt   # Local notification data
│   │   └── mapper/
│   │       └── NotificationMapper.kt            # Notification data mapping
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Notification.kt                  # Notification domain model
│   │   │   └── NotificationType.kt              # Notification type enum
│   │   ├── repository/
│   │   │   └── NotificationRepository.kt        # Notification repository interface
│   │   └── usecase/
│   │       ├── GetNotificationsUseCase.kt       # Get notifications use case
│   │       ├── MarkAsReadUseCase.kt             # Mark as read use case
│   │       ├── ScheduleNotificationUseCase.kt   # Schedule notification use case
│   │       └── DeleteNotificationUseCase.kt     # Delete notification use case
│   ├── presentation/
│   │   ├── viewmodel/
│   │   │   └── NotificationViewModel.kt         # Notification ViewModel
│   │   ├── ui/
│   │   │   ├── NotificationListFragment.kt      # Notification list UI
│   │   │   ├── NotificationDetailFragment.kt    # Notification detail UI
│   │   │   └── NotificationSettingsFragment.kt  # Notification settings UI
│   │   ├── adapter/
│   │   │   └── NotificationAdapter.kt           # Notification list adapter
│   │   └── state/
│   │       └── NotificationUiState.kt           # Notification UI state
│   ├── service/
│   │   ├── NotificationService.kt               # Notification display service
│   │   └── PushNotificationService.kt           # Push notification handling
│   ├── worker/
│   │   ├── NotificationWorker.kt                # Scheduled notification worker
│   │   └── NotificationCleanupWorker.kt         # Old notification cleanup
│   ├── manager/
│   │   ├── NotificationManager.kt               # System notification manager
│   │   └── NotificationChannelManager.kt        # Notification channel setup
│   └── di/
│       └── NotificationModule.kt                # Notification DI module
├── src/main/res/
│   └── layout/
│       ├── fragment_notification_list.xml       # Notification list layout
│       ├── fragment_notification_detail.xml     # Notification detail layout
│       ├── fragment_notification_settings.xml   # Settings layout
│       └── item_notification.xml                # Notification item layout
└── build.gradle.kts                             # Notification module build config
```

### 4. Data Layer (`/data`)

#### Repository Module (`/data/repository`)
```
data/repository/
├── src/main/java/com/jamiaapp/data/repository/
│   ├── BaseRepository.kt                        # Base repository with common functions
│   ├── UserRepositoryImpl.kt                    # User data repository
│   ├── PreferencesRepositoryImpl.kt             # User preferences repository
│   └── di/
│       └── RepositoryModule.kt                  # Repository DI module
└── build.gradle.kts                             # Repository module build config
```

### 5. Build Logic (`/build-logic`)
```
build-logic/
├── convention/
│   ├── src/main/kotlin/
│   │   ├── AndroidApplicationConventionPlugin.kt    # App module convention
│   │   ├── AndroidLibraryConventionPlugin.kt        # Library module convention
│   │   ├── AndroidFeatureConventionPlugin.kt        # Feature module convention
│   │   └── AndroidComposeConventionPlugin.kt        # Compose convention
│   └── build.gradle.kts                             # Convention plugin build
├── settings.gradle.kts                              # Build logic settings
└── build.gradle.kts                                 # Build logic configuration
```

### 6. Root Configuration Files
```
JamiaApp/
├── settings.gradle.kts                              # Project settings and module inclusion
├── build.gradle.kts                                 # Root build configuration
├── gradle.properties                                # Gradle properties
├── local.properties                                 # Local SDK paths
├── google-services.json                            # Firebase configuration
└── README.md                                        # Project documentation
```

## Key Dependencies per Module

### App Module Dependencies
- Koin (Dependency Injection)
- Navigation Component
- WorkManager
- Material Design Components

### Core Network Dependencies
- Retrofit & OkHttp
- HTML Unit for web scraping
- Gson/Moshi for JSON parsing

### Feature-specific Dependencies
- **Notice**: HTML Unit, WorkManager
- **Attendance**: Google Play Services (Maps, Location), Geofencing
- **Chatroom**: Firebase (Firestore, FCM, Auth)
- **Result**: ML Kit Text Recognition, HTML Unit
- **Notifications**: WorkManager, Firebase FCM

### Database Dependencies
- Room Database
- DataStore (SharedPreferences replacement)

This modular structure provides:
- **Clear separation of concerns**
- **Easy testing and maintenance**
- **Scalable architecture**
- **Reusable components**
- **Independent feature development**
- **Optimized build times through parallel builds**

---------------------------------------- Version 3: actual feature --------------------------------------------
Okay, this is a comprehensive plan! Building on your existing setup and incorporating the new features and technologies, here's a detailed file structure for your multi-module Jetpack Compose project using Clean Architecture (MVVM) and Koin for DI.

This structure emphasizes **feature-wise modularization** while centralizing core components and shared data/library implementations.

```
├── .gradle/
├── .idea/
├── app/
│   ├── build.gradle.kts
│   └── src/main/java/com/yourpackage/universityapp/
│       ├── UniversityApp.kt                   // Koin Application initialization, loads all modules
│       ├── MainActivity.kt                    // Main entry point, Splash Screen API integration, NavHost setup
│       └── navigation/
│           ├── AppNavHost.kt                  // Centralizes app's navigation graph logic
│           ├── AppRoutes.kt                   // Sealed class/object for global navigation routes (from core-navigation)
│           └── AppTopLevelDestination.kt      // Data class for bottom navigation bar items
├── build-logic/                               // Optional: For custom Gradle plugins/conventions
│   └── src/main/kotlin/
│       ├── build-logic-common-plugins.gradle.kts
│       └── build-logic-feature-plugins.gradle.kts
├── build.gradle.kts                           // Root project build.gradle.kts
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── libs.versions.toml                         // Gradle Version Catalogs for dependencies
├── settings.gradle.kts                        // Declares all modules
├── core/
│   ├── build.gradle.kts                       // Common dependencies for core modules
│   ├── core-common/                           // Common utilities, extensions, constants, global observers
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/common/
│   │       ├── utils/
│   │       │   ├── Resource.kt                // Sealed class for data states (Loading, Success, Error)
│   │       │   ├── DateUtils.kt
│   │       │   ├── Extensions.kt
│   │       │   └── NetworkConnectivityObserver.kt // Observes network status (Wi-Fi, Mobile, etc.)
│   │       └── constants/
│   │           └── AppConstants.kt
│   ├── core-ui/                               // Reusable UI components for Jetpack Compose
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/ui/
│   │       ├── theme/
│   │       │   ├── Theme.kt
│   │       │   ├── Color.kt
│   │       │   └── Type.kt
│   │       ├── components/
│   │       │   ├── CustomTopAppBar.kt         // Top App Bar composable
│   │       │   ├── CustomBottomNavigationBar.kt // Bottom Navigation Bar composable
│   │       │   ├── LoadingIndicator.kt
│   │       │   └── ErrorScreen.kt
│   │       └── preview/
│   │           └── ThemePreview.kt
│   ├── core-data/                             // Base interfaces/models for shared data layer concerns
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/data/
│   │       └── models/
│   │           └── BaseResponse.kt            // Generic base response for APIs
│   ├── core-domain/                           // Base interfaces/models for shared domain layer concerns
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/domain/
│   │       ├── usecase/
│   │       │   └── BaseUseCase.kt             // Abstract base class for use cases
│   │       └── models/
│   │           └── BaseModel.kt               // Generic base domain model
│   ├── core-di/                               // Koin modules for core application-wide dependencies
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/di/
│   │       └── CoreModule.kt                  // Provides CoroutineDispatchers, NetworkConnectivityObserver
│   ├── core-navigation/                       // Centralized navigation logic/routes definitions
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/core/navigation/
│   │       ├── AppRoutes.kt                   // Defines all app-wide navigation destinations
│   │       └── NavHostExtensions.kt           // Extensions for NavHost setup
│   └── core-network/                          // Base Retrofit setup, OkHttp client, interceptors (for REST APIs)
│       ├── build.gradle.kts
│       └── src/main/java/com/yourpackage/core/network/
│           ├── RetrofitBuilder.kt
│           ├── NetworkClient.kt
│           └── interceptor/
│               └── AuthInterceptor.kt
├── data/
│   ├── build.gradle.kts                       // Common dependencies for data modules
│   ├── data-local-room/                       // Centralized Room database definition
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/data/local/room/
│   │       ├── AppDatabase.kt                 // Main Room database class, listing all entities/DAOs
│   │       └── converters/
│   │           └── Converters.kt              // Custom TypeConverters for Room
│   │       └── di/
│   │           └── RoomModule.kt              // Koin module for AppDatabase and shared DAOs
│   ├── data-local-preferences/                // Centralized DataStore preferences setup
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/data/local/preferences/
│   │       ├── AppPreferencesDataSource.kt    // Generic DataStore operations
│   │       └── AppPreferencesKeys.kt          // Central place for DataStore preference keys
│   │       └── di/
│   │           └── PreferencesModule.kt       // Koin module for DataStore
├── library/
│   ├── build.gradle.kts                       // Common dependencies for library modules
│   ├── library-htmlunit/                      // Encapsulates HTMLUnit setup and client provision
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/library/htmlunit/
│   │       ├── HtmlUnitClientProvider.kt      // Provides and configures HtmlUnit WebClient instance
│   │       └── di/
│   │           └── HtmlUnitModule.kt          // Koin module for HTMLUnit client
│   ├── library-mlkit/                         // Encapsulates Google ML Kit setup and common utilities
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/library/mlkit/
│   │       ├── MLKitImageProcessor.kt         // Common utilities for image processing (e.g., for text recognition)
│   │       └── di/
│   │           └── MLKitModule.kt             // Koin module for ML Kit components
│   ├── library-location/                      // Encapsulates GPS/Map related functionalities
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/library/location/
│   │       ├── GpsTracker.kt                  // Handles requesting location updates
│   │       ├── MapUtils.kt                    // Utilities for map calculations (e.g., distance, geofencing)
│   │       └── di/
│   │           └── LocationModule.kt          // Koin module for location services
├── feature/
│   ├── build.gradle.kts                       // Common dependencies for feature modules
│   ├── feature-auth/                          // Captive Portal Login feature
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/auth/
│   │       ├── data/
│   │       │   ├── AuthRepository.kt          // Interface
│   │       │   ├── AuthRepositoryImpl.kt      // Implements AuthRepository, uses LoginDataSource
│   │       │   ├── AuthDataStore.kt           // Handles storing student ID/password in DataStore
│   │       │   └── remote/
│   │       │       └── LoginScraper.kt        // Handles HTMLUnit login request & parsing
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── PerformLoginUseCase.kt
│   │       │   │   └── GetLoginStatusUseCase.kt // For splash screen check
│   │       │   └── models/
│   │       │       └── AuthResult.kt
│   │       ├── presentation/
│   │       │   ├── AuthViewModel.kt
│   │       │   ├── LoginScreen.kt             // Compose UI for login
│   │       │   └── components/
│   │       │       └── LoginInputField.kt
│   │       └── di/
│   │           └── AuthModule.kt              // Koin module for feature-auth
│   ├── feature-notice/                        // Notice display feature
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/notice/
│   │       ├── data/
│   │       │   ├── NoticeRepository.kt        // Interface
│   │       │   ├── NoticeRepositoryImpl.kt    // Implements, uses web scraper & Room
│   │       │   ├── remote/
│   │       │   │   └── NoticeScraper.kt       // HTMLUnit logic for scraping notices
│   │       │   └── local/
│   │       │       ├── NoticeDao.kt           // Room DAO
│   │       │       └── NoticeEntity.kt        // Room Entity
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetNoticesUseCase.kt
│   │       │   └── models/
│   │       │       └── Notice.kt              // Domain model
│   │       ├── presentation/
│   │       │   ├── NoticeViewModel.kt
│   │       │   ├── NoticeScreen.kt            // Compose UI
│   │       │   └── components/
│   │       │       └── NoticeListItem.kt
│   │       └── di/
│   │           └── NoticeModule.kt            // Koin module for feature-notice
│   ├── feature-result/                        // Student Result display feature
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/result/
│   │       ├── data/
│   │       │   ├── ResultRepository.kt        // Interface
│   │       │   ├── ResultRepositoryImpl.kt    // Implements, uses web scraper & ML Kit
│   │       │   ├── remote/
│   │       │   │   └── JamiaResultScraper.kt  // HTMLUnit for scraping result
│   │       │   │   └── CaptchaRecognizer.kt   // Uses library-mlkit to process captcha images
│   │       │   └── models/
│   │       │       └── ResultDto.kt
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetStudentResultUseCase.kt
│   │       │   │   └── RecognizeCaptchaUseCase.kt // Uses CaptchaRecognizer
│   │       │   └── models/
│   │       │       └── StudentResult.kt
│   │       ├── presentation/
│   │       │   ├── ResultViewModel.kt
│   │       │   ├── ResultScreen.kt            // Compose UI
│   │       │   └── components/
│   │       │       └── CaptchaInputView.kt
│   │       └── di/
│   │           └── ResultModule.kt            // Koin module for feature-result
│   ├── feature-schedule/                      // Class Schedule feature
│   │   ├── build.gradle.kts
│   │   └── src/main/java/com/yourpackage/feature/schedule/
│   │       ├── data/
│   │       │   ├── ScheduleRepository.kt      // Interface
│   │       │   ├── ScheduleRepositoryImpl.kt  // Implements, uses Room (and possibly remote source for initial sync)
│   │       │   ├── local/
│   │       │   │   ├── ScheduleDao.kt         // Room DAO
│   │       │   │   └── ScheduleEntity.kt      // Room Entity
│   │       ├── domain/
│   │       │   ├── usecase/
│   │       │   │   └── GetClassScheduleUseCase.kt
│   │       │   │   └── UpdateClassScheduleUseCase.kt // For data sync
│   │       │   └── models/
│   │       │       └── ClassSchedule.kt
│   │       ├── presentation/
│   │       │   ├── ScheduleViewModel.kt
│   │       │   ├── ScheduleScreen.kt          // Compose UI
│   │       │   └── components/
│   │       │       └── ScheduleItem.kt
│   │       └── di/
│   │           └── ScheduleModule.kt          // Koin module for feature-schedule
├── gradle.properties
└── README.md
```

### Key Considerations and Integrations:

1.  **`app/UniversityApp.kt` (Koin Initialization):**
    * This is where all your Koin modules from `core-di`, `data-local-room/di`, `data-local-preferences/di`, `library-htmlunit/di`, `library-mlkit/di`, `library-location/di`, and all `feature-X/di` modules will be loaded. This creates your complete DI graph.

2.  **`app/MainActivity.kt` (Splash Screen & Navigation):**
    * Uses `installSplashScreen()` from the Splash Screen API.
    * Injects `GetLoginStatusUseCase` (from `feature-auth:domain`) to check user login status during splash screen.
    * The `AppNavHost` will then be initialized with the `isUserLoggedIn` state to navigate to either the `LoginScreen` or the main authenticated screen.

3.  **Network Observer (`core:common`):**
    * `NetworkConnectivityObserver.kt` will be provided as a `single` instance in `core-di/CoreModule.kt`.
    * Any `ViewModel` or `Repository` that needs to react to network changes (e.g., `NoticeViewModel`, `AuthViewModel`, `ResultViewModel` for triggering scrapes only on Wi-Fi) can inject `NetworkConnectivityObserver` and observe its `Flow`.

4.  **HTMLUnit (`library:htmlunit`):**
    * `HtmlUnitClientProvider.kt` will be responsible for setting up and configuring the `WebClient` instance. This instance can then be injected into the data sources (`LoginScraper`, `NoticeScraper`, `JamiaResultScraper`) in your feature modules.
    * This centralizes the HTMLUnit setup and ensures a single, well-configured client.

5.  **Room (`data:local-room`):**
    * `AppDatabase.kt` will list *all* `Entity` classes and provide *abstract functions* for all `Dao` interfaces from your feature modules.
    * The `RoomModule.kt` in `data-local-room/di` will provide the `AppDatabase` instance and all individual DAOs (e.g., `get<AppDatabase>().notificationDao()`).
    * Each feature using Room (Notice, Schedule) will define its `Entity` and `Dao` within its own `data/local` sub-package, but they will be part of the single `AppDatabase`.

6.  **DataStore (`data:local-preferences` & `feature:auth`):**
    * `data-local-preferences` provides the generic `AppPreferencesDataSource` (for accessing `DataStore<Preferences>`).
    * `feature-auth/data/AuthDataStore.kt` will *use* `AppPreferencesDataSource` to specifically store and retrieve student ID/password. This keeps the auth logic within its feature module while leveraging the shared DataStore setup.

7.  **Google ML Kit (`library:mlkit` & `feature:result`):**
    * `library-mlkit` can provide common ML Kit configurations or helper classes (like `MLKitImageProcessor` to convert images for ML Kit processing).
    * `feature-result/data/remote/CaptchaRecognizer.kt` will implement the actual captcha solving logic, using `MLKitImageProcessor` to feed the captcha image to ML Kit's Text Recognition API.

8.  **Navigation:**
    * `core-navigation/AppRoutes.kt` defines all major navigation routes.
    * `app/navigation/AppNavHost.kt` stitches together the navigation graph using composables from different feature modules. Each feature module provides its composable entry point to the `NavHost`.

This structure should give you a very robust, maintainable, and scalable project, allowing different features to be developed somewhat independently while sharing essential core functionalities and tools.