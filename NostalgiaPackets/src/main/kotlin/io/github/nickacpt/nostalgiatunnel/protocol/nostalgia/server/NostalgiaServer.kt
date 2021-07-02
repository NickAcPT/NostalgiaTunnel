package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.CryptManager
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session.NostalgiaClientSession
import java.net.InetSocketAddress
import java.nio.channels.ServerSocketChannel
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.thread

abstract class NostalgiaServer(private val address: String, private val port: Int) {

    var keyPair = CryptManager.generateKeyPair()
    var socketChannel: ServerSocketChannel? = null
    var isRunning = false

    fun start() {
        isRunning = true
        socketChannel = ServerSocketChannel.open().also { it.bind(InetSocketAddress(address, port)) }
        startListening()
    }

    var clientList = ConcurrentLinkedQueue<NostalgiaClientSession>()
    private fun startListening() {
        while (isRunning) {
            val clientSocket = socketChannel!!.socket().accept()
            // Received client - create session.
            val session = NostalgiaClientSession(clientSocket)

            // Add to client list
            clientList.add(session)

            // Handle new session
            handleNewSession(session)
        }
    }

    abstract fun onServerPing(session: NostalgiaClientSession): ServerListPingResult

    abstract fun onPlayerFinishLogin(session: NostalgiaClientSession)

    abstract fun onPacketReceived(session: NostalgiaClientSession, packet: BaseNostalgiaPacket)

    private fun handleNewSession(session: NostalgiaClientSession) {
        thread { NostalgiaClientSessionReadHandler(session, this).run() }
    }

    abstract fun onServerUnknownPacketReceived(session: NostalgiaClientSession, packetId: Int)

    fun removeSession(session: NostalgiaClientSession) {
        clientList.remove(session)
    }
}