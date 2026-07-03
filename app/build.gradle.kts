import com.android.build.api.dsl.Packaging
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlinx-serialization")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 37

    defaultConfig {
        applicationId = "com.bnyro.translate"
        minSdk = 23
        targetSdk = 36
        versionCode = 56
        versionName = "19.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    fun Packaging.() {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.bnyro.translate"
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    val compose_version: String by rootProject.extra
    // Android Core
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.11.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.exifinterface:exifinterface:1.4.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Compose
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")
    implementation("androidx.navigation:navigation-compose:2.9.8")
    implementation("com.materialkolor:material-kolor:4.1.1")

    // Retrofit & API
    implementation(project(":translation-engines"))
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    // retrofit:3.0.0 based on okhttp3:4.12.0
    implementation("com.squareup.okhttp3:logging-interceptor:5.4.0")

    // Room database
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    // Tesseract OCR
    implementation("cz.adaptech.tesseract4android:tesseract4android-openmp:4.9.0")

    // Dynamic color scheme
    implementation("com.google.android.material:material:1.14.0")
}
