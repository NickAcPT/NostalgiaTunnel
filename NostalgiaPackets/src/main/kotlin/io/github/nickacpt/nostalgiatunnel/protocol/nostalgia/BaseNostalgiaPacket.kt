package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia

import java.io.DataInputStream
import java.io.DataOutputStream

abstract class BaseNostalgiaPacket(val packetId: Int) {

    abstract fun readPacketContent(input: DataInputStream)

    abstract fun writePacketContent(output: DataOutputStream)

    fun readPacket(input: DataInputStream, readId: Boolean = false) {
        if (readId) input.read()
        readPacketContent(input)
    }

    fun writePacket(output: DataOutputStream) {
        output.write(packetId)
        writePacketContent(output)
    }

}