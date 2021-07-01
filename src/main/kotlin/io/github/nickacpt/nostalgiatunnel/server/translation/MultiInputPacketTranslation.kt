package io.github.nickacpt.nostalgiatunnel.server.translation

import com.github.steveice10.packetlib.packet.Packet
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket

interface MultiInputPacketTranslation<N : BaseNostalgiaPacket, M : Packet> {
    fun translateInput(input: N): List<M>
}