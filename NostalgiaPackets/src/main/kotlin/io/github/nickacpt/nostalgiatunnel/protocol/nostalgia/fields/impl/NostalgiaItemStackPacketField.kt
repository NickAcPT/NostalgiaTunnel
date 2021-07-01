package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NullableNostalgiaPacketField
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import java.io.DataInputStream
import java.io.DataOutputStream

class NostalgiaItemStackPacketField(default: NostalgiaItemStack? = null) :
    NullableNostalgiaPacketField<NostalgiaItemStack>(default) {
    override fun readValue(input: DataInputStream) {
        value = NostalgiaItemStack.readItemStack(input)
    }

    override fun writeValue(output: DataOutputStream) {
        NostalgiaItemStack.writeItemStack(output, value)
    }
}
