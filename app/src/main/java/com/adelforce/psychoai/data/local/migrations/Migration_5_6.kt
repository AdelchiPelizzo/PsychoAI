package com.adelforce.psychoai.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration_5_6 {

    val MIGRATION_5_6 =
        object : Migration(5,6) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    CREATE TABLE message_embeddings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        messageId INTEGER NOT NULL,
                        embedding TEXT NOT NULL,
                        createdAt INTEGER NOT NULL
                    )
                    """
                )
            }
        }
}