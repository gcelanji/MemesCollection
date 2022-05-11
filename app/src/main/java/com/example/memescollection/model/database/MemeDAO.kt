package com.example.memescollection.model.database

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.memescollection.model.Meme

@Dao
interface MemeDAO {
    @Update(entity = MemeEntity::class,
        onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMemeToDatabase(memeEntity: MemeEntity)

    @Query(value = "SELECT * FROM memes")
    suspend fun getMemesFromDatabase(): List<Meme>
}