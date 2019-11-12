import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
    id("com.github.dcendents.android-maven")
}

group = groupId
version = Versions.versionName

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
    }
}

dependencies {
    api(project(":core"))
    api(Dependencies.Firebase.storage)
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/docs"

        configuration {
            perPackageOption {
                prefix = "com.strv.chat.storage.di"
                suppress = false
            }

            perPackageOption {
                prefix = "com.strv.chat.storage"
                suppress = true
            }
        }
    }
}
