package com.strv.chat.component.di

import com.strv.chat.component.business.MediaProviderImpl
import com.strv.chat.component.business.MemberProviderImpl
import com.strv.chat.library.domain.provider.MediaProvider
import com.strv.chat.library.domain.provider.MemberProvider

class CompositionRoot {

    fun mediaProvider(): MediaProvider = MediaProviderImpl()

    fun memberProvider(): MemberProvider = MemberProviderImpl()
}