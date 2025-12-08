import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

// shared/src/commonMain/kotlin/createDataStore.kt

/**
 *   Gets the singleton DataStore instance, creating it if necessary.
 */

//internal const val DATASTORE_FILE_NAME = "user_prefs.preferences_pb"
internal const val dataStoreFileName = "user_prefs.preferences_pb"

fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

