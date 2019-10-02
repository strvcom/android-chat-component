package com.strv.chat.core.core.ui

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.AnyRes
import androidx.core.content.ContextCompat

interface ResourceProvider {
    val color: ResourceMapper<Int>
    val string: ResourceMapper<String>
    val stringf: ResourceMapper<FormattedString>
    val quantityString: ResourceMapper<QuantityString>
    val dimension: ResourceMapper<Float>
    val stringArray: ResourceMapper<Array<String>>
    val drawable: ResourceMapper<Drawable?>
}

class ChatComponentResourceProvider(private val app: Application) : ResourceProvider {
    override val color = ResourceMapper { ContextCompat.getColor(app, it) }
    override val string = ResourceMapper { app.resources.getString(it) }
    override val stringf = ResourceMapper { FormattedString(app.resources, it) }
    override val quantityString = ResourceMapper { QuantityString(app.resources, it) }
    override val dimension = ResourceMapper { app.resources.getDimension(it) }
    override val stringArray = ResourceMapper { app.resources.getStringArray(it) }
    override val drawable = ResourceMapper { ContextCompat.getDrawable(app, it) }
}

class ResourceMapper<out T>(private val mapRes: (resId: Int) -> T) {
    operator fun get(@AnyRes resId: Int) = mapRes(resId)
}

class FormattedString(
    private val resources: Resources,
    private val resId: Int
) {
    operator fun invoke(vararg values: Any?): String =
        resources.getString(resId, *values)
}

class QuantityString(
    private val resources: Resources,
    private val resId: Int
) {
    operator fun invoke(quantity: Int, vararg values: Any?): String =
        resources.getQuantityString(resId, quantity, *values)
}