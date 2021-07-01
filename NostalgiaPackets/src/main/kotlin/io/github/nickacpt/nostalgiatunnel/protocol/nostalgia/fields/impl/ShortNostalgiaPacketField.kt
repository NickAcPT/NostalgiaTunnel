package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class ShortNostalgiaPacketField(default: Short? = null) : NostalgiaPacketField<Short>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readShort()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeShort((value ?: throw Exception("Short value is null")).toInt())
    }
}

