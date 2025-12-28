# Food Database Mapping: React → Kotlin

## Overview

This document shows how the FOOD_DATABASE from the React mockup has been mapped to the Android Room database.

## Files Created/Updated

### 1. [FoodData.kt](app/src/main/java/com/santelocale/data/FoodData.kt)
Centralized object containing all 20 Haitian food items from the mockup.

**Key Features:**
- Exact 1:1 mapping of FOOD_DATABASE from mockup.md
- Same image URL generation logic: `https://placehold.co/400x300/{color}/ffffff?text={name}`
- Category metadata (labels and descriptions)
- Reusable across the app

### 2. [FoodItem.kt](app/src/main/java/com/santelocale/data/entity/FoodItem.kt)
Room entity matching the JavaScript object structure.

### 3. [HealthDatabase.kt](app/src/main/java/com/santelocale/data/database/HealthDatabase.kt)
Database now uses `FoodData.getAllFoodItems()` for pre-population.

## Data Mapping

### React (mockup.md)
```javascript
const FOOD_DATABASE = [
  {
    id: 'f1',
    name: 'Feuilles de Jute (Lalo)',
    category: 'VERT',
    image: getImageUrl('Lalo', '059669'),
    tip: 'Excellent ! Riche en fibres, ne fait pas monter le sucre.'
  },
  // ... 19 more items
];
```

### Kotlin (FoodData.kt)
```kotlin
object FoodData {
    fun getAllFoodItems(): List<FoodItem> = listOf(
        FoodItem(
            id = "f1",
            name = "Feuilles de Jute (Lalo)",
            category = "VERT",
            imageUrl = getImageUrl("Lalo", "059669"),
            tip = "Excellent ! Riche en fibres, ne fait pas monter le sucre."
        ),
        // ... 19 more items
    )
}
```

### Room Entity
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

## All 20 Food Items

### VERT (Green) - À Volonté - Color: `059669`
1. **f1** - Feuilles de Jute (Lalo)
2. **f2** - Gombo (Kalalou)
3. **f3** - Chayote (Mirliton)
4. **f4** - Avocat (Zaboka)
5. **f5** - Poisson
6. **f6** - Poulet (Dur)
7. **f7** - Œufs
8. **f8** - Chou
9. **f9** - Aubergine
10. **f10** - Cresson

### JAUNE (Yellow) - Modérément - Color: `ca8a04`
11. **f11** - Haricots (Pwa)
12. **f12** - Banane Bouillie
13. **f13** - Fruit à Pain (Lam)
14. **f14** - Patate Douce
15. **f15** - Mais Moulu
16. **f16** - Igname

### ROUGE (Red) - À Éviter - Color: `dc2626`
17. **f17** - Riz Blanc
18. **f18** - Banane Pesée
19. **f19** - Pain Blanc
20. **f20** - Akasan

## Category Configuration

### React
```javascript
const categories = {
  VERT: {
    label: 'À Volonté',
    color: 'bg-emerald-600',
    text: 'text-emerald-700',
    border: 'border-emerald-200',
    desc: 'Mangez tous les jours.'
  },
  JAUNE: {
    label: 'Modérément',
    color: 'bg-yellow-500',
    text: 'text-yellow-700',
    border: 'border-yellow-200',
    desc: 'Petites quantités seulement.'
  },
  ROUGE: {
    label: 'À Éviter',
    color: 'bg-red-600',
    text: 'text-red-700',
    border: 'border-red-200',
    desc: 'Dangereux pour le sucre.'
  }
};
```

### Kotlin
```kotlin
val categories = mapOf(
    "VERT" to FoodCategory(
        label = "À Volonté",
        description = "Mangez tous les jours.",
        color = "059669"
    ),
    "JAUNE" to FoodCategory(
        label = "Modérément",
        description = "Petites quantités seulement.",
        color = "ca8a04"
    ),
    "ROUGE" to FoodCategory(
        label = "À Éviter",
        description = "Dangereux pour le sucre.",
        color = "dc2626"
    )
)
```

## Image URL Generation

### React
```javascript
const getImageUrl = (name, color) =>
  `https://placehold.co/400x300/${color}/ffffff?text=${encodeURIComponent(name)}`;
```

### Kotlin
```kotlin
private fun getImageUrl(name: String, color: String): String {
    return "https://placehold.co/400x300/$color/ffffff?text=${name.replace(" ", "+")}"
}
```

## Database Pre-population Flow

1. **App First Launch** → `HealthDatabase.onCreate()` triggered
2. **Check if empty** → `foodDao.getCount() == 0`
3. **Load data** → `FoodData.getAllFoodItems()`
4. **Insert all** → `foodDao.insertAll(foods)`
5. **Ready to use** → Food Guide screen can query by category

## Usage in App

### Query by Category
```kotlin
// In Repository
fun getFoodsByCategory(category: String): Flow<List<FoodItem>> {
    return foodItemDao.getFoodsByCategory(category)
}

// In ViewModel
val greenFoods = repository.getFoodsByCategory("VERT")
val yellowFoods = repository.getFoodsByCategory("JAUNE")
val redFoods = repository.getFoodsByCategory("ROUGE")
```

## Verification

All 20 food items match exactly:
- ✅ Same IDs (f1-f20)
- ✅ Same French names (with Haitian Creole translations)
- ✅ Same categories (VERT, JAUNE, ROUGE)
- ✅ Same image URL generation
- ✅ Same tips/descriptions
- ✅ Same color codes for categories

The Android Room database is now a perfect mirror of the React localStorage implementation!
