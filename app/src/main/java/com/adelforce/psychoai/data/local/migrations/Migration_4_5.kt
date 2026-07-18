package com.adelforce.psychoai.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object Migration_4_5 {

    val MIGRATION_4_5 =
        object : Migration(4, 5) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    ALTER TABLE user_memory
                    ADD COLUMN lastProcessedMessageId INTEGER NOT NULL DEFAULT 0
                    """
                )

            }
        }
}