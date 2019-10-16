package com.strv.chat.core.core.ui.chat.imageDetail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.strv.chat.core.core.session.ChatComponent.Companion.chatComponent

class ImageDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    fun init(imageUrl: String) {
        chatComponent.imageLoader().loadImageDetail(this, imageUrl)
    }
}
