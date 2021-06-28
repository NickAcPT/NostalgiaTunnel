package io.github.nickacpt.nostalgiatunnel.server

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket
import com.github.steveice10.packetlib.Session
import io.github.nickacpt.nostalgiatunnel.client.nostalgia.NostalgiaClient
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.TranslationManager
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class TranslatingNostalgiaClient(val session: Session, address: String, port: Int) : NostalgiaClient(address, port) {
    override fun onUnknownPacketRead(packetId: Int) {
        this.disconnect()

//        println("Disconnected player for [Unknown 1.5.2 packet received $packetId]")
//        session.disconnect("Unknown 1.5.2 packet received $packetId")
    }

    override fun onPacketRead(packet: NostalgiaPacket) {
        super.onPacketRead(packet)
        if (isPing) return
        val result = TranslationManager.translateInput(packet)
        if (result != null) {
            session.send(result)
            if (result is ServerDisconnectPacket) {
                session.disconnect(LegacyComponentSerializer.legacySection().serialize(result.reason))
            }
            println("Translated packet ${packet.javaClass.simpleName} to ${result.javaClass.simpleName}")
        } else {
            println("Skipped translation of packet ${packet.javaClass.simpleName}")
        }
    }
}