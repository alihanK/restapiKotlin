package com.package.socialapp.uiscreens.model

data class Post(
    val id: String = "",
    val authorId: String = "",
    val authorEmail: String = "",
    val authorName: String = "",
    val gender: String = "NON",
    val text: String = "",
    val timestamp: Long = 0,
    val likes: List<String> = emptyList()
)
