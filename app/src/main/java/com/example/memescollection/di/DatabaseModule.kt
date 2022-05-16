package com.example.memescollection.di

import android.content.Context
import androidx.room.Room
import com.example.memescollection.model.database.MemeDAO
import com.example.memescollection.model.database.MemeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{
    @Singleton
    @Provides
    fun provideSearchDatabase(@ApplicationContext context : Context) =
        Room.databaseBuilder(context, MemeDatabase::class.java, "room_DB")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSearchDAO(memeDatabase: MemeDatabase): MemeDAO {
        return memeDatabase.memeDAO()
    }
}


//companion object{
//    private var INSTANCE: MemeDatabase? = null
//
//    fun newInstance(context: Context) : MemeDatabase =
//        INSTANCE ?: synchronized(this){
//            var temp = INSTANCE
//            if (temp != null) return temp
//
//            temp = Room.databaseBuilder(
//                context,
//                MemeDatabase::class.java,
//                "meme_db"
//            ).build()
//
//            INSTANCE = temp
//
//            temp
//        }
//}