package com.strv.chat.library.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.R
import com.strv.chat.library.ui.chat.data.ChatItemView
import com.strv.chat.library.ui.chat.data.ChatItemView.Header
import com.strv.chat.library.ui.chat.data.ChatItemView.MyTextMessage
import com.strv.chat.library.ui.chat.data.ChatItemView.OtherTextMessage
import com.strv.chat.library.ui.imageCircle
import com.strv.chat.library.ui.view.RelativeTimeTextView
import com.strv.chat.library.ui.view.TimeTextView

open class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val diffUtilCallback = object : DiffUtil.ItemCallback<ChatItemView>() {

        //todo is it enought?
        override fun areItemsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
            return oldItem.sentDate == newItem.sentDate
        }

        override fun areContentsTheSame(oldItem: ChatItemView, newItem: ChatItemView): Boolean {
            return oldItem == newItem
        }
    }

    private val differ by lazy { AsyncListDiffer(this, diffUtilCallback) }

    override fun getItemCount(): Int =
        differ.currentList.size

    open fun headerViewHolder(parent: ViewGroup) =
        DefaultHeaderViewHolder(parent)

    open fun myMessageViewHolder(parent: ViewGroup) =
        DefaultMyMessageViewHolder(parent)

    open fun otherMessageViewHolder(parent: ViewGroup) =
        DefaultOtherMessageViewHolder(parent)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_header -> headerViewHolder(parent)
            R.layout.item_my_message -> myMessageViewHolder(parent)
            R.layout.item_other_message -> otherMessageViewHolder(parent)
            else -> throw IllegalArgumentException("Undefined message type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(getItem(position) as Header)
            is MyMessageViewHolder -> holder.bind(getItem(position) as MyTextMessage)
            is OtherMessageViewHolder -> holder.bind(getItem(position) as OtherTextMessage)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is Header -> R.layout.item_header
            is MyTextMessage -> R.layout.item_my_message
            is OtherTextMessage -> R.layout.item_other_message
        }

    fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ChatItemView =
        differ.currentList[position]
}

abstract class HeaderViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun bind(item: Header)
}

abstract class MyMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun bind(item: MyTextMessage)
}

abstract class OtherMessageViewHolder(
    parent: ViewGroup,
    layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    abstract fun bind(item: OtherTextMessage)
}

class DefaultHeaderViewHolder(parent: ViewGroup) :
    HeaderViewHolder(parent, R.layout.item_header) {
    private val textDate = itemView.findViewById<RelativeTimeTextView>(R.id.tv_date)

    override fun bind(item: Header) {
        textDate.date = item.sentDate
    }
}

class DefaultMyMessageViewHolder(parent: ViewGroup) :
    MyMessageViewHolder(parent, R.layout.item_my_message) {
    private val textMessage = itemView.findViewById<TextView>(R.id.tv_message)

    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)
    override fun bind(item: MyTextMessage) {
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}

class DefaultOtherMessageViewHolder(parent: ViewGroup) :
    OtherMessageViewHolder(parent, R.layout.item_other_message) {

    private val imageIcon = itemView.findViewById<ImageView>(R.id.iv_user_icon)
    private val textMessage = itemView.findViewById<TextView>(R.id.tv_message)
    private val textDate = itemView.findViewById<TimeTextView>(R.id.tv_message_date)

    override fun bind(item: OtherTextMessage) {
        imageIcon.imageCircle(item.sender.userPhotoUrl)
        textMessage.text = item.text

        textDate.run {
            date = item.sentDate
            visibility = if (item.showSentDate) View.VISIBLE else View.GONE
        }

        itemView.setOnClickListener {
            bind(item.copy(showSentDate = !item.showSentDate))
        }
    }
}
