package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.CryptManager
import java.security.PrivateKey
import javax.crypto.SecretKey

class NostalgiaSharedKeyPacket : NostalgiaPacket(0xFC) {
    var sharedSecret by value().byteArray(ByteArray(0))
    var verifyToken by value().byteArray(ByteArray(0))

    fun getSharedKey(privateKey: PrivateKey): SecretKey {
        return CryptManager.decryptSharedKey(privateKey, this.sharedSecret)
    }
}