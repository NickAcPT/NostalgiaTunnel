package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class StringNostalgiaPacketField(val maxLength: Short = Short.MAX_VALUE, default: String? = null) :
    NostalgiaPacketField<String>(default) {

    override fun readValue(input: DataInputStream) {
        value = readString(input, maxLength)
    }

    override fun writeValue(output: DataOutputStream) {
        val stringValue = value ?: throw Exception("String value is null")
        writeString(output, stringValue, maxLength)
    }

    companion object {

         fun readString(input: DataInputStream, maxLength: Short = Short.MAX_VALUE): String {
            val length: Short = input.readShort()

            if (length > maxLength)
                throw IOException("Received string length longer than maximum allowed ($length > $maxLength)")
            if (length < 0) throw IOException("Received string length is less than zero! Weird string!")

            val resultBuilder = StringBuilder()
            for (n in 0 until length) {
                resultBuilder.append(input.readChar())
            }

            return resultBuilder.toString()
        }

        fun writeString(output: DataOutputStream, stringValue: String, maxLength: Short = Short.MAX_VALUE) {
            if (stringValue.length > maxLength) {
                throw IOException("String too big")
            }

            output.writeShort(stringValue.length)
            output.writeChars(stringValue)
        }

    }
}
