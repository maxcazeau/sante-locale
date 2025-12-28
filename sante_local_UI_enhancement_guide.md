# Santé Locale – UI Enhancement Guide

This guide defines the **Enhanced Design Language** to be applied to all secondary screens:

* GlucoseInput
* ActivityInput
* FoodGuide
* History
* Settings

---

## 🎨 Core Design Principles

* **Curved Backdrop**
  All screens must use a curved gradient header (Emerald / Green).

* **Overlapping Content**
  The main content card must overlap the header by **-32dp to -48dp**.

* **Soft Corners**
  Use `RoundedCornerShape(24.dp)` for cards and buttons.

* **Background Color**
  Use `Color(0xFFF1F5F9)` (Slate 100) for the screen background.

---

## 🛠️ Reusable Component: `CurvedScreenWrapper`

Create this helper composable to avoid repeating code across screens.

```kotlin
@Composable
fun CurvedScreenWrapper(
    title: String,
    onBack: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9)) // Slate 100
    ) {
        // 1. The Backdrop
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF059669), Color(0xFF047857))
                    )
                )
        ) {
            // Header Content
            Row(
                modifier = Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Glass effect)
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // 2. The Floating Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-48).dp)
                .padding(horizontal = 20.dp),
            content = content
        )
    }
}
```

---

## 📱 Screen-Specific Instructions

### A. Glucose Input Screen (`GlucoseInputScreen.kt`)

**Goal:** Make the number display look like a premium card and the keypad buttons softer.

* Wrap everything in `CurvedScreenWrapper(title = "Nouvelle Glycémie")`
* **Display Card**

    * First element inside the `content` Box
    * `RoundedCornerShape(24.dp)`
    * Elevation: `8.dp`
    * Background: White
* **Keypad**

    * Individual buttons: `RoundedCornerShape(16.dp)`
    * Background: White
* **Save Button**

    * Gradient background (Emerald)
    * `RoundedCornerShape(24.dp)`

---

### B. Food Guide Screen (`FoodGuideScreen.kt`)

**Goal:** Make the tabs feel integrated into the design.

* Wrap in `CurvedScreenWrapper(title = "Guide Alimentaire")`
* **Floating Tabs**

    * Place `ScrollableTabRow` inside a `Card`
    * `RoundedCornerShape(24.dp)`
    * Overlaps the header to appear "floating"
* **Food List**

    * Starts below the floating tabs
* **Food Cards**

    * Increase corner radius to `20.dp`

---

### C. Activity Input Screen (`ActivityInputScreen.kt`)

**Goal:** Make grid buttons look friendly.

* Wrap in `CurvedScreenWrapper(title = "Activité")`
* **Grid Buttons**

    * `RoundedCornerShape(24.dp)`
    * Background: White
* **Selection State**

    * Background: `Orange50`
    * Border: `Orange500`

---

### D. Settings Screen (`SettingsScreen.kt`)

**Goal:** Group settings into clean sections.

* Wrap in `CurvedScreenWrapper(title = "Paramètres")`
* **Content Structure**

    * One large overlapping Card
    * `RoundedCornerShape(24.dp)`
    * Background: White
* **Form Layout**

    * Place Name, Unit selector, and Danger button inside the same card
* **Unit Toggle**

    * Use a segmented pill shape
    * Fully rounded corners
    * Avoid square toggle buttons

---

## 🤖 Prompt for AI Agent

Copy and paste this block to refactor the screens automatically:

```
I want to apply the new "Enhanced UI" design to my secondary screens.

Task:
- Create a helper file `components/CurvedScreenWrapper.kt` using the code provided in the UI Enhancement Guide.
- Refactor:
  - GlucoseInputScreen.kt
  - ActivityInputScreen.kt
  - FoodGuideScreen.kt
  - HistoryScreen.kt
  - SettingsScreen.kt

Rules:
- Replace their top Header component with `CurvedScreenWrapper`.
- Ensure their main content overlaps the header by -48dp.
- Update all inner Cards and Buttons to use `RoundedCornerShape(24.dp)`.
```
