package io.github.nickacpt.nostalgiatunnel.server.translation

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import com.github.steveice10.packetlib.packet.Packet as ModernPacket

interface InputPacketTranslation<N : BaseNostalgiaPacket, M : ModernPacket> {
    fun translateInput(input: N): M
}

