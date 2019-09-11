package com.strv.chat.library.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.library.firestore.entity.TIMESTAMP

const val CONVERSATIONS_COLLECTION = "conversations"
const val MESSAGES_COLLECTION = "messages"
const val SEEN_COLLECTION = "seen"

internal fun firestoreChatMessages(
    firestoreDb: FirebaseFirestore,
    conversationId: String,
    limit: Long = 50
) =
    firestoreDb
        .collection(CONVERSATIONS_COLLECTION)
        .document(conversationId)
        .collection(MESSAGES_COLLECTION)
        .limit(limit)
        .orderBy(TIMESTAMP, Query.Direction.DESCENDING)

internal fun firestoreChatMessages(
    firestoreDb: FirebaseFirestore,
    conversationId: String,
    limit: Long = 50,
    startAfter: DocumentSnapshot
) =
    firestoreDb
        .collection(CONVERSATIONS_COLLECTION)
        .document(conversationId)
        .collection(MESSAGES_COLLECTION)
        .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
        .startAfter(startAfter)
        .limit(limit)

