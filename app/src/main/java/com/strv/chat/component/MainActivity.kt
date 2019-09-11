package com.strv.chat.component

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.library.domain.provider.MemberModel
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.firestore.di.firestoreChatClient
import com.strv.chat.library.ui.chat.ChatAdapter
import com.strv.chat.library.ui.chat.ChatRecyclerView
import com.strv.chat.library.ui.chat.SendWidget

class MainActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    val firestoreDb by lazy {
        FirebaseFirestore.getInstance()
    }

    val chatRecyclerView by lazy {
        findViewById<ChatRecyclerView>(R.id.rv_chat)
    }

    val sendWidget by lazy {
        findViewById<SendWidget>(R.id.w_send)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatRecyclerView {
            adapter = ChatAdapter()
            chatClient = firestoreChatClient {
                firebaseDb = firestoreDb
                conversationId = "WWRweQVRkNBRMsxQ0UFh"
            }
            memberProvider = object : MemberProvider {
                override fun currentUserId(): String = "user-2"

                override fun member(memberId: String): MemberModel {
                    if (memberId == "user-1") {
                        return user1()
                    } else {
                        return user2()
                    }
                }

            }
        }

        sendWidget {
            chatClient = firestoreChatClient {
                firebaseDb = firestoreDb
                conversationId = "WWRweQVRkNBRMsxQ0UFh"
            }
            memberProvider = object : MemberProvider {
                override fun currentUserId(): String = "user-2"

                override fun member(memberId: String): MemberModel {
                    if (memberId == "user-1") {
                        return user1()
                    } else {
                        return user2()
                    }
                }

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

        chatRecyclerView.startObserving()
    }

    override fun onStop() {
        super.onStop()

        chatRecyclerView.stopObserving()
    }
}
