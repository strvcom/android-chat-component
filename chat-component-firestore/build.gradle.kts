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
}

dependencies {
    implementation(project(":chat-component-core"))

    api(Dependencies.Firebase.firestore)

    testImplementation(Dependencies.Test.junit)
    testImplementation(Dependencies.Test.mockk)
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/docs"

        configuration {
            perPackageOption {
                prefix = "com.strv.chat.firestore.di"
                suppress = false
            }

            perPackageOption {
                prefix = "com.strv.chat.firestore"
                suppress = true
            }
        }
    }
}