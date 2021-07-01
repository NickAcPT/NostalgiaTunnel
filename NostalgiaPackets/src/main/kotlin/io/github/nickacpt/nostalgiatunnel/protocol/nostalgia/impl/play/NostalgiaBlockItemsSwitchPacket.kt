package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaBlockItemsSwitchPacket : NostalgiaPacket(0x10) {
    val id by value().short()
}