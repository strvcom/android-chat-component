package com.strv.chat.core.core.ui.chat.sending.style

import android.content.Context
import android.util.AttributeSet
import androidx.core.graphics.drawable.DrawableCompat
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

            style.sendIcon =
                typedArray.getResourceId(
                    R.styleable.SendWidget_sw_sendIcon,
                    style.defaultSendIcon()
                )

            style.textOptionIcon =
                typedArray.getResourceId(
                    R.styleable.SendWidget_sw_textOptionIcon,
                    style.defaultTextOptionIcon()
                )

            style.imageOptionIcon =
                typedArray.getResourceId(
                    R.styleable.SendWidget_sw_imageOptionIcon,
                    style.defaultImageOptionIcon()
                )

            style.sendIconTint =
                typedArray.getColor(
                    R.styleable.SendWidget_sw_sendIconTint,
                    -1
                )

            style.filterColorNormal =
                typedArray.getColor(
                    R.styleable.SendWidget_sw_messageOptionIconColorNormal,
                    -1
                )

            style.filterColorActivated =
                typedArray.getColor(
                    R.styleable.SendWidget_sw_messageOptionIconColorActivated,
                    style.defaultControlActivatedColor()
                )

            style.hintText =
                typedArray.getString(
                    R.styleable.SendWidget_sw_hint
                ) ?: style.defaultHint()

            style.imageOptionEnabled =
                typedArray.getBoolean(
                    R.styleable.SendWidget_sw_imageOptionEnabled,
                    true
                )

            style.inputTextAppearance =
                typedArray.getResourceId(
                    R.styleable.SendWidget_sw_inputTextAppearance,
                    -1
                )

            typedArray.recycle()

            return style
        }
    }

    //style attributes
    private var sendIconTint: Int = -1

    private var filterColorNormal: Int = -1

    private var sendIcon: Int = -1
    private var textOptionIcon: Int = -1
    private var imageOptionIcon: Int = -1

    var filterColorActivated: Int = -1
    var backgroundColor: Int = -1
    var hintText: String = ""
    var imageOptionEnabled: Boolean = true

    var inputTextAppearance: Int = -1

    fun sendIcon() =
        if (sendIcon == defaultSendIcon()) {
            val tint = if (sendIconTint == -1) defaultSendIconTint() else sendIconTint

            drawable(sendIcon)?.also { drawable ->
                DrawableCompat.setTint(drawable, tint)
            }
        } else {
            drawable(sendIcon)?.also { drawable ->
                if (sendIconTint != -1) {
                    DrawableCompat.setTint(drawable, sendIconTint)
                }
            }
        }

    fun textOptionIcon() =
        if (textOptionIcon == defaultTextOptionIcon()) {
            val tint =
                if (filterColorNormal == -1) defaultControlNormalColor() else filterColorNormal

            drawable(textOptionIcon)?.also { drawable ->
                DrawableCompat.setTint(drawable, tint)
            }
        } else {
            drawable(textOptionIcon)?.also { drawable ->
                if (filterColorNormal != -1) {
                    DrawableCompat.setTint(drawable, filterColorNormal)
                }
            }
        }

    fun imageOptionIcon() =
        if (imageOptionIcon == defaultImageOptionIcon()) {
            val tint =
                if (filterColorNormal == -1) defaultControlNormalColor() else filterColorNormal

            drawable(imageOptionIcon)?.also { drawable ->
                DrawableCompat.setTint(drawable, tint)
            }
        } else {
            drawable(imageOptionIcon)?.also { drawable ->
                if (filterColorNormal != -1) {
                    DrawableCompat.setTint(drawable, filterColorNormal)
                }
            }
        }

    //private methods
    private fun defaultBackgroundColor() =
        systemBackgroundColor()

    private fun defaultSendIcon() =
        R.drawable.ic_send

    private fun defaultTextOptionIcon() =
        R.drawable.ic_text

    private fun defaultImageOptionIcon() =
        R.drawable.ic_camera

    private fun defaultSendIconTint() =
        systemAccentColor()

    private fun defaultControlNormalColor() =
        systemControlNormalColor()

    private fun defaultControlActivatedColor() =
        systemControlActivatedColor()

    private fun defaultHint() =
        string(R.string.write_your_message)

}

