package com.package.socialapp.repositor

import com.package.socialapp.uiscreens.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllPosts(): Flow<List<Post>>
    suspend fun createPost(post: Post)
    suspend fun likePost(postId: String, userId: String)
    suspend fun unlikePost(postId: String, userId: String)
    suspend fun deletePost(postId: String)
}
