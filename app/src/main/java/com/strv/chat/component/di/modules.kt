package com.strv.chat.component.di

import com.strv.chat.component.di.module.appModule
import com.strv.chat.component.di.module.firebaseModule
import com.strv.chat.component.di.module.viewModelModule

val applicationModules = listOf(
    appModule,
    viewModelModule,
    firebaseModule
)