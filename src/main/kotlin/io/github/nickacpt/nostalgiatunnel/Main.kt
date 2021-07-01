package io.github.nickacpt.nostalgiatunnel

import io.github.nickacpt.nostalgiatunnel.server.TranslatingTunnelServer

fun main() {

    val server = TranslatingTunnelServer("0.0.0.0", 25566, "localhost", 25565)
    server.setup()
    server.bind()
}