package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.world

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.chunk.NostalgiaChunk
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import java.util.zip.DataFormatException

import java.util.zip.Inflater

class NostalgiaMapChunksPacket : BaseNostalgiaPacket(0x38) {

    lateinit var chunkPosX: IntArray
    lateinit var chunkPosZ: IntArray
    lateinit var primary: IntArray
    lateinit var add: IntArray
    lateinit var chunkDataBuffer: ByteArray
    lateinit var decompressedData: Array<ByteArray?>
    var dataLength = 0
    var hasSkyLight = false
    var compressedData = ByteArray(0)

    val chunks: List<NostalgiaChunk>
        get() {
            return chunkPosX.indices.map { i ->
                NostalgiaChunk().apply { fillChunk(decompressedData[i], primary[i], add[i], true, hasSkyLight) }
            }
        }

    override fun readPacketContent(input: DataInputStream) {
        val chunksCount: Int = input.readShort().toInt()
        dataLength = input.readInt()
        hasSkyLight = input.readBoolean()
        chunkPosX = IntArray(chunksCount)
        chunkPosZ = IntArray(chunksCount)
        primary = IntArray(chunksCount)
        add = IntArray(chunksCount)
        decompressedData = arrayOfNulls(chunksCount)
        if (compressedData.size < dataLength) {
            compressedData = ByteArray(dataLength)
        }
        input.readFully(compressedData, 0, dataLength)
        val byArray = ByteArray(196864 * chunksCount)
        val inflater = Inflater()
        inflater.setInput(compressedData, 0, dataLength)
        try {
            inflater.inflate(byArray)
        } catch (dataFormatException: DataFormatException) {
            throw IOException("Bad compressed data format")
        } finally {
            inflater.end()
        }
        var compressedOffset = 0
        for (i in 0 until chunksCount) {
            chunkPosX[i] = input.readInt()
            chunkPosZ[i] = input.readInt()
            primary[i] = input.readShort().toInt()
            add[i] = input.readShort().toInt()
            var primarySize = 0
            var addSize = 0
            var decompressedDataSize = 0
            while (decompressedDataSize < 16) {
                primarySize += primary[i] shr decompressedDataSize and 1
                addSize += add[i] shr decompressedDataSize and 1
                ++decompressedDataSize
            }
            decompressedDataSize = 2048 * (4 * primarySize) + 256
            decompressedDataSize += 2048 * addSize
            if (hasSkyLight) {
                decompressedDataSize += 2048 * primarySize
            }
            decompressedData[i] = ByteArray(decompressedDataSize)
            System.arraycopy(byArray, compressedOffset, decompressedData[i]!!, 0, decompressedDataSize)
            compressedOffset += decompressedDataSize
        }
    }

    override fun writePacketContent(output: DataOutputStream) {
        output.writeShort(chunkPosX.size)
        output.writeInt(dataLength)
        output.writeBoolean(hasSkyLight)
        output.write(chunkDataBuffer, 0, dataLength)
        for (i in chunkPosX.indices) {
            output.writeInt(chunkPosX[i])
            output.writeInt(chunkPosZ[i])
            output.writeShort((primary[i] and 0xFFFF).toShort().toInt())
            output.writeShort((add[i] and 0xFFFF).toShort().toInt())
        }
    }

}