package com.example.memescollection.model.database

import androidx.room.*
import com.example.memescollection.model.Meme

@Dao
interface MemeDAO {
    @Insert(entity = MemeEntity::class,
        onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateMemeToDatabase(memeEntity: MemeEntity)

    @Query(value = "SELECT * FROM memes")
    suspend fun getMemesFromDatabase(): List<MemeEntity>
}