plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

    implementation(Dependencies.Glide.library)
    kapt(Dependencies.Glide.compiler)
}