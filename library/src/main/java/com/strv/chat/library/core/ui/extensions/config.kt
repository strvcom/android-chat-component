package com.strv.chat.library.core.ui.extensions

import com.strv.chat.library.core.session.config.ServiceConfig

fun serviceConfig(channelId: String, init: ServiceConfig.Builder.() -> Unit = {}) =
    ServiceConfig.Builder(channelId).apply(init).build()