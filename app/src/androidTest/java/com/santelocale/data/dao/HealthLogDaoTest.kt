package com.santelocale.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.santelocale.data.database.HealthDatabase
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HealthLogDaoTest {
    private lateinit var healthLogDao: HealthLogDao
    private lateinit var db: HealthDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, HealthDatabase::class.java
        ).build()
        healthLogDao = db.healthLogDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeLogAndReadInList() = runBlocking {
        val log = HealthLog(
            id = 0, // Auto-generate
            type = "GLUCOSE",
            value = 120f,
            displayValue = "120",
            label = "Avant repas",
            date = System.currentTimeMillis()
        )
        healthLogDao.insert(log)

        val byDate = healthLogDao.getAllLogs().first()
        assertEquals(120f, byDate[0].value)
    }
}
