package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.data.local.*
import com.example.myapplication.data.remote.DogRemoteDataSource
import com.example.myapplication.data.remote.VetRemoteDataSource
import com.example.myapplication.domain.DogRepository
import com.example.myapplication.domain.VetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDogDao(db: AppDatabase): DogDao {
        return db.dogDao
    }

    @Provides
    @Singleton
    fun provideVetDao(db: AppDatabase): VetDao {
        return db.vetDao
    }

    @Provides
    @Singleton
    fun provideDogRepository(
        dogDao: DogDao,
        remoteDataSource: DogRemoteDataSource
    ): DogRepository {
        return DogRepository(dogDao, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideVetRepository(
        vetDao: VetDao,
        remoteDataSource: VetRemoteDataSource
    ): VetRepository {
        return VetRepository(vetDao, remoteDataSource)
    }
}