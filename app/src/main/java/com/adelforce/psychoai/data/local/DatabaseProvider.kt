package com.adelforce.psychoai.data.local

import android.content.Context
import androidx.room.Room

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
                    .fallbackToDestructiveMigration()
                    .build()

            INSTANCE = db

            db
        }
    }
}