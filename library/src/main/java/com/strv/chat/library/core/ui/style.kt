package com.strv.chat.library.core.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.strv.chat.library.R

interface Styleable<T: Style> {

    fun applyStyle(style: T)
}

abstract class Style(
    val context: Context
) {

    protected val resources = context.resources

    protected fun systemAccentColor(): Int =
        systemColor(R.attr.colorAccent)

    protected fun systemBackgroundColor(): Int =
        systemColor(R.attr.background)

    protected fun systemColorButtonNormal(): Int =
        systemColor(R.attr.colorButtonNormal)

    protected fun systemPrimaryColor(): Int =
        systemColor(R.attr.colorPrimary)

    protected fun systemPrimaryDarkColor(): Int =
        systemColor(R.attr.colorPrimaryDark)

    protected fun systemPrimaryTextColor(): Int =
        systemColor(android.R.attr.textColorPrimary)

    protected fun systemHintColor(): Int =
        systemColor(android.R.attr.textColorHint)

    protected fun systemColor(@AttrRes attr: Int): Int {
        val typedValue = TypedValue()

        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        return color
    }

    protected fun dimension(@DimenRes dimen: Int): Int =
        resources.getDimensionPixelSize(dimen)

    protected fun color(@ColorRes color: Int): Int =
        ContextCompat.getColor(context, color)

    protected fun colorStateList(@ColorRes color: Int): ColorStateList =
        ColorStateList.valueOf(color(color))

    protected fun drawable(@DrawableRes drawable: Int): Drawable? =
        ContextCompat.getDrawable(context, drawable)

    protected fun vectorDrawable(@DrawableRes drawable: Int): Drawable? =
        ContextCompat.getDrawable(context, drawable)
}