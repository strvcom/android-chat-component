package com.strv.chat.component

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun notificationChannel(id: String, name: String, importance: Int, init: NotificationChannel.() -> Unit) =
    NotificationChannel(id, name, importance).apply(init)

inline var NotificationChannel.vibration: Boolean
    get() = throw UnsupportedOperationException("")
    @RequiresApi(Build.VERSION_CODES.O)
    set(value) {
        enableVibration(value)
    }

inline var NotificationChannel.lights: Boolean
    get() = throw UnsupportedOperationException("")
    @RequiresApi(Build.VERSION_CODES.O)
    set(value) {
        enableLights(value)
    }

inline var NotificationChannel.showBadge: Boolean
    get() = throw UnsupportedOperationException("")
    @RequiresApi(Build.VERSION_CODES.O)
    set(value) {
        setShowBadge(value)
    }