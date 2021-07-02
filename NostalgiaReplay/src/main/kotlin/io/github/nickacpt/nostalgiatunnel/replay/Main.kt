package io.github.nickacpt.nostalgiatunnel.replay

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session.NostalgiaClientSession
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaLoginPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaGameType
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server.NostalgiaServer
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server.ServerListPingResult
import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class ReplayNostalgiaServer(port: Int) : NostalgiaServer("0.0.0.0", port) {
    override fun onServerPing(session: NostalgiaClientSession): ServerListPingResult {
        return ServerListPingResult(
            text {
                it.append(text("NostalgiaReplay", NamedTextColor.GOLD, TextDecoration.BOLD))
                it.append(space())
                it.append(text("<- Replay the past", NamedTextColor.AQUA, TextDecoration.UNDERLINED))
            }, this.clientList.size - 1, 1
        )
    }

    override fun onPlayerFinishLogin(session: NostalgiaClientSession) {
        session.sendPacket(NostalgiaLoginPacket().apply {
            clientEntityId = -1
            gameType = NostalgiaGameType.CREATIVE
            maxPlayers = 1
        })
    }

    override fun onPacketReceived(session: NostalgiaClientSession, packet: BaseNostalgiaPacket) {
    }

    override fun onServerUnknownPacketReceived(session: NostalgiaClientSession, packetId: Int) {
    }

}

fun main() {
    val server = ReplayNostalgiaServer(25566)
    server.start()
}