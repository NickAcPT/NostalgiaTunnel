package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.impl.play.player

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.NostalgiaPacket

open class NostalgiaClientInfoPacket : NostalgiaPacket(0xCC) {
    var language by value().string(7)
    var renderDistance by value().byte()
    var rawChatSettings by value().byte()
    var gameDifficulty by value().byte()
    var showCape by value().booleanAsByte()

    var chatVisible: Int
        get() = rawChatSettings and 7
        set(value) = computeChatSettings(value, chatColours).run { rawChatSettings = this }

    var chatColours
        get() = (rawChatSettings and 8) == 8
        set(value) = computeChatSettings(chatVisible, value).run { rawChatSettings = this }

    private fun computeChatSettings(chatVisibility: Int, chatColours: Boolean): Int {
        return (chatVisibility or (if (chatColours) 1 else 0) shl 3)
    }

}