package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaServerAuthDataPacket : NostalgiaPacket(0xFD) {
    var serverId by value().string()
    var publicKey by value().byteArray()
    var verifyToken by value().byteArray()
}