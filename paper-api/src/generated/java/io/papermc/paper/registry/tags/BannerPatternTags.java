package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.BannerPatternTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.block.banner.PatternType;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#BANNER_PATTERN}.
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
@GeneratedFrom("1.21.6")
public final class BannerPatternTags {
    /**
     * {@code #minecraft:no_item_required}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> NO_ITEM_REQUIRED = fetch(BannerPatternTagKeys.NO_ITEM_REQUIRED);

    /**
     * {@code #minecraft:pattern_item/bordure_indented}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_BORDURE_INDENTED = fetch(BannerPatternTagKeys.PATTERN_ITEM_BORDURE_INDENTED);

    /**
     * {@code #minecraft:pattern_item/creeper}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_CREEPER = fetch(BannerPatternTagKeys.PATTERN_ITEM_CREEPER);

    /**
     * {@code #minecraft:pattern_item/field_masoned}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_FIELD_MASONED = fetch(BannerPatternTagKeys.PATTERN_ITEM_FIELD_MASONED);

    /**
     * {@code #minecraft:pattern_item/flow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_FLOW = fetch(BannerPatternTagKeys.PATTERN_ITEM_FLOW);

    /**
     * {@code #minecraft:pattern_item/flower}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_FLOWER = fetch(BannerPatternTagKeys.PATTERN_ITEM_FLOWER);

    /**
     * {@code #minecraft:pattern_item/globe}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_GLOBE = fetch(BannerPatternTagKeys.PATTERN_ITEM_GLOBE);

    /**
     * {@code #minecraft:pattern_item/guster}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_GUSTER = fetch(BannerPatternTagKeys.PATTERN_ITEM_GUSTER);

    /**
     * {@code #minecraft:pattern_item/mojang}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_MOJANG = fetch(BannerPatternTagKeys.PATTERN_ITEM_MOJANG);

    /**
     * {@code #minecraft:pattern_item/piglin}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_PIGLIN = fetch(BannerPatternTagKeys.PATTERN_ITEM_PIGLIN);

    /**
     * {@code #minecraft:pattern_item/skull}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<PatternType> PATTERN_ITEM_SKULL = fetch(BannerPatternTagKeys.PATTERN_ITEM_SKULL);

    private BannerPatternTags() {
    }

    private static Tag<PatternType> fetch(final TagKey<PatternType> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).getTag(tagKey);
    }
}
