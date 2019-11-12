plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "com.strv.chat.component"
        minSdkVersion(Versions.targetSdk)
        targetSdkVersion(Versions.targetSdk)

        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":firestore"))
    implementation(project(":storage"))

    implementation(kotlin("stdlib", Versions.Gradle.kotlin))
    implementation(Dependencies.Androidx.appcompat)
    implementation(Dependencies.Androidx.recyclerView)
    implementation(Dependencies.Androidx.constraintLayout)
    implementation(Dependencies.Androidx.viewModel)
    implementation(Dependencies.Androidx.liveData)
    implementation(Dependencies.Androidx.lifecycleRuntime)
    kapt(Dependencies.Androidx.lifecycleCompiler)

    implementation(Dependencies.Koin.library)
    implementation(Dependencies.Koin.viewmodel)

    implementation(Dependencies.Glide.library)
    kapt(Dependencies.Glide.compiler)
}
