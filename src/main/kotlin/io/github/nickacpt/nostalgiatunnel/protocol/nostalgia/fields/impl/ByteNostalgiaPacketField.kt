package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class ByteNostalgiaPacketField(default: Int? = null) : NostalgiaPacketField<Int>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.read()
    }

    override fun writeValue(output: DataOutputStream) {
        output.write(value ?: throw Exception("Byte value is null"))
    }
}

