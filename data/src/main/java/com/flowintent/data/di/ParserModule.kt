package com.flowintent.data.di

import com.flowintent.data.db.parser.GsonJsonParser
import com.flowintent.data.db.parser.JsonParser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ParserModule {

    @Binds
    @Singleton
    internal abstract fun bindJsonParser(gsonJsonParser: GsonJsonParser): JsonParser

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return GsonBuilder()
                .create()
        }
    }
}
