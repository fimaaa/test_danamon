pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "Test-Danamon"
include ("app")
include ("library:common")
include ("navigation")
include ("model:common")
include ("network")
include ("repository")
include ("local")
include ("feature:auth")
include ("feature:splashscreen")
include ("library:unittest")
include ("feature:main")
include ("model:jsonplaceholder")
include ("feature:jsonplaceholder")
include ("feature:admin")

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.3"
////                            # available:"0.60.4"
////                            # available:"0.60.5"
}