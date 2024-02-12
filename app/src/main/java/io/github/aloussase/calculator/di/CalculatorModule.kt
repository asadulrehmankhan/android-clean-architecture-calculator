package io.github.aloussase.calculator.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.aloussase.calculator.api.MathApi
import io.github.aloussase.calculator.database.HistoryDatabase
import io.github.aloussase.calculator.repository.CalculatorRepository
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalculatorModule {

    @Provides
    @Singleton
    fun provideHistoryDatabase(
        application: Application
    ): HistoryDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            HistoryDatabase::class.java,
            "calculator.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMathApi(): MathApi {
        return Retrofit.Builder()
            .baseUrl("https://api.mathjs.org/v4/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(MathApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCalculatorRepository(
        mathApi: MathApi,
        historyDatabase: HistoryDatabase
    ): CalculatorRepository {
        return CalculatorRepository(mathApi, historyDatabase)
    }

}