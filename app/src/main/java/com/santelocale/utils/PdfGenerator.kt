package com.santelocale.utils

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import com.santelocale.data.entity.HealthLog
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * PDF Generator for health reports.
 * Creates a formatted PDF with glucose and activity logs.
 */
object PdfGenerator {

    private const val PAGE_WIDTH = 595  // A4 width in points
    private const val PAGE_HEIGHT = 842 // A4 height in points
    private const val MARGIN = 50f
    private const val LINE_HEIGHT = 25f

    // Column positions
    private const val COL_DATE = 50f
    private const val COL_TYPE = 200f
    private const val COL_VALUE = 400f

    /**
     * Generates a PDF health report.
     *
     * @param context Application context
     * @param userName Patient's name
     * @param logs List of health logs to include
     * @param unit Glucose unit preference (mg/dL or mmol/L)
     * @return The generated PDF file
     */
    fun generateReport(
        context: Context,
        userName: String,
        logs: List<HealthLog>,
        unit: String = "mg/dL"
    ): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        var page = document.startPage(pageInfo)
        var canvas = page.canvas
        var yPosition = MARGIN

        // Paints
        val titlePaint = Paint().apply {
            color = Color.parseColor("#047857") // Emerald-700
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }

        val subHeaderPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 14f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = 12f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val warningPaint = Paint().apply {
            color = Color.parseColor("#DC2626") // Red-600
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1f
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE)
        val today = dateFormat.format(Date())

        // === HEADER ===
        yPosition += 30f
        canvas.drawText(
            "RAPPORT DE SANTÉ",
            PAGE_WIDTH / 2f,
            yPosition,
            titlePaint
        )

        // === SUB-HEADER ===
        yPosition += 40f
        canvas.drawText(
            "Patient: ${userName.ifEmpty { "Non spécifié" }}",
            MARGIN,
            yPosition,
            subHeaderPaint
        )

        yPosition += 20f
        canvas.drawText(
            "Généré le: $today",
            MARGIN,
            yPosition,
            subHeaderPaint
        )

        // === SEPARATOR LINE ===
        yPosition += 20f
        canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint)

        // === TABLE HEADERS ===
        yPosition += 30f
        canvas.drawText("DATE", COL_DATE, yPosition, headerPaint)
        canvas.drawText("TYPE", COL_TYPE, yPosition, headerPaint)
        canvas.drawText("VALEUR", COL_VALUE, yPosition, headerPaint)

        // Header underline
        yPosition += 8f
        canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint)

        // === TABLE ROWS ===
        yPosition += LINE_HEIGHT

        // Sort logs by date (newest first)
        val sortedLogs = logs.sortedByDescending { it.date }

        for (log in sortedLogs) {
            // Check if we need a new page
            if (yPosition > PAGE_HEIGHT - MARGIN - 50) {
                document.finishPage(page)
                val newPageInfo = PdfDocument.PageInfo.Builder(
                    PAGE_WIDTH, PAGE_HEIGHT, document.pages.size + 1
                ).create()
                page = document.startPage(newPageInfo)
                canvas = page.canvas
                yPosition = MARGIN + 30f

                // Redraw table headers on new page
                canvas.drawText("DATE", COL_DATE, yPosition, headerPaint)
                canvas.drawText("TYPE", COL_TYPE, yPosition, headerPaint)
                canvas.drawText("VALEUR", COL_VALUE, yPosition, headerPaint)
                yPosition += 8f
                canvas.drawLine(MARGIN, yPosition, PAGE_WIDTH - MARGIN, yPosition, linePaint)
                yPosition += LINE_HEIGHT
            }

            // Date column
            val logDate = dateTimeFormat.format(Date(log.date))
            canvas.drawText(logDate, COL_DATE, yPosition, textPaint)

            // Type and Value columns
            when (log.type) {
                "GLUCOSE" -> {
                    canvas.drawText("Glycémie", COL_TYPE, yPosition, textPaint)

                    // Display value with unit
                    val displayVal = log.displayValue ?: log.value.toString()
                    val valueText = "$displayVal $unit"
                    canvas.drawText(valueText, COL_VALUE, yPosition, textPaint)

                    // Warning indicator for high glucose
                    val threshold = if (unit == "mmol/L") 7.8f else 140f
                    if (log.value > threshold) {
                        val valueWidth = textPaint.measureText(valueText)
                        canvas.drawText(
                            " (!)",
                            COL_VALUE + valueWidth,
                            yPosition,
                            warningPaint
                        )
                    }
                }
                "ACTIVITY" -> {
                    canvas.drawText("Activité", COL_TYPE, yPosition, textPaint)
                    val activityText = log.label ?: "${log.value.toInt()} min"
                    canvas.drawText(activityText, COL_VALUE, yPosition, textPaint)
                }
                else -> {
                    canvas.drawText(log.type, COL_TYPE, yPosition, textPaint)
                    canvas.drawText(log.value.toString(), COL_VALUE, yPosition, textPaint)
                }
            }

            yPosition += LINE_HEIGHT
        }

        // === FOOTER ===
        yPosition = PAGE_HEIGHT - MARGIN
        val footerPaint = Paint().apply {
            color = Color.GRAY
            textSize = 10f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            "Santé Locale - Application de suivi du pré-diabète",
            PAGE_WIDTH / 2f,
            yPosition,
            footerPaint
        )

        // Finish the document
        document.finishPage(page)

        // Save to file
        val fileName = "rapport_sante_${System.currentTimeMillis()}.pdf"
        val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: context.filesDir
        val file = File(documentsDir, fileName)

        FileOutputStream(file).use { outputStream ->
            document.writeTo(outputStream)
        }

        document.close()

        return file
    }

    /**
     * Shares a PDF file via Intent.ACTION_SEND.
     * Supports email, WhatsApp, and other sharing apps.
     *
     * @param context Application context
     * @param file The PDF file to share
     * @param userName Patient's name for the subject line
     */
    fun shareReport(context: Context, file: File, userName: String = "") {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Rapport de Santé${if (userName.isNotEmpty()) " - $userName" else ""}"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Voici le rapport de santé généré par Santé Locale."
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(shareIntent, "Partager le rapport")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    /**
     * Generates and immediately shares a PDF report.
     * Convenience function combining generateReport and shareReport.
     *
     * @param context Application context
     * @param userName Patient's name
     * @param logs List of health logs
     * @param unit Glucose unit preference
     */
    fun generateAndShare(
        context: Context,
        userName: String,
        logs: List<HealthLog>,
        unit: String = "mg/dL"
    ) {
        val file = generateReport(context, userName, logs, unit)
        shareReport(context, file, userName)
    }
}
