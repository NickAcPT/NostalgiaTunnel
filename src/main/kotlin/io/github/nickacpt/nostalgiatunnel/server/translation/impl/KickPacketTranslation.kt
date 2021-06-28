package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaKickPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class KickPacketTranslation : InputPacketTranslation<NostalgiaKickPacket, ServerDisconnectPacket> {
    override fun translateInput(input: NostalgiaKickPacket): ServerDisconnectPacket {
        return ServerDisconnectPacket(LegacyComponentSerializer.legacySection().deserialize(input.message))
    }
}