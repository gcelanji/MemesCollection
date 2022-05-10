package com.example.memescollection.model

data class MemesResponse(
    val data: Memes
)

data class Memes(
    val memes: List<Meme>
)

data class Meme(
    val name: String,
    val url: String
)