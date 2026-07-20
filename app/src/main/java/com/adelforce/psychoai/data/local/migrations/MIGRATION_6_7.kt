package com.adelforce.psychoai.data.local.migrations
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration_6_7 {

    val MIGRATION_6_7 =
        object : Migration(6,7) {

            override fun migrate(
                database: SupportSQLiteDatabase
            ) {

                database.execSQL(
                    """
                    ALTER TABLE message_embeddings
                    ADD COLUMN conversationId INTEGER NOT NULL DEFAULT 0
                    """
                )
            }
        }
}