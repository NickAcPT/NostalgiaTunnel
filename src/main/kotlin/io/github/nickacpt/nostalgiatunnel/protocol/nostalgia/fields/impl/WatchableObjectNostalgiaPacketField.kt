package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.ChunkCoordinates
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.metadata.WatchableObject
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.metadata.WatchableObjectType
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Float.valueOf
import kotlin.experimental.and

class WatchableObjectNostalgiaPacketField(default: List<WatchableObject>? = null) : NostalgiaPacketField<List<WatchableObject>>(default) {
    override fun readValue(input: DataInputStream) {
        val arrayList: ArrayList<WatchableObject> = ArrayList()
        var metadataValue: Byte = input.readByte()
        while (metadataValue.toInt() != 127) {
            val type: WatchableObjectType = WatchableObjectType.values()[metadataValue.toInt() and 0xE0 shr 5]
            val dataValueId: Int = (metadataValue and 0x1F).toInt()
            var result: WatchableObject? = null
            when (type) {
                WatchableObjectType.BYTE -> {
                    result = WatchableObject(type, dataValueId, input.readByte())
                }
                WatchableObjectType.SHORT -> {
                    result = WatchableObject(type, dataValueId, input.readShort())
                }
                WatchableObjectType.INTEGER -> {
                    result = WatchableObject(type, dataValueId, input.readInt())
                }
                WatchableObjectType.FLOAT -> {
                    result = WatchableObject(type, dataValueId, valueOf(input.readFloat()))
                }
                WatchableObjectType.STRING -> {
                    result = WatchableObject(type, dataValueId, StringNostalgiaPacketField.readString(input))
                }
                WatchableObjectType.ITEM_STACK -> {
                    result = WatchableObject(
                        type,
                        dataValueId,
                        NostalgiaItemStack.readItemStack(input) ?: NostalgiaItemStack(0, 0, 0)
                    )
                }
                WatchableObjectType.CHUNK_COORDINATES -> {
                    val x: Int = input.readInt()
                    val y: Int = input.readInt()
                    val z: Int = input.readInt()
                    result = WatchableObject(type, dataValueId, ChunkCoordinates(x, y, z))
                }
            }
            arrayList.add(result)
            metadataValue = input.readByte()
        }

        value = arrayList
    }

    override fun writeValue(output: DataOutputStream) {
        value!!.forEach { watchableObject ->
            val result: Int = watchableObject.type.ordinal shl 5 or watchableObject.dataValueId and 0x1F and 0xFF
            output.writeByte(result)
            when (watchableObject.type) {
                WatchableObjectType.BYTE -> {
                    output.writeByte((watchableObject.watchedObject as Byte).toByte().toInt())
                }
                WatchableObjectType.SHORT -> {
                    output.writeShort((watchableObject.watchedObject as Short).toShort().toInt())
                }
                WatchableObjectType.INTEGER -> {
                    output.writeInt(watchableObject.watchedObject as Int)
                }
                WatchableObjectType.FLOAT -> {
                    output.writeFloat((watchableObject.watchedObject as Float).toFloat())
                }
                WatchableObjectType.STRING -> {
                    StringNostalgiaPacketField.writeString(output, watchableObject.watchedObject as String)
                }
                WatchableObjectType.ITEM_STACK -> {
                    NostalgiaItemStack.writeItemStack(output, watchableObject.watchedObject as NostalgiaItemStack)
                }
                WatchableObjectType.CHUNK_COORDINATES -> {
                    val chunkCoordinates = watchableObject.watchedObject as ChunkCoordinates
                    output.writeInt(chunkCoordinates.x)
                    output.writeInt(chunkCoordinates.y)
                    output.writeInt(chunkCoordinates.z)
                }
            }
        }
        output.writeByte(127)
    }
}
