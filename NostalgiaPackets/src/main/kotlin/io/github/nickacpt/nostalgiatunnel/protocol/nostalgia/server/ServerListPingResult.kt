package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.server

import net.kyori.adventure.text.Component

data class ServerListPingResult(
    val motd: Component,
    val currentPlayerCount: Int,
    val maxPlayerCount: Int
)
