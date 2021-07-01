package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaSharedKeyPacket : NostalgiaPacket(0xFC) {
    var sharedSecret by value().byteArray()
    var verifyToken by value().byteArray()
}