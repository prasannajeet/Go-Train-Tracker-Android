package com.p2.apps.mygotraintracker.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Helper class to manage database migrations
 */
object DatabaseMigrations {
    /**
     * Migration from version 1 to version 2
     * Adds the user_preferences table for storing the home station
     */
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the user_preferences table
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `user_preferences` (
                    `id` INTEGER NOT NULL PRIMARY KEY,
                    `homeStationCode` TEXT NOT NULL,
                    `homeStationName` TEXT NOT NULL,
                    `homeStationPublicStopId` TEXT NOT NULL
                )
                """
            )
        }
    }
    
    /**
     * List of all migrations
     */
    val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2
    )
} 