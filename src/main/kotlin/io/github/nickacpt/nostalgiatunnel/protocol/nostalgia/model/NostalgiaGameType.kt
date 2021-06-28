package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model

import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode

enum class NostalgiaGameType(val id: Int, val modernGameMode: GameMode) {
    NOT_SET(-1, GameMode.SPECTATOR),
    SURVIVAL(0, GameMode.SURVIVAL),
    CREATIVE(1, GameMode.CREATIVE),
    ADVENTURE(2, GameMode.ADVENTURE),
}