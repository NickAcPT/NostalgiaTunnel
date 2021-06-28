package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaPingPacket : NostalgiaPacket(0xFE) {
    var readProperly by value().byte(1)
}

