package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaPlayerInfoPacket : NostalgiaPacket(0xC9) {
    val playerName by value().string()
    val isConnected by value().booleanAsByte()
    val ping by value().short()
}