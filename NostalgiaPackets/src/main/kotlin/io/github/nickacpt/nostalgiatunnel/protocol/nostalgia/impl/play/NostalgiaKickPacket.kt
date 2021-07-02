package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaKickPacket : NostalgiaPacket(0xFF) {
    var message by value().string(256)

    private val messageSplit get() = message.split(0.toChar()).drop(1)

    val protocolVersion get() = messageSplit[0].toInt()
    val serverVersion get() = messageSplit[1]
    val motd get() = messageSplit[2]
    val currentCount get() = messageSplit[3].toInt()
    val maxCount get() = messageSplit[4].toInt()

}