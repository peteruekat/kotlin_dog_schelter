package com.example.myapplication.di

import com.example.myapplication.data.remote.DogApi
import com.example.myapplication.data.remote.DogRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // oznacza, że zależności żyją tak długo jak aplikacja
object NetworkModule {

    // Tworzy klienta HTTP z logowaniem
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY  // będzie logował szczegóły zapytań
            })
            .build()
    }

    // Tworzy instancję Retrofita
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DogApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())  // konwertuje JSON na obiekty Kotlina
            .build()
    }

    // Tworzy implementację naszego API
    @Provides
    @Singleton
    fun provideDogApi(retrofit: Retrofit): DogApi {
        return retrofit.create(DogApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDogRemoteDataSource(api: DogApi): DogRemoteDataSource {
        return DogRemoteDataSource(api)
    }
}