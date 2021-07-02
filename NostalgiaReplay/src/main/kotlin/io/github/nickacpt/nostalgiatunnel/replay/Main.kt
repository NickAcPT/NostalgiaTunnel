package io.github.nickacpt.nostalgiatunnel.replay

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.NostalgiaClient
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session.NostalgiaClientSession
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server.NostalgiaServer
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server.ServerListPingResult
import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import java.lang.Thread.sleep
import kotlin.concurrent.thread

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

}

fun main() {
    val server = ReplayNostalgiaServer(25566)

    thread {
        while (!server.isRunning) {
            sleep(2)
        }
        val client = object : NostalgiaClient("localhost", 25566) {
            override fun onPacketRead(packet: BaseNostalgiaPacket) {
                println("Read packet [${packet.javaClass.simpleName}] from server")
            }

            override fun onPacketWrite(packet: BaseNostalgiaPacket) {
                println("Replied server with packet [${packet.javaClass.simpleName}]")
            }

            override fun onUnknownPacketRead(packetId: Int) {
                println("Read unknown packet [${packetId}]")
                disconnect()
            }
        }

        client.connect("NickAc")

    }
    server.start()
}