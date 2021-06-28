package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import java.io.DataInputStream
import java.io.DataOutputStream
import kotlin.reflect.KProperty

abstract class NostalgiaPacketField<T>(default: T? = null) {
    var value: T? = default

    abstract fun readValue(input: DataInputStream)

    abstract fun writeValue(output: DataOutputStream)

    operator fun getValue(packet: NostalgiaPacket, property: KProperty<*>): T {
        return value ?: throw Exception("Value for [${property.name}] not defined")
    }

    operator fun setValue(packet: NostalgiaPacket, property: KProperty<*>, value: Any) {
        this.value = value as? T ?: throw Exception("Value for [${property.name}] not defined")
    }

    @JvmName("setValueNullable")
    operator fun setValue(packet: NostalgiaPacket, property: KProperty<*>, value: Any?) {
        this.value = value as? T
    }
}
