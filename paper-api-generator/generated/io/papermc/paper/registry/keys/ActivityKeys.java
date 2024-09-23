package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.entity.ai.Activity;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.ApiStatus;

/**
 * Vanilla keys for {@link RegistryKey#ACTIVITY}.
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
public final class ActivityKeys {
    /**
     * {@code minecraft:admire_item}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> ADMIRE_ITEM = create(key("admire_item"));

    /**
     * {@code minecraft:avoid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> AVOID = create(key("avoid"));

    /**
     * {@code minecraft:celebrate}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> CELEBRATE = create(key("celebrate"));

    /**
     * {@code minecraft:core}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> CORE = create(key("core"));

    /**
     * {@code minecraft:dig}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> DIG = create(key("dig"));

    /**
     * {@code minecraft:emerge}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> EMERGE = create(key("emerge"));

    /**
     * {@code minecraft:fight}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> FIGHT = create(key("fight"));

    /**
     * {@code minecraft:hide}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> HIDE = create(key("hide"));

    /**
     * {@code minecraft:idle}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> IDLE = create(key("idle"));

    /**
     * {@code minecraft:investigate}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> INVESTIGATE = create(key("investigate"));

    /**
     * {@code minecraft:lay_spawn}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> LAY_SPAWN = create(key("lay_spawn"));

    /**
     * {@code minecraft:long_jump}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> LONG_JUMP = create(key("long_jump"));

    /**
     * {@code minecraft:meet}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> MEET = create(key("meet"));

    /**
     * {@code minecraft:panic}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> PANIC = create(key("panic"));

    /**
     * {@code minecraft:play}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> PLAY = create(key("play"));

    /**
     * {@code minecraft:play_dead}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> PLAY_DEAD = create(key("play_dead"));

    /**
     * {@code minecraft:pre_raid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> PRE_RAID = create(key("pre_raid"));

    /**
     * {@code minecraft:raid}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> RAID = create(key("raid"));

    /**
     * {@code minecraft:ram}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> RAM = create(key("ram"));

    /**
     * {@code minecraft:rest}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> REST = create(key("rest"));

    /**
     * {@code minecraft:ride}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> RIDE = create(key("ride"));

    /**
     * {@code minecraft:roar}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> ROAR = create(key("roar"));

    /**
     * {@code minecraft:sniff}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> SNIFF = create(key("sniff"));

    /**
     * {@code minecraft:swim}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> SWIM = create(key("swim"));

    /**
     * {@code minecraft:tongue}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> TONGUE = create(key("tongue"));

    /**
     * {@code minecraft:work}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Activity> WORK = create(key("work"));

    private ActivityKeys() {
    }

    private static @NonNull TypedKey<Activity> create(final @NonNull Key key) {
        return TypedKey.create(RegistryKey.ACTIVITY, key);
    }
}
