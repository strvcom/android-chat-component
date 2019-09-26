package com.strv.chat.library.core.ui.conversation.style

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import com.strv.chat.library.R
import com.strv.chat.library.core.ui.Style

class ConversationRecyclerViewStyle private constructor(
    context: Context
) : Style(context) {

    companion object {
        fun parse(context: Context, attrs: AttributeSet): ConversationRecyclerViewStyle {
            val style = ConversationRecyclerViewStyle(context)
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ConversationRecyclerView)

            parseTitle(typedArray, style)
            parseMessage(typedArray, style)
            typedArray.recycle()

            return style
        }

        private fun parseTitle(typedArray: TypedArray, style: ConversationRecyclerViewStyle) {
            style.titleTextColor = typedArray.getColor(
                R.styleable.ConversationRecyclerView_crv_titleTextColor,
                style.defaultTitleColor()
            )

            style.titleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.ConversationRecyclerView_crv_titleTextSize,
                style.defaultTitleSize()
            )

            style.titleTextStyle = typedArray.getInt(
                R.styleable.ConversationRecyclerView_crv_titleTextStyle,
                style.defaultTitleStyle()
            )
        }

        private fun parseMessage(typedArray: TypedArray, style: ConversationRecyclerViewStyle) {
            style.messageTextColor = typedArray.getColor(
                R.styleable.ConversationRecyclerView_crv_messageTextColor,
                style.defaultMessageColor()
            )

            style.messageTextSize = typedArray.getDimensionPixelSize(
                R.styleable.ConversationRecyclerView_crv_messageTextSize,
                style.defaultMessageSize()
            )

            style.messageTextStyle = typedArray.getInt(
                R.styleable.ConversationRecyclerView_crv_messageTextStyle,
                style.defaultMessageStyle()
            )
        }
    }

    var titleTextSize: Int = -1
    var titleTextColor: Int = -1
    var titleTextStyle: Int = -1

    var messageTextSize: Int = -1
    var messageTextColor: Int = -1
    var messageTextStyle: Int = -1

    private fun defaultTitleColor() =
        systemPrimaryTextColor()

    private fun defaultTitleSize() =
        dimension(R.dimen.conversation_title_text_size)

    private fun defaultTitleStyle() =
        Typeface.BOLD

    private fun defaultMessageColor() =
        systemPrimaryTextColor()

    private fun defaultMessageSize() =
        dimension(R.dimen.conversation_message_text_size)

    private fun defaultMessageStyle() =
        Typeface.NORMAL
}
