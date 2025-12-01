import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)

    kotlin("plugin.serialization") version "2.0.0"
//    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        val voyagerVersion = "1.1.0-beta02"
        val koinVersion = "4.0.0"
        val kamelVersion = "0.9.5"
        val ktorVersion = "3.0.0"

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.insert-koin:koin-android:$koinVersion") // ✅ Koin Android
            implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

            implementation(libs.androidx.room.sqlite.wrapper)
        }

        commonMain.dependencies {
            // ✅ Core Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // ✅ Lifecycle helpers (AndroidX)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.material.icons.extended)

            // ✅ Voyager Navigation
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-kodein:$voyagerVersion")

            // ✅ Koin Multiplatform (for DI)
            implementation("io.insert-koin:koin-core:$koinVersion")
            implementation("io.insert-koin:koin-compose:$koinVersion") // Koin + Compose support

            // ✅ Kamel (cross-platform image loader)
//            implementation("media.kamel:kamel-image:$kamelVersion")
            implementation("media.kamel:kamel-image-default:1.0.8")
            implementation("io.ktor:ktor-client-core:$ktorVersion")

            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.9.1")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

            // ✅ Coroutines (for shared ViewModel/repo logic)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

            implementation("io.insert-koin:koin-compose-viewmodel:$koinVersion")

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:$ktorVersion")
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("io.ktor:ktor-client-cio:$ktorVersion")
        }
    }
}

android {
    namespace = "org.tiffinservice.app"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.tiffinservice.app"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core-ktx:1.15.0")
        force("androidx.core:core:1.15.0")
    }
}

dependencies {
    debugImplementation(compose.uiTooling)

    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
//    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
//    add("kspDesktop", libs.androidx.room.compiler)

}

room {
    // Folder where Room will generate schema JSON files
    schemaDirectory("$projectDir/schemas")
}

compose.desktop {
    application {
        mainClass = "org.tiffinservice.app.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.tiffinservice.app"
            packageVersion = "1.0.0"
        }
    }
}
