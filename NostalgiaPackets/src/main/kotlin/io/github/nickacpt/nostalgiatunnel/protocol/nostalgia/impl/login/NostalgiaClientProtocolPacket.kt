package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaClientProtocolPacket : NostalgiaPacket(2) {
    var protocolVersion by value().byte()
    var userName by value().string()
    var serverHost by value().string()
    var serverPort by value().int()
}