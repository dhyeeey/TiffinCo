package org.tiffinservice.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.tiffinservice.app.di.appModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // ✅ Prevent "Koin already started" crash
        if (GlobalContext.getOrNull() == null) {
//            initKoin()
            startKoin {
                androidContext(applicationContext)
                modules(
                    listOf(androidModule,appModule)
                )
            }
        }

//        createDataStore(applicationContext)

        setContent {
            App()
        }
    }
}
