// To parse the JSON, install jackson-module-kotlin and do:
//
//   val modernBlockMapping = ModernBlockMapping.fromJson(jsonString)

package io.github.nickacpt.nostalgiatunnel.server.translation.mapping

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class ModernBlockMapping(elements: Map<String, ModernBlockMappingValue>) : HashMap<String, ModernBlockMappingValue>(elements) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        val mapper = jacksonObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        fun fromJson(json: String) = mapper.readValue<ModernBlockMapping>(json)
    }
}

data class ModernBlockMappingValue (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val states: List<State>,

    val properties: Map<String, List<String>>? = null
)

data class State (
    @get:JsonProperty(required=true)@field:JsonProperty(required=true)
    val id: Long,

    val default: Boolean? = null,
    val properties: Map<String, String>? = null
)
