package com.package.socialapp.repository

import com.package.socialapp.repositor.PostRepository
import com.package.socialapp.uiscreens.model.Post
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PostRepository {

    private val postsCollection = firestore.collection("posts")

    override fun getAllPosts(): Flow<List<Post>> = callbackFlow {
        val subscription = postsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val posts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(posts)
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun createPost(post: Post) {
        postsCollection.add(post).await()
    }

    override suspend fun likePost(postId: String, userId: String) {
        postsCollection.document(postId)
            .update("likes", FieldValue.arrayUnion(userId))
            .await()
    }

    override suspend fun unlikePost(postId: String, userId: String) {
        postsCollection.document(postId)
            .update("likes", FieldValue.arrayRemove(userId))
            .await()
    }

    override suspend fun deletePost(postId: String) {
        postsCollection.document(postId).delete().await()
    }
}
