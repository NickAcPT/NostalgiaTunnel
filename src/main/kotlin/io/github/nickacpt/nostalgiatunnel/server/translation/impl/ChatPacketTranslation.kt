package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaChatPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class ChatPacketTranslation : InputPacketTranslation<NostalgiaChatPacket, ServerChatPacket> {
    override fun translateInput(input: NostalgiaChatPacket): ServerChatPacket {
        return ServerChatPacket(LegacyComponentSerializer.legacySection().deserialize(input.message))
    }

}