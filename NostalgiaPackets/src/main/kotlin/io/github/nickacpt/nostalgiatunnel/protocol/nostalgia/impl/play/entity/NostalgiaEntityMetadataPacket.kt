package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.entity

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaEntityMetadataPacket : NostalgiaPacket(0x28) {
    var entityId by value().int()
    var metadata by value().watchableObjectList()
}