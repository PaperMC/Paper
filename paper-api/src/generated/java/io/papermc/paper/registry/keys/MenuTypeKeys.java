package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#MENU}.
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
@GeneratedFrom("1.21.6-pre1")
public final class MenuTypeKeys {
    /**
     * {@code minecraft:anvil}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> ANVIL = create(key("anvil"));

    /**
     * {@code minecraft:beacon}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> BEACON = create(key("beacon"));

    /**
     * {@code minecraft:blast_furnace}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> BLAST_FURNACE = create(key("blast_furnace"));

    /**
     * {@code minecraft:brewing_stand}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> BREWING_STAND = create(key("brewing_stand"));

    /**
     * {@code minecraft:cartography_table}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> CARTOGRAPHY_TABLE = create(key("cartography_table"));

    /**
     * {@code minecraft:crafter_3x3}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> CRAFTER_3X3 = create(key("crafter_3x3"));

    /**
     * {@code minecraft:crafting}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> CRAFTING = create(key("crafting"));

    /**
     * {@code minecraft:enchantment}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> ENCHANTMENT = create(key("enchantment"));

    /**
     * {@code minecraft:furnace}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> FURNACE = create(key("furnace"));

    /**
     * {@code minecraft:generic_3x3}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_3X3 = create(key("generic_3x3"));

    /**
     * {@code minecraft:generic_9x1}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X1 = create(key("generic_9x1"));

    /**
     * {@code minecraft:generic_9x2}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X2 = create(key("generic_9x2"));

    /**
     * {@code minecraft:generic_9x3}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X3 = create(key("generic_9x3"));

    /**
     * {@code minecraft:generic_9x4}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X4 = create(key("generic_9x4"));

    /**
     * {@code minecraft:generic_9x5}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X5 = create(key("generic_9x5"));

    /**
     * {@code minecraft:generic_9x6}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GENERIC_9X6 = create(key("generic_9x6"));

    /**
     * {@code minecraft:grindstone}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> GRINDSTONE = create(key("grindstone"));

    /**
     * {@code minecraft:hopper}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> HOPPER = create(key("hopper"));

    /**
     * {@code minecraft:lectern}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> LECTERN = create(key("lectern"));

    /**
     * {@code minecraft:loom}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> LOOM = create(key("loom"));

    /**
     * {@code minecraft:merchant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> MERCHANT = create(key("merchant"));

    /**
     * {@code minecraft:shulker_box}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> SHULKER_BOX = create(key("shulker_box"));

    /**
     * {@code minecraft:smithing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> SMITHING = create(key("smithing"));

    /**
     * {@code minecraft:smoker}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> SMOKER = create(key("smoker"));

    /**
     * {@code minecraft:stonecutter}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<MenuType> STONECUTTER = create(key("stonecutter"));

    private MenuTypeKeys() {
    }

    private static TypedKey<MenuType> create(final Key key) {
        return TypedKey.create(RegistryKey.MENU, key);
    }
}
