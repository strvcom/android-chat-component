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
import com.strv.chat.library.ui.TimeTextView
import com.strv.chat.library.ui.chat.view.ChatItemView
import com.strv.chat.library.ui.chat.view.ChatItemView.MyTextMessage
import com.strv.chat.library.ui.chat.view.ChatItemView.OtherTextMessage
import com.strv.chat.library.ui.imageCircle

abstract class ChatAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun submitList(list: List<ChatItemView>)

    abstract fun getItem(position: Int): ChatItemView
}

class DefaultChatAdapter(diffCallback: DiffUtil.ItemCallback<ChatItemView>) :
    ChatAdapter<ChatViewHolder>() {

    private val differ = AsyncListDiffer(this, diffCallback)

    override fun submitList(list: List<ChatItemView>) {
        differ.submitList(list)
    }

    override fun getItem(position: Int): ChatItemView =
        differ.currentList[position]

    override fun getItemCount(): Int =
        differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder =
        when (viewType) {
            R.layout.item_my_message -> ChatViewHolder.MyMessageViewHolder(view(parent, viewType))
            R.layout.item_other_message -> ChatViewHolder.OtherMessageViewHolder(
                view(
                    parent,
                    viewType
                )
            )
            else -> throw IllegalArgumentException("Undefined message type")
        }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) =
        when (holder) {
            is ChatViewHolder.MyMessageViewHolder -> holder.bind(getItem(position) as MyTextMessage)
            is ChatViewHolder.OtherMessageViewHolder -> holder.bind(getItem(position) as OtherTextMessage)
        }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is MyTextMessage -> R.layout.item_my_message
            is OtherTextMessage -> R.layout.item_other_message
            else -> throw IllegalArgumentException("Undefined message type")
        }

    private fun view(parent: ViewGroup, layoutId: Int) =
        LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
}

sealed class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class MyMessageViewHolder(private val view: View) : ChatViewHolder(view) {

        private val textMessage = view.findViewById<TextView>(R.id.tv_message)
        private val textDate = view.findViewById<TimeTextView>(R.id.tv_message_date)

        fun bind(item: MyTextMessage) {
            textMessage.text = item.text

            textDate.run {
                date = item.sentDate
                visibility = if (item.showSentDate) View.VISIBLE else View.GONE
            }

            view.setOnClickListener {
                bind(item.copy(showSentDate = !item.showSentDate))
            }
        }
    }

    class OtherMessageViewHolder(private val view: View) : ChatViewHolder(view) {

        private val imageIcon = view.findViewById<ImageView>(R.id.iv_user_icon)
        private val textMessage = view.findViewById<TextView>(R.id.tv_message)
        private val textDate = view.findViewById<TimeTextView>(R.id.tv_message_date)

        fun bind(item: OtherTextMessage) {
            imageIcon.imageCircle(item.sender.userPhotoUrl)
            textMessage.text = item.text

            textDate.run {
                date = item.sentDate
                visibility = if (item.showSentDate) View.VISIBLE else View.GONE
            }

            view.setOnClickListener {
                bind(item.copy(showSentDate = !item.showSentDate))
            }
        }
    }
}