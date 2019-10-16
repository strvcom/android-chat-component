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
    api(kotlin("stdlib", Versions.Gradle.kotlin))

    implementation(Dependencies.Androidx.appcompat)
    implementation(Dependencies.Androidx.recyclerView)
    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.Androidx.ktx)
}