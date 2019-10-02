package com.strv.chat.component

import android.app.Application
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.component.di.CompositionRoot
import com.strv.chat.library.core.session.ChatComponent
import com.strv.chat.library.core.session.config.Configuration
import com.strv.chat.library.core.ui.extensions.serviceConfig

private const val CHANNEL_ID = "upload"
private const val CHANNEL_DESCRIPTION = "ImageModel upload"

class App : Application() {

    private lateinit var compositionRoot: CompositionRoot

    val firestoreDb by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val zoeNotificationChannels
        @RequiresApi(Build.VERSION_CODES.O)
        get() = listOf(
            notificationChannel(
                CHANNEL_ID,
                CHANNEL_DESCRIPTION,
                NotificationManager.IMPORTANCE_DEFAULT
            ) {
                vibration = true
                lights = true
                showBadge = true
                setSound(null, null)
            }
        )

    override fun onCreate() {
        super.onCreate()

        compositionRoot = CompositionRoot()

        //register notification channels
        (getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).run {
            zoeNotificationChannels.forEach { channel ->
                createNotificationChannel(channel)
            }
        }

        ChatComponent.init(
            this,
            Configuration(
                compositionRoot.chatClient(firestoreDb),
                compositionRoot.conversationClient(firestoreDb),
                compositionRoot.mediaClient(firebaseStorage),
                serviceConfig(CHANNEL_ID),
                compositionRoot.memberProvider()
            )
        )
    }

    fun compositionRoot() =
        compositionRoot
}