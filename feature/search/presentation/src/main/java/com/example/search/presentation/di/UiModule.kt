package com.example.search.presentation.di

import com.example.search.presentation.navigation.SearchFeatureApi
import com.example.search.presentation.navigation.SearchFeatureApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UiModule {
    @Provides
    fun provideSearchFeatureApi(): SearchFeatureApi{
        return SearchFeatureApiImpl()
    }

}