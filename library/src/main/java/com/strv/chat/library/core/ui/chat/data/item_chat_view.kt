package com.strv.chat.library.core.ui.chat.data

import com.strv.chat.library.core.ui.data.MemberView
import com.strv.chat.library.core.ui.extensions.OnClickAction
import java.util.*

const val HEADER_ID = "NO_ID"

sealed class ChatItemView(open val id: String, open val sentDate: Date) {

    data class Header(
        override val sentDate: Date
    ) : ChatItemView(HEADER_ID, sentDate)

    data class MyTextMessage(
        override val id: String,
        override val sentDate: Date,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    data class OtherTextMessage(
        override val id: String,
        override val sentDate: Date,
        val sender: MemberView,
        val text: String,
        val showSentDate: Boolean = false
    ) : ChatItemView(id, sentDate)

    sealed class Image(
        override val id: String,
        override val sentDate: Date,
        open val imageUrl: String,
        open val onClick: OnClickAction<Image>?
    ) : ChatItemView(id, sentDate) {

        data class MyImageMessage(
            override val id: String,
            override val sentDate: Date,
            override val imageUrl: String,
            override val onClick: OnClickAction<Image>?,
            val showSentDate: Boolean = false
        ) : Image(id, sentDate, imageUrl, onClick) {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                if (!super.equals(other)) return false

                other as MyImageMessage

                if (id != other.id) return false
                if (sentDate != other.sentDate) return false
                if (imageUrl != other.imageUrl) return false
                if (showSentDate != other.showSentDate) return false

                return true
            }

            override fun hashCode(): Int {
                var result = super.hashCode()
                result = 31 * result + id.hashCode()
                result = 31 * result + sentDate.hashCode()
                result = 31 * result + imageUrl.hashCode()
                result = 31 * result + showSentDate.hashCode()
                return result
            }
        }

        data class OtherImageMessage(
            override val id: String,
            override val sentDate: Date,
            override val imageUrl: String,
            override val onClick: OnClickAction<Image>?,
            val sender: MemberView,
            val showSentDate: Boolean = false
        ) : Image(id, sentDate, imageUrl, onClick) {

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false
                if (!super.equals(other)) return false

                other as OtherImageMessage

                if (id != other.id) return false
                if (sentDate != other.sentDate) return false
                if (sender != other.sender) return false
                if (imageUrl != other.imageUrl) return false
                if (showSentDate != other.showSentDate) return false

                return true
            }

            override fun hashCode(): Int {
                var result = super.hashCode()
                result = 31 * result + id.hashCode()
                result = 31 * result + sentDate.hashCode()
                result = 31 * result + sender.hashCode()
                result = 31 * result + imageUrl.hashCode()
                result = 31 * result + showSentDate.hashCode()
                return result
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}