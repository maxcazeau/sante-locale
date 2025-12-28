# Santé Locale – Android Development Handover Guide

## Project Overview

**Name:** Santé Locale  
**Goal:** Offline-first pre-diabetes management app for Haitian seniors

### Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Database:** Room (Offline storage)
- **Background Tasks:** WorkManager (Data syncing)
- **Architecture:** MVVM (Model–View–ViewModel)

---

## 🤖 Prompt for Your AI Agent

Copy and paste the following into your AI Chat / Agent to scaffold the project:

> I am building a native Android app called **"Santé Locale"** using Kotlin and Jetpack Compose.
>
> ### Core Requirements:
> 1. **Room Database**  
>    I need a database entity called `HealthLog` with fields:
>    - `id` (Int, auto-generated)
>    - `type` (String: `GLUCOSE`, `ACTIVITY`)
>    - `value` (Float)
>    - `displayValue` (String)
>    - `timestamp` (Long)
>    - `note` (String)
>
> 2. **Offline-First**  
>    All UI must read from a local `HealthRepository` linked to the Room DAO.
>
> 3. **UI Structure**  
>    A main `Scaffold` with a strictly defined navigation graph:
>    - Dashboard
>    - GlucoseInput
>    - FoodGuide
>    - ActivityInput
>    - History
>    - Settings
>
> 4. **UI Style**  
>    - Material 3
>    - Large touch targets
>    - "Big Button" component for seniors  
>    - Colors:
>      - Emerald Green (Primary)
>      - Blue (Glucose)
>      - Orange (Activity)
>
> Please generate the folder structure and the basic `MainActivity.kt` setup with Room database instantiation.

---

## Technical Specifications

### 1. Data Layer (Room)

> The React prototype uses `localStorage`.  
> In Android, we use **Room**.

#### Entity: `HealthLog`

```kotlin
@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "GLUCOSE" or "ACTIVITY"
    val value: Float, // Numeric value for calculations
    val displayValue: String?, // What the user saw (e.g. "5,4")
    val date: Long // Epoch timestamp
)
```

#### Entity: `FoodItem` (For the Guide)

```kotlin
@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "VERT", "JAUNE", "ROUGE"
    val imageUrl: String,
    val tip: String
)
```

---

### 2. UI Components (Compose vs React)

Mapping React components to Jetpack Compose:

| React Component | Jetpack Compose Equivalent |
|-----------------|----------------------------|
| `<div>` | `Box`, `Column`, or `Row` |
| `<button onClick={...}>` | `Button(onClick = { ... })` |
| `useState` | `var state by remember { mutableStateOf(...) }` |
| `className="bg-emerald-700"` | `Modifier.background(Color(0xFF047857))` |
| `<img src={...} />` | `AsyncImage` (Coil library) |

---

### 3. Traffic Light Logic (Food Guide)

```kotlin
var activeTab by remember { mutableStateOf("VERT") }
val filteredFoods = allFoods.filter { it.category == activeTab }

val tabColor = when (activeTab) {
    "VERT" -> Color(0xFF059669)  // Emerald 600
    "JAUNE" -> Color(0xFFFACA04) // Yellow 600
    "ROUGE" -> Color(0xFFFDC262) // Red 600
    else -> Color.Gray
}
```

---

### 4. PDF Generation (Export Feature)

For the export feature, request the following from your AI agent:

> Create a function `generatePdf(context: Context, logs: List<HealthLog>)`  
> using `android.graphics.pdf.PdfDocument`.
>
> The PDF should:
> - Render a simple table
> - Include headers: **Date**, **Type**, **Valeur**
> - Save the file to the **Downloads** folder

---

## 🚀 Workflow Checklist

1. Create a new **Empty Activity** project in Android Studio
2. Add dependencies:
   - `androidx.room:room-runtime`
   - `io.coil-kt:coil-compose`
3. Create and connect the Room database
4. Rewrite React `App.jsx` logic in Jetpack Compose
5. Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

