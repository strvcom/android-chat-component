package com.strv.chat.core.data.entity

/**
 * Name of the [SourceEntity.id] property for logging purposes
 */
const val ID = "id"

/**
 * Classes that inherit from this interface represents structures of server responses.
 */
interface SourceEntity {
    /**
     * An unique identifier of the entity.
     */
    var id: String?
}