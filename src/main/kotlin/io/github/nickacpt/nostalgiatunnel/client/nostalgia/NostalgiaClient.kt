package io.github.nickacpt.nostalgiatunnel.client.nostalgia

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaProtocol
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.NostalgiaPingPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaClientProtocolPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaServerAuthDataPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaSharedKeyPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaClientCommandPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaKeepAlivePacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaKickPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaLoginPacket
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Thread.sleep
import java.net.InetSocketAddress
import java.nio.channels.SocketChannel
import java.security.PublicKey
import java.util.concurrent.ForkJoinPool
import javax.crypto.SecretKey
import kotlin.concurrent.thread


abstract class NostalgiaClient(private val address: String, private val port: Int) {
    private val writingPool = ForkJoinPool(1)
    var socketChannel: SocketChannel? = null
    var inputStream: DataInputStream? = null
    var outputStream: DataOutputStream? = null

    var publicKey: PublicKey? = null
    var secretKey: SecretKey? = null
    var isInputDecrypted = false
    var isOutputEncrypted = false

    fun connect(userName: String) {
        openConnection()
        writePacket(NostalgiaClientProtocolPacket().apply {
            protocolVersion = NostalgiaProtocol.protocolVersion
            this.userName = userName
            serverHost = address
            serverPort = port
        })
    }

    fun openConnection() {
        socketChannel = SocketChannel.open(InetSocketAddress(address, port))
        inputStream = DataInputStream(socketChannel!!.socket().getInputStream())
        outputStream = DataOutputStream(socketChannel!!.socket().getOutputStream())
        startReadingThread()
    }

    private var pingResult: NostalgiaKickPacket? = null
    var isPing = false
    fun pingServer(): NostalgiaKickPacket {
        isPing = true
        openConnection()
        writePacket(NostalgiaPingPacket())
        while (pingResult == null)
            sleep(2)
        disconnect()
        return pingResult!!
    }

    fun writePacket(packet: NostalgiaPacket) {
        writingPool.execute {
            packet.writePacket(outputStream!!)
            onPacketWrite(packet)
            outputStream!!.flush()

            if (packet is NostalgiaSharedKeyPacket) {
                println("Packet is shared key. START ENCRYPTING")
                encryptOutputStream()
                println("Packet is shared key. END ENCRYPTING")
            }
        }
    }

    var readingThread: Thread? = null
    private fun startReadingThread() {
        readingThread = thread {
            while (socketChannel!!.isOpen) {
                if (socketChannel?.socket()?.isClosed == true) break
                val packetId = inputStream!!.read()
                if (packetId == -1) sleep(2)
                try {
                    val packet = NostalgiaProtocol.readPacket(packetId, inputStream!!)
                    packet?.also { internalHandleReadPacket(it) }
                    if (packet != null) onPacketRead(packet) else onUnknownPacketRead(packetId)
                } catch (e: Exception) {
                    println("An error occurred reading packet $packetId")
                    onUnknownPacketRead(packetId)
                }
            }
        }
    }

    private fun internalHandleReadPacket(packet: NostalgiaPacket) {
        if (isPing) {
            if (packet is NostalgiaKickPacket) {
                pingResult = packet
            }
            return
        }
        when (packet) {
            is NostalgiaServerAuthDataPacket -> {
                handleAuthData(packet)
            }
            is NostalgiaSharedKeyPacket -> {
                decryptInputStream()
                writePacket(NostalgiaClientCommandPacket())
            }
            is NostalgiaKeepAlivePacket -> {
                writePacket(packet)
            }
            is NostalgiaLoginPacket -> {
                println("User has logged in.")
            }
        }
    }

    open fun onPacketRead(packet: NostalgiaPacket) {}

    open fun onPacketWrite(packet: NostalgiaPacket) {}

    open fun onUnknownPacketRead(packetId: Int) {}
    fun disconnect() {
        socketChannel?.close()
    }

    private fun handleAuthData(packet: NostalgiaServerAuthDataPacket) {
        publicKey = CryptManager.decodePublicKey(packet.publicKey)
        secretKey = CryptManager.createNewSharedKey()
        writePacket(NostalgiaSharedKeyPacket().apply {
            this.sharedSecret = CryptManager.encryptData(publicKey!!, secretKey!!.encoded)
            this.verifyToken = CryptManager.encryptData(publicKey!!, packet.verifyToken)
        })
    }

    private fun encryptOutputStream() {
        outputStream?.flush()
        isOutputEncrypted = true
        outputStream = DataOutputStream(
            BufferedOutputStream(
                CryptManager.encryptOutputStream(
                    secretKey!!,
                    outputStream
                ), 5120
            )
        )
        println("Output is now encrypted")
    }

    private fun decryptInputStream() {
        isInputDecrypted = true
        inputStream = DataInputStream(
            CryptManager.decryptInputStream(
                secretKey!!,
                inputStream
            )
        )
        println("Input is now decrypted")
    }
}
