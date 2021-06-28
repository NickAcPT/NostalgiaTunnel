package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream

class IntNostalgiaPacketField(default: Int? = null) : NostalgiaPacketField<Int>(default) {
    override fun readValue(input: DataInputStream) {
        value = input.readInt()
    }

    override fun writeValue(output: DataOutputStream) {
        output.writeInt(value ?: throw Exception("Int value is not defined"))
    }

}
