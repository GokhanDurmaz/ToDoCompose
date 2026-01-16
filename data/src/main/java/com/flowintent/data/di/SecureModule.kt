package com.flowintent.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.flowintent.core.db.security.ISecurePrefsRepository
import com.flowintent.data.secure.CryptoProvider
import com.flowintent.data.secure.EncryptedSecurePrefsSerializer
import com.flowintent.data.secure.SecurePrefs
import com.flowintent.data.secure.SecurePrefsRepositoryImpl
import com.google.crypto.tink.Aead
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SecureModule {

    @Provides
    @Singleton
    fun provideAead(@ApplicationContext context: Context): Aead =
        CryptoProvider.aead(context)

    @Provides
    @Singleton
    internal fun provideSecurePrefsRepo(@ApplicationContext context: Context): SecurePrefsRepositoryImpl =
        SecurePrefsRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideSecurePrefsDataStore(
        @ApplicationContext context: Context
    ): DataStore<SecurePrefs> {
        return DataStoreFactory.create(
            serializer = EncryptedSecurePrefsSerializer(context),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.dataStoreFile("secure_prefs.pb") }
        )
    }
}
