package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaGameType
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaWorldType

class NostalgiaLoginPacket : NostalgiaPacket(0x1) {
    var clientEntityId by value().int(-1)
    var rawTerrainType by value().string(16, "default")
    var rawGameType by value().byte(0)
    var dimension by value().byte(0)
    var difficultySetting by value().byte(0)
    var worldHeight by value().byte(Byte.MAX_VALUE * 2)
    var maxPlayers by value().byte(0)

    var isHardcoreMode
        get() = (rawGameType and 0x8) == 0x8
        set(value) {
            rawGameType = computeGameType(gameType, value)
        }

    var gameType
        get() = NostalgiaGameType.values().firstOrNull { it.id == (rawGameType and 0xFFFFFFF7.toInt()) }
            ?: NostalgiaGameType.NOT_SET
        set(value) {
            rawGameType = computeGameType(value, isHardcoreMode)
        }

    private fun computeGameType(gameType: NostalgiaGameType, isHardcoreMode: Boolean): Int {
        return gameType.id.let { if (isHardcoreMode) it or 8 else it }
    }

    var worldType
        get() = NostalgiaWorldType.values().firstOrNull { it.wireValue.equals(rawTerrainType, true) }
            ?: NostalgiaWorldType.DEFAULT
        set(value) {
            rawTerrainType = value.wireValue
        }
}