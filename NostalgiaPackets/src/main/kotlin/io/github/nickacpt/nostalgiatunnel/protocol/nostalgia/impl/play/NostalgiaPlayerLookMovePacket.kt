package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

class NostalgiaPlayerLookMovePacket : NostalgiaPacket(0xD) {
    val xPosition by value().double()
    val yPosition by value().double()
    val stance by value().double()
    val zPosition by value().double()
    val yaw by value().float()
    val pitch by value().float()
    val isOnGround by value().booleanAsByte()
}