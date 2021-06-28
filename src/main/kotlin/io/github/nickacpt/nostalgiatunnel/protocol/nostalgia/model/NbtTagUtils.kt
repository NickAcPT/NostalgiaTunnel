package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model

import com.github.steveice10.opennbt.NBTIO
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream


object NbtTagUtils {

    fun readTag(inputStream: DataInputStream): CompoundTag? {
        val size = inputStream.readShort()
        if (size < 0)
            return null

        val tempArray = ByteArray(size.toInt())
        inputStream.readFully(tempArray)

        return decompressTag(tempArray)
    }

    private fun decompressTag(array: ByteArray): CompoundTag {
        return DataInputStream(BufferedInputStream(GZIPInputStream(ByteArrayInputStream(array)))).use { str ->
            NBTIO.readTag(str as InputStream) as CompoundTag
        }
    }

    private fun compressTag(tag: CompoundTag): ByteArray {
        val out = ByteArrayOutputStream()
        DataOutputStream(GZIPOutputStream(out)).use { stream ->
            NBTIO.writeTag(stream as DataOutput, tag)
        }
        return out.toByteArray()
    }

    fun writeTag(output: DataOutputStream, nbt: CompoundTag?) {
        if (nbt == null) {
            output.writeShort(-1)
            return
        }
        val compressed = compressTag(nbt)
        output.writeShort(compressed.size)
        output.write(compressed)
    }
}