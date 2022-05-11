package com.example.memescollection.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemeEntity::class], version = 1)
abstract class MemeDatabase : RoomDatabase(){

    abstract fun memeDAO() : MemeDAO

    companion object{
        private var INSTANCE: MemeDatabase? = null

        fun newInstance(context: Context) : MemeDatabase =
            INSTANCE ?: synchronized(this){
                var temp = INSTANCE
                if (temp != null) return temp

                temp = Room.databaseBuilder(
                    context,
                    MemeDatabase::class.java,
                    "meme_db"
                ).build()

                INSTANCE = temp

                temp
            }
    }

}