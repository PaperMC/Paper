package org.bukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Locale;
import java.util.Map; // Paper
import java.util.UUID; // Paper
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a villager NPC
 */
public interface Villager extends AbstractVillager {

    /**
     * Gets the current profession of this villager.
     *
     * @return Current profession.
     */
    @NotNull
    public Profession getProfession();

    /**
     * Sets the new profession of this villager.
     *
     * @param profession New profession.
     */
    public void setProfession(@NotNull Profession profession);

    /**
     * Gets the current type of this villager.
     *
     * @return Current type.
     */
    @NotNull
    public Type getVillagerType();

    /**
     * Sets the new type of this villager.
     *
     * @param type New type.
     */
    public void setVillagerType(@NotNull Type type);

    /**
     * Gets the level of this villager.
     *
     * A villager with a level of 1 and no experience is liable to lose its
     * profession.
     *
     * @return this villager's level
     */
    public int getVillagerLevel();

    /**
     * Sets the level of this villager.
     *
     * A villager with a level of 1 and no experience is liable to lose its
     * profession.
     *
     * This doesn't update the trades of this villager.
     *
     * @param level the new level
     * @throws IllegalArgumentException if level not between [1, 5]
     * @see #increaseLevel(int)
     */
    public void setVillagerLevel(int level);

    /**
     * Gets the trading experience of this villager.
     *
     * @return trading experience
     */
    public int getVillagerExperience();

    /**
     * Sets the trading experience of this villager.
     *
     * @param experience new experience
     * @throws IllegalArgumentException if experience &lt; 0
     */
    public void setVillagerExperience(int experience);

    // Paper start
    /**
     * Increases the level of this villager.
     * The villager will also unlock new recipes unlike the raw
     * method {@link #setVillagerLevel(int)}.
     * <p>
     * A villager with a level of 1 and no experience is liable to lose its
     * profession.
     * <p>
     * A master villager has a level of 5 in its profession and
     * will unlock 10 trades (2 per level).
     *
     * @param amount The amount of level
     * @return Whether trades are unlocked
     * @throws IllegalArgumentException if current level plus the amount
     * isn't between [1, 5] or the amount isn't positive
     * @see #setVillagerLevel(int)
     */
    boolean increaseLevel(int amount);

    /**
     * Gives to this villager some potential new trades
     * based to its profession and level.
     * @param amount The amount of trades to give
     * @return Whether trades are added
     * @throws IllegalArgumentException if the amount isn't positive
     */
    boolean addTrades(int amount);

    /**
     * Gets the amount of times a villager has restocked their trades today
     * @return The amount of trade restocks.
     */
    public int getRestocksToday();

    /**
     * Sets the amount of times a villager has restocked their trades today
     * @param restocksToday new restock count
     */
    public void setRestocksToday(int restocksToday);
    // Paper end

    /**
     * Attempts to make this villager sleep at the given location.
     * <br>
     * The location must be in the current world and have a bed placed at the
     * location. The villager will put its head on the specified block while
     * sleeping.
     *
     * @param location the location of the bed
     * @return whether the sleep was successful
     */
    public boolean sleep(@NotNull Location location);

    /**
     * Causes this villager to wake up if he's currently sleeping.
     *
     * @throws IllegalStateException if not sleeping
     */
    public void wakeup();

    /**
     * Causes this villager to shake his head.
     */
    public void shakeHead();

    /**
     * Convert this Villager into a ZombieVillager as if it was killed by a
     * Zombie.
     *
     * <b>Note:</b> this will fire a EntityTransformEvent
     *
     * @return the converted entity {@link ZombieVillager} or null if the
     * conversion its cancelled
     */
    @Nullable
    public ZombieVillager zombify();

    /**
     * Represents Villager type, usually corresponding to what biome they spawn
     * in.
     */
    interface Type extends OldEnum<Type>, Keyed {

        // Start generate - VillagerType
        // @GeneratedFrom 1.21.6-pre1
        Type DESERT = getType("desert");

        Type JUNGLE = getType("jungle");

        Type PLAINS = getType("plains");

        Type SAVANNA = getType("savanna");

        Type SNOW = getType("snow");

        Type SWAMP = getType("swamp");

        Type TAIGA = getType("taiga");
        // End generate - VillagerType

        @NotNull
        private static Type getType(@NotNull String key) {
            return Registry.VILLAGER_TYPE.getOrThrow(NamespacedKey.minecraft(key));
        }

        /**
         * @param name of the villager type.
         * @return the villager type with the given name.
         * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type valueOf(@NotNull String name) {
            Type type = Registry.VILLAGER_TYPE.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
            Preconditions.checkArgument(type != null, "No villager type found with the name %s", name);
            return type;
        }

        /**
         * @return an array of all known villager types.
         * @deprecated use {@link Registry#iterator()}.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type[] values() {
            return Lists.newArrayList(Registry.VILLAGER_TYPE).toArray(new Type[0]);
        }
    }

    /**
     * Represents the various different Villager professions there may be.
     * Villagers have different trading options depending on their profession,
     */
    interface Profession extends OldEnum<Profession>, Keyed, net.kyori.adventure.translation.Translatable {

        // Start generate - VillagerProfession
        // @GeneratedFrom 1.21.6-pre1
        /**
         * Armorer profession. Wears a black apron. Armorers primarily trade for
         * iron armor, chainmail armor, and sometimes diamond armor.
         */
        Profession ARMORER = getProfession("armorer");

        /**
         * Butcher profession. Wears a white apron. Butchers primarily trade for
         * raw and cooked food.
         */
        Profession BUTCHER = getProfession("butcher");

        /**
         * Cartographer profession. Wears a white robe. Cartographers primarily
         * trade for explorer maps and some paper.
         */
        Profession CARTOGRAPHER = getProfession("cartographer");

        /**
         * Cleric profession. Wears a purple robe. Clerics primarily trade for
         * rotten flesh, gold ingot, redstone, lapis, ender pearl, glowstone,
         * and bottle o' enchanting.
         */
        Profession CLERIC = getProfession("cleric");

        /**
         * Farmer profession. Wears a brown robe. Farmers primarily trade for
         * food-related items.
         */
        Profession FARMER = getProfession("farmer");

        /**
         * Fisherman profession. Wears a brown robe. Fisherman primarily trade
         * for fish, as well as possibly selling string and/or coal.
         */
        Profession FISHERMAN = getProfession("fisherman");

        /**
         * Fletcher profession. Wears a brown robe. Fletchers primarily trade
         * for string, bows, and arrows.
         */
        Profession FLETCHER = getProfession("fletcher");

        /**
         * Leatherworker profession. Wears a white apron. Leatherworkers
         * primarily trade for leather, and leather armor, as well as saddles.
         */
        Profession LEATHERWORKER = getProfession("leatherworker");

        /**
         * Librarian profession. Wears a white robe. Librarians primarily trade
         * for paper, books, and enchanted books.
         */
        Profession LIBRARIAN = getProfession("librarian");

        /**
         * Mason profession.
         */
        Profession MASON = getProfession("mason");

        /**
         * Nitwit profession. Wears a green apron, cannot trade. Nitwit
         * villagers do not do anything. They do not have any trades by default.
         */
        Profession NITWIT = getProfession("nitwit");

        Profession NONE = getProfession("none");

        /**
         * Shepherd profession. Wears a brown robe. Shepherds primarily trade for
         * wool items, and shears.
         */
        Profession SHEPHERD = getProfession("shepherd");

        /**
         * Toolsmith profession. Wears a black apron. Tool smiths primarily
         * trade for iron and diamond tools.
         */
        Profession TOOLSMITH = getProfession("toolsmith");

        /**
         * Weaponsmith profession. Wears a black apron. Weapon smiths primarily
         * trade for iron and diamond weapons, sometimes enchanted.
         */
        Profession WEAPONSMITH = getProfession("weaponsmith");
        // End generate - VillagerProfession

        @NotNull
        private static Profession getProfession(@NotNull String key) {
            return Registry.VILLAGER_PROFESSION.getOrThrow(NamespacedKey.minecraft(key));
        }

        /**
         * @param name of the villager profession.
         * @return the villager profession with the given name.
         * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Profession valueOf(@NotNull String name) {
            Profession profession = Registry.VILLAGER_PROFESSION.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
            Preconditions.checkArgument(profession != null, "No villager profession found with the name %s", name);
            return profession;
        }

        /**
         * @return an array of all known villager professions.
         * @deprecated use {@link Registry#iterator()}.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Profession[] values() {
            return Lists.newArrayList(Registry.VILLAGER_PROFESSION).toArray(new Profession[0]);
        }

        // Paper start
        @Override
        default @NotNull String translationKey() {
            return "entity.minecraft.villager." + this.getKey().getKey();
        }
        // Paper end
    }

    /**
     * Get the {@link com.destroystokyo.paper.entity.villager.Reputation reputation}
     * for a specific player by {@link UUID}.
     *
     * @param uniqueId The {@link UUID} of the player to get the reputation of.
     * @return The player's copied reputation with this villager.
     */
    @NotNull
    public com.destroystokyo.paper.entity.villager.Reputation getReputation(@NotNull UUID uniqueId);

    /**
     * Get all {@link com.destroystokyo.paper.entity.villager.Reputation reputations}
     * for all players mapped by their {@link UUID unique IDs}.
     *
     * @return All {@link com.destroystokyo.paper.entity.villager.Reputation reputations} for all players
     * in a copied map.
     */
    @NotNull
    public Map<UUID, com.destroystokyo.paper.entity.villager.Reputation> getReputations();

    /**
     * Set the {@link com.destroystokyo.paper.entity.villager.Reputation reputation}
     * for a specific player by {@link UUID}.
     *
     * @param uniqueId The {@link UUID} of the player to set the reputation of.
     * @param reputation The {@link com.destroystokyo.paper.entity.villager.Reputation reputation} to set.
     */
    public void setReputation(@NotNull UUID uniqueId, @NotNull com.destroystokyo.paper.entity.villager.Reputation reputation);

    /**
     * Set all {@link com.destroystokyo.paper.entity.villager.Reputation reputations}
     * for all players mapped by their {@link UUID unique IDs}.
     *
     * @param reputations All {@link com.destroystokyo.paper.entity.villager.Reputation reputations}
     * for all players mapped by their {@link UUID unique IDs}.
     */
    public void setReputations(@NotNull Map<UUID, com.destroystokyo.paper.entity.villager.Reputation> reputations);

    /**
     * Clear all reputations from this villager. This removes every single
     * reputation regardless of its impact and the player associated.
     */
    public void clearReputations();
}
