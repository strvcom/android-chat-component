package com.strv.chat.component

import android.app.Application
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.strv.chat.library.cloudStorage.di.cloudStorageMediaClient
import com.strv.chat.library.core.session.ChatComponent
import com.strv.chat.library.core.session.config.Configuration
import com.strv.chat.library.core.ui.extensions.serviceConfig
import com.strv.chat.library.firestore.di.firestoreChatClient
import com.strv.chat.library.firestore.di.firestoreConversationClient

class App: Application() {

    val firestoreDb by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val zoeNotificationChannels
        @RequiresApi(Build.VERSION_CODES.O)
        get() = listOf(
            notificationChannel("upload", "Image upload", NotificationManager.IMPORTANCE_DEFAULT) {
                vibration = true
                lights = true
                showBadge = true
                setSound(null, null)
            }
        )

    override fun onCreate() {
        super.onCreate()

        //register notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).run {
                zoeNotificationChannels.forEach { channel ->
                    createNotificationChannel(channel)
                }
            }
        }

        ChatComponent.init(
            Configuration(
                firestoreChatClient(firestoreDb),
                firestoreConversationClient(firestoreDb),
                cloudStorageMediaClient(firebaseStorage),
                serviceConfig("upload")
            )
        )
    }
}