package io.papermc.paper.registry.tags;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.tags.GameEventTagKeys;
import io.papermc.paper.registry.tag.Tag;
import io.papermc.paper.registry.tag.TagKey;
import org.bukkit.GameEvent;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla tags for {@link RegistryKey#GAME_EVENT}.
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
public final class GameEventTags {
    /**
     * {@code #minecraft:allay_can_listen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<GameEvent> ALLAY_CAN_LISTEN = fetch(GameEventTagKeys.ALLAY_CAN_LISTEN);

    /**
     * {@code #minecraft:ignore_vibrations_sneaking}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<GameEvent> IGNORE_VIBRATIONS_SNEAKING = fetch(GameEventTagKeys.IGNORE_VIBRATIONS_SNEAKING);

    /**
     * {@code #minecraft:shrieker_can_listen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<GameEvent> SHRIEKER_CAN_LISTEN = fetch(GameEventTagKeys.SHRIEKER_CAN_LISTEN);

    /**
     * {@code #minecraft:vibrations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<GameEvent> VIBRATIONS = fetch(GameEventTagKeys.VIBRATIONS);

    /**
     * {@code #minecraft:warden_can_listen}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final Tag<GameEvent> WARDEN_CAN_LISTEN = fetch(GameEventTagKeys.WARDEN_CAN_LISTEN);

    private GameEventTags() {
    }

    private static Tag<GameEvent> fetch(final TagKey<GameEvent> tagKey) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.GAME_EVENT).getTag(tagKey);
    }
}
