pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // used for OCR
        maven { setUrl("https://jitpack.io") }
    }
}
rootProject.name = "Translate You"
include(":app")
