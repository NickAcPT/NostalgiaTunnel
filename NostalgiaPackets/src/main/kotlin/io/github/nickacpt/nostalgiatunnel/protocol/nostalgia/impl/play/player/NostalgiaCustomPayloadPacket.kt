package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

open class NostalgiaCustomPayloadPacket : NostalgiaPacket(0xFA) {
    var channel by value().string()
    var data by value().byteArray()
}