package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaGameEventPacket : NostalgiaPacket(0x46) {
    val eventType by value().byte()
    val gameMode by value().byte()
}