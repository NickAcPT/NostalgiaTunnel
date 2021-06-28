package io.github.nickacpt.nostalgiatunnel.server.translation.impl

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaSpawnPositionPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.InputPacketTranslation

class SpawnPositionPacketTranslation : InputPacketTranslation<NostalgiaSpawnPositionPacket, ServerSpawnPositionPacket> {
    override fun translateInput(input: NostalgiaSpawnPositionPacket): ServerSpawnPositionPacket {
        return ServerSpawnPositionPacket(Position(input.x, input.y, input.z))
    }
}