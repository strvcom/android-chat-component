package com.strv.chat.component

import android.app.NotificationChannel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.strv.chat.core.domain.task.Task
import strv.ktools.logE

@RequiresApi(Build.VERSION_CODES.O)
fun notificationChannel(
    id: String,
    name: String,
    importance: Int,
    init: NotificationChannel.() -> Unit
) =
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

fun <R> Task<R, Throwable>.toLiveData(): LiveData<R> =
    MutableLiveData<R>().apply {
        onSuccess { result ->
            value = result
        }.onError { error ->
            logE(error.localizedMessage ?: "Unknown error")
        }
    }