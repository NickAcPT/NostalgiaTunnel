package io.github.nickacpt.nostalgiatunnel.replay

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.BaseNostalgiaPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.NostalgiaClient

fun main() {
    val client = object : NostalgiaClient("localhost", 25545) {
        override fun onPacketRead(packet: BaseNostalgiaPacket) {
            println("Read packet [${packet.javaClass.simpleName}] from server")
        }

        override fun onPacketWrite(packet: BaseNostalgiaPacket) {
            println("Replied server with packet [${packet.javaClass.simpleName}]")
        }

        override fun onUnknownPacketRead(packetId: Int) {
            println("Read unknown packet [${packetId}]")
            disconnect()
        }
    }

    client.connect("NickAc")
}