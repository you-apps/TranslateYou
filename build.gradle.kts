buildscript {
    val compose_version by extra("1.11.4")
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "9.2.1" apply false
    id("com.android.library") version "9.2.1" apply false
    id("org.jetbrains.kotlin.android") version "2.4.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.4.0"
    id("com.google.devtools.ksp") version "2.3.9" apply false
    id("org.jetbrains.kotlin.jvm") version "2.4.0" apply false
}
