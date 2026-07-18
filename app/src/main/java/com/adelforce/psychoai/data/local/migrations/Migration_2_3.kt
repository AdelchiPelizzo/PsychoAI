package com.adelforce.psychoai.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_2_3 =
        object : Migration(2, 3) {

            override fun migrate(
                db: SupportSQLiteDatabase
            ) {

                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS themes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        lastUsedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )


                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS message_themes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        messageId INTEGER NOT NULL,
                        themeId INTEGER NOT NULL,
                        confidence REAL NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
}