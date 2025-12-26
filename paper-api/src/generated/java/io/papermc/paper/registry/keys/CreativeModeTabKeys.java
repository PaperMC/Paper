package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.annotation.GeneratedClass;
import io.papermc.paper.inventory.CreativeModeTab;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#CREATIVE_MODE_TAB}.
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
public final class CreativeModeTabKeys {
    /**
     * {@code minecraft:building_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> BUILDING_BLOCKS = create(key("building_blocks"));

    /**
     * {@code minecraft:colored_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> COLORED_BLOCKS = create(key("colored_blocks"));

    /**
     * {@code minecraft:combat}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> COMBAT = create(key("combat"));

    /**
     * {@code minecraft:food_and_drinks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> FOOD_AND_DRINKS = create(key("food_and_drinks"));

    /**
     * {@code minecraft:functional_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> FUNCTIONAL_BLOCKS = create(key("functional_blocks"));

    /**
     * {@code minecraft:hotbar}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> HOTBAR = create(key("hotbar"));

    /**
     * {@code minecraft:ingredients}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> INGREDIENTS = create(key("ingredients"));

    /**
     * {@code minecraft:inventory}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> INVENTORY = create(key("inventory"));

    /**
     * {@code minecraft:natural_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> NATURAL_BLOCKS = create(key("natural_blocks"));

    /**
     * {@code minecraft:op_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> OP_BLOCKS = create(key("op_blocks"));

    /**
     * {@code minecraft:redstone_blocks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> REDSTONE_BLOCKS = create(key("redstone_blocks"));

    /**
     * {@code minecraft:search}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> SEARCH = create(key("search"));

    /**
     * {@code minecraft:spawn_eggs}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> SPAWN_EGGS = create(key("spawn_eggs"));

    /**
     * {@code minecraft:tools_and_utilities}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<CreativeModeTab> TOOLS_AND_UTILITIES = create(key("tools_and_utilities"));

    private CreativeModeTabKeys() {
    }

    private static TypedKey<CreativeModeTab> create(final Key key) {
        return TypedKey.create(RegistryKey.CREATIVE_MODE_TAB, key);
    }
}
