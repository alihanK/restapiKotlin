package com.package.socialapp.uiscreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.package.socialapp.repositor.PostRepository
import com.package.socialapp.uiscreens.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val posts = postRepository.getAllPosts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun createPost(
        text: String,
        authorId: String,
        authorEmail: String,
        authorName: String,
        gender: String
    ) {
        val post = Post(
            authorId = authorId,
            authorEmail = authorEmail,
            authorName = authorName,
            gender = gender,
            text = text,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            postRepository.createPost(post)
        }
    }

    fun likePost(post: Post, userId: String) {
        viewModelScope.launch {
            if (post.likes.contains(userId)) {
                postRepository.unlikePost(post.id, userId)
            } else {
                postRepository.likePost(post.id, userId)
            }
        }
    }

    fun deletePost(post: Post, currentUserId: String) {
        if (post.authorId == currentUserId) {
            viewModelScope.launch {
                postRepository.deletePost(post.id)
            }
        }
    }
}
