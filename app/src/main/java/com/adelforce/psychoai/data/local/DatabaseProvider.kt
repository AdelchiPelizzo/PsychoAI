package com.adelforce.psychoai.data.local

import android.content.Context
import androidx.room.Room
import com.adelforce.psychoai.data.local.migrations.DatabaseMigrations

object DatabaseProvider {

    @Volatile
    private var INSTANCE: PsychoDatabase? = null

    fun getDatabase(context: Context): PsychoDatabase {

        return INSTANCE ?: synchronized(this) {

            val db =
                Room.databaseBuilder(
                    context.applicationContext,
                    PsychoDatabase::class.java,
                    "psycho_database"
                )
                    .addMigrations(
                        DatabaseMigrations.MIGRATION_2_3
                    )
                    .build()

            INSTANCE = db

            db
        }
    }
}