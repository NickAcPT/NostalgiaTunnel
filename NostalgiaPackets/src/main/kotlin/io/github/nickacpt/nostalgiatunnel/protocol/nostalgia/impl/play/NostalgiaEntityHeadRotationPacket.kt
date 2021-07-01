package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaEntityHeadRotationPacket : NostalgiaPacket(0x23) {
    var entityId by value().int()
    var headRotationYaw by value().byte()
}