package com.strv.chat.core.core.ui.chat.imageDetail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.strv.chat.core.domain.IMAGE_LOADER
import com.strv.chat.core.domain.ImageLoader
import strv.ktools.logE

const val IMAGE_URL = "imageUrl"

class ImageDetailView : AppCompatImageView {

    private var _imageLoader: ImageLoader? = null
    private var _imageUrl: String? = null

    var imageLoader: ImageLoader
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageLoader = value
        }

    var imageUrl: String
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageUrl = value
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun init(builder: ImageDetailView.() -> Unit) {
        builder()

        _imageUrl?.let { url ->
            _imageLoader?.loadImageDetail(this, url) ?: logE("$IMAGE_LOADER is not defined")
        } ?: logE("$IMAGE_URL is not defined")
    }
}
