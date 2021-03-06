package com.example.memescollection.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MemeEntity::class], version = 1)
abstract class MemeDatabase : RoomDatabase() {

    abstract fun memeDAO(): MemeDAO


}