package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaMobSpawnPacket : NostalgiaPacket(0x18) {
    var entityId by value().int()
    var type by value().byte()
    var xPosition by value().int()
    var yPosition by value().int()
    var zPosition by value().int()
    var yaw by value().byte()
    var pitch by value().byte()
    var headYaw by value().byte()
    var velocityX by value().short()
    var velocityY by value().short()
    var velocityZ by value().short()
    var metadata by value().watchableObjectList()
}