const val groupId = "com.strv.android.chat.component"

object Versions {

    const val targetSdk = 29
    const val minSdk = 21

    const val versionMajor = 1 // max two digits
    const val versionMinor = 0 // max two digits
    const val versionPatch = 0 // max two digits
    const val versionBuild = 0 // max three digits

    const val versionCode =
        versionMajor * 10000000 + versionMinor * 100000 + versionPatch * 1000 + versionBuild
    const val versionName = "$versionMajor.$versionMinor.$versionPatch"

    const val dokka = "0.10.0"

    object Gradle {
        const val kotlin = "1.3.50"
        const val gradle = "3.5.0"
        const val googleServices = "4.3.2"
    }

    object Androidx {
        const val appcompat = "1.1.0"
        const val recyclerView = "1.0.0"
        const val constraintLayout = "1.1.3"
        const val ktx = "1.1.0"
        const val lifecycle = "2.1.0"
    }

    object Glide {
        const val library = "4.9.0"
    }

    object Firebase {
        const val database = "19.1.0"
        const val firestore = "21.1.1"
        const val storage = "19.1.0"
    }

    object Test {
        const val junit = "4.12"
        const val mockk = "1.9.3"
    }

    const val koin = "2.0.1"

    const val maven = "2.1"
}

object Dependencies {

    const val Dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.dokka}"

    object Gradle {
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Gradle.kotlin}"
        const val gradle = "com.android.tools.build:gradle:${Versions.Gradle.gradle}"
        const val googleServices =
            "com.google.gms:google-services:${Versions.Gradle.googleServices}"
    }

    object Androidx {
        const val appcompat = "androidx.appcompat:appcompat:${Versions.Androidx.appcompat}"
        const val recyclerView =
            "androidx.recyclerview:recyclerview:${Versions.Androidx.recyclerView}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Versions.Androidx.constraintLayout}"
        const val ktx = "androidx.core:core-ktx:${Versions.Androidx.ktx}"
        const val viewModel =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Androidx.lifecycle}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata:${Versions.Androidx.lifecycle}"
        const val lifecycleRuntime =
            "androidx.lifecycle:lifecycle-runtime:${Versions.Androidx.lifecycle}"
        const val lifecycleCompiler =
            "androidx.lifecycle:lifecycle-compiler:${Versions.Androidx.lifecycle}"
    }

    object Glide {
        const val library = "com.github.bumptech.glide:glide:${Versions.Glide.library}"
        const val compiler = "com.github.bumptech.glide:compiler:${Versions.Glide.library}"
    }

    object Firebase {
        const val database = "com.google.firebase:firebase-database:${Versions.Firebase.database}"
        const val firestore =
            "com.google.firebase:firebase-firestore:${Versions.Firebase.firestore}"
        const val storage = "com.google.firebase:firebase-storage:${Versions.Firebase.storage}"
    }

    object Koin {
        const val library = "org.koin:koin-android:${Versions.koin}"
        const val viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val mockk = "io.mockk:mockk:${Versions.Test.mockk}"
    }

    const val maven = "com.github.dcendents:android-maven-gradle-plugin:${Versions.maven}"
}