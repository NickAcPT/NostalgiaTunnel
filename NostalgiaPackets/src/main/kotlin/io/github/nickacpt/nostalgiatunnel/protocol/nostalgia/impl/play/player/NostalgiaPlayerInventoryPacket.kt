package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaPlayerInventoryPacket : NostalgiaPacket(0x5) {
    var entityID by value().int()
    var slot by value().short()
    var itemStack by value().itemStack()
}