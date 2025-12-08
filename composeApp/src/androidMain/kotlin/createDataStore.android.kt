import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

// shared/src/androidMain/kotlin/createDataStore.android.kt

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)