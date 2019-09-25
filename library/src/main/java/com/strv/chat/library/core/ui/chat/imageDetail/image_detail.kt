package com.strv.chat.library.core.ui.chat.imageDetail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.strv.chat.library.core.ui.extensions.imageUrl

class ImageDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    fun init(imageUrl: String) {
        imageUrl(imageUrl)
    }
}
