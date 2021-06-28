package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaWindowItemsPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation
import io.github.nickacpt.nostalgiatunnel.server.translation.mapping.ItemStackTranslator

class WindowItemsPacketTranslation : InputPacketTranslation<NostalgiaWindowItemsPacket, ServerWindowItemsPacket> {
    override fun translateInput(input: NostalgiaWindowItemsPacket): ServerWindowItemsPacket {
        return ServerWindowItemsPacket(input.windowId, input.items.map {
            it?.let {
                ItemStackTranslator.translateLegacyToModern(it)
            } ?: ItemStack(0)
        }.toTypedArray())
    }

}