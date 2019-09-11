package com.strv.chat.library.data.model

interface Mapper<Entity, Domain> {

    fun mapToDomain(entity: Entity): Domain

    fun mapToEntity(domain: Domain): Entity

}