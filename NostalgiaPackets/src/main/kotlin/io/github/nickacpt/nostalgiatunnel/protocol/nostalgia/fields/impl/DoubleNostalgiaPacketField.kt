package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class DoubleNostalgiaPacketField(default: Double? = null) : NostalgiaPacketField<Double>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readDouble()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeDouble(value ?: throw Exception("Double value is null"))
    }
}