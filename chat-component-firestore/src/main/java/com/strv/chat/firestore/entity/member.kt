package com.strv.chat.firestore.entity

import com.google.firebase.firestore.PropertyName
import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.data.entity.SourceEntity

internal const val NAME = "name"
internal const val PHOTO_URL = "url"

internal data class FirestoreMemberEntity(
    @get:PropertyName(ID) @set:PropertyName(ID) override var id: String? = null,
    @get:PropertyName(NAME) @set:PropertyName(NAME) var name: String? = null,
    @get:PropertyName(PHOTO_URL) @set:PropertyName(PHOTO_URL) var photoUrl: String? = null
    ) : SourceEntity {

    fun toMap(): HashMap<String, Any?> = hashMapOf(
        NAME to name,
        PHOTO_URL to photoUrl
    )
}