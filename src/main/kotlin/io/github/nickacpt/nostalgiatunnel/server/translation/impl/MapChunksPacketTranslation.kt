package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.data.game.chunk.Chunk
import com.github.steveice10.mc.protocol.data.game.chunk.Column
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket
import com.github.steveice10.opennbt.tag.builtin.CompoundTag
import com.github.steveice10.packetlib.packet.Packet
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaMapChunksPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.chunk.NostalgiaChunk
import io.github.nickacpt.nostalgiatunnel.server.translation.MultiInputPacketTranslation
import io.github.nickacpt.nostalgiatunnel.server.translation.mapping.ItemStackTranslator


class MapChunksPacketTranslation : MultiInputPacketTranslation<NostalgiaMapChunksPacket, Packet> {
    override fun translateInput(input: NostalgiaMapChunksPacket): List<Packet> {
        return input.chunkPosX.indices.map { i ->

            val x = input.chunkPosX[i]
            val z = input.chunkPosZ[i]

            val nostalgiaChunk = NostalgiaChunk()
            nostalgiaChunk.fillChunk(input.decompressedData[i]!!, input.primary[i], input.add[i], true, input.hasSkyLight)
            val column =
                Column(
                    x,
                    z,
                    (nostalgiaChunk.sectionsToChunkArray() + arrayOfNulls<Chunk>(16 - nostalgiaChunk.storageArrays.size).map {
                        Chunk()
                    }),
                    arrayOf(
                        CompoundTag("TileEntity")
                    ),
                    CompoundTag("HeightMaps"),
                    IntArray(1024)
                )
            ServerChunkDataPacket(column)
        }/* + ServerSpawnPositionPacket(Position(109, 74, 216)) + ServerPlayerPositionRotationPacket(
            109.0,
            74.0,
            216.0,
            0F,
            0F,
            1
        )*/
    }
}

private fun NostalgiaChunk.sectionsToChunkArray(): Array<Chunk> {
    return this.storageArrays.map {
        val resultChunk = Chunk()
        if (it != null) {
            for (y in 0 until 16) {
                for (z in 0 until 16) {
                    for (x in 0 until 16) {
                        val blockId = it.getBlockId(x, y, z)
                        val blockMetadata = it.getBlockMetadata(x, y, z)
                        resultChunk.set(
                            x, y, z,
                            (ItemStackTranslator.getBlockModernIdForLegacyId(blockId.toLong(), blockMetadata.toLong())).toInt()
                        )
                    }
                }
            }
        }
        resultChunk
    }.toTypedArray()
}
