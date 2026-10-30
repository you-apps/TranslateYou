group = "io.github.you-apps"
version = "1.0"

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.vanniktech.maven.publish")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:3.0.0")
    // retrofit:3.0.0 based on okhttp3:4.12.0
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "translation-engines", version.toString())

    pom {
        name = "Translation engines"
        description = "Kotlin library for translating text with different translation providers."
        inceptionYear = "2026"
        url = "https://github.com/you-apps/TranslateYou"
        licenses {
            license {
                name = "GPL-3.0"
                url = "https://www.gnu.org/licenses/gpl-3.0.txt"
                distribution = "https://www.gnu.org/licenses/gpl-3.0.txt"
            }
        }
        developers {
            developer {
                id = "you-apps"
                name = "You Apps"
                url = "https://github.com/you-apps/"
            }
        }
        scm {
            url = "https://github.com/you-apps/TranslateYou/"
            connection = "scm:git:git://github.com/you-apps/TranslateYou.git"
            developerConnection = "scm:git:ssh://git@github.com/you-apps/TranslateYou.git"
        }
    }
}
