package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.datacomponent.item.blocktransformer.BlockTransformer;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#BLOCK_TRANSFORMER}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedClass
public final class BlockTransformerKeys {
    /**
     * {@code minecraft:axe}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<BlockTransformer> AXE = create(key("axe"));

    /**
     * {@code minecraft:hoe}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<BlockTransformer> HOE = create(key("hoe"));

    /**
     * {@code minecraft:shovel}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<BlockTransformer> SHOVEL = create(key("shovel"));

    private BlockTransformerKeys() {
    }

    /**
     * Creates a typed key for {@link BlockTransformer} in the registry {@code minecraft:block_transformer}.
     *
     * @param key the value's key in the registry
     * @return a new typed key
     */
    public static TypedKey<BlockTransformer> create(final Key key) {
        return TypedKey.create(RegistryKey.BLOCK_TRANSFORMER, key);
    }
}
