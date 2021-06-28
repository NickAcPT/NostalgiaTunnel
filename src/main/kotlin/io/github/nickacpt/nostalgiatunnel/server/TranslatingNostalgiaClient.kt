package io.github.nickacpt.nostalgiatunnel.server

import com.github.steveice10.packetlib.Session
import io.github.nickacpt.nostalgiatunnel.client.nostalgia.NostalgiaClient
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.server.translation.TranslationManager

class TranslatingNostalgiaClient(val session: Session, address: String, port: Int) : NostalgiaClient(address, port) {
    override fun onUnknownPacketRead(packetId: Int) {
        this.disconnect()
        println("Disconnected player for [Unknown 1.5.2 packet received $packetId]")
        session.disconnect("Unknown 1.5.2 packet received $packetId")
    }

    override fun onDisconnect() {
        super.onDisconnect()
        println("Upstream server request kick of player")
        session.disconnect("Disconnected by server")
    }

    override fun onPacketRead(packet: NostalgiaPacket) {
        super.onPacketRead(packet)
        if (isPing) return
        val result = TranslationManager.translateInput(packet)
        if (result != null) {
            session.send(result)
            println("Translated packet ${packet.javaClass.simpleName} to ${result.javaClass.simpleName}")
        } else {
            println("Skipped translation of packet ${packet.javaClass.simpleName}")
        }
    }
}