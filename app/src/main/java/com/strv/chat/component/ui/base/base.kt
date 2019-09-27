package com.strv.chat.component.ui.base

import androidx.appcompat.app.AppCompatActivity
import com.strv.chat.component.App
import com.strv.chat.component.di.ControllerCompositionRoot

abstract class BaseActivity : AppCompatActivity() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    protected fun controllerCompositionRoot(): ControllerCompositionRoot {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot =
                ControllerCompositionRoot((application as App).compositionRoot(), this)
        }

        return controllerCompositionRoot!!
    }
}
