# Suggested Commit Messages

## Commit 1: Initial Android Project Setup
```
feat: Set up initial Android project with MainActivity and Dashboard

- Create complete project structure for Santé Locale Android app
- Implement Room database with HealthLog and FoodItem entities
- Set up DAO layer with Flow-based reactive queries
- Create HealthDatabase with singleton pattern
- Add HealthRepository for offline-first data access
- Implement DashboardViewModel with StateFlow
- Configure Material 3 theme with exact colors from React mockup
  - Emerald-700 (#047857) for header
  - Blue-600 (#2563EB) for glucose button
  - Orange-500 (#F97316) for activity button
- Create reusable UI components (Header, BigButton)
- Implement Dashboard screen matching React prototype
- Add DataStore for settings persistence (user name, unit preference)
- Configure navigation state management
- Add all Gradle dependencies and Android manifest

Tech stack: Kotlin, Jetpack Compose, Room, Material 3, DataStore
Architecture: MVVM with Repository pattern
```

## Commit 2: Food Database Pre-population
```
feat: Add Haitian food database with pre-population

- Create FoodData object with all 20 food items from mockup
- Implement pre-population logic in HealthDatabase
- Match exact FOOD_DATABASE structure from React prototype
- Add category metadata (VERT, JAUNE, ROUGE) with labels and descriptions
- Generate placeholder image URLs matching React implementation
- Refactor HealthDatabase to use centralized FoodData object

Food categories:
- VERT (Green): 10 items - À Volonté
- JAUNE (Yellow): 6 items - Modérément
- ROUGE (Red): 4 items - À Éviter

All data matches mockup.md exactly for consistency with React prototype
```

## Commit 3: Glucose Status Check Logic
```
feat: Add glucose status check extension functions

- Create HealthLogExtensions.kt with glucose validation logic
- Implement checkGlucoseStatus() matching React Dashboard logic
- Add convenience methods: getGlucoseStatusDisplay(), isGlucoseNormal()
- Update DashboardScreen to use extension function
- Replace inline logic with reusable, testable functions

Thresholds (matching React mockup):
- mmol/L: Normal if < 7.8
- mg/dL: Normal if < 140

Returns "✅ Normal" or "⚠️ Attention" with emoji
Improves code maintainability and follows DRY principle
```

## Alternative: Single Combined Commit
```
feat: Implement Android app with Dashboard and food database

Set up complete Android project structure:
- Room database with HealthLog and FoodItem entities
- MVVM architecture with Repository and ViewModel
- Material 3 UI with exact colors from React mockup
- Reusable Header and BigButton components
- Dashboard screen with glucose status display
- DataStore for user settings persistence

Add Haitian food database:
- 20 pre-populated food items (VERT/JAUNE/ROUGE categories)
- Centralized FoodData object matching React prototype
- Category metadata for Food Guide feature

Implement glucose validation:
- Extension functions for HealthLog entity
- Status check logic matching React Dashboard
- Thresholds: <7.8 mmol/L or <140 mg/dL = Normal

All implementations match mockup.md React prototype exactly
Tech stack: Kotlin, Jetpack Compose, Room, Material 3, DataStore
```

## Commit Messages by File

### Core Setup Files
```
feat(database): Add Room database with HealthLog and FoodItem entities
feat(repository): Implement offline-first data repository pattern
feat(viewmodel): Create DashboardViewModel with StateFlow
```

### UI Files
```
feat(theme): Configure Material 3 theme with React mockup colors
feat(components): Add reusable Header and BigButton composables
feat(screens): Implement Dashboard screen matching React prototype
```

### Data Files
```
feat(data): Add Haitian food database with 20 pre-populated items
feat(extensions): Add glucose status check extension functions
```

### Configuration Files
```
chore(gradle): Add dependencies for Room, Compose, DataStore, Coil
chore(manifest): Configure app permissions and theme
```

## Recommended Approach

**Option 1: Three Focused Commits** (Recommended for better git history)
1. Initial project setup (architecture, database, UI foundation)
2. Food database pre-population (data layer)
3. Glucose validation logic (business logic)

**Option 2: Single Feature Commit** (If you prefer one commit)
- Use the "Alternative: Single Combined Commit" message above

## Conventional Commit Format

All messages follow the pattern:
```
<type>(<scope>): <subject>

<body>

<footer>
```

Types used:
- `feat:` New features
- `chore:` Build/config changes
- `refactor:` Code improvements

---

**Note:** These commit messages are ready to use with git. They provide clear context for what was built and why, making the git history useful for future reference.
