package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaUpdateTimePacket : NostalgiaPacket(0x4) {
    val worldAge by value().long()
    val time by value().long()
}