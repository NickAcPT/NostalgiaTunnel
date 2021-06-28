package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaSpawnPositionPacket : NostalgiaPacket(0x6) {
    var x by value().int()
    var y by value().int()
    var z by value().int()
}