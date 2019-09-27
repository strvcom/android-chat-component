package com.strv.chat.component.di

import android.app.Activity

class ControllerCompositionRoot(
    val compositionRoot: CompositionRoot,
    activity: Activity
) {

    fun mediaProvider() = compositionRoot.mediaProvider()
}