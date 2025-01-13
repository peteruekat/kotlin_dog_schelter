package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.data.local.DogDatabase
import com.example.myapplication.data.local.DogDao
import com.example.myapplication.domain.DogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.myapplication.data.remote.DogRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDogDatabase(app: Application): DogDatabase {
        return Room.databaseBuilder(
            app,
            DogDatabase::class.java,
            DogDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDogDao(db: DogDatabase): DogDao {
        return db.dogDao
    }

    @Provides
    @Singleton
    fun provideDogRepository(
        dogDao: DogDao,
        remoteDataSource: DogRemoteDataSource
    ): DogRepository {
        return DogRepository(dogDao, remoteDataSource)
    }
}