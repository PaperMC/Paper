package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.statistic.StatisticType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#STAT_TYPE}.
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
@GeneratedFrom("1.21.4")
@NullMarked
@ApiStatus.Experimental
public final class StatisticTypeKeys {
    /**
     * {@code minecraft:broken}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> BROKEN = create(key("broken"));

    /**
     * {@code minecraft:crafted}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> CRAFTED = create(key("crafted"));

    /**
     * {@code minecraft:custom}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> CUSTOM = create(key("custom"));

    /**
     * {@code minecraft:dropped}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> DROPPED = create(key("dropped"));

    /**
     * {@code minecraft:killed}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> KILLED = create(key("killed"));

    /**
     * {@code minecraft:killed_by}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> KILLED_BY = create(key("killed_by"));

    /**
     * {@code minecraft:mined}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> MINED = create(key("mined"));

    /**
     * {@code minecraft:picked_up}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> PICKED_UP = create(key("picked_up"));

    /**
     * {@code minecraft:used}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<StatisticType<?>> USED = create(key("used"));

    private StatisticTypeKeys() {
    }

    private static TypedKey<StatisticType<?>> create(final Key key) {
        return TypedKey.create(RegistryKey.STAT_TYPE, key);
    }
}
