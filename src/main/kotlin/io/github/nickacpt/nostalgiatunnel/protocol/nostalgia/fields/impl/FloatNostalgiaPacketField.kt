package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class FloatNostalgiaPacketField(default: Float? = null) : NostalgiaPacketField<Float>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readFloat()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeFloat(value ?: throw Exception("Float value is null"))
    }
}
