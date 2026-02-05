package org.bukkit.scoreboard;

import com.google.common.base.Preconditions;
import io.papermc.paper.InternalAPIBridge;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a scoreboard criteria, either custom or built-in to the Minecraft server, used to
 * keep track of and manually or automatically change scores on a scoreboard.
 * <p>
 * While this class outlines constants for standard criteria, see {@link io.papermc.paper.statistic.Statistic}
 * for statistically-backed criteria.
 */
@NullMarked
public interface Criteria {

    /**
     * The dummy criteria. Not changed by the server.
     */
    Criteria DUMMY = InternalAPIBridge.get().getCriteria("dummy");
    /**
     * The trigger criteria. Changed when a player runs the /trigger command for an objective.
     */
    Criteria TRIGGER = InternalAPIBridge.get().getCriteria("trigger");
    /**
     * Increments automatically when a player dies.
     */
    Criteria DEATH_COUNT = InternalAPIBridge.get().getCriteria("deathCount");
    /**
     * Increments automatically when a player kills another player.
     */
    Criteria PLAYER_KILL_COUNT = InternalAPIBridge.get().getCriteria("playerKillCount");
    /**
     * Increments automatically when a player kills another living entity.
     */
    Criteria TOTAL_KILL_COUNT = InternalAPIBridge.get().getCriteria("totalKillCount");
    /**
     * Mirrors the player's health points (0 for no health, 20 for maximum default health).
     */
    Criteria HEALTH = InternalAPIBridge.get().getCriteria("health");
    /**
     * Mirrors the player's food points (0 for no food, 20 for maximum food).
     */
    Criteria FOOD = InternalAPIBridge.get().getCriteria("food");
    /**
     * Mirrors the player's air supply (0 for no air, 300 for maximum air).
     */
    Criteria AIR = InternalAPIBridge.get().getCriteria("air");
    /**
     * Mirrors the player's armor points (0 for no armor, 20 for maximum armor).
     */
    Criteria ARMOR = InternalAPIBridge.get().getCriteria("armor");
    /**
     * Mirrors the player's experience points.
     */
    Criteria XP = InternalAPIBridge.get().getCriteria("xp");
    /**
     * Mirrors the player's experience level.
     */
    Criteria LEVEL = InternalAPIBridge.get().getCriteria("level");

    /**
     * Increments automatically when a player kills another player on the black team.
     */
    Criteria TEAM_KILL_BLACK = InternalAPIBridge.get().getCriteria("teamkill.black");
    /**
     * Increments automatically when a player kills another player on the dark blue team.
     */
    Criteria TEAM_KILL_DARK_BLUE = InternalAPIBridge.get().getCriteria("teamkill.dark_blue");
    /**
     * Increments automatically when a player kills another player on the dark green team.
     */
    Criteria TEAM_KILL_DARK_GREEN = InternalAPIBridge.get().getCriteria("teamkill.dark_green");
    /**
     * Increments automatically when a player kills another player on the dark aqua team.
     */
    Criteria TEAM_KILL_DARK_AQUA = InternalAPIBridge.get().getCriteria("teamkill.dark_aqua");
    /**
     * Increments automatically when a player kills another player on the dark red team.
     */
    Criteria TEAM_KILL_DARK_RED = InternalAPIBridge.get().getCriteria("teamkill.dark_red");
    /**
     * Increments automatically when a player kills another player on the dark purple team.
     */
    Criteria TEAM_KILL_DARK_PURPLE = InternalAPIBridge.get().getCriteria("teamkill.dark_purple");
    /**
     * Increments automatically when a player kills another player on the gold team.
     */
    Criteria TEAM_KILL_GOLD = InternalAPIBridge.get().getCriteria("teamkill.gold");
    /**
     * Increments automatically when a player kills another player on the gray team.
     */
    Criteria TEAM_KILL_GRAY = InternalAPIBridge.get().getCriteria("teamkill.gray");
    /**
     * Increments automatically when a player kills another player on the dark gray team.
     */
    Criteria TEAM_KILL_DARK_GRAY = InternalAPIBridge.get().getCriteria("teamkill.dark_gray");
    /**
     * Increments automatically when a player kills another player on the blue team.
     */
    Criteria TEAM_KILL_BLUE = InternalAPIBridge.get().getCriteria("teamkill.blue");
    /**
     * Increments automatically when a player kills another player on the green team.
     */
    Criteria TEAM_KILL_GREEN = InternalAPIBridge.get().getCriteria("teamkill.green");
    /**
     * Increments automatically when a player kills another player on the aqua team.
     */
    Criteria TEAM_KILL_AQUA = InternalAPIBridge.get().getCriteria("teamkill.aqua");
    /**
     * Increments automatically when a player kills another player on the red team.
     */
    Criteria TEAM_KILL_RED = InternalAPIBridge.get().getCriteria("teamkill.red");
    /**
     * Increments automatically when a player kills another player on the light purple team.
     */
    Criteria TEAM_KILL_LIGHT_PURPLE = InternalAPIBridge.get().getCriteria("teamkill.light_purple");
    /**
     * Increments automatically when a player kills another player on the yellow team.
     */
    Criteria TEAM_KILL_YELLOW = InternalAPIBridge.get().getCriteria("teamkill.yellow");
    /**
     * Increments automatically when a player kills another player on the white team.
     */
    Criteria TEAM_KILL_WHITE = InternalAPIBridge.get().getCriteria("teamkill.white");

    /**
     * Increments automatically when a player is killed by a player on the black team.
     */
    Criteria KILLED_BY_TEAM_BLACK = InternalAPIBridge.get().getCriteria("killedByTeam.black");
    /**
     * Increments automatically when a player is killed by a player on the dark blue team.
     */
    Criteria KILLED_BY_TEAM_DARK_BLUE = InternalAPIBridge.get().getCriteria("killedByTeam.dark_blue");
    /**
     * Increments automatically when a player is killed by a player on the dark green team.
     */
    Criteria KILLED_BY_TEAM_DARK_GREEN = InternalAPIBridge.get().getCriteria("killedByTeam.dark_green");
    /**
     * Increments automatically when a player is killed by a player on the dark aqua team.
     */
    Criteria KILLED_BY_TEAM_DARK_AQUA = InternalAPIBridge.get().getCriteria("killedByTeam.dark_aqua");
    /**
     * Increments automatically when a player is killed by a player on the dark red team.
     */
    Criteria KILLED_BY_TEAM_DARK_RED = InternalAPIBridge.get().getCriteria("killedByTeam.dark_red");
    /**
     * Increments automatically when a player is killed by a player on the dark purple team.
     */
    Criteria KILLED_BY_TEAM_DARK_PURPLE = InternalAPIBridge.get().getCriteria("killedByTeam.dark_purple");
    /**
     * Increments automatically when a player is killed by a player on the gold team.
     */
    Criteria KILLED_BY_TEAM_GOLD = InternalAPIBridge.get().getCriteria("killedByTeam.gold");
    /**
     * Increments automatically when a player is killed by a player on the gray team.
     */
    Criteria KILLED_BY_TEAM_GRAY = InternalAPIBridge.get().getCriteria("killedByTeam.gray");
    /**
     * Increments automatically when a player is killed by a player on the dark gray team.
     */
    Criteria KILLED_BY_TEAM_DARK_GRAY = InternalAPIBridge.get().getCriteria("killedByTeam.dark_gray");
    /**
     * Increments automatically when a player is killed by a player on the blue team.
     */
    Criteria KILLED_BY_TEAM_BLUE = InternalAPIBridge.get().getCriteria("killedByTeam.blue");
    /**
     * Increments automatically when a player is killed by a player on the green team.
     */
    Criteria KILLED_BY_TEAM_GREEN = InternalAPIBridge.get().getCriteria("killedByTeam.green");
    /**
     * Increments automatically when a player is killed by a player on the aqua team.
     */
    Criteria KILLED_BY_TEAM_AQUA = InternalAPIBridge.get().getCriteria("killedByTeam.aqua");
    /**
     * Increments automatically when a player is killed by a player on the red team.
     */
    Criteria KILLED_BY_TEAM_RED = InternalAPIBridge.get().getCriteria("killedByTeam.red");
    /**
     * Increments automatically when a player is killed by a player on the light purple team.
     */
    Criteria KILLED_BY_TEAM_LIGHT_PURPLE = InternalAPIBridge.get().getCriteria("killedByTeam.light_purple");
    /**
     * Increments automatically when a player is killed by a player on the yellow team.
     */
    Criteria KILLED_BY_TEAM_YELLOW = InternalAPIBridge.get().getCriteria("killedByTeam.yellow");
    /**
     * Increments automatically when a player is killed by a player on the white team.
     */
    Criteria KILLED_BY_TEAM_WHITE = InternalAPIBridge.get().getCriteria("killedByTeam.white");

    /**
     * Get the name of this criteria (its unique id).
     *
     * @return the name
     */
    String getName();

    /**
     * Get whether or not this criteria is read only. If read only, scoreboards with this criteria
     * cannot have their scores changed.
     *
     * @return true if read only, false otherwise
     */
    boolean isReadOnly();

    /**
     * Get the {@link RenderType} used by default for this criteria.
     *
     * @return the default render type
     */
    RenderType getDefaultRenderType();

    /**
     * Get a {@link Criteria} for the specified statistic pertaining to blocks or items.
     * <p>
     * This method expects a {@link Statistic} of {@link Type#BLOCK} or {@link Type#ITEM} and the
     * {@link Material} matching said type (e.g. BLOCK statistics require materials where
     * {@link Material#isBlock()} is true). This acts as a convenience to create more complex
     * compound criteria such as those that increment on block breaks, or item uses. An example
     * would be {@code Criteria.statistic(Statistic.CRAFT_ITEM, Material.STICK)}, returning a
     * Criteria representing "minecraft.crafted:minecraft.stick" which will increment when the
     * player crafts a stick.
     * <p>
     * If the provided statistic does not require additional data, {@link #statistic(Statistic)}
     * is called and returned instead.
     * <p>
     * This method provides no guarantee that any given criteria exists on the vanilla server.
     *
     * @param statistic the statistic for which to get a criteria
     * @param material the relevant material
     * @return the criteria
     * @throws IllegalArgumentException if {@link Statistic#getType()} is anything other than
     * {@link Type#BLOCK} or {@link Type#ITEM}
     * @throws IllegalArgumentException if {@link Statistic#getType()} is {@link Type#BLOCK}, but
     * {@link Material#isBlock()} is false
     * @throws IllegalArgumentException if {@link Statistic#getType()} is {@link Type#ITEM}, but
     * {@link Material#isItem()} is false
     * @deprecated use {@link io.papermc.paper.statistic.Statistic}
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    static Criteria statistic(Statistic statistic, Material material) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        Preconditions.checkArgument(material != null, "material must not be null");
        return statistic.toModern(null, material);
    }

    /**
     * Get a {@link Criteria} for the specified statistic pertaining to an entity type.
     * <p>
     * This method expects a {@link Statistic} of {@link Type#ENTITY}. This acts as a convenience
     * to create more complex compound criteria such as being killed by a specific entity type.
     * An example would be {@code Criteria.statistic(Statistic.KILL_ENTITY, EntityType.CREEPER)},
     * returning a Criteria representing "minecraft.killed:minecraft.creeper" which will increment
     * when the player kills a creepers.
     * <p>
     * If the provided statistic does not require additional data, {@link #statistic(Statistic)}
     * is called and returned instead.
     * <p>
     * This method provides no guarantee that any given criteria exists on the vanilla server.
     *
     * @param statistic the statistic for which to get a criteria
     * @param entityType the relevant entity type
     * @return the criteria
     * @throws IllegalArgumentException if {@link Statistic#getType()} is not {@link Type#ENTITY}
     * @deprecated use {@link io.papermc.paper.statistic.Statistic}
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    static Criteria statistic(Statistic statistic, EntityType entityType) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        Preconditions.checkArgument(entityType != null, "entityType must not be null");
        return statistic.toModern(entityType, null);
    }

    /**
     * Get a {@link Criteria} for the specified statistic.
     * <p>
     * An example would be {@code Criteria.statistic(Statistic.FLY_ONE_CM)}, returning a Criteria
     * representing "minecraft.custom:minecraft.aviate_one_cm" which will increment when the player
     * flies with an elytra.
     * <p>
     * This method provides no guarantee that any given criteria exists on the vanilla server. All
     * statistics are accepted, however some may not operate as expected if additional data is
     * required. For block/item-related statistics, see {@link #statistic(Statistic, Material)},
     * and for entity-related statistics, see {@link #statistic(Statistic, EntityType)}
     *
     * @param statistic the statistic for which to get a criteria
     * @return the criteria
     * @deprecated Use {@link io.papermc.paper.statistic.Statistic}
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    static Criteria statistic(Statistic statistic) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        return statistic.toModern(null, null);
    }

    /**
     * Get (or create) a new {@link Criteria} by its name.
     *
     * @param name the criteria name
     * @return the created criteria
     * @deprecated use the constants here, or {@link io.papermc.paper.statistic.Statistic}
     */
    @Deprecated(since = "1.21.11", forRemoval = true)
    static Criteria create(String name) {
        return InternalAPIBridge.get().getCriteria(name);
    }

}
