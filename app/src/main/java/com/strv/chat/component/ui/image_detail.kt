package com.strv.chat.component.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.strv.chat.component.R
import com.strv.chat.library.core.ui.chat.imageDetail.ImageDetailView

const val IMAGE_URL_EXTRA = "image_url"

class ImageDetailActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context, imageUrl: String) =
            Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(IMAGE_URL_EXTRA, imageUrl)
            }
    }

    val imageDetail by lazy {
        findViewById<ImageDetailView>(R.id.iv_photo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        imageDetail.init(
            requireNotNull(intent.getStringExtra(IMAGE_URL_EXTRA))
        )
    }
}
