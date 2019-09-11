package com.strv.chat.library.ui.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.conversation.data.ConversationItemView
import com.strv.chat.library.ui.imageCircle
import com.strv.chat.library.ui.view.TimeTextView

typealias OnClickAction = (String) -> Unit

abstract class ConversationAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun submitList(list: List<ConversationItemView>)

    abstract fun getItem(position: Int): ConversationItemView
}

class DefaultConversationAdapter(
    diffCallback: DiffUtil.ItemCallback<ConversationItemView>
) :
    ConversationAdapter<ConversationViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun submitList(list: List<ConversationItemView>) {
        differ.submitList(list)
    }

    override fun getItem(position: Int): ConversationItemView =
        differ.currentList[position]

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder(view(parent, R.layout.item_conversation), {})

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_conversation

    private fun view(parent: ViewGroup, layoutId: Int) =
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
}

class ConversationViewHolder(private val view: View, private val onClick: OnClickAction) :
    RecyclerView.ViewHolder(view) {

    private val imageIcon = view.findViewById<ImageView>(R.id.iv_photo)
    private val textUserName = view.findViewById<TextView>(R.id.tv_user_name)
    private val textLastMessage = view.findViewById<TextView>(R.id.tv_message)
    private val textDate = view.findViewById<TimeTextView>(R.id.tv_date)

    fun bind(item: ConversationItemView) {
        imageIcon.imageCircle(item.iconUrl)
        textUserName.text = item.title
        textLastMessage.text = item.message
        textDate.date = item.sentDate

        view.setOnClickListener {
            onClick(item.id)
        }
    }
}