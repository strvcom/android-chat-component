@file:JvmName("ServiceConfig")

package com.strv.chat.core.core.session.config.di

import com.strv.chat.core.core.session.config.ServiceConfig

@JvmOverloads
fun serviceConfig(channelId: String, init: ServiceConfig.Builder.() -> Unit = {}) =
    ServiceConfig.Builder().apply(init).build(channelId)