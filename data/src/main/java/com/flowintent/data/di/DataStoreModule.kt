package com.flowintent.data.di

import com.flowintent.core.db.source.IAssetDataSource
import com.flowintent.core.db.source.ILocalTaskDataProvider
import com.flowintent.data.db.AssetDataSourceImpl
import com.flowintent.data.db.LocalTaskDataProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    @Binds
    @Singleton
    abstract fun bindAssetDataSource(assetDataSourceImpl: AssetDataSourceImpl): IAssetDataSource

    @Binds
    @Singleton
    abstract fun bindLocalTaskDataProvider(
        localTaskDataProviderImpl: LocalTaskDataProviderImpl
    ): ILocalTaskDataProvider
}
