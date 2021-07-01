package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.NostalgiaPingPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaClientProtocolPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaServerAuthDataPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.login.NostalgiaSharedKeyPacket
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.*
import java.io.DataInputStream

object NostalgiaProtocol {
    const val protocolVersion = 61
    private val packetMap = mutableMapOf<Int, () -> BaseNostalgiaPacket>()

    private fun registerPacket(creator: () -> BaseNostalgiaPacket) {
        val tempPacket = creator()
        packetMap[tempPacket.packetId] = creator
    }

    init {
        registerPacket { NostalgiaPingPacket() }
        registerPacket { NostalgiaKickPacket() }
        registerPacket { NostalgiaClientProtocolPacket() }
        registerPacket { NostalgiaServerAuthDataPacket() }
        registerPacket { NostalgiaSharedKeyPacket() }
        registerPacket { NostalgiaLoginPacket() }
        registerPacket { NostalgiaKeepAlivePacket() }
        registerPacket { NostalgiaSpawnPositionPacket() }
        registerPacket { NostalgiaPlayerAbilitiesPacket() }
        registerPacket { NostalgiaBlockItemsSwitchPacket() }
        registerPacket { NostalgiaUpdateTimePacket() }
        registerPacket { NostalgiaChatPacket() }
        registerPacket { NostalgiaPlayerInfoPacket() }
        registerPacket { NostalgiaPlayerLookMovePacket() }
        registerPacket { NostalgiaGameEventPacket() }
        registerPacket { NostalgiaWindowItemsPacket() }
        registerPacket { NostalgiaSetSlotPacket() }
        registerPacket { NostalgiaMapChunksPacket() }
        registerPacket { NostalgiaMobSpawnPacket() }
        registerPacket { NostalgiaEntityMetadataPacket() }
        registerPacket { NostalgiaEntityHeadRotationPacket() }
        registerPacket { NostalgiaPlayerInventoryPacket() }
    }

    fun readPacket(id: Int, inputStream: DataInputStream): BaseNostalgiaPacket? {
        return packetMap[id]?.invoke()?.also { it.readPacket(inputStream) }
    }

}