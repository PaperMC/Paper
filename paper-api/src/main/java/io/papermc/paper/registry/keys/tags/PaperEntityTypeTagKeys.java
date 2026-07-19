package io.papermc.paper.registry.keys.tags;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.EntityType;

import static net.kyori.adventure.key.Key.key;

/**
 * Tag keys for {@link RegistryKey#ENTITY_TYPE}
 * provided by the Paper data pack.
 */
public final class PaperEntityTypeTagKeys {

    /**
     * Covers all horses
     */
    public static final TagKey<EntityType> HORSES = create(key("paper:horses"));

    /**
     * Covers all minecarts
     */
    public static final TagKey<EntityType> MINECART = create(key("paper:minecart"));

    /**
     * Covers mobs that split into smaller mobs
     */
    public static final TagKey<EntityType> SPLITTING_MOB = create(key("paper:splitting_mob"));

    private PaperEntityTypeTagKeys() {
    }

    private static TagKey<EntityType> create(final Key key) {
        return EntityTypeTagKeys.create(key);
    }
}
