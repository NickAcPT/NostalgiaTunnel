package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class BooleanAsByteNostalgiaPacketField(default: Boolean? = null) : NostalgiaPacketField<Boolean>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readByte() != 0.toByte()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeByte(if (value ?: throw Exception("Byte value is null")) 1 else 0)
    }
}