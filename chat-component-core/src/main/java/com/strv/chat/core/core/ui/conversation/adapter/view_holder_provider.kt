package com.strv.chat.core.core.ui.conversation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.core.core.ui.Styleable
import com.strv.chat.core.core.ui.conversation.adapter.ConversationViewType.CONVERSATION
import com.strv.chat.core.core.ui.conversation.style.ConversationRecyclerViewStyle
import com.strv.chat.core.core.ui.extensions.LayoutId

typealias ConversationVHConstructor = (ViewGroup, LayoutId) -> ConversationViewHolder

/**
 * Holds all possible ViewHolder configurations and allows to add a custom implementations of ViewHolder.
 *
 * In case that one property is not filled, the default implementation will be used.
 *
 * @conversationVHConfig [ConversationViewHolder] configuration.
 */
class ConversationViewHolderProvider(
    private val conversationVHConfig: ConversationVHConfig =
        ConversationVHConfig(CONVERSATION.id) { parent, _ ->
            DefaultConversationViewHolder(
                parent
            )
        }
) {

    /**
     * Returns a [ConversationViewHolder] based on [viewType].
     */
    internal fun holder(
        parent: ViewGroup,
        viewType: Int,
        style: ConversationRecyclerViewStyle?
    ): RecyclerView.ViewHolder =
        when (ConversationViewType.viewType(viewType)) {
            CONVERSATION -> {
                conversationVHConfig.viewHolder(parent).also { holder ->
                    checkStyle(holder, style)
                }
            }
            null -> throw IllegalStateException("Wrong message view type.")
        }

    /**
     * Applies style for [Styleable].
     */
    @Suppress("UNCHECKED_CAST")
    private fun checkStyle(holder: ConversationViewHolder, style: ConversationRecyclerViewStyle?) {
        if (style != null && holder is Styleable<*>) {
            (holder as Styleable<ConversationRecyclerViewStyle>).applyStyle(style)
        }
    }
}

/**
 * Container holding configuration for creating a [ConversationViewHolder].
 *
 * @param layoutId layout resource id.
 * @param constructor function that returns a new [ConversationViewHolder] object.
 *
 * @constructor Creates a new configuration for creating a [ConversationViewHolder].
 */
class ConversationVHConfig(
    layoutId: LayoutId,
    constructor: ConversationVHConstructor
) {

    /**
     * Creates a new [ConversationViewHolder] object.
     *
     * @return [ConversationViewHolder]
     */
    val viewHolder: (ViewGroup) -> ConversationViewHolder = { group ->
        constructor(group, layoutId)
    }
}