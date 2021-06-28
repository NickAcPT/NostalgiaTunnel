package io.github.nickacpt.nostalgiatunnel.protocol

import io.github.nickacpt.nostalgiatunnel.client.nostalgia.NostalgiaClient
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaChatPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaKickPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.NostalgiaWindowItemsPacket

fun main() {

    val client = object : NostalgiaClient("localhost", 25565) {
        override fun onPacketRead(packet: NostalgiaPacket) {
            println("Read packet [${packet.javaClass.simpleName}] $isInputDecrypted")
            if (packet is NostalgiaKickPacket) {
                println("Got kicked with message ${packet.message}")
            } else if (packet is NostalgiaChatPacket) {
                println("Received chat message [${packet.message}]")
            } else if (packet is NostalgiaWindowItemsPacket) {
                println("Received inventory")
            }
        }

        override fun onPacketWrite(packet: NostalgiaPacket) {
            println("Wrote packet [${packet.javaClass.simpleName}] $isOutputEncrypted")
        }

        override fun onUnknownPacketRead(packetId: Int) {
            println("Read unknown packet with ID [$packetId]")
            this.disconnect()
        }
    }
    client.connect("NostalgiaTunnel")

    readLine()
}