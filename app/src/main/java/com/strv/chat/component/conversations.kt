package com.strv.chat.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.firestore.di.firestoreConversationClient
import com.strv.chat.library.ui.conversation.ConversationRecyclerView

class ConversationsActivity : AppCompatActivity() {

    val firestoreDb by lazy {
        FirebaseFirestore.getInstance()
    }

    val conversationRecyclerView by lazy {
        findViewById<ConversationRecyclerView>(R.id.rv_chat)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)

        conversationRecyclerView {
            memberProvider = object : MemberProvider {
                override fun currentUserId(): String = "user-1"

                override fun member(memberId: String): MemberModel {
                    if (memberId == "user-1") {
                        return user1()
                    } else {
                        return user2()
                    }
                }

            }
            conversationClient = firestoreConversationClient {
                firebaseDb = firestoreDb
                userId = "user-1"
            }

        }


    }


    fun user1() = object : MemberModel {
        override val userId: String = "user-1"
        override val userName: String = "John"
        override val userPhotoUrl: String = "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/ccbc2eb4-4902-40b1-9f2f-6c516964c038.jpg"

    }

    fun user2() = object : MemberModel {
        override val userId: String = "user-2"
        override val userName: String = "Camila"
        override val userPhotoUrl: String = "https://d1uzq9i69r6hkq.cloudfront.net/profile-photos/d0727450-0984-4108-993f-a6173008264d.jpg"

    }

    override fun onStart() {
        super.onStart()

        conversationRecyclerView.startObserving()
    }

    override fun onStop() {
        super.onStop()

        conversationRecyclerView.stopObserving()
    }
}
