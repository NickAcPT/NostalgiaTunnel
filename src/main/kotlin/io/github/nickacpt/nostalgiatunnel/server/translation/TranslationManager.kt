package io.github.nickacpt.nostalgiatunnel.server.translation

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.impl.*
import com.github.steveice10.packetlib.packet.Packet as ModernPacket

object TranslationManager {
    val inputTranslationMap = mutableMapOf<Int, InputPacketTranslation<out BaseNostalgiaPacket, out ModernPacket>>()
    val multiInputTranslationMap = mutableMapOf<Int, MultiInputPacketTranslation<out BaseNostalgiaPacket, out ModernPacket>>()

    inline fun <reified N:BaseNostalgiaPacket, M: ModernPacket> registerTranslation(noinline translation: () -> InputPacketTranslation<N, M>) {
        val temp = N::class.java.newInstance()
        inputTranslationMap[temp.packetId] = translation()
    }
    inline fun <reified N:BaseNostalgiaPacket, M: ModernPacket> registerMultiTranslation(noinline translation: () -> MultiInputPacketTranslation<N, M>) {
        val temp = N::class.java.newInstance()
        multiInputTranslationMap[temp.packetId] = translation()
    }

    fun translateInput(input: BaseNostalgiaPacket): ModernPacket? {
        val packetTranslation = inputTranslationMap[input.packetId] as? InputPacketTranslation<BaseNostalgiaPacket, ModernPacket>
        return packetTranslation?.translateInput(input)
    }

    fun translateMultiInput(input: BaseNostalgiaPacket): List<ModernPacket>? {
        val packetTranslation = multiInputTranslationMap[input.packetId] as? MultiInputPacketTranslation<BaseNostalgiaPacket, ModernPacket>
        return packetTranslation?.translateInput(input)
    }

    init {
        registerTranslation { ChatPacketTranslation() }
        registerTranslation { LoginPacketTranslation() }
        registerTranslation { PlayerLookMovePacketTranslation() }
        registerTranslation { SpawnPositionPacketTranslation() }
        registerTranslation { WindowItemsPacketTranslation() }
        registerTranslation { KickPacketTranslation() }
        registerTranslation { SetSlotPacketTranslation() }
        registerMultiTranslation { MapChunksPacketTranslation() }
        registerTranslation { PlayerInventoryPacketTranslation() }
    }
}