package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player.NostalgiaPlayerInventoryPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation
import io.github.nickacpt.nostalgiatunnel.server.translation.mapping.ItemStackTranslator

class PlayerInventoryPacketTranslation : InputPacketTranslation<NostalgiaPlayerInventoryPacket, ServerSetSlotPacket> {
    override fun translateInput(input: NostalgiaPlayerInventoryPacket): ServerSetSlotPacket {
        //TODO: Check entity ID
        return ServerSetSlotPacket(0, input.slot.toInt(), input.itemStack?.let { ItemStackTranslator.translateLegacyToModern(it) })
    }
}