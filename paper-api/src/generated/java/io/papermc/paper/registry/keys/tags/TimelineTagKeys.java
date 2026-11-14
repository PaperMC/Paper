package io.papermc.paper.registry.keys.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.world.Timeline;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tag keys for {@link RegistryKey#TIMELINE}.
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
public final class TimelineTagKeys {
    /**
     * {@code #minecraft:in_end}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Timeline> IN_END = create(key("in_end"));

    /**
     * {@code #minecraft:in_nether}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Timeline> IN_NETHER = create(key("in_nether"));

    /**
     * {@code #minecraft:in_overworld}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Timeline> IN_OVERWORLD = create(key("in_overworld"));

    /**
     * {@code #minecraft:universal}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TagKey<Timeline> UNIVERSAL = create(key("universal"));

    private TimelineTagKeys() {
    }

    /**
     * Creates a tag key for {@link Timeline} in the registry {@code minecraft:timeline}.
     *
     * @param key the tag key's key
     * @return a new tag key
     */
    public static TagKey<Timeline> create(final Key key) {
        return TagKey.create(RegistryKey.TIMELINE, key);
    }
}
