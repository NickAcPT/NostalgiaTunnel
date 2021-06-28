package io.github.nickacpt.nostalgiatunnel

import com.github.steveice10.mc.auth.data.GameProfile
import com.github.steveice10.mc.protocol.MinecraftConstants
import com.github.steveice10.mc.protocol.MinecraftProtocol
import com.github.steveice10.mc.protocol.ServerLoginHandler
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk
import com.github.steveice10.mc.protocol.data.game.chunk.Column
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord
import com.github.steveice10.mc.protocol.data.status.PlayerInfo
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo
import com.github.steveice10.mc.protocol.data.status.VersionInfo
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket
import com.github.steveice10.opennbt.tag.builtin.*
import com.github.steveice10.packetlib.Server
import com.github.steveice10.packetlib.Session
import com.github.steveice10.packetlib.tcp.TcpServer
import net.kyori.adventure.text.Component


fun main() {
    val server: Server = TcpServer("0.0.0.0", 25566, MinecraftProtocol::class.java)
    server.setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, false)
    server.setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, ServerInfoBuilder { session: Session? ->
        ServerStatusInfo(
            VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION),
            PlayerInfo(100, 0, Array<GameProfile>(0) { null!! }),
            Component.text("Hello world!"),
            null
        )
    }
    )

    server.setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, ServerLoginHandler { session: Session ->
        session.send(
            ServerJoinGamePacket(
                0,
                false,
                GameMode.CREATIVE,
                GameMode.CREATIVE,
                1, arrayOf("minecraft:world"),
                getDimensionTag(),
                getOverworldTag(),
                "minecraft:world",
                100,
                0,
                16,
                false,
                false,
                false,
                false
            )
        )
        session.send(ServerPlayerPositionRotationPacket(0.0, 100.0, 0.0, 0f, 0f, 0))
        // Send chunk data
        val chunk = Chunk()


        for (x in -1..1) {
            for (z in -1..1) {
                session.send(
                    (
                            ServerChunkDataPacket(
                                Column(
                                    x, z, arrayOf(
                                        chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk,
                                        chunk, chunk, chunk, chunk, chunk, chunk, chunk, chunk
                                    ), arrayOf(
                                        CompoundTag("TileEntity")
                                    ), CompoundTag("HeightMaps"), IntArray(1024)
                                )
                            ))
                )
            }
        }

        //Stone block below player

        session.send(ServerBlockChangePacket(BlockChangeRecord(Position(0, 99, 0), 1)))

        //Send spawn position to player
        session.send(ServerSpawnPositionPacket(Position(0, 100, 0)))

        session.send(ServerPlayerPositionRotationPacket(0.5, 100.0, 0.5, 90F, 0F, 1))

    })

    server.bind()
}


private fun getDimensionTag(): CompoundTag {
    val tag = CompoundTag("")
    val dimensionTypes = CompoundTag("minecraft:dimension_type")
    dimensionTypes.put(StringTag("type", "minecraft:dimension_type"))
    val dimensionTag = ListTag("value")
    val overworldTag = convertToValue("minecraft:overworld", 0, getOverworldTag().value)
    dimensionTag.add(overworldTag)
    dimensionTypes.put(dimensionTag)
    tag.put(dimensionTypes)
    val biomeTypes = CompoundTag("minecraft:worldgen/biome")
    biomeTypes.put(StringTag("type", "minecraft:worldgen/biome"))
    val biomeTag = ListTag("value")
    val plainsTag = convertToValue("minecraft:plains", 0, getPlainsTag().value)
    biomeTag.add(plainsTag)
    biomeTypes.put(biomeTag)
    tag.put(biomeTypes)
    return tag
}

private fun getOverworldTag(): CompoundTag {
    val overworldTag = CompoundTag("")
    overworldTag.put(StringTag("name", "minecraft:overworld"))
    overworldTag.put(ByteTag("piglin_safe", 0.toByte()))
    overworldTag.put(ByteTag("natural", 1.toByte()))
    overworldTag.put(FloatTag("ambient_light", 0f))
    overworldTag.put(StringTag("infiniburn", "minecraft:infiniburn_overworld"))
    overworldTag.put(ByteTag("respawn_anchor_works", 0.toByte()))
    overworldTag.put(ByteTag("has_skylight", 1.toByte()))
    overworldTag.put(ByteTag("bed_works", 1.toByte()))
    overworldTag.put(StringTag("effects", "minecraft:overworld"))
    overworldTag.put(ByteTag("has_raids", 1.toByte()))
    overworldTag.put(IntTag("logical_height", 256))
    overworldTag.put(FloatTag("coordinate_scale", 1f))
    overworldTag.put(ByteTag("ultrawarm", 0.toByte()))
    overworldTag.put(ByteTag("has_ceiling", 0.toByte()))
    return overworldTag
}

private fun getPlainsTag(): CompoundTag {
    val plainsTag = CompoundTag("")
    plainsTag.put(StringTag("name", "minecraft:plains"))
    plainsTag.put(StringTag("precipitation", "rain"))
    plainsTag.put(FloatTag("depth", 0.125f))
    plainsTag.put(FloatTag("temperature", 0.8f))
    plainsTag.put(FloatTag("scale", 0.05f))
    plainsTag.put(FloatTag("downfall", 0.4f))
    plainsTag.put(StringTag("category", "plains"))
    val effects = CompoundTag("effects")
    effects.put(LongTag("sky_color", 7907327))
    effects.put(LongTag("water_fog_color", 329011))
    effects.put(LongTag("fog_color", 12638463))
    effects.put(LongTag("water_color", 4159204))
    val moodSound = CompoundTag("mood_sound")
    moodSound.put(IntTag("tick_delay", 6000))
    moodSound.put(FloatTag("offset", 2.0f))
    moodSound.put(StringTag("sound", "minecraft:ambient.cave"))
    moodSound.put(IntTag("block_search_extent", 8))
    effects.put(moodSound)
    plainsTag.put(effects)
    return plainsTag
}

private fun convertToValue(name: String, id: Int, values: Map<String, Tag>): CompoundTag {
    val tag = CompoundTag(name)
    tag.put(StringTag("name", name))
    tag.put(IntTag("id", id))
    val element = CompoundTag("element")
    element.value = values
    tag.put(element)
    return tag
}