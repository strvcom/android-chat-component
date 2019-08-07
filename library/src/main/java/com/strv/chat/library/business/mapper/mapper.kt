package com.strv.chat.library.business.mapper

interface Mapper<Domain, Entity> {

    fun mapToDomain(entity: Entity): Domain

    fun mapToEntity(domain: Domain): Entity

}