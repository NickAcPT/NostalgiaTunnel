package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaSetSlotPacket : NostalgiaPacket(0x67) {
    var windowId by value().byte()
    var itemSlot by value().short()
    var itemStack by value().itemStack()
}