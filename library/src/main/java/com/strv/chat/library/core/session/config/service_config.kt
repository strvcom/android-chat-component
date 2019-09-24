package com.strv.chat.library.core.session.config

class ServiceConfig(
    val channelId: String,
    val largeIconRes: Int,
    var smallIconProgressRes: Int,
    var smallIconSuccessRes: Int,
    var smallIconErrorRes: Int
) {

    class Builder(
        val channelId: String,
        var largeIconRes: Int? = null,
        var smallIconProgressRes: Int? = null,
        var smallIconSuccessRes: Int? = null,
        var smallIconErrorRes: Int? = null
    ) {

        fun build() = ServiceConfig(
            channelId,
            largeIconRes ?: android.R.mipmap.sym_def_app_icon,
            smallIconProgressRes ?: android.R.drawable.stat_sys_upload,
            smallIconSuccessRes ?: android.R.drawable.stat_sys_upload_done,
            smallIconErrorRes ?: android.R.drawable.stat_notify_error
        )
    }
}