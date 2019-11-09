package com.strv.chat.component.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.strv.chat.component.R
import com.strv.chat.core.core.ui.chat.imageDetail.ImageDetailView
import com.strv.chat.core.domain.ImageLoader
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import strv.ktools.logMe

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

    private val loader by inject<ImageLoader>()

    private val imageDetailViewModel: ImageDetailViewModel by viewModel {
        parametersOf(
            requireNotNull(intent.getStringExtra(IMAGE_URL_EXTRA)) { "$IMAGE_URL_EXTRA must be defined" }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        imageDetailViewModel.imageUrl.logMe()

        imageDetail.init {
            imageLoader = loader
            imageUrl = imageDetailViewModel.imageUrl
        }
    }
}

class ImageDetailViewModel(
    val imageUrl: String
) : ViewModel()
