buildscript {
    val compose_version by extra("1.8.3")
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
}
