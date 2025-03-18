package com.package.socialapp.hilt

import com.package.socialapp.repository.AuthRepository
import com.package.socialapp.repository.AuthRepositoryImpl
import com.package.socialapp.repositor.PostRepository
import com.package.socialapp.repository.PostRepositoryImpl
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        googleSignInClient: GoogleSignInClient
    ): AuthRepository = AuthRepositoryImpl(firebaseAuth, googleSignInClient)

    @Provides
    @Singleton
    fun providePostRepository(firestore: FirebaseFirestore): PostRepository = PostRepositoryImpl(firestore)
}
