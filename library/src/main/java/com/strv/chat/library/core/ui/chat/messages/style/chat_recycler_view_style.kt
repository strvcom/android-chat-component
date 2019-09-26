package com.strv.chat.library.core.ui.chat.messages.style

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Style

class ChatRecyclerViewStyle private constructor(
    context: Context
) : Style(context) {

    companion object {
        fun parse(context: Context, attrs: AttributeSet): ChatRecyclerViewStyle {
            val style = ChatRecyclerViewStyle(context)
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatRecyclerView)

            style.textMessageTextSize = typedArray.getDimensionPixelSize(
                R.styleable.ChatRecyclerView_chrv_textMessageTextSize,
                style.defaultTextSize()
            )

            parseMyTextMessage(typedArray, style)
            parseOtherTextMessage(typedArray, style)

            typedArray.recycle()

            return style
        }

        private fun parseMyTextMessage(typedArray: TypedArray, style: ChatRecyclerViewStyle) {
            style.myTextMessageBackground = typedArray.getResourceId(
                R.styleable.ChatRecyclerView_chrv_myTextMessageBackground,
                -1
            )

            style.myTextMessageCornerRadius = typedArray.getDimensionPixelSize(
                R.styleable.ChatRecyclerView_chrv_myTextMessageCornerRadius,
                style.defaultMyMessageCornerRadius()
            )

            style.myTextMessageStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.ChatRecyclerView_chrv_myTextMessageStrokeWidth,
                style.defaultMyMessageStrokeWidth()
            )

            style.myTextMessageStrokeColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_myTextMessageStrokeColor,
                -1
            )

            style.myTextMessageBackgroundColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_myTextMessageBackgroundColor,
                style.defaultMyMessageBackgroundColor()
            )

            style.myTextMessageTextColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_myTextMessageTextColor,
                style.defaultMyMessageTextColor()
            )
        }

        private fun parseOtherTextMessage(typedArray: TypedArray, style: ChatRecyclerViewStyle) {
            style.otherTextMessageBackground = typedArray.getResourceId(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageBackground,
                -1
            )

            style.otherTextMessageCornerRadius = typedArray.getDimensionPixelSize(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageCornerRadius,
                style.defaultOtherMessageCornerRadius()
            )

            style.otherTextMessageStrokeWidth = typedArray.getDimensionPixelSize(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageStrokeWidth,
                style.defaultOtherMessageStrokeWidth()
            )

            style.otherTextMessageStrokeColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageStrokeColor,
                -1
            )

            style.otherTextMessageBackgroundColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageBackgroundColor,
                style.defaultOtherMessageBackgroundColor()
            )

            style.otherTextMessageTextColor = typedArray.getColor(
                R.styleable.ChatRecyclerView_chrv_otherTextMessageTextColor,
                style.defaultOtherMessageTextColor()
            )
        }
    }

    var textMessageTextSize: Int = -1

    var myTextMessageBackground: Int = -1
    var myTextMessageCornerRadius: Int = -1
    var myTextMessageStrokeColor: Int = -1
    var myTextMessageStrokeWidth: Int = -1
    var myTextMessageBackgroundColor: Int = -1
    var myTextMessageTextColor: Int = -1

    val myTextMessageBackgroundDrawable: Drawable
        get() =
            if (myTextMessageBackground == -1) {
                GradientDrawable().apply {
                    setStroke(
                        myTextMessageStrokeWidth,
                        if (myTextMessageStrokeColor == -1) myTextMessageBackgroundColor else myTextMessageStrokeColor
                    )
                    cornerRadius = myTextMessageCornerRadius.toFloat()
                    shape = GradientDrawable.RECTANGLE
                    setColor(myTextMessageBackgroundColor)
                }
            } else {
                requireNotNull(drawable(myTextMessageBackground)) { "Drawable resource is not defined" }
            }

    var otherTextMessageBackground: Int = -1
    var otherTextMessageCornerRadius: Int = -1
    var otherTextMessageStrokeColor: Int = -1
    var otherTextMessageStrokeWidth: Int = -1
    var otherTextMessageBackgroundColor: Int = -1
    var otherTextMessageTextColor: Int = -1

    val otherTextMessageBackgroundDrawable: Drawable
        get() =
            if (otherTextMessageBackground == -1) {
                GradientDrawable().apply {
                    setStroke(
                        otherTextMessageStrokeWidth,
                        if (otherTextMessageStrokeColor == -1) otherTextMessageBackgroundColor else otherTextMessageStrokeColor
                    )
                    cornerRadius = otherTextMessageCornerRadius.toFloat()
                    shape = GradientDrawable.RECTANGLE
                    setColor(otherTextMessageBackgroundColor)
                }
            } else {
                requireNotNull(drawable(otherTextMessageBackground)) { "Drawable resource is not defined" }
            }

    private fun defaultTextSize() =
        dimension(R.dimen.text_message_text_size)

    private fun defaultMyMessageCornerRadius() =
        dimension(R.dimen.text_message_corner_radius)

    private fun defaultMyMessageStrokeWidth() =
        dimension(R.dimen.text_message_stroke_width)

    private fun defaultMyMessageBackgroundColor() =
        systemPrimaryColor()

    private fun defaultMyMessageTextColor() =
        color(R.color.my_message_text)

    private fun defaultOtherMessageCornerRadius() =
        dimension(R.dimen.text_message_corner_radius)

    private fun defaultOtherMessageStrokeWidth() =
        dimension(R.dimen.text_message_stroke_width)

    private fun defaultOtherMessageBackgroundColor() =
        color(R.color.other_message_background)

    private fun defaultOtherMessageTextColor() =
        systemPrimaryTextColor()

}
