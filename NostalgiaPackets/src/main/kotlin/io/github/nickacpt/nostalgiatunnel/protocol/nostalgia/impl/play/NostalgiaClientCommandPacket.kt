package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import kotlin.experimental.and

class NostalgiaClientCommandPacket : NostalgiaPacket(0xCD) {
    var rawIsRespawn by value().customWrite(
        0,
        fieldGetter = { it.byte() },
        onWrite = { t, stream -> stream.writeByte((t.toByte() and 0xFF.toByte()).toInt()) })

    val isRespawn
        get() = rawIsRespawn == 1
}