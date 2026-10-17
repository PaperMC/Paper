package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

/**
 * All the vanilla block transformers.
 */
public final class BlockTransformers {

    // Start generate - BlockTransformer
    public static final BlockTransformer AXE = getBlockTransformer("axe");

    public static final BlockTransformer HOE = getBlockTransformer("hoe");

    public static final BlockTransformer SHOVEL = getBlockTransformer("shovel");
    // End generate - BlockTransformer

    private static BlockTransformer getBlockTransformer(final @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK_TRANSFORMER).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

}
