package io.github.nickacpt.nostalgiatunnel.server.translation.mapping

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class RegistryMapping(var legacyId: Long, var legacyData: Long, var modernId: Long, var name: String) {
    companion object {
        private val mapper = jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }

        fun fromJson(json: String) = RegistryMapping.mapper.readValue<List<RegistryMapping>>(json)
    }
}
