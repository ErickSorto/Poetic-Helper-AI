package com.ballisticapps.poetichelper.di

import com.ballisticapps.poetichelper.feature_poetic_helper.data.remote.OpenAITextApi
import com.ballisticapps.poetichelper.feature_poetic_helper.data.repository.OpenAIRepositoryImpl
import com.ballisticapps.poetichelper.feature_poetic_helper.domain.repository.OpenAIRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenAIModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(45, TimeUnit.SECONDS)
        .writeTimeout(45, TimeUnit.SECONDS).addInterceptor(httpLoggingInterceptor).build()


    @Provides
    @Singleton
    fun provideOpenAITextApi(
        okHttpClient: OkHttpClient
    ): OpenAITextApi {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/chat/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(OpenAITextApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenAIRepository(
        api: OpenAITextApi
    ): OpenAIRepository {
        return OpenAIRepositoryImpl(api)
    }
}