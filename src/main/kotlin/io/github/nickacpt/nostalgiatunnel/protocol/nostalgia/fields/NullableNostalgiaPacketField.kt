package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.fields

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import kotlin.reflect.KProperty

abstract class NullableNostalgiaPacketField<T>(default: T? = null) : NostalgiaPacketField<T?>(default) {

    override operator fun getValue(packet: NostalgiaPacket, property: KProperty<*>): T? {
        return value
    }

    @JvmName("setValueNullable")
    operator fun setValue(packet: NostalgiaPacket, property: KProperty<*>, value: Any?) {
        this.value = value as? T
    }

}