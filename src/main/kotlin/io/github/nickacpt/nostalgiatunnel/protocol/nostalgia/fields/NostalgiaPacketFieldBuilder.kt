package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields.impl.*
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import java.io.DataInputStream
import java.io.DataOutputStream

class NostalgiaPacketFieldBuilder(var onResult: (NostalgiaPacketField<*>) -> Unit) {

    fun byte(default: Int? = null): NostalgiaPacketField<Int> {
        return result(ByteNostalgiaPacketField(default))
    }

    fun string(maxLength: Short = Short.MAX_VALUE, default: String? = null): NostalgiaPacketField<String> {
        return result(StringNostalgiaPacketField(maxLength, default))
    }

    fun int(default: Int? = null): NostalgiaPacketField<Int> {
        return result(IntNostalgiaPacketField(default))
    }

    fun byteArray(default: ByteArray? = null): NostalgiaPacketField<ByteArray> {
        return result(ByteArrayNostalgiaPacketField(default))
    }

    fun short(default: Short? = null): NostalgiaPacketField<Short> {
        return result(ShortNostalgiaPacketField(default))
    }

    fun long(default: Long? = null): NostalgiaPacketField<Long> {
        return result(LongNostalgiaPacketField(default))
    }

    fun booleanAsByte(default: Boolean? = null): NostalgiaPacketField<Boolean> {
        return result(BooleanAsByteNostalgiaPacketField(default))
    }

    fun float(default: Float? = null): NostalgiaPacketField<Float> {
        return result(FloatNostalgiaPacketField(default))
    }

    fun double(default: Double? = null): NostalgiaPacketField<Double> {
        return result(DoubleNostalgiaPacketField(default))
    }

    fun itemStack(default: NostalgiaItemStack? = null): NullableNostalgiaPacketField<NostalgiaItemStack> {
        return result(NostalgiaItemStackPacketField(default))
    }

    fun itemStackArray(default: Array<NostalgiaItemStack?>? = null): NostalgiaPacketField<Array<NostalgiaItemStack?>> {
        return result(NostalgiaItemStackArrayPacketField(default))
    }

    inline fun <reified T> custom(
        default: T? = null,
        crossinline onRead: (DataInputStream) -> T,
        crossinline onWrite: (T, DataOutputStream) -> Unit
    ): NostalgiaPacketField<T> {
        return (object : NostalgiaPacketField<T>(default) {
            override fun readValue(input: DataInputStream) {
                onRead(input).also { value = it }
            }

            override fun writeValue(output: DataOutputStream) {
                onWrite(value ?: throw Exception("Value not set"), output)
            }
        }).also(onResult)
    }

    inline fun <reified T> customWrite(
        default: T? = null,
        fieldGetter: (NostalgiaPacketFieldBuilder) -> NostalgiaPacketField<T>,
        crossinline onWrite: (T, DataOutputStream) -> Unit
    ): NostalgiaPacketField<T> {
        isResultDisabled = true
        val getter = fieldGetter(this)
        isResultDisabled = false
        return (custom(default, { stream ->
            getter.readValue(stream)
            getter.value!!
        }, {t, stream ->
            onWrite(t, stream)
        }))
    }
    var isResultDisabled = false

    private inline fun <reified T> result(packetField: NostalgiaPacketField<T>): NostalgiaPacketField<T> {
        if (!isResultDisabled) onResult.invoke(packetField)
        return packetField
    }

    private inline fun <reified T> result(packetField: NullableNostalgiaPacketField<T>): NullableNostalgiaPacketField<T> {
        if (!isResultDisabled) onResult.invoke(packetField)
        return packetField
    }
}

