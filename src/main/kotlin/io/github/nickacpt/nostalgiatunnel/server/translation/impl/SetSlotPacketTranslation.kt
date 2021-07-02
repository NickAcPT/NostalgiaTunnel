package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player.NostalgiaSetSlotPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation
import io.github.nickacpt.nostalgiatunnel.server.translation.mapping.ItemStackTranslator

class SetSlotPacketTranslation : InputPacketTranslation<NostalgiaSetSlotPacket, ServerSetSlotPacket> {
    override fun translateInput(input: NostalgiaSetSlotPacket): ServerSetSlotPacket {
        return ServerSetSlotPacket(input.windowId, input.itemSlot.toInt(),
            input.itemStack?.let { ItemStackTranslator.translateLegacyToModern(it) })
    }
}