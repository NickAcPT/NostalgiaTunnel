package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model

import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import java.io.DataInputStream
import java.io.DataOutputStream

data class NostalgiaItemStack(
    var itemId: Short,
    var itemDamage: Short,
    var stackSize: Byte,
    var nbt: CompoundTag? = null
) {
    companion object {
        fun readItemStack(input: DataInputStream): NostalgiaItemStack? {
            val itemId = input.readShort()
            if (itemId < 0)
                return null
            val stackSize = input.readByte()
            val itemDamage = input.readShort()

            val tag = NbtTagUtils.readTag(input)

            return NostalgiaItemStack(itemId, itemDamage, stackSize, tag)
        }
        fun writeItemStack(output: DataOutputStream, stack: NostalgiaItemStack?) {
            if (stack == null){
                output.writeShort(-1)
                return
            }
            output.writeShort(stack.itemId.toInt())
            output.writeByte(stack.stackSize.toInt())
            output.writeShort(stack.itemDamage.toInt())

            NbtTagUtils.writeTag(output, stack.nbt)
        }
    }
}
