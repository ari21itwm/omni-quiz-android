package com.omni.quiz.core.network.di

import com.google.firebase.firestore.FirebaseFirestore
import com.omni.quiz.core.network.FirebaseNetworkDataSource
import com.omni.quiz.core.network.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindNetworkDataSource(
        impl: FirebaseNetworkDataSource
    ): NetworkDataSource

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    }
}
