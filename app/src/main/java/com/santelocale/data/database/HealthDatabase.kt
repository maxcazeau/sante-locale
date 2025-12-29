package com.santelocale.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.santelocale.data.loadFoodData
import com.santelocale.data.dao.FoodItemDao
import com.santelocale.data.dao.HealthLogDao
import com.santelocale.data.entity.FoodItem
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [HealthLog::class, FoodItem::class],
    version = 2,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {
    abstract fun healthLogDao(): HealthLogDao
    abstract fun foodItemDao(): FoodItemDao

    companion object {
        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val appContext = context.applicationContext
                val instance = Room.databaseBuilder(
                    appContext,
                    HealthDatabase::class.java,
                    "sante_locale_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                // Prepopulate food items if empty (handles first launch and destructive migrations)
                CoroutineScope(Dispatchers.IO).launch {
                    val count = instance.foodItemDao().getCount()
                    if (count == 0) {
                        loadFoodData(appContext, instance)
                    }
                }

                instance
            }
        }
    }
}
