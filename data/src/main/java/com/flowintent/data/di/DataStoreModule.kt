package com.flowintent.data.di

import com.flowintent.core.db.source.AssetDataSource
import com.flowintent.core.db.source.LocalTaskDataProvider
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
    internal abstract fun bindAssetDataSource(assetDataSourceImpl: AssetDataSourceImpl): AssetDataSource

    @Binds
    @Singleton
    internal abstract fun bindLocalTaskDataProvider(
        localTaskDataProviderImpl: LocalTaskDataProviderImpl
    ): LocalTaskDataProvider
}
