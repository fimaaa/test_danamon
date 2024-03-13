// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.nio.file.Paths

buildscript {
    val appName by extra(Release.APP_NAME)


    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
//        classpath(libs.agp)
        classpath(libs.gradle)
        classpath(libs.gradlePlugin)
        classpath(libs.navigation)
        classpath(libs.daggerHilt.gradle)
//        classpath(libs.googleService)

        // Add the Crashlytics Gradle plugin
//        classpath(libs.firebaseCrashlytics.gradle)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

val ndkVersion by extra("26.1.10909125")

tasks.register<Copy>("installGitHook") {
    println("TAG TASK INSTALLGITHOOK")
    from(Paths.get(rootProject.rootDir.toString(), "pre-commit"))
    into(Paths.get(rootProject.rootDir.toString(), ".git", "hooks"))
    doLast {
        val hooksDir = File(rootProject.rootDir, ".git/hooks")
        hooksDir.listFiles()?.filter { it.isFile && it.name == "pre-commit" }?.forEach {
            val content = it.readText()
            val modifiedContent = content.replace("\r\n", "\n") // Ensure consistent line endings
            it.writeText(modifiedContent)
        }
        hooksDir.setExecutable(true)
    }
}

tasks {
    register("clean", Delete::class) {
        println("TAG CLEAN ${rootProject.buildDir}")
        delete(rootProject.buildDir)
    }
}

tasks.getByPath(":app:preBuild").dependsOn(tasks.getByName("installGitHook"))
