# Santé Locale - Implementation Notes

## What Has Been Implemented

I've successfully set up the initial Android project with MainActivity.kt and the Dashboard screen, translating from the React prototype to Jetpack Compose.

### Completed Components

#### 1. **Data Layer (Room Database)**
- [HealthLog.kt](app/src/main/java/com/santelocale/data/entity/HealthLog.kt) - Entity for health logs (glucose & activity)
- [FoodItem.kt](app/src/main/java/com/santelocale/data/entity/FoodItem.kt) - Entity for food guide database
- [HealthLogDao.kt](app/src/main/java/com/santelocale/data/dao/HealthLogDao.kt) - DAO for health logs with Flow support
- [FoodItemDao.kt](app/src/main/java/com/santelocale/data/dao/FoodItemDao.kt) - DAO for food items
- [HealthDatabase.kt](app/src/main/java/com/santelocale/data/database/HealthDatabase.kt) - Room database with pre-populated Haitian food data

#### 2. **Repository & ViewModel**
- [HealthRepository.kt](app/src/main/java/com/santelocale/data/repository/HealthRepository.kt) - Offline-first data repository
- [DashboardViewModel.kt](app/src/main/java/com/santelocale/ui/viewmodel/DashboardViewModel.kt) - MVVM pattern with StateFlow

#### 3. **UI Theme (Exact Colors from React)**
- [Color.kt](app/src/main/java/com/santelocale/ui/theme/Color.kt) - All colors translated from Tailwind CSS:
  - Emerald-700 (`#047857`) for header
  - Blue-600 (`#2563EB`) for glucose button
  - Orange-500 (`#F97316`) for activity button
  - Yellow-600 (`#ca8a04`) for food guide yellow category
  - Red-600 (`#dc2626`) for food guide red category
  - Complete Slate color palette
- [Theme.kt](app/src/main/java/com/santelocale/ui/theme/Theme.kt) - Material 3 theme configuration
- [Type.kt](app/src/main/java/com/santelocale/ui/theme/Type.kt) - Large font sizes for senior accessibility

#### 4. **Reusable UI Components**
- [Header.kt](app/src/main/java/com/santelocale/ui/components/Header.kt) - App header with:
  - Back navigation (ChevronLeft icon)
  - Title text
  - "Hors Ligne" (Offline) badge
  - Settings button
- [BigButton.kt](app/src/main/java/com/santelocale/ui/components/BigButton.kt) - Large accessible button with:
  - Icon in circular background (`bg-white/20`)
  - Title and subtitle
  - Custom colors per button type

#### 5. **Dashboard Screen**
- [DashboardScreen.kt](app/src/main/java/com/santelocale/ui/screens/DashboardScreen.kt) - Main dashboard with:
  - Status card showing last glucose reading
  - Three big action buttons (Glucose, Food Guide, Activity)
  - Full translation from React component
  - Material icons: WaterDrop (Droplets), Restaurant (Utensils), DirectionsRun (Footprints)

#### 6. **MainActivity & Navigation**
- [MainActivity.kt](app/src/main/java/com/santelocale/MainActivity.kt) - Main activity with:
  - Room database initialization
  - DataStore for settings (user name, unit preference)
  - Navigation state management (dashboard, settings, glucose, food, activity, history)
  - ViewModel integration

#### 7. **Project Configuration**
- [app/build.gradle.kts](app/build.gradle.kts) - Dependencies for Room, Compose, DataStore, Coil
- [AndroidManifest.xml](app/src/main/AndroidManifest.xml) - App configuration with INTERNET permission
- Gradle configuration files
- Resource files (strings, themes, backup rules)

## Color Mapping (React → Compose)

| React Tailwind Class | Hex Color | Compose Color Constant |
|---------------------|-----------|------------------------|
| `bg-emerald-700` | `#047857` | `Emerald700` |
| `bg-emerald-600` | `#059669` | `Emerald600` |
| `bg-emerald-500` | `#10B981` | `Emerald500` |
| `bg-blue-600` | `#2563EB` | `Blue600` |
| `bg-orange-500` | `#F97316` | `Orange500` |
| `bg-yellow-600` | `#ca8a04` | `Yellow600` |
| `bg-red-600` | `#dc2626` | `Red600` |
| `bg-slate-50` | `#F8FAFC` | `Slate50` |
| `bg-slate-800` | `#1E293B` | `Slate800` |
| `bg-white/20` | `#33FFFFFF` | `WhiteAlpha20` |

## Icon Mapping (Lucide → Material Icons)

| React Lucide Icon | Material Icon | Used In |
|------------------|---------------|---------|
| `Droplets` | `Icons.Default.WaterDrop` | Glucose button |
| `Utensils` | `Icons.Default.Restaurant` | Food guide button |
| `Footprints` | `Icons.Default.DirectionsRun` | Activity button |
| `History` | `Icons.Default.History` | History navigation |
| `ChevronLeft` | `Icons.Default.ChevronLeft` | Back button |
| `Settings` | `Icons.Default.Settings` | Settings button |

## Database Schema

### HealthLog Table
```kotlin
@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,          // "GLUCOSE" or "ACTIVITY"
    val value: Float,          // Numeric value for calculations
    val displayValue: String?, // User-facing value (e.g., "5,4" with comma)
    val label: String?,        // Activity label (e.g., "Marche (30 min)")
    val date: Long             // Epoch timestamp
)
```

### FoodItem Table (Pre-populated)
```kotlin
@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,  // "VERT", "JAUNE", "ROUGE"
    val imageUrl: String,
    val tip: String
)
```

## What's Next (To Implement)

The following screens need to be implemented to match the React prototype:

1. **SettingsScreen** - User name and unit preference
2. **GlucoseInputScreen** - Custom numeric keypad with comma support
3. **FoodGuideScreen** - Three-tab interface (VERT/JAUNE/ROUGE)
4. **ActivityInputScreen** - Activity selection grid
5. **HistoryScreen** - List of all health logs

All placeholder screens are currently handled by `PlaceholderScreen()` in MainActivity.kt.

## Running the Project

1. Open the project in Android Studio
2. Sync Gradle files
3. Run on emulator or device (API 24+)

The app will:
- Initialize Room database on first launch
- Pre-populate 20 Haitian food items
- Display the Dashboard screen
- Allow navigation (other screens show placeholders)

## Architecture Compliance

✅ **MVVM Pattern** - ViewModel separates UI from data layer
✅ **Offline-First** - All data stored in Room database
✅ **Material 3** - Using latest Compose Material Design
✅ **Accessibility** - Large touch targets (100.dp buttons), large fonts
✅ **Exact Colors** - All colors match React prototype exactly
✅ **Room Database** - Follows DEVELOPMENT_GUIDE.md schema

## Notes

- DataStore is used for settings (replaces React's localStorage)
- Flow/StateFlow for reactive data (replaces React's useState)
- Navigation is state-based (replaces React's view state)
- Coil library ready for image loading from URLs
- WorkManager included in dependencies for future background sync
