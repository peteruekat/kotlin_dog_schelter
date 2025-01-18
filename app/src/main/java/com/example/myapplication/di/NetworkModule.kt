package com.example.myapplication.di

import com.example.myapplication.data.remote.DogApi
import com.example.myapplication.data.remote.DogRemoteDataSource
import com.example.myapplication.data.remote.VetApi
import com.example.myapplication.data.remote.VetRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("dogApi")
    fun provideDogRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DogApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("vetApi")
    fun provideVetRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(VetApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDogApi(@Named("dogApi") retrofit: Retrofit): DogApi {
        return retrofit.create(DogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVetApi(@Named("vetApi") retrofit: Retrofit): VetApi {
        return retrofit.create(VetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogRemoteDataSource(api: DogApi): DogRemoteDataSource {
        return DogRemoteDataSource(api)
    }

    @Provides
    @Singleton
    fun provideVetRemoteDataSource(api: VetApi): VetRemoteDataSource {
        return VetRemoteDataSource(api)
    }
}