package com.santelocale.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.santelocale.data.dao.FoodItemDao
import com.santelocale.data.dao.HealthLogDao
import com.santelocale.data.entity.FoodItem
import com.santelocale.data.entity.HealthLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [HealthLog::class, FoodItem::class],
    version = 1,
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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "sante_locale_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Prepopulate food items on first launch
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    prepopulateFoodItems(database.foodItemDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateFoodItems(foodDao: FoodItemDao) {
            // Check if already populated
            if (foodDao.getCount() > 0) return

            // Use the centralized FoodData object which matches FOOD_DATABASE from mockup.md
            foodDao.insertAll(com.santelocale.data.FoodData.getAllFoodItems())
        }
    }
}
