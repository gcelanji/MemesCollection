package com.example.memescollection.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
class MemeEntity(
    @PrimaryKey
    val url : String,
    @ColumnInfo(name = "name")
    val name : String,
)