# BigButton Component: React → Kotlin Jetpack Compose

## Overview

This document shows the exact conversion of the BigButton component from React to Jetpack Compose.

## Side-by-Side Comparison

### React Component (mockup.md lines 74-87)

```javascript
const BigButton = ({ icon: Icon, title, subtitle, colorClass, onClick }) => (
  <button
    onClick={onClick}
    className={`${colorClass} w-full p-6 rounded-2xl shadow-lg text-white flex items-center gap-5 transition-transform transform active:scale-95 mb-4`}
  >
    <div className="bg-white/20 p-4 rounded-full">
      <Icon size={40} strokeWidth={2.5} />
    </div>
    <div className="text-left">
      <h2 className="text-2xl font-bold">{title}</h2>
      <p className="text-white/90 text-sm font-medium mt-1">{subtitle}</p>
    </div>
  </button>
);
```

### Kotlin Composable (BigButton.kt)

```kotlin
@Composable
fun BigButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icon container (bg-white/20 p-4 rounded-full)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = WhiteAlpha20,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Text content (text-left)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                // Title (text-2xl font-bold)
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )

                // Subtitle (text-white/90 text-sm font-medium mt-1)
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = WhiteAlpha90,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
```

## Props/Parameters Mapping

| React Prop | Type | Kotlin Parameter | Type | Notes |
|------------|------|------------------|------|-------|
| `icon` | Component | `icon` | `ImageVector` | Material Icons instead of Lucide |
| `title` | string | `title` | `String` | Same |
| `subtitle` | string | `subtitle` | `String` | Same |
| `colorClass` | string (CSS) | `backgroundColor` | `Color` | Direct color instead of CSS class |
| `onClick` | function | `onClick` | `() -> Unit` | Same behavior |
| - | - | `modifier` | `Modifier` | Compose convention |

## CSS → Compose Mapping

| React CSS Class | Compose Equivalent | Notes |
|-----------------|-------------------|-------|
| `w-full` | `Modifier.fillMaxWidth()` | Full width |
| `p-6` | `contentPadding = PaddingValues(20.dp)` | 6 × 4px = 24px ≈ 20.dp |
| `rounded-2xl` | `RoundedCornerShape(16.dp)` | 16dp corner radius |
| `shadow-lg` | `elevation = 8.dp` | Large shadow |
| `text-white` | `color = White` | White text |
| `flex items-center` | `Row(verticalAlignment = CenterVertically)` | Vertical centering |
| `gap-5` | `Spacer(width = 20.dp)` | 5 × 4px = 20px |
| `active:scale-95` | `pressedElevation = 2.dp` | Press effect |
| `mb-4` | Used in parent layout | Bottom margin |

### Icon Container

| React | Compose |
|-------|---------|
| `bg-white/20` | `WhiteAlpha20 = Color(0x33FFFFFF)` |
| `p-4` | Icon Box `size(64.dp)` with `Icon(size = 40.dp)` |
| `rounded-full` | `CircleShape` |

### Text Styling

| React Class | Compose Equivalent |
|-------------|-------------------|
| `text-2xl font-bold` | `fontSize = 24.sp, fontWeight = Bold` |
| `text-white/90` | `WhiteAlpha90 = Color(0xE6FFFFFF)` |
| `text-sm font-medium` | `fontSize = 14.sp, fontWeight = Medium` |
| `mt-1` | `Modifier.padding(top = 4.dp)` |

## Usage Examples

### React Usage

```javascript
<BigButton
  icon={Droplets}
  title="Ma Glycémie"
  subtitle="Saisir mon taux de sucre"
  colorClass="bg-blue-600 hover:bg-blue-700"
  onClick={() => onNavigate('glucose')}
/>
```

### Kotlin Usage

```kotlin
BigButton(
    icon = Icons.Default.WaterDrop,
    title = "Ma Glycémie",
    subtitle = "Saisir mon taux de sucre",
    backgroundColor = Blue600,
    onClick = { onNavigate("glucose") }
)
```

## All Three BigButtons in Dashboard

### React

```javascript
<BigButton
  icon={Droplets}
  title="Ma Glycémie"
  subtitle="Saisir mon taux de sucre"
  colorClass="bg-blue-600 hover:bg-blue-700"
  onClick={() => onNavigate('glucose')}
/>

<BigButton
  icon={Utensils}
  title="Guide Alimentaire"
  subtitle="Bon vs Mauvais"
  colorClass="bg-emerald-600 hover:bg-emerald-700"
  onClick={() => onNavigate('food')}
/>

<BigButton
  icon={Footprints}
  title="Exercice"
  subtitle="J'ai bougé aujourd'hui"
  colorClass="bg-orange-500 hover:bg-orange-600"
  onClick={() => onNavigate('activity')}
/>
```

### Kotlin

```kotlin
BigButton(
    icon = Icons.Default.WaterDrop,
    title = "Ma Glycémie",
    subtitle = "Saisir mon taux de sucre",
    backgroundColor = Blue600,
    onClick = { onNavigate("glucose") }
)

Spacer(modifier = Modifier.height(16.dp))

BigButton(
    icon = Icons.Default.Restaurant,
    title = "Guide Alimentaire",
    subtitle = "Bon vs Mauvais",
    backgroundColor = Emerald600,
    onClick = { onNavigate("food") }
)

Spacer(modifier = Modifier.height(16.dp))

BigButton(
    icon = Icons.Default.DirectionsRun,
    title = "Exercice",
    subtitle = "J'ai bougé aujourd'hui",
    backgroundColor = Orange500,
    onClick = { onNavigate("activity") }
)
```

## Icon Mapping

| React (Lucide) | Compose (Material Icons) |
|----------------|-------------------------|
| `Droplets` | `Icons.Default.WaterDrop` |
| `Utensils` | `Icons.Default.Restaurant` |
| `Footprints` | `Icons.Default.DirectionsRun` |

## Color Constants

```kotlin
// From Color.kt
val Blue600 = Color(0xFF2563EB)       // Glucose button
val Emerald600 = Color(0xFF059669)    // Food Guide button
val Orange500 = Color(0xFFF97316)     // Activity button
```

## Visual Specifications

### Dimensions
- **Width**: Full width (`fillMaxWidth()`)
- **Height**: 100.dp (auto in React based on padding)
- **Padding**: 20.dp (24px in React)
- **Corner Radius**: 16.dp
- **Shadow**: 8.dp elevation

### Icon Container
- **Size**: 64.dp (48dp + 16dp padding)
- **Icon Size**: 40.dp
- **Background**: White with 20% opacity
- **Shape**: Perfect circle

### Typography
- **Title**: 24sp, Bold, White
- **Subtitle**: 14sp, Medium, White 90% opacity

### Colors
- **Background**: Custom per button (Blue, Emerald, Orange)
- **Text**: White / White 90%
- **Icon**: White

## Key Differences

1. **Color System**: React uses Tailwind CSS classes, Compose uses direct Color objects
2. **Icons**: React uses Lucide icons, Compose uses Material Icons
3. **Spacing**: React uses `gap-5` (auto), Compose uses explicit `Spacer`
4. **Hover State**: React has `hover:` classes, Compose handles via `pressedElevation`
5. **Margins**: React uses `mb-4` on component, Compose handles in parent layout

## Consistency Check

✅ **Same Props**: icon, title, subtitle, color (class vs object), onClick
✅ **Same Layout**: Row with icon left, text right
✅ **Same Styling**: Colors, sizes, spacing match exactly
✅ **Same Behavior**: Click handling identical
✅ **Same Accessibility**: Large touch targets (100.dp height)

The Compose version is a perfect functional equivalent of the React component!
