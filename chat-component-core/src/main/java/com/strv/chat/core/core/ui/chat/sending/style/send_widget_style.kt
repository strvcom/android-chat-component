package com.strv.chat.core.core.ui.chat.sending.style

import android.content.Context
import android.util.AttributeSet
import com.strv.chat.core.R
import com.strv.chat.core.core.ui.Style
import com.strv.chat.core.core.ui.chat.sending.SendWidget

class SendWidgetStyle private constructor(
    context: Context
) : Style(context) {

    companion object {

        /**
         * Applies style to [SendWidget].
         *
         * @param context [Context].
         * @param attrs AttributeSet of [SendWidget].
         *
         * @return [SendWidgetStyle]
         */
        fun parse(context: Context, attrs: AttributeSet): SendWidgetStyle {
            val style = SendWidgetStyle(context)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SendWidget)

            style.backgroundColor = typedArray.getColor(
                R.styleable.SendWidget_sw_backgroundColor,
                style.defaultBackgroundColor()
            )

            style.sendIconTint =
                typedArray.getColor(R.styleable.SendWidget_sw_sendIconTint, style.defaultIconTint())

            style.filterColorNormal =
                typedArray.getColor(R.styleable.SendWidget_sw_messageOptionIconColorNormal, style.defaultControlNormalColor())

            style.filterColorActivated =
                typedArray.getColor(R.styleable.SendWidget_sw_messageOptionIconColorActivated, style.defaultControlActivatedColor())

            typedArray.recycle()

            return style
        }
    }

    //style attributes
    var backgroundColor: Int = -1

    var sendIconTint: Int = -1

    var filterColorActivated: Int = -1
    var filterColorNormal: Int = -1

    //private methods
    private fun defaultBackgroundColor() =
        systemBackgroundColor()

    private fun defaultIconTint() =
        systemAccentColor()

    private fun defaultControlNormalColor() =
        systemControlNormalColor()

    private fun defaultControlActivatedColor() =
        systemControlActivatedColor()
}

