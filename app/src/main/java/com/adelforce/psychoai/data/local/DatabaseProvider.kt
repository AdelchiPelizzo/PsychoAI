package com.adelforce.psychoai.data.local

import android.content.Context
import androidx.room.Room
import com.adelforce.psychoai.data.local.migrations.DatabaseMigrations
import com.adelforce.psychoai.data.local.migrations.Migration_3_4
import com.adelforce.psychoai.data.local.migrations.Migration_4_5
import com.adelforce.psychoai.data.local.migrations.Migration_5_6
import com.adelforce.psychoai.data.local.migrations.Migration_6_7

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
                        DatabaseMigrations.MIGRATION_2_3,
                        Migration_3_4.MIGRATION_3_4,
                        Migration_4_5.MIGRATION_4_5,
                        Migration_5_6.MIGRATION_5_6,
                        Migration_6_7.MIGRATION_6_7
                    )
                    .build()

            INSTANCE = db

            db
        }
    }
}