@file:JvmName("ServiceConfig")

package com.strv.chat.core.core.session.config.di

import com.strv.chat.core.core.session.config.ServiceConfig

/**
 * DSL wrapper for creating a [ServiceConfig] object.
 *
 * @param channelId Id of the channel.
 * @param init Type-safe builder that allows creating Kotlin-based domain-specific languages (DSLs) suitable for building [ServiceConfig] object in a semi-declarative way.
 *
 * @return [ServiceConfig] object.
 */
@JvmOverloads
fun serviceConfig(channelId: String, init: ServiceConfig.Builder.() -> Unit = {}) =
    ServiceConfig.Builder().apply(init).build(channelId)