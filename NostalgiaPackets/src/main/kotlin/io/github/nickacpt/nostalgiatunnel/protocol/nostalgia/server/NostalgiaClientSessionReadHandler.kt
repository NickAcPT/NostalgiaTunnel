package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaProtocol
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.CryptManager
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session.NostalgiaClientSession
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaClientProtocolPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaPingPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaServerAuthDataPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaSharedKeyPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaClientCommandPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaKickPacket
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import kotlin.random.Random

class NostalgiaClientSessionReadHandler(
    private val session: NostalgiaClientSession,
    private val server: NostalgiaServer
) {

    fun run() {
        while (session.isConnected) {
            if (session.clientSocket.isClosed) break
            val packetId = session.inputStream.read()
            if (packetId == -1) Thread.sleep(2)

            try {
                val packet = NostalgiaProtocol.readPacket(packetId, session.inputStream)
                packet?.also { internalHandleReadPacket(it) }
                if (packet != null) onPacketRead(packet) else onUnknownPacketRead(packetId)
            } catch (e: Exception) {
                println("An error occurred reading packet $packetId")
                e.printStackTrace()
                onUnknownPacketRead(packetId)
            }
        }
        println("[Server] Reading thread died")
        server.removeSession(session)
    }

    private fun internalHandleReadPacket(packet: BaseNostalgiaPacket) {
        println("Received packet [${packet.javaClass.simpleName}] from client ")
        when (packet) {
            // Ping
            is NostalgiaPingPacket -> {
                val result = server.onServerPing(session)
                session.sendPacket(NostalgiaKickPacket(
                    NostalgiaProtocol.protocolVersion,
                    "1.5.2",
                    LegacyComponentSerializer.legacySection().serialize(result.motd),
                    result.currentPlayerCount,
                    result.maxPlayerCount
                ), blocking = true)
                session.disconnect()
            }
            // Client is trying to join
            is NostalgiaClientProtocolPacket -> {
                // Start Encryption
                session.sendPacket(NostalgiaServerAuthDataPacket().apply {
                    serverId = "-"
                    publicKey = server.keyPair.public.encoded
                    verifyToken = Random.Default.nextBytes(4).also { session.verifyToken = it }
                })
            }
            // Client is encrypting the connection
            is NostalgiaSharedKeyPacket -> {
                // Finish Encryption
                session.sharedKey = packet.getSharedKey(server.keyPair.private)

                val isSameVerifyToken = CryptManager.decryptData(server.keyPair.private, packet.verifyToken).contentEquals(session.verifyToken)
                println("Got verify token from client. Is same: $isSameVerifyToken")

                session.sendPacket(NostalgiaSharedKeyPacket(), true)

                session.decryptInputStream()
                session.encryptOutputStream()
            }
            is NostalgiaClientCommandPacket -> {
                if (packet.isRespawn) return
                server.onPlayerFinishLogin(session)
            }
        }
    }

    private fun onPacketRead(packet: BaseNostalgiaPacket) {
        server.onPacketReceived(session, packet)
    }

    private fun onUnknownPacketRead(packetId: Int) {
        println("Read unknown packet [${packetId}] from client")
        server.onServerUnknownPacketReceived(session, packetId)
    }
}