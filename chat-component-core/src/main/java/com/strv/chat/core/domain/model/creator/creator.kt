package com.strv.chat.core.domain.model.creator

interface Creator<DATA, CONFIGURATION: CreatorConfiguration> {

    val create : CONFIGURATION.() -> DATA
}

interface CreatorConfiguration