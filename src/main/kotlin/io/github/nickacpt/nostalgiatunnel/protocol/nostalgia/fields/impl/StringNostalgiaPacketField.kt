package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.NostalgiaPacketField
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class StringNostalgiaPacketField(val maxLength: Short = Short.MAX_VALUE, default: String? = null) :
    NostalgiaPacketField<String>(default) {

    override fun readValue(input: DataInputStream) {
        val length: Short = input.readShort()

        if (length > maxLength)
            throw IOException("Received string length longer than maximum allowed ($length > $maxLength)")
        if (length < 0) throw IOException("Received string length is less than zero! Weird string!")

        val resultBuilder = StringBuilder()
        for (n in 0 until length) {
            resultBuilder.append(input.readChar())
        }

        value = resultBuilder.toString()
    }

    override fun writeValue(output: DataOutputStream) {
        val stringValue = value ?: throw Exception("String value is null")
        if (stringValue.length > maxLength) {
            throw IOException("String too big")
        }

        output.writeShort(stringValue.length)
        output.writeChars(stringValue)
    }

}
