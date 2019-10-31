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
    api(kotlin("stdlib", Versions.Gradle.kotlin))

    implementation(Dependencies.Androidx.appcompat)
    implementation(Dependencies.Androidx.recyclerView)
    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.Androidx.ktx)
}

tasks {
    val dokka by getting(DokkaTask::class) {
        outputFormat = "html"
        outputDirectory = "$buildDir/docs"

        configuration {
            perPackageOption {
                prefix = "com.strv.chat.core.core.ui.chat.data.creator"
                suppress = true
            }
            perPackageOption {
                prefix = "com.strv.chat.core.core.ui.conversation.data.creator"
                suppress = true
            }
            perPackageOption {
                prefix = "com.strv.chat.core.core.ui.data.creator"
                suppress = true
            }
            perPackageOption {
                prefix = "strv.ktools"
                suppress = true
            }
        }
    }
}