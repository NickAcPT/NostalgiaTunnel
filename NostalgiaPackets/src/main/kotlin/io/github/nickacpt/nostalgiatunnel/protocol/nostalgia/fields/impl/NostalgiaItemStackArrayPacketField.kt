package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import java.io.DataInputStream
import java.io.DataOutputStream

class NostalgiaItemStackArrayPacketField(default: Array<NostalgiaItemStack?>? = null) :
    NostalgiaPacketField<Array<NostalgiaItemStack?>>(default) {
    override fun readValue(input: DataInputStream) {
        val tempList = mutableListOf<NostalgiaItemStack?>()
        val size = input.readShort().toInt()
        repeat(size) {
            tempList.add(NostalgiaItemStack.readItemStack(input))
        }
        value = tempList.toTypedArray()
    }

    override fun writeValue(output: DataOutputStream) {
        val itemArray = value ?: throw Exception("ItemStack array not provided")
        output.writeShort(itemArray.size)
        itemArray.forEach {
            NostalgiaItemStack.writeItemStack(output, it)
        }
    }
}