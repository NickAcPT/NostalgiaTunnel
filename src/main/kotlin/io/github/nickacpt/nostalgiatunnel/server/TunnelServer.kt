package io.github.nickacpt.nostalgiatunnel.server

import com.github.steveice10.mc.protocol.MinecraftConstants
import com.github.steveice10.mc.protocol.MinecraftProtocol
import com.github.steveice10.mc.protocol.ServerLoginHandler
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo
import com.github.steveice10.mc.protocol.data.status.handler.ServerInfoBuilder
import com.github.steveice10.packetlib.Session
import com.github.steveice10.packetlib.tcp.TcpServer

abstract class TunnelServer(host: String, port: Int) : TcpServer(host, port, MinecraftProtocol::class.java) {

    fun setup() {
        setGlobalFlag(MinecraftConstants.VERIFY_USERS_KEY, false)
        setGlobalFlag(MinecraftConstants.SERVER_INFO_BUILDER_KEY, ServerInfoBuilder { session: Session ->
            onServerPing(session)
        })
        setGlobalFlag(MinecraftConstants.SERVER_LOGIN_HANDLER_KEY, ServerLoginHandler { session: Session ->
            onPlayerLogin(session)
        })
    }

    abstract fun onPlayerLogin(session: Session)

    abstract fun onServerPing(session: Session): ServerStatusInfo
}