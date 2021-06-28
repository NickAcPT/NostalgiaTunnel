package io.github.nickacpt.nostalgiatunnel.server.translation.mapping

import io.github.nickacpt.nostalgiatunnel.protocol.nostalgia.model.NostalgiaItemStack
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack as ModernItemStack

object ItemStackTranslator {
    val itemMappings =
        RegistryMapping.fromJson(javaClass.classLoader.getResource("item_mapping.json")!!.readBytes().decodeToString())

    val blockMappings =
        RegistryMapping.fromJson(javaClass.classLoader.getResource("block_mapping.json")!!.readBytes().decodeToString())

    fun getItemMappingForLegacyId(legacyId: Long, legacyData: Long): RegistryMapping? {
        return itemMappings.firstOrNull { it.legacyId == legacyId && it.legacyData == legacyData }
    }

    fun getItemModernIdForLegacyId(legacyId: Long, legacyData: Long): Long {
        return getItemMappingForLegacyId(legacyId, legacyData)?.modernId ?: 0
    }

    fun getBlockMappingForLegacyId(legacyId: Long, legacyData: Long): RegistryMapping? {
        return blockMappings.firstOrNull { it.legacyId == legacyId && it.legacyData == legacyData }
    }

    fun getBlockModernIdForLegacyId(legacyId: Long, legacyData: Long): Long {
        return getBlockMappingForLegacyId(legacyId, legacyData)?.modernId ?: 0
    }

    fun translateLegacyToModern(stack: NostalgiaItemStack): ModernItemStack {
        return ModernItemStack(
            getItemModernIdForLegacyId(stack.itemId.toLong(), stack.itemDamage.toLong()).toInt(),
            stack.stackSize.toInt()
        )
    }
}