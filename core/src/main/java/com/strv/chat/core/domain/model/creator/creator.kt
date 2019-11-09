package com.strv.chat.core.domain.model.creator

/**
 * Mapper between two representations of the same object.
 *
 * @param Data the type of the result of mapping.
 * @param Configuration additional configuration for the mapping.
 */
interface Creator<Data, Configuration: CreatorConfiguration> {

    /**
     * Converts an object of one type to an object of [Data] type.
     *
     * @return an object of [Data] type.
     */
    val create : Configuration.() -> Data
}

/**
 * Additional configuration for [Creator].
 */
interface CreatorConfiguration