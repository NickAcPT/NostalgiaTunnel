package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaKeepAlivePacket : NostalgiaPacket(0x0) {
    var randomId by value().int()
}