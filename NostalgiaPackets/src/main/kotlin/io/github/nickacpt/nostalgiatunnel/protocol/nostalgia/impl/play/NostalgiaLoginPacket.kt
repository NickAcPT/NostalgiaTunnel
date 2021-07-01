package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaGameType
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaWorldType

class NostalgiaLoginPacket : NostalgiaPacket(0x1) {
    var clientEntityId by value().int()
    var rawTerrainType by value().string(16)
    var rawGameType by value().byte()
    var dimension by value().byte()
    var difficultySetting by value().byte()
    var worldHeight by value().byte()
    var maxPlayers by value().byte()

    val isHardcoreMode
        get() = (rawGameType and 0x8) == 0x8

    val gameType
        get() = NostalgiaGameType.values().firstOrNull { it.id == (rawGameType and 0xFFFFFFF7.toInt()) } ?: NostalgiaGameType.NOT_SET

    var worldType
        get() = NostalgiaWorldType.values().firstOrNull { it.wireValue.equals(rawTerrainType, true) } ?: NostalgiaWorldType.DEFAULT
        set(value) {
            rawTerrainType = value.wireValue
        }
}