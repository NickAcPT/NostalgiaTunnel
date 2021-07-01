package io.github.nickacpt.nostalgiatunnel.server

import com.github.steveice10.mc.auth.data.GameProfile
import com.github.steveice10.mc.protocol.MinecraftConstants
import com.github.steveice10.mc.protocol.data.status.PlayerInfo
import com.github.steveice10.mc.protocol.data.status.ServerStatusInfo
import com.github.steveice10.mc.protocol.data.status.VersionInfo
import com.github.steveice10.packetlib.Session
import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.NostalgiaClient
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

class TranslatingTunnelServer(host: String, port: Int, val hostTarget: String, val portTarget: Int) :
    TunnelServer(host, port) {
    override fun onPlayerLogin(session: Session) {
        val profile = session.profile
        println("[Downstream connecting ${profile.name} to upstream]")

        val client = session.nostalgiaClient
        client.connect(profile.name)

    }

    override fun onServerPing(session: Session): ServerStatusInfo {
        val result = session.nostalgiaClient.pingServer()
        return ServerStatusInfo(
            VersionInfo.CURRENT,
            PlayerInfo(result.maxCount, result.currentCount, emptyArray()),
            Component.text("[NostalgiaTunnel] ").append(LegacyComponentSerializer.legacySection().deserialize(result.motd)),
            ByteArray(0)
        )
    }

    val Session.nostalgiaClient: NostalgiaClient
        get() {
            var flagResult = getFlag<NostalgiaClient?>(nostalgiaClientFlag, null)
            if (flagResult == null) {
                flagResult = TranslatingNostalgiaClient(this, hostTarget, portTarget)
                setFlag(nostalgiaClientFlag, flagResult)
            }

            return flagResult
        }

    companion object {
        const val nostalgiaClientFlag = "NOSTALGIA_CLIENT"
    }
}

val Session.profile: GameProfile
    get() {
        return getFlag(MinecraftConstants.PROFILE_KEY)
    }
