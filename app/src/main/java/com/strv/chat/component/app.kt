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
import com.strv.chat.library.domain.model.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider
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
            notificationChannel("upload", "IImageModel upload", NotificationManager.IMPORTANCE_DEFAULT) {
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
                serviceConfig("upload"),
                memberProvider

            )
        )
    }

    val memberProvider = object : MemberProvider {
        override fun currentUserId(): String = "user-1"

        override fun member(memberId: String): MemberModel {
            if (memberId == "user-1") {
                return user1()
            } else {
                return user2()
            }
        }

    }

    fun user1() = object : MemberModel {
        override val userId: String = "user-1"
        override val userName: String = "John"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"

    }

    fun user2() = object : MemberModel {
        override val userId: String = "user-2"
        override val userName: String = "Camila"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"

    }
}