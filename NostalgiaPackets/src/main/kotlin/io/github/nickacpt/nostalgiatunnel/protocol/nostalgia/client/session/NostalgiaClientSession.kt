package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.CryptManager
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.concurrent.ForkJoinPool
import javax.crypto.SecretKey

data class NostalgiaClientSession(
    val clientSocket: Socket
) {
    val isConnected: Boolean get() = clientSocket.isConnected
    var inputStream: DataInputStream = DataInputStream(clientSocket.getInputStream())
    var outputStream: DataOutputStream = DataOutputStream(clientSocket.getOutputStream())

    var sharedKey: SecretKey? = null
    var verifyToken: ByteArray = ByteArray(0)

    var isInputDecrypted = false
    var isOutputEncrypted = false

    //region Extra data
    private val extraData = mutableMapOf<String, Any?>()
    operator fun <T> contains(dataTag: NostalgiaClientSessionKey<T>): Boolean {
        return get(dataTag) != null
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(dataTag: NostalgiaClientSessionKey<T>): T? {
        val result = extraData[dataTag.tagName]
        return result as? T?
    }


    operator fun <T> set(dataTag: NostalgiaClientSessionKey<T>, value: T?) {
        if (value == null) {
            extraData.remove(dataTag.tagName)
            return
        }
        extraData[dataTag.tagName] = value
    }
    //endregion

    internal fun encryptOutputStream() {
        outputStream.flush()
        isOutputEncrypted = true
        outputStream = DataOutputStream(
            BufferedOutputStream(
                CryptManager.encryptOutputStream(
                    sharedKey!!,
                    outputStream
                ), 5120
            )
        )
        println("Server Output is now encrypted")
    }

    internal fun decryptInputStream() {
        isInputDecrypted = true
        inputStream = DataInputStream(
            CryptManager.decryptInputStream(
                sharedKey!!,
                inputStream
            )
        )
        println("Client Input is now decrypted")
    }

    private val writingPool = ForkJoinPool(1)
    fun sendPacket(packet: BaseNostalgiaPacket, blocking: Boolean = false) {
        val toRun = {
            try {
                packet.writePacket(outputStream)
                onPacketWrite(packet)
                outputStream.flush()
            } catch (e: Exception) {
                println("Error writing packet [${packet.javaClass.simpleName}] to client")
            }
        }

        if (!blocking) writingPool.execute(toRun) else run(toRun)
    }

    private fun onPacketWrite(packet: BaseNostalgiaPacket) {
    }

    fun disconnect() {
        clientSocket.getOutputStream().flush()
        clientSocket.close()
    }
}
