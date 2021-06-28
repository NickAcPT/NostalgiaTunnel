package io.github.nickacpt.nostalgiatunnel.client.nostalgia

import org.bouncycastle.crypto.BufferedBlockCipher
import org.bouncycastle.crypto.CipherKeyGenerator
import org.bouncycastle.crypto.KeyGenerationParameters
import org.bouncycastle.crypto.engines.AESFastEngine
import org.bouncycastle.crypto.io.CipherInputStream
import org.bouncycastle.crypto.io.CipherOutputStream
import org.bouncycastle.crypto.modes.CFBBlockCipher
import org.bouncycastle.crypto.params.KeyParameter
import org.bouncycastle.crypto.params.ParametersWithIV
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

object CryptManager {
    private val charSet: Charset = Charset.forName("ISO_8859_1")
    fun createNewSharedKey(): SecretKey {
        val generator = CipherKeyGenerator()
        generator.init(KeyGenerationParameters(SecureRandom(), 128))
        return SecretKeySpec(generator.generateKey(), "AES")
    }

    fun generateKeyPair(): KeyPair? {
        return try {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(1024)
            generator.generateKeyPair()
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
            System.err.println("Key pair generation failed!")
            null
        }
    }

    fun getServerIdHash(string: String, publicKey: PublicKey, secretKey: SecretKey): ByteArray? {
        return try {
            digestOperation("SHA-1", string.toByteArray(charSet), secretKey.encoded, publicKey.encoded)
        } catch (ex: UnsupportedEncodingException) {
            ex.printStackTrace()
            null
        }
    }

    private fun digestOperation(digest: String, vararg arr: ByteArray): ByteArray? {
        return try {
            val digestInstance = MessageDigest.getInstance(digest)
            val length = arr.size
            var i = 0
            while (i < length) {
                digestInstance.update(arr[i])
                ++i
            }
            digestInstance.digest()
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
            null
        }
    }

    fun decodePublicKey(arr: ByteArray): PublicKey {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(arr))
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        } catch (ex: InvalidKeySpecException) {
            ex.printStackTrace()
        }
        throw Exception("Public key reconstitute failed!")
    }

    fun decryptSharedKey(privateKey: PrivateKey, arr: ByteArray): SecretKey {
        return SecretKeySpec(decryptData(privateKey, arr), "AES")
    }

    fun encryptData(key: Key, arr: ByteArray): ByteArray {
        return cipherOperation(1, key, arr)
    }

    fun decryptData(key: Key, arr: ByteArray): ByteArray {
        return cipherOperation(2, key, arr)
    }

    private fun cipherOperation(integer: Int, key: Key, arr: ByteArray): ByteArray {
        try {
            return createTheCipherInstance(integer, key.algorithm, key)!!.doFinal(arr)
        } catch (ex: IllegalBlockSizeException) {
            ex.printStackTrace()
        } catch (ex: BadPaddingException) {
            ex.printStackTrace()
        }
        throw Exception("Cipher data failed!")
    }

    private fun createTheCipherInstance(integer: Int, string: String, key: Key): Cipher? {
        try {
            val instance = Cipher.getInstance(string)
            instance.init(integer, key)
            return instance
        } catch (ex: InvalidKeyException) {
            ex.printStackTrace()
        } catch (ex: NoSuchPaddingException) {
            ex.printStackTrace()
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }
        System.err.println("Cipher creation failed!")
        return null
    }

    private fun createBufferedBlockCipher(forEncryption: Boolean, key: Key): BufferedBlockCipher {
        val bufferedBlockCipher3 = BufferedBlockCipher(CFBBlockCipher(AESFastEngine(), 8))
        bufferedBlockCipher3.init(forEncryption, ParametersWithIV(KeyParameter(key.encoded), key.encoded, 0, 16))
        return bufferedBlockCipher3
    }

    fun encryptOutputStream(secretKey: SecretKey, outputStream: OutputStream?): OutputStream {
        return CipherOutputStream(outputStream, createBufferedBlockCipher(true, secretKey))
    }

    fun decryptInputStream(secretKey: SecretKey, inputStream: InputStream?): InputStream {
        return CipherInputStream(inputStream, createBufferedBlockCipher(false, secretKey))
    }

    init {
        Security.addProvider(BouncyCastleProvider())
    }
}