package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import net.kyori.adventure.key.Key;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#POTION}.
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
public final class PotionTypeTagKeys {
    /**
     * {@code #minecraft:douses_fire}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<PotionType> DOUSES_FIRE = create(key("douses_fire"));

    /**
     * {@code #minecraft:extinguishes_entities}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<PotionType> EXTINGUISHES_ENTITIES = create(key("extinguishes_entities"));

    /**
     * {@code #minecraft:hurts_water_sensitive_entities}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<PotionType> HURTS_WATER_SENSITIVE_ENTITIES = create(key("hurts_water_sensitive_entities"));

    /**
     * {@code #minecraft:rehydrates_axolotls}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<PotionType> REHYDRATES_AXOLOTLS = create(key("rehydrates_axolotls"));

    /**
     * {@code #minecraft:tradeable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<PotionType> TRADEABLE = create(key("tradeable"));

    private PotionTypeTagKeys() {
    }

    /**
     * Creates a tag key for {@link PotionType} in the registry {@code minecraft:potion}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    public static TagKey<PotionType> create(final Key key) {
        return TagKey.create(RegistryKey.POTION, key);
    }
}
