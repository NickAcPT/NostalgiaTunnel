package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketFieldBuilder
import java.io.DataInputStream
import java.io.DataOutputStream

abstract class NostalgiaPacket(packetId: Int) : BaseNostalgiaPacket(packetId) {
    @PublishedApi
    internal val packetValues = mutableListOf<NostalgiaPacketField<*>>()

    fun value(): NostalgiaPacketFieldBuilder {
        return NostalgiaPacketFieldBuilder { packetValues.add(it) }
    }

    override fun readPacketContent(input: DataInputStream) {
        packetValues.forEach {
            it.readValue(input)
        }
    }

    override fun writePacketContent(output: DataOutputStream) {
        packetValues.forEach {
            it.writeValue(output)
        }
    }
}