package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.client.session

interface NostalgiaClientSessionKey<T> {
    val tagName: String

    companion object {
        fun <T> of(name: String): NostalgiaClientSessionKey<T> {
            return object : NostalgiaClientSessionKey<T> {
                override val tagName: String
                    get() = name
            }
        }
    }
}