```agsl
feature/
 └── rent/
     ├── build.gradle.kts
     ├── consumer-rules.pro
     ├── proguard-rules.pro
     └── src/
         ├── main/
         │   └── java/
         │       └── com/
         │           └── reyaz/
         │               └── feature/
         │                   └── rent/
         │                       ├── data/
         │                       │   ├── remote/
         │                       │   │   ├── RentFirestoreService.kt
         │                       │   │   └── RentDto.kt
         │                       │   ├── repository/
         │                       │   │   └── RentRepositoryImpl.kt
         │                       │   └── mapper/
         │                       │       └── RentMapper.kt
         │                       │
         │                       ├── di/
         │                       │   └── RentModule.kt
         │                       │
         │                       ├── domain/
         │                       │   ├── model/
         │                       │   │   └── RentProperty.kt
         │                       │   ├── repository/
         │                       │   │   └── RentRepository.kt
         │                       │   └── usecase/
         │                       │       ├── GetPropertiesUseCase.kt
         │                       │       ├── PostPropertyUseCase.kt
         │                       │       └── SearchPropertiesUseCase.kt
         │                       │
         │                       ├── presentation/
         │                       │   ├── list/
         │                       │   │   ├── RentListScreen.kt
         │                       │   │   ├── RentListViewModel.kt
         │                       │   │   └── RentListUiState.kt
         │                       │   ├── post/
         │                       │   │   ├── PostPropertyScreen.kt
         │                       │   │   ├── PostPropertyViewModel.kt
         │                       │   │   └── PostPropertyUiState.kt
         │                       │   └── components/
         │                       │       ├── PropertyCard.kt
         │                       │       ├── SearchBar.kt
         │                       │       ├── FilterSheet.kt
         │                       │       └── AmenitiesChips.kt
         │                       │
         │                       └── util/
         │                           └── PropertyValidator.kt
         │
         └── test/

```