package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaWindowItemsPacket : NostalgiaPacket(0x68) {
    val windowId by value().byte()
    val items by value().itemStackArray()
}