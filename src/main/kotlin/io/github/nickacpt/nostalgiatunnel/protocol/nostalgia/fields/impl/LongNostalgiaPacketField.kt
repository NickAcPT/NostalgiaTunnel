package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class LongNostalgiaPacketField(default: Long? = null) : NostalgiaPacketField<Long>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readLong()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeLong((value ?: throw Exception(".toInt() value is null")))
    }
}