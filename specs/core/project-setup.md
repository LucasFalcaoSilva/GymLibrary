# Spec: Project Setup

**ID:** CORE-000  
**Layer:** Core / Setup  
**Status:** Approved

## Description

Generate the full Android project structure for GymLibrary from scratch, including Gradle files, package skeleton, base classes, and placeholder files. No feature logic is included — this spec produces a buildable, runnable empty shell.

## Output: File Tree

```
GymLibrary/
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── build.gradle.kts                  (root)
├── settings.gradle.kts
├── gradle.properties
├── local.properties                  (not versioned)
├── .gitignore
└── app/
    ├── build.gradle.kts
    └── src/main/
        ├── AndroidManifest.xml
        └── java/com/miranda/gymlibrary/
            ├── GymLibraryApplication.kt
            ├── core/
            │   ├── di/
            │   │   ├── NetworkModule.kt       (empty module)
            │   │   ├── DataModule.kt          (empty module)
            │   │   ├── DomainModule.kt        (empty module)
            │   │   └── PresentationModule.kt  (empty module)
            │   ├── network/
            │   │   └── AuthInterceptor.kt     (stub)
            │   ├── ui/
            │   │   └── theme/
            │   │       ├── Color.kt
            │   │       ├── Theme.kt
            │   │       └── Type.kt
            │   └── util/
            │       └── UiState.kt
            ├── data/
            │   ├── remote/
            │   │   ├── api/                   (empty)
            │   │   ├── dto/                   (empty)
            │   │   └── datasource/            (empty)
            │   ├── mapper/                    (empty)
            │   └── repository/               (empty)
            ├── domain/
            │   ├── model/                    (empty)
            │   ├── repository/               (empty)
            │   └── usecase/                  (empty)
            └── presentation/
                ├── navigation/
                │   └── Routes.kt             (stub)
                ├── home/                     (empty)
                ├── exerciselist/             (empty)
                └── exercisedetail/           (empty)
```

---

## gradle/libs.versions.toml

```toml
[versions]
agp = "8.5.0"
kotlin = "2.0.0"
compose-bom = "2024.06.00"
koin = "3.5.6"
retrofit = "2.11.0"
okhttp = "4.12.0"
coil = "2.6.0"
coroutines = "1.8.1"
kotlinx-serialization = "1.7.0"
navigation-compose = "2.7.7"
lifecycle = "2.8.2"

[libraries]
# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "compose-bom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigation-compose" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycle" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version = "1.9.0" }

# Network
retrofit-core = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-kotlinx-serialization = { group = "com.jakewharton.retrofit", name = "retrofit2-kotlinx-serialization-converter", version = "1.0.0" }
okhttp-core = { group = "com.squareup.okhttp3", name = "okhttp", version.ref = "okhttp" }
okhttp-logging = { group = "com.squareup.okhttp3", name = "logging-interceptor", version.ref = "okhttp" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinx-serialization" }

# DI
koin-android = { group = "io.insert-koin", name = "koin-android", version.ref = "koin" }
koin-compose = { group = "io.insert-koin", name = "koin-androidx-compose", version.ref = "koin" }

# Image
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }
coil-gif = { group = "io.coil-kt", name = "coil-gif", version.ref = "coil" }

# Async
coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }

# Test
junit = { group = "junit", name = "junit", version = "4.13.2" }
mockk = { group = "io.mockk", name = "mockk", version = "1.13.11" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

---

## gradle/wrapper/gradle-wrapper.properties

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.7-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

> **Note — JDK:** If the system Java is not JDK 17+, the build will fail. Use the JDK bundled with Android Studio: `File → Project Structure → SDK Location → JDK Location`. If needed, set `org.gradle.java.home` in the **user-level** `~/.gradle/gradle.properties` (never in the project's `gradle.properties` — that file is versioned and the path is machine-specific, which breaks other developers and CI).
> ```properties
> # ~/.gradle/gradle.properties  (user-level, never committed)
> org.gradle.java.home=C\:\\path\\to\\android-studio\\jbr
> ```

---

## settings.gradle.kts

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GymLibrary"
include(":app")
```

---

## build.gradle.kts (root)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

---

## app/build.gradle.kts

```kotlin
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

android {
    namespace = "com.miranda.gymlibrary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.miranda.gymlibrary"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        buildConfigField(
            "String",
            "RAPIDAPI_KEY",
            "\"${localProperties.getProperty("RAPIDAPI_KEY") ?: ""}\""
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Network
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.kotlinx.serialization.json)
    debugImplementation(libs.okhttp.logging)

    // DI
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Image
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    // Async
    implementation(libs.coroutines.android)

    // Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
}
```

---

## AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".GymLibraryApplication"
        android:label="GymLibrary"
        android:theme="@style/Theme.GymLibrary"
        android:supportsRtl="true">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.GymLibrary">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
```

---

## GymLibraryApplication.kt

```kotlin
package com.miranda.gymlibrary

import android.app.Application
import com.miranda.gymlibrary.core.di.dataModule
import com.miranda.gymlibrary.core.di.domainModule
import com.miranda.gymlibrary.core.di.networkModule
import com.miranda.gymlibrary.core.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class GymLibraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@GymLibraryApplication)
            modules(
                networkModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
```

---

## core/util/UiState.kt

```kotlin
package com.miranda.gymlibrary.core.util

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

---

## core/ui/theme/Color.kt

```kotlin
package com.miranda.gymlibrary.core.ui.theme

import androidx.compose.ui.graphics.Color

val Red700 = Color(0xFFE53935)
val Background = Color(0xFF121212)
val Surface = Color(0xFF1E1E1E)
val OnBackground = Color(0xFFFFFFFF)
val OnSurface = Color(0xFFB0B0B0)
```

---

## core/ui/theme/Type.kt

```kotlin
package com.miranda.gymlibrary.core.ui.theme

import androidx.compose.material3.Typography

// Named AppTypography to avoid shadowing the Typography class in the same package
val AppTypography = Typography()
```

---

## core/ui/theme/Theme.kt

```kotlin
package com.miranda.gymlibrary.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Red700,
    onPrimary = OnBackground,
    background = Background,
    surface = Surface,
    onBackground = OnBackground,
    onSurface = OnSurface
)

@Composable
fun GymLibraryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
```

---

## core/network/AuthInterceptor.kt

```kotlin
package com.miranda.gymlibrary.core.network

import com.miranda.gymlibrary.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", BuildConfig.RAPIDAPI_KEY)
            .addHeader("X-RapidAPI-Host", HOST)
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val BASE_URL = "https://exercisedb.p.rapidapi.com/"
        const val HOST = "exercisedb.p.rapidapi.com"
    }
}
```

---

## core/di/NetworkModule.kt

```kotlin
package com.miranda.gymlibrary.core.di

import org.koin.dsl.module

val networkModule = module {
    // TODO: inject ExerciseDbService after CORE-001 is implemented
}
```

---

## core/di/DataModule.kt

```kotlin
package com.miranda.gymlibrary.core.di

import org.koin.dsl.module

val dataModule = module {
    // TODO: inject RemoteDataSource and Repository after data layer is implemented
}
```

---

## core/di/DomainModule.kt

```kotlin
package com.miranda.gymlibrary.core.di

import org.koin.dsl.module

val domainModule = module {
    // TODO: inject UseCases after domain layer is implemented
}
```

---

## core/di/PresentationModule.kt

```kotlin
package com.miranda.gymlibrary.core.di

import org.koin.dsl.module

val presentationModule = module {
    // TODO: inject ViewModels after presentation layer is implemented
}
```

---

## presentation/navigation/Routes.kt

```kotlin
package com.miranda.gymlibrary.presentation.navigation

import android.net.Uri

object HomeRoute {
    const val route = "home"
}

object ExerciseListRoute {
    const val route = "exercise-list/{bodyPart}"
    fun createRoute(bodyPart: String) = "exercise-list/${Uri.encode(bodyPart)}"
}

object ExerciseDetailRoute {
    const val route = "exercise-detail/{exerciseId}"
    fun createRoute(exerciseId: String) = "exercise-detail/${Uri.encode(exerciseId)}"
}
```

---

## .gitignore

```
# Android Studio
.idea/
*.iml
.DS_Store

# Gradle
.gradle/
build/
**/build/

# Local config — NEVER version
local.properties

# Keystore — NEVER version
*.jks
*.keystore

# Generated
.kotlin/
**/generated/

# Output artifacts
*.apk
*.aab
*.aar
```

---

## local.properties (not versioned — template only)

```
sdk.dir=/path/to/your/android/sdk
RAPIDAPI_KEY=your_key_here
```

---

## Acceptance Criteria

- [ ] Project syncs with Gradle without errors
- [ ] App builds and installs on emulator/device (API 26+)
- [ ] App launches showing a blank dark screen
- [ ] `GymLibraryApplication` starts Koin without crash
- [ ] `BuildConfig.RAPIDAPI_KEY` is populated from `local.properties`
- [ ] No hardcoded API key in any source file

## Business Rules

- BR-01: `local.properties` must be in `.gitignore` before first commit
- BR-02: All empty packages must contain at least a `.gitkeep` or a stub file to be tracked by Git
- BR-03: The app must compile on minSdk 26
- BR-04: Before the first commit, run `git rm -r --cached .idea/` to remove Android Studio files from tracking — they are listed in `.gitignore` but may have been added automatically
- BR-05: Add `MainActivity.kt` (stub with `setContent { GymLibraryTheme {} }`) and `res/values/themes.xml` (defines `Theme.GymLibrary`) — both required for the Manifest references to resolve
- BR-06: Gradle wrapper must be generated via `gradle wrapper --gradle-version 8.7` before the first build. `gradlew`, `gradlew.bat`, `gradle-wrapper.jar` and `gradle-wrapper.properties` must all be committed to version control
- BR-07: Build requires JDK 17+. If the system Java is lower, configure `org.gradle.java.home` in the **user-level** `~/.gradle/gradle.properties` — never in the project's `gradle.properties` (versioned, machine-specific path breaks other developers and CI)
- BR-08: `okhttp-logging` must be declared as `debugImplementation` — never `implementation`. Logging interceptor in release builds exposes request/response bodies including API keys in logcat
- BR-09: Read `local.properties` values using `getProperty("KEY")` instead of `["KEY"]` — the `[]` operator returns `Any?` (inherited from `Hashtable`), while `getProperty()` returns `String?` as expected
- BR-10: Typography value in `core/ui/theme` must be named `AppTypography`, not `Typography` — the latter shadows the `androidx.compose.material3.Typography` class within the same package, causing compilation errors on any custom typography usage
- BR-11: `startKoin` must include `androidLogger(Level.ERROR)` to surface Koin dependency resolution failures in logcat
- BR-12: `local.properties` file stream must be closed after reading — use `file.inputStream().use { load(it) }` instead of `load(file.inputStream())`. The Gradle daemon is long-lived; unclosed `FileInputStream` instances accumulate across builds and may exhaust the process file descriptor limit (typically 1024 on Linux CI)
- BR-13: `kotlinOptions { jvmTarget }` is deprecated in AGP 8.x and will be removed in AGP 9.x — use `kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_17) } }` instead

## Out of Scope

- MainActivity UI (blank screen is sufficient)
- Any feature implementation
- Unit tests (belong in feature specs)
