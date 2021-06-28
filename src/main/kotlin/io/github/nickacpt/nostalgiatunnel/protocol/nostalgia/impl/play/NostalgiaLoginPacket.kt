package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.WorldType

class NostalgiaLoginPacket : NostalgiaPacket(0x1) {
    var clientEntityId by value().int()
    var rawTerrainType by value().string(16)
    var rawGameType by value().byte()
    var dimension by value().byte()
    var difficultySetting by value().byte()
    var worldHeight by value().byte()
    var maxPlayers by value().byte()

    var worldType
        get() = WorldType.values().firstOrNull { it.wireValue.equals(rawTerrainType, true) } ?: WorldType.DEFAULT
        set(value) {
            rawTerrainType = value.wireValue
        }
}