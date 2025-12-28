# Glucose Check Logic: React → Kotlin

## Overview

This document shows how the glucose status check logic from the React Dashboard has been implemented as a Kotlin extension function.

## React Implementation (mockup.md)

```javascript
// Lines 108-112 in Dashboard component
{unit === 'mmol/L'
  ? (lastLog.value < 7.8 ? '✅ Normal' : '⚠️ Attention')
  : (lastLog.value < 140 ? '✅ Normal' : '⚠️ Attention')
}
```

### Logic Breakdown:
- **mmol/L**: Normal if `< 7.8`, otherwise Attention
- **mg/dL**: Normal if `< 140`, otherwise Attention
- Display: Emoji + Status text

## Kotlin Implementation

### File: [HealthLogExtensions.kt](app/src/main/java/com/santelocale/data/entity/HealthLogExtensions.kt)

```kotlin
fun HealthLog.checkGlucoseStatus(unit: String): GlucoseCheckResult {
    if (this.type != "GLUCOSE") {
        throw IllegalStateException("Cannot check glucose status for non-GLUCOSE log type")
    }

    val isNormal = if (unit == "mmol/L") {
        this.value < 7.8f
    } else {
        this.value < 140f
    }

    return if (isNormal) {
        GlucoseCheckResult(
            status = GlucoseStatus.NORMAL,
            displayText = "Normal",
            emoji = "✅"
        )
    } else {
        GlucoseCheckResult(
            status = GlucoseStatus.ATTENTION,
            displayText = "Attention",
            emoji = "⚠️"
        )
    }
}
```

## Usage Examples

### 1. Get Full Display String (Used in DashboardScreen)

```kotlin
// Before (inline logic)
val isNormal = if (unit == "mmol/L") {
    lastLog.value < 7.8f
} else {
    lastLog.value < 140f
}
Text(text = if (isNormal) "✅ Normal" else "⚠️ Attention")

// After (using extension)
Text(text = lastLog.getGlucoseStatusDisplay(unit))
```

### 2. Get Detailed Result

```kotlin
val result = healthLog.checkGlucoseStatus("mmol/L")
println(result.status)       // GlucoseStatus.NORMAL or GlucoseStatus.ATTENTION
println(result.displayText)  // "Normal" or "Attention"
println(result.emoji)        // "✅" or "⚠️"
println(result.isNormal)     // true or false
```

### 3. Simple Boolean Check

```kotlin
if (healthLog.isGlucoseNormal("mg/dL")) {
    // Glucose is normal
}
```

### 4. Get Threshold Value

```kotlin
val threshold = getGlucoseThreshold("mmol/L")  // Returns 7.8f
val threshold = getGlucoseThreshold("mg/dL")   // Returns 140f
```

## Glucose Thresholds

| Unit | Threshold | Classification |
|------|-----------|----------------|
| mmol/L | < 7.8 | Normal (2 hours post-meal) |
| mmol/L | ≥ 7.8 | Attention needed |
| mg/dL | < 140 | Normal (2 hours post-meal) |
| mg/dL | ≥ 140 | Attention needed |

### Medical Context
These thresholds represent **post-prandial (2-hour after meal)** glucose levels:
- **Normal**: < 7.8 mmol/L (< 140 mg/dL)
- **Pre-diabetes**: 7.8-11.0 mmol/L (140-199 mg/dL)
- **Diabetes**: ≥ 11.1 mmol/L (≥ 200 mg/dL)

The app uses a simplified "Normal" vs "Attention" categorization for seniors.

## Data Classes

### GlucoseStatus Enum
```kotlin
enum class GlucoseStatus {
    NORMAL,
    ATTENTION
}
```

### GlucoseCheckResult
```kotlin
data class GlucoseCheckResult(
    val status: GlucoseStatus,
    val displayText: String,
    val emoji: String
) {
    val isNormal: Boolean get() = status == GlucoseStatus.NORMAL
}
```

### GlucoseUnit Constants
```kotlin
object GlucoseUnit {
    const val MMOL_L = "mmol/L"
    const val MG_DL = "mg/dL"
}
```

## Updated DashboardScreen

**Before:**
```kotlin
// Inline logic (lines 173-185)
val isNormal = if (unit == "mmol/L") {
    lastLog.value < 7.8f
} else {
    lastLog.value < 140f
}

Text(
    text = if (isNormal) "✅ Normal" else "⚠️ Attention",
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    color = Emerald600
)
```

**After:**
```kotlin
// Using extension function (line 176)
Text(
    text = lastLog.getGlucoseStatusDisplay(unit),
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    color = Emerald600
)
```

## Benefits

1. **DRY Principle**: Logic is defined once, reused everywhere
2. **Type Safety**: Enum-based status instead of strings
3. **Testability**: Easy to unit test the extension function
4. **Maintainability**: Change thresholds in one place
5. **Exact Match**: Replicates React logic perfectly

## Testing Example

```kotlin
@Test
fun testGlucoseCheckLogic() {
    // Test mmol/L
    val log1 = HealthLog(type = "GLUCOSE", value = 7.5f, ...)
    assertEquals("✅ Normal", log1.getGlucoseStatusDisplay("mmol/L"))

    val log2 = HealthLog(type = "GLUCOSE", value = 8.0f, ...)
    assertEquals("⚠️ Attention", log2.getGlucoseStatusDisplay("mmol/L"))

    // Test mg/dL
    val log3 = HealthLog(type = "GLUCOSE", value = 135f, ...)
    assertEquals("✅ Normal", log3.getGlucoseStatusDisplay("mg/dL"))

    val log4 = HealthLog(type = "GLUCOSE", value = 145f, ...)
    assertEquals("⚠️ Attention", log4.getGlucoseStatusDisplay("mg/dL"))
}
```

## Consistency with React

| Aspect | React | Kotlin |
|--------|-------|--------|
| mmol/L threshold | `< 7.8` | `< 7.8f` ✅ |
| mg/dL threshold | `< 140` | `< 140f` ✅ |
| Normal display | `'✅ Normal'` | `"✅ Normal"` ✅ |
| Attention display | `'⚠️ Attention'` | `"⚠️ Attention"` ✅ |
| Conditional logic | Ternary operator | if/else ✅ |

Perfect 1:1 match with the React implementation!
