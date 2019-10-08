package com.strv.chat.firestore

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.strv.chat.firestore.entity.LAST_MESSAGE
import com.strv.chat.firestore.entity.MEMBERS
import com.strv.chat.firestore.entity.TIMESTAMP

const val CONVERSATIONS_COLLECTION = "conversations"
const val USERS_COLLECTION = "users"
const val MESSAGES_COLLECTION = "messages"

internal fun firestoreMember(
    firestoreDb: FirebaseFirestore,
    userId: String
) =
    firestoreDb
        .collection(USERS_COLLECTION)
        .document(userId)

internal fun firestoreConversations(
    firestoreDb: FirebaseFirestore,
    userId: String
) =
    firestoreDb
        .collection(CONVERSATIONS_COLLECTION)
        .whereArrayContains(MEMBERS, userId)
        .orderBy(FieldPath.of(LAST_MESSAGE, TIMESTAMP), Query.Direction.DESCENDING)

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
    startAfter: Timestamp,
    limit: Long = 50
) =
    firestoreDb
        .collection(CONVERSATIONS_COLLECTION)
        .document(conversationId)
        .collection(MESSAGES_COLLECTION)
        .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
        .startAfter(startAfter)
        .limit(limit)

