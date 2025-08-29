package com.flowintent.workspace.di

import com.flowintent.workspace.data.parser.GsonJsonParser
import com.flowintent.workspace.data.parser.JsonParser
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParserModule {

    @Binds
    @Singleton
    abstract fun bindJsonParser(gsonJsonParser: GsonJsonParser): JsonParser

    companion object {
        @Provides
        @Singleton
        fun provideGson(): Gson {
            return Gson()
        }
    }
}
