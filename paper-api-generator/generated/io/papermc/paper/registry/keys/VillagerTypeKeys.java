package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Villager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

/**
 * Vanilla keys for {@link RegistryKey#VILLAGER_TYPE}.
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
@GeneratedFrom("1.21.1")
@ApiStatus.Experimental
public final class VillagerTypeKeys {
    /**
     * {@code minecraft:desert}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> DESERT = create(key("desert"));

    /**
     * {@code minecraft:jungle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> JUNGLE = create(key("jungle"));

    /**
     * {@code minecraft:plains}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> PLAINS = create(key("plains"));

    /**
     * {@code minecraft:savanna}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> SAVANNA = create(key("savanna"));

    /**
     * {@code minecraft:snow}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> SNOW = create(key("snow"));

    /**
     * {@code minecraft:swamp}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> SWAMP = create(key("swamp"));

    /**
     * {@code minecraft:taiga}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Type> TAIGA = create(key("taiga"));

    private VillagerTypeKeys() {
    }

    private static @NonNull TypedKey<Villager.Type> create(final @NonNull Key key) {
        return TypedKey.create(RegistryKey.VILLAGER_TYPE, key);
    }
}
