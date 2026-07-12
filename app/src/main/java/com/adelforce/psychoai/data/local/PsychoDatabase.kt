package com.adelforce.psychoai.data.local


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        MessageEntity::class
    ],
    version = 1
)
abstract class PsychoDatabase :
    RoomDatabase() {


    abstract fun messageDao():
            MessageDao

}