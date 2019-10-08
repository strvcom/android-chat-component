@file:JvmName("FirestoreCompositionRoot")

package com.strv.chat.firestore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.strv.chat.core.domain.client.ChatClient
import com.strv.chat.core.domain.client.ConversationClient
import com.strv.chat.core.domain.client.MemberClient
import com.strv.chat.firestore.client.FirestoreChatClient
import com.strv.chat.firestore.client.FirestoreConversationClient
import com.strv.chat.firestore.client.FirestoreMemberClient

fun firestoreChatClient(firebaseDb: FirebaseFirestore): ChatClient =
    FirestoreChatClient(firebaseDb)

fun firestoreConversationClient(firebaseDb: FirebaseFirestore): ConversationClient =
    FirestoreConversationClient(firebaseDb)

fun firestoreMemberClient(firebaseDb: FirebaseFirestore, currentUserId: String): MemberClient =
    FirestoreMemberClient(firebaseDb, currentUserId)