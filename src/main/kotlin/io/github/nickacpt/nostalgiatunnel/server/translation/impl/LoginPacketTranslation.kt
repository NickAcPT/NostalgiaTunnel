package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket
import com.github.steveice10.opennbt.tag.builtin.*
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaLoginPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaGameType
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation

class LoginPacketTranslation : InputPacketTranslation<NostalgiaLoginPacket, ServerJoinGamePacket> {

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

    override fun translateInput(input: NostalgiaLoginPacket): ServerJoinGamePacket {
        return ServerJoinGamePacket(
            input.clientEntityId,
            false,
            getModernGameMode(input),
            getModernGameMode(input),
            1, arrayOf("minecraft:world"),
            getDimensionTag(),
            getOverworldTag(),
            "minecraft:world",
            100,
            input.maxPlayers,
            16,
            false,
            false,
            false,
            false
        )

    }

    private fun getModernGameMode(input: NostalgiaLoginPacket): GameMode {
        return when (input.gameType) {
            NostalgiaGameType.NOT_SET, NostalgiaGameType.SURVIVAL -> GameMode.SURVIVAL
            NostalgiaGameType.CREATIVE -> GameMode.CREATIVE
            NostalgiaGameType.ADVENTURE -> GameMode.ADVENTURE
        }
    }

}