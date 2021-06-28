package io.github.nickacpt.nostalgiatunnel.server.translation

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.impl.*
import com.github.steveice10.packetlib.packet.Packet as ModernPacket

object TranslationManager {
    val inputTranslationMap = mutableMapOf<Int, InputPacketTranslation<out NostalgiaPacket, out ModernPacket>>()

    inline fun <reified N:NostalgiaPacket, M: ModernPacket> registerTranslation(noinline translation: () -> InputPacketTranslation<N, M>) {
        val temp = N::class.java.newInstance()
        inputTranslationMap[temp.packetId] = translation()
    }

    fun translateInput(input: NostalgiaPacket): ModernPacket? {
        val packetTranslation = inputTranslationMap[input.packetId] as? InputPacketTranslation<NostalgiaPacket, ModernPacket>
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
    }
}