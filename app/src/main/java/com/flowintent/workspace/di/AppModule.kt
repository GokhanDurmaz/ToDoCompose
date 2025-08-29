package com.flowintent.workspace.di

import com.flowintent.core.db.source.AssetDataSource
import com.flowintent.workspace.data.local.AssetDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAssetDataSource(assetDataSourceImpl: AssetDataSourceImpl): AssetDataSource
}
