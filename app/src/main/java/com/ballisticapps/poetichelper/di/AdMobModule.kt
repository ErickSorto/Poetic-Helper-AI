package com.ballisticapps.poetichelper.di

import android.content.Context
import com.ballisticapps.poetichelper.reward_ads_feature.data.repository.AdManagerRepositoryImpl
import com.ballisticapps.poetichelper.reward_ads_feature.domain.repository.AdManagerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdMobModule {

    @Singleton
    @Provides
    fun provideAdManagerRepository(@ApplicationContext context: Context): AdManagerRepository {
        return AdManagerRepositoryImpl(context, "ca-app-pub-8710979310678386/2619801589")
    }
}