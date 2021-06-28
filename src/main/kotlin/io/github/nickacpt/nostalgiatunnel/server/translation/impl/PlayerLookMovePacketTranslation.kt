package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaPlayerLookMovePacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation

class PlayerLookMovePacketTranslation : InputPacketTranslation<NostalgiaPlayerLookMovePacket, ServerPlayerPositionRotationPacket> {
    override fun translateInput(input: NostalgiaPlayerLookMovePacket): ServerPlayerPositionRotationPacket {
        return ServerPlayerPositionRotationPacket(
            input.xPosition,
            input.yPosition,
            input.zPosition,
            input.yaw,
            input.pitch,
            0
        )
    }
}