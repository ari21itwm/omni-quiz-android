package com.omni.quiz.core.network.di

import com.omni.quiz.core.network.MockNetworkDataSource
import com.omni.quiz.core.network.NetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun bindNetworkDataSource(
        impl: MockNetworkDataSource
    ): NetworkDataSource

    // Real FirebaseFirestore provider removed to avoid initialization crash without google-services.json
}
