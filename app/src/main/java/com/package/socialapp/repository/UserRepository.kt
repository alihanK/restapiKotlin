package com.package.socialapp.repository

import com.package.socialapp.uiscreens.model.UserSettings
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    suspend fun updateUserSettings(userId: String, settings: UserSettings)
    suspend fun getUserSettings(userId: String): UserSettings?
}

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun updateUserSettings(userId: String, settings: UserSettings) {
        firestore.collection("users")
            .document(userId)
            .set(settings)
            .await()
    }

    override suspend fun getUserSettings(userId: String): UserSettings? {
        return firestore.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(UserSettings::class.java)
    }
}
