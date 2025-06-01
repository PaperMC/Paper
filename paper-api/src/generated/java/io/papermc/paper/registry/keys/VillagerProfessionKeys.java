package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Villager;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#VILLAGER_PROFESSION}.
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
public final class VillagerProfessionKeys {
    /**
     * {@code minecraft:armorer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> ARMORER = create(key("armorer"));

    /**
     * {@code minecraft:butcher}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> BUTCHER = create(key("butcher"));

    /**
     * {@code minecraft:cartographer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> CARTOGRAPHER = create(key("cartographer"));

    /**
     * {@code minecraft:cleric}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> CLERIC = create(key("cleric"));

    /**
     * {@code minecraft:farmer}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> FARMER = create(key("farmer"));

    /**
     * {@code minecraft:fisherman}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> FISHERMAN = create(key("fisherman"));

    /**
     * {@code minecraft:fletcher}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> FLETCHER = create(key("fletcher"));

    /**
     * {@code minecraft:leatherworker}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> LEATHERWORKER = create(key("leatherworker"));

    /**
     * {@code minecraft:librarian}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> LIBRARIAN = create(key("librarian"));

    /**
     * {@code minecraft:mason}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> MASON = create(key("mason"));

    /**
     * {@code minecraft:nitwit}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> NITWIT = create(key("nitwit"));

    /**
     * {@code minecraft:none}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> NONE = create(key("none"));

    /**
     * {@code minecraft:shepherd}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> SHEPHERD = create(key("shepherd"));

    /**
     * {@code minecraft:toolsmith}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> TOOLSMITH = create(key("toolsmith"));

    /**
     * {@code minecraft:weaponsmith}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<Villager.Profession> WEAPONSMITH = create(key("weaponsmith"));

    private VillagerProfessionKeys() {
    }

    private static TypedKey<Villager.Profession> create(final Key key) {
        return TypedKey.create(RegistryKey.VILLAGER_PROFESSION, key);
    }
}
