package io.github.nickacpt.nostalgiatunnel.server.translation

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import com.github.steveice10.packetlib.packet.Packet as ModernPacket

interface InputPacketTranslation<N : NostalgiaPacket, M : ModernPacket> {
    fun translateInput(input: N): M

}