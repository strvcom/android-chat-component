package com.strv.chat.component

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strv.chat.library.core.ui.conversation.ConversationRecyclerView
import com.strv.chat.library.domain.model.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider

class ConversationsActivity : AppCompatActivity() {

    val conversationRecyclerView by lazy {
        findViewById<ConversationRecyclerView>(R.id.rv_chat)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    val memberProvider = object : MemberProvider {
        override fun currentUserId(): String = "user-1"

        override fun member(memberId: String): MemberModel {
            if (memberId == "user-1") {
                return user1()
            } else {
                return user2()
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        conversationRecyclerView.init(
            memberProvider
        )
    }


    fun user1() = object : MemberModel {
        override val userId: String = "user-1"
        override val userName: String = "John"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"

    }

    fun user2() = object : MemberModel {
        override val userId: String = "user-2"
        override val userName: String = "Camila"
        override val userPhotoUrl: String =
            "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"

    }

    override fun onStart() {
        super.onStart()

        conversationRecyclerView.onStart { startActivity(MainActivity.newIntent(this@ConversationsActivity)) }
            .onError {
                Toast.makeText(this@ConversationsActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }.onNext {
                progress.visibility = View.GONE
            }
    }

    override fun onStop() {
        super.onStop()

        conversationRecyclerView.onStop()
    }
}


