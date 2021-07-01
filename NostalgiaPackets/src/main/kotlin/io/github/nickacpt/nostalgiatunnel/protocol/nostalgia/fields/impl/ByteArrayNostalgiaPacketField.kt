package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class ByteArrayNostalgiaPacketField(default: ByteArray? = null) : NostalgiaPacketField<ByteArray>(default) {
    override fun readValue(input: DataInputStream) {
        val length = input.readShort()
        val result = ByteArray(length.toInt())
        input.readFully(result)
        value = result
    }

    override fun writeValue(output: DataOutputStream) {
        val bytes = value ?: throw Exception("ByteArray value is null")
        output.writeShort(bytes.size)
        output.write(bytes)
    }
}
