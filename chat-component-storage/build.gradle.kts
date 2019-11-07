import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    kotlin("android")
    id("org.jetbrains.dokka")
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":chat-component-core"))

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
