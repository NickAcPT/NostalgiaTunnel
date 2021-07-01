package io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.metadata

data class WatchableObject(
    var type: WatchableObjectType,
    var dataValueId: Int,
    var watchedObject: Any
)
