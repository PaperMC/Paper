package io.papermc.paper.datacomponent.item.blocktransformer;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.NonExtendable
public interface BlockTransformer extends Keyed {

    // Start generate - BlockTransformer
    BlockTransformer AXE = getBlockTransformer("axe");

    BlockTransformer HOE = getBlockTransformer("hoe");

    BlockTransformer SHOVEL = getBlockTransformer("shovel");
    // End generate - BlockTransformer

    private static BlockTransformer getBlockTransformer(final @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BLOCK_TRANSFORMER).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    @Contract(pure = true)
    @Unmodifiable List<BlockTransformData> transforms();
}
