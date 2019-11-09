package com.strv.chat.core.core.ui.conversation.style

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import com.strv.chat.core.R
import com.strv.chat.core.core.ui.Style
import com.strv.chat.core.core.ui.conversation.ConversationRecyclerView
import com.strv.chat.core.core.ui.conversation.data.ConversationItemView

class ConversationRecyclerViewStyle private constructor(
    context: Context
) : Style(context) {

    companion object {

        /**
         * Applies style to [ConversationRecyclerView].
         *
         * @param context [Context].
         * @param attrs [AttributeSet] of [ConversationRecyclerView].
         *
         * @return [ConversationRecyclerViewStyle]
         */
        fun parse(context: Context, attrs: AttributeSet): ConversationRecyclerViewStyle {
            val style = ConversationRecyclerViewStyle(context)
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ConversationRecyclerView)

            parseTitle(typedArray, style)
            parseMessage(typedArray, style)
            typedArray.recycle()

            return style
        }

        /**
         * Applies style to [ConversationItemView.title].
         */
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

        /**
         * Applies style to [ConversationItemView.message].
         */
        private fun parseMessage(typedArray: TypedArray, style: ConversationRecyclerViewStyle) {
            style.messageTextColor = typedArray.getColor(
                R.styleable.ConversationRecyclerView_crv_messageTextColor,
                style.defaultMessageColor()
            )

            style.messageTextSize = typedArray.getDimensionPixelSize(
                R.styleable.ConversationRecyclerView_crv_messageTextSize,
                style.defaultMessageSize()
            )
        }
    }

    //style attributes
    var titleTextSize: Int = -1
    var titleTextColor: Int = -1
    var titleTextStyle: Int = -1

    var messageTextSize: Int = -1
    var messageTextColor: Int = -1

    //private methods
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
}
