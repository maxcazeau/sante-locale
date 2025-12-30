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
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

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

        private const val DATABASE_NAME = "sante_locale_database"

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {
                val appContext = context.applicationContext

                // Get or create the encryption key
                val keyManager = DatabaseKeyManager(appContext)
                val passphrase = keyManager.getOrCreateDatabaseKey()

                // Create SQLCipher SupportOpenHelperFactory with the passphrase
                val factory = SupportOpenHelperFactory(passphrase)

                val instance = Room.databaseBuilder(
                    appContext,
                    HealthDatabase::class.java,
                    DATABASE_NAME
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // Prepopulate food items if the database is empty
                CoroutineScope(Dispatchers.IO).launch {
                    if (instance.foodItemDao().getCount() == 0) {
                        loadFoodData(appContext, instance)
                    }
                }

                instance
            }
        }

        /**
         * Delete the existing unencrypted database.
         * Call this before first access when migrating to encrypted database.
         */
        fun deleteExistingDatabase(context: Context) {
            context.deleteDatabase(DATABASE_NAME)
            context.deleteDatabase("$DATABASE_NAME-wal")
            context.deleteDatabase("$DATABASE_NAME-shm")
        }
    }
}
