package com.adelforce.psychoai.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object Migration_3_4 {


    val MIGRATION_3_4 =
        object : Migration(3, 4) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user_memory (
                        id INTEGER NOT NULL,
                        summary TEXT NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        sourceMessageCount INTEGER NOT NULL,
                        PRIMARY KEY(id)
                    )
                    """.trimIndent()
                )

            }
        }
}