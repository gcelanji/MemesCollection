package com.example.memescollection.model.database

import androidx.room.*

@Dao
interface MemeDAO {
    @Insert(
        entity = MemeEntity::class,
        onConflict = OnConflictStrategy.IGNORE
    )
    suspend fun updateMemeToDatabase(memeEntity: MemeEntity)

    @Delete
    suspend fun deleteMemeFromDatabase(memeEntity: MemeEntity)

    @Query(value = "SELECT * FROM memes")
    suspend fun getMemesFromDatabase(): List<MemeEntity>

}