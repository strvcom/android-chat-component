package com.strv.chat.component

import android.app.Application
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.annotation.RequiresApi
import com.strv.chat.component.di.applicationModules
import com.strv.chat.core.core.session.ChatComponent
import com.strv.chat.core.core.session.config.Configuration
import com.strv.chat.core.core.session.config.di.serviceConfig
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MediaClient
import com.strv.chat.core.domain.client.MemberClient
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

private const val CHANNEL_ID = "upload"
private const val CHANNEL_DESCRIPTION = "ImageModel upload"
private const val CURRENT_USER_ID = "user-1"

class App : Application() {

    private val chatClient: ChatClient by inject()
    private val conversationClient: ConversationClient by inject()
    private val memberClient: MemberClient by inject {
        parametersOf(CURRENT_USER_ID)
    }
    private val mediaClient: MediaClient by inject()

    private val appChannels
        @RequiresApi(Build.VERSION_CODES.O)
        get() = listOf(
            notificationChannel(
                CHANNEL_ID,
                CHANNEL_DESCRIPTION,
                NotificationManager.IMPORTANCE_DEFAULT
            ) {
                vibration = false
                lights = true
                showBadge = true
                setSound(null, null)
            }
        )

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(applicationModules)
        }

        //register notification channels
        (getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager).run {
            appChannels.forEach { channel ->
                createNotificationChannel(channel)
            }
        }

        ChatComponent.init(
            this,
            Configuration(
                chatClient,
                conversationClient,
                memberClient,
                mediaClient,
                serviceConfig(CHANNEL_ID)
            )
        )
    }
}