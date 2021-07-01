// To parse the JSON, install jackson-module-kotlin and do:
//
//   val legacyBlockMapping = LegacyBlockMapping.fromJson(jsonString)

package io.github.nickacpt.nostalgiatunnel.server.translation.mapping

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class LegacyBlockMapping(elements: Collection<LegacyBlockMappingElement>) : ArrayList<LegacyBlockMappingElement>(elements) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        val mapper = jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        fun fromJson(json: String) = mapper.readValue<LegacyBlockMapping>(json)
    }
}

data class LegacyBlockMappingElement (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val blockdata: String,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val legacyid: Long,

    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val legacydata: Long
)
