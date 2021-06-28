package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import java.io.DataInputStream
import java.io.DataOutputStream

class NostalgiaItemStackPacketField(default: NostalgiaItemStack? = null) :
    NostalgiaPacketField<NostalgiaItemStack?>(default) {
    override fun readValue(input: DataInputStream) {
        value = NostalgiaItemStack.readItemStack(input)
    }

    override fun writeValue(output: DataOutputStream) {
        NostalgiaItemStack.writeItemStack(output, value)
    }
}
