package com.strv.chat.library.data.mapper

interface Mapper<Entity, Model> {

    fun mapToDomain(entity: Entity): Model

    fun mapToEntity(domain: Model): Entity

}