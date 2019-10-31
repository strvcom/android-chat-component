package com.strv.chat.core.core.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.strv.chat.core.R

/**
 * Decorates RecyclerView.ViewHolder component with customizable style.
 *
 * @param T Style type.
 */
interface Styleable<T : Style> {

    /**
     * Applies style to the given [Styleable].
     *
     * @param style Style to apply.
     */
    fun applyStyle(style: T)
}

/**
 * Base class for UI component styles.
 *
 * @property context
 */
abstract class Style(
    val context: Context
) {

    private val resources = context.resources

    /**
     * Returns system accent color resource id.
     *
     * @return System accent color resource id.
     */
    protected fun systemAccentColor(): Int =
        systemColor(R.attr.colorAccent)

    /**
     * Returns system background color resource id.
     *
     * @return System background color resource id.
     */
    protected fun systemBackgroundColor(): Int =
        systemColor(R.attr.background)

    /**
     * Returns system control normal color resource id.
     *
     * @return System control normal color resource id.
     */
    protected fun systemControlNormalColor(): Int =
        systemColor(R.attr.colorControlNormal)

    /**
     * Returns system control activated color resource id.
     *
     * @return System control activated color resource id.
     */
    protected fun systemControlActivatedColor(): Int =
        systemColor(R.attr.colorControlActivated)

    /**
     * Returns system primary color resource id.
     *
     * @return System primary color resource id.
     */
    protected fun systemPrimaryColor(): Int =
        systemColor(R.attr.colorPrimary)

    /**
     * Returns system primary dark color resource id.
     *
     * @return System primary dark color resource id.
     */
    protected fun systemPrimaryDarkColor(): Int =
        systemColor(R.attr.colorPrimaryDark)

    /**
     * Returns system primary text color resource id.
     *
     * @return System primary text color resource id.
     */
    protected fun systemPrimaryTextColor(): Int =
        systemColor(android.R.attr.textColorPrimary)

    /**
     * Returns system hint color resource id.
     *
     * @return System hint color resource id.
     */
    protected fun systemHintColor(): Int =
        systemColor(android.R.attr.textColorHint)

    /**
     * Returns dimension size in pixels
     *
     * @param dimen Resource id of the dimension.
     *
     * @return Dimension size in pixels.
     */
    protected fun dimension(@DimenRes dimen: Int): Int =
        resources.getDimensionPixelSize(dimen)

    /**
     * Returns color.
     *
     * @param color Resource id of the color.
     *
     * @return A single color value in the form 0xAARRGGBB.
     */
    protected fun color(@ColorRes color: Int): Int =
        ContextCompat.getColor(context, color)

    /**
     * Returns drawable.
     *
     * @param drawable Resource id of the drawable.
     *
     * @return Drawable object.
     */
    protected fun drawable(@DrawableRes drawable: Int): Drawable? =
        ContextCompat.getDrawable(context, drawable)

    /**
     * Returns system color resource id.
     *
     * @param attr Desired attributes in the style.
     * @return System color resource id.
     */
    private fun systemColor(@AttrRes attr: Int): Int {
        val typedValue = TypedValue()

        val typedArray = context.obtainStyledAttributes(typedValue.data, intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        return color
    }
}