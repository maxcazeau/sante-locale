package com.santelocale.data.entity

/**
 * Extension functions for HealthLog entity
 * Matches the glucose check logic from the React Dashboard component
 */

/**
 * Glucose status result
 */
enum class GlucoseStatus {
    NORMAL,
    ATTENTION
}

/**
 * Data class representing the glucose check result with display strings
 */
data class GlucoseCheckResult(
    val status: GlucoseStatus,
    val displayText: String,
    val emoji: String
) {
    val isNormal: Boolean get() = status == GlucoseStatus.NORMAL
}

/**
 * Check if glucose level is normal based on unit type.
 *
 * Logic from mockup.md Dashboard component (lines 109-112):
 * ```javascript
 * {unit === 'mmol/L'
 *   ? (lastLog.value < 7.8 ? '✅ Normal' : '⚠️ Attention')
 *   : (lastLog.value < 140 ? '✅ Normal' : '⚠️ Attention')
 * }
 * ```
 *
 * @param unit The glucose unit: "mmol/L" or "mg/dL"
 * @return GlucoseCheckResult with status, display text, and emoji
 */
fun HealthLog.checkGlucoseStatus(unit: String): GlucoseCheckResult {
    // Only check glucose logs
    if (this.type != "GLUCOSE") {
        throw IllegalStateException("Cannot check glucose status for non-GLUCOSE log type")
    }

    // Apply the same logic as React Dashboard
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

/**
 * Get the full display string with emoji
 * e.g., "✅ Normal" or "⚠️ Attention"
 */
fun HealthLog.getGlucoseStatusDisplay(unit: String): String {
    val result = checkGlucoseStatus(unit)
    return "${result.emoji} ${result.displayText}"
}

/**
 * Check if glucose is normal (simple boolean)
 */
fun HealthLog.isGlucoseNormal(unit: String): Boolean {
    return checkGlucoseStatus(unit).isNormal
}

/**
 * Get threshold value for the given unit
 */
fun getGlucoseThreshold(unit: String): Float {
    return if (unit == "mmol/L") 7.8f else 140f
}

/**
 * Glucose unit constants
 */
object GlucoseUnit {
    const val MMOL_L = "mmol/L"
    const val MG_DL = "mg/dL"
}
