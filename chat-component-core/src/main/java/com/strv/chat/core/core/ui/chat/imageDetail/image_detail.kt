package com.strv.chat.core.core.ui.chat.imageDetail

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.strv.chat.core.domain.IMAGE_LOADER
import com.strv.chat.core.domain.ImageLoader
import strv.ktools.logE

internal const val IMAGE_URL = "imageUrl"

/**
 * Component that displays an image fetched from a given url.
 */
class ImageDetailView : AppCompatImageView {

    private var _imageLoader: ImageLoader? = null
    private var _imageUrl: String? = null

    /**
     * Defines a way how to upload [imageUrl].
     */
    var imageLoader: ImageLoader
        get() = throw UnsupportedOperationException("")
        set(value) {
            _imageLoader = value
        }

    /**
     * Image url.
     */
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

    /**
     * Type-safe builder that allows creating Kotlin-based domain-specific languages (DSLs) suitable for configuring [ImageDetailView] in a semi-declarative way.
     */
    fun init(builder: ImageDetailView.() -> Unit) {
        builder()

        _imageUrl?.let { url ->
            _imageLoader?.loadImageDetail(this, url) ?: logE("$IMAGE_LOADER is not defined")
        } ?: logE("$IMAGE_URL is not defined")
    }
}
