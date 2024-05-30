package dev.veryniche.quickqr.storage.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.veryniche.quickqr.core.network.AppDispatchers
import dev.veryniche.quickqr.core.network.Dispatcher
import dev.veryniche.quickqr.storage.QRCodesDataSource.Companion.PROTO_FILE_NAME
import dev.veryniche.quickqr.storage.QRCodesSerializer
import dev.veryniche.quickqr.storage.models.Qrcodes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesQRCodesStore(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatchers.IO) ioDispatcher: CoroutineDispatcher,
        qRCodesSerializer: QRCodesSerializer
    ): DataStore<Qrcodes> =
        DataStoreFactory.create(
            serializer = qRCodesSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            migrations = listOf()
        ) {
            context.dataStoreFile(PROTO_FILE_NAME)
        }
}
