package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.chunk
import kotlin.experimental.and

class ExtendedBlockStorage(hasSkyLight: Boolean) {
    var blockLSBArray: ByteArray
    var blockMSBArray: NibbleArray? = null
    var metadataArray: NibbleArray
    var blocklightArray: NibbleArray
    var skylightArray: NibbleArray? = null

    fun getBlockId(x: Int, y: Int, z: Int): Int {
        val n: Int = (blockLSBArray[y shl 8 or (z shl 4) or x] and 0xFF.toByte()).toInt()
        return if (blockMSBArray != null) {
            blockMSBArray!![x, y, z] shl 8 or n
        } else n
    }

    fun setBlockId(x: Int, y: Int, z: Int, id: Int) {
        var n: Int = (blockLSBArray[y shl 8 or (z shl 4) or x] and 0xFF.toByte()).toInt()
        if (blockMSBArray != null) {
            n = blockMSBArray!![x, y, z] shl 8 or n
        }
        blockLSBArray[y shl 8 or (z shl 4) or x] = (id and 0xFF).toByte()
        if (id > 255) {
            if (blockMSBArray == null) {
                blockMSBArray = NibbleArray(blockLSBArray.size, 4)
            }
            blockMSBArray!![x, y, z] = id and 0xF00 shr 8
        } else if (blockMSBArray != null) {
            blockMSBArray!![x, y, z] = 0
        }
    }

    fun getBlockMetadata(x: Int, y: Int, z: Int): Int {
        return metadataArray[x, y, z]
    }

    fun setBlockMetadata(x: Int, y: Int, z: Int, value: Int) {
        metadataArray[x, y, z] = value
    }

    fun clearMSBArray() {
        blockMSBArray = null
    }

    fun createBlockMSBArray(): NibbleArray {
        blockMSBArray = NibbleArray(blockLSBArray.size, 4)
        return blockMSBArray!!
    }

    init {
        blockLSBArray = ByteArray(4096)
        metadataArray = NibbleArray(blockLSBArray.size, 4)
        blocklightArray = NibbleArray(blockLSBArray.size, 4)
        if (hasSkyLight) {
            skylightArray = NibbleArray(blockLSBArray.size, 4)
        }
    }
}