plugins {
    id("com.android.library")
    kotlin("android")
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
}