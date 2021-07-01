package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaPlayerAbilitiesPacket : NostalgiaPacket(0xCA) {
    var rawAbilities by value().byte()
    var flySpeed by value().byte()
    var walkSpeed by value().byte()
}