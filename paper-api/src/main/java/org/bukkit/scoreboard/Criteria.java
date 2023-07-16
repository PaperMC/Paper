package org.bukkit.scoreboard;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.Statistic.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a scoreboard criteria, either custom or built-in to the Minecraft server, used to
 * keep track of and manually or automatically change scores on a scoreboard.
 * <p>
 * While this class outlines constants for standard criteria, see {@link #statistic(Statistic)}
 * (and its overloads) to create instances for statistically-backed criteria.
 */
public interface Criteria {

    /**
     * The dummy criteria. Not changed by the server.
     */
    public static final Criteria DUMMY = Bukkit.getScoreboardCriteria("dummy");
    /**
     * The trigger criteria. Changed when a player runs the /trigger command for an objective.
     */
    public static final Criteria TRIGGER = Bukkit.getScoreboardCriteria("trigger");
    /**
     * Increments automatically when a player dies.
     */
    public static final Criteria DEATH_COUNT = Bukkit.getScoreboardCriteria("deathCount");
    /**
     * Increments automatically when a player kills another player.
     */
    public static final Criteria PLAYER_KILL_COUNT = Bukkit.getScoreboardCriteria("playerKillCount");
    /**
     * Increments automatically when a player kills another living entity.
     */
    public static final Criteria TOTAL_KILL_COUNT = Bukkit.getScoreboardCriteria("totalKillCount");
    /**
     * Mirrors the player's health points (0 for no health, 20 for maximum default health).
     */
    public static final Criteria HEALTH = Bukkit.getScoreboardCriteria("health");
    /**
     * Mirrors the player's food points (0 for no food, 20 for maximum food).
     */
    public static final Criteria FOOD = Bukkit.getScoreboardCriteria("food");
    /**
     * Mirrors the player's air supply (0 for no air, 300 for maximum air).
     */
    public static final Criteria AIR = Bukkit.getScoreboardCriteria("air");
    /**
     * Mirrors the player's armor points (0 for no armor, 20 for maximum armor).
     */
    public static final Criteria ARMOR = Bukkit.getScoreboardCriteria("armor");
    /**
     * Mirrors the player's experience points.
     */
    public static final Criteria XP = Bukkit.getScoreboardCriteria("xp");
    /**
     * Mirrors the player's experience level.
     */
    public static final Criteria LEVEL = Bukkit.getScoreboardCriteria("level");

    /**
     * Increments automatically when a player kills another player on the black team.
     */
    public static final Criteria TEAM_KILL_BLACK = Bukkit.getScoreboardCriteria("teamkill.black");
    /**
     * Increments automatically when a player kills another player on the dark blue team.
     */
    public static final Criteria TEAM_KILL_DARK_BLUE = Bukkit.getScoreboardCriteria("teamkill.dark_blue");
    /**
     * Increments automatically when a player kills another player on the dark green team.
     */
    public static final Criteria TEAM_KILL_DARK_GREEN = Bukkit.getScoreboardCriteria("teamkill.dark_green");
    /**
     * Increments automatically when a player kills another player on the dark aqua team.
     */
    public static final Criteria TEAM_KILL_DARK_AQUA = Bukkit.getScoreboardCriteria("teamkill.dark_aqua");
    /**
     * Increments automatically when a player kills another player on the dark red team.
     */
    public static final Criteria TEAM_KILL_DARK_RED = Bukkit.getScoreboardCriteria("teamkill.dark_red");
    /**
     * Increments automatically when a player kills another player on the dark purple team.
     */
    public static final Criteria TEAM_KILL_DARK_PURPLE = Bukkit.getScoreboardCriteria("teamkill.dark_purple");
    /**
     * Increments automatically when a player kills another player on the gold team.
     */
    public static final Criteria TEAM_KILL_GOLD = Bukkit.getScoreboardCriteria("teamkill.gold");
    /**
     * Increments automatically when a player kills another player on the gray team.
     */
    public static final Criteria TEAM_KILL_GRAY = Bukkit.getScoreboardCriteria("teamkill.gray");
    /**
     * Increments automatically when a player kills another player on the dark gray team.
     */
    public static final Criteria TEAM_KILL_DARK_GRAY = Bukkit.getScoreboardCriteria("teamkill.dark_gray");
    /**
     * Increments automatically when a player kills another player on the blue team.
     */
    public static final Criteria TEAM_KILL_BLUE = Bukkit.getScoreboardCriteria("teamkill.blue");
    /**
     * Increments automatically when a player kills another player on the green team.
     */
    public static final Criteria TEAM_KILL_GREEN = Bukkit.getScoreboardCriteria("teamkill.green");
    /**
     * Increments automatically when a player kills another player on the aqua team.
     */
    public static final Criteria TEAM_KILL_AQUA = Bukkit.getScoreboardCriteria("teamkill.aqua");
    /**
     * Increments automatically when a player kills another player on the red team.
     */
    public static final Criteria TEAM_KILL_RED = Bukkit.getScoreboardCriteria("teamkill.red");
    /**
     * Increments automatically when a player kills another player on the light purple team.
     */
    public static final Criteria TEAM_KILL_LIGHT_PURPLE = Bukkit.getScoreboardCriteria("teamkill.light_purple");
    /**
     * Increments automatically when a player kills another player on the yellow team.
     */
    public static final Criteria TEAM_KILL_YELLOW = Bukkit.getScoreboardCriteria("teamkill.yellow");
    /**
     * Increments automatically when a player kills another player on the white team.
     */
    public static final Criteria TEAM_KILL_WHITE = Bukkit.getScoreboardCriteria("teamkill.white");

    /**
     * Increments automatically when a player is killed by a player on the black team.
     */
    public static final Criteria KILLED_BY_TEAM_BLACK = Bukkit.getScoreboardCriteria("killedByTeam.black");
    /**
     * Increments automatically when a player is killed by a player on the dark blue team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_BLUE = Bukkit.getScoreboardCriteria("killedByTeam.dark_blue");
    /**
     * Increments automatically when a player is killed by a player on the dark green team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_GREEN = Bukkit.getScoreboardCriteria("killedByTeam.dark_green");
    /**
     * Increments automatically when a player is killed by a player on the dark aqua team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_AQUA = Bukkit.getScoreboardCriteria("killedByTeam.dark_aqua");
    /**
     * Increments automatically when a player is killed by a player on the dark red team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_RED = Bukkit.getScoreboardCriteria("killedByTeam.dark_red");
    /**
     * Increments automatically when a player is killed by a player on the dark purple team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_PURPLE = Bukkit.getScoreboardCriteria("killedByTeam.dark_purple");
    /**
     * Increments automatically when a player is killed by a player on the gold team.
     */
    public static final Criteria KILLED_BY_TEAM_GOLD = Bukkit.getScoreboardCriteria("killedByTeam.gold");
    /**
     * Increments automatically when a player is killed by a player on the gray team.
     */
    public static final Criteria KILLED_BY_TEAM_GRAY = Bukkit.getScoreboardCriteria("killedByTeam.gray");
    /**
     * Increments automatically when a player is killed by a player on the dark gray team.
     */
    public static final Criteria KILLED_BY_TEAM_DARK_GRAY = Bukkit.getScoreboardCriteria("killedByTeam.dark_gray");
    /**
     * Increments automatically when a player is killed by a player on the blue team.
     */
    public static final Criteria KILLED_BY_TEAM_BLUE = Bukkit.getScoreboardCriteria("killedByTeam.blue");
    /**
     * Increments automatically when a player is killed by a player on the green team.
     */
    public static final Criteria KILLED_BY_TEAM_GREEN = Bukkit.getScoreboardCriteria("killedByTeam.green");
    /**
     * Increments automatically when a player is killed by a player on the aqua team.
     */
    public static final Criteria KILLED_BY_TEAM_AQUA = Bukkit.getScoreboardCriteria("killedByTeam.aqua");
    /**
     * Increments automatically when a player is killed by a player on the red team.
     */
    public static final Criteria KILLED_BY_TEAM_RED = Bukkit.getScoreboardCriteria("killedByTeam.red");
    /**
     * Increments automatically when a player is killed by a player on the light purple team.
     */
    public static final Criteria KILLED_BY_TEAM_LIGHT_PURPLE = Bukkit.getScoreboardCriteria("killedByTeam.light_purple");
    /**
     * Increments automatically when a player is killed by a player on the yellow team.
     */
    public static final Criteria KILLED_BY_TEAM_YELLOW = Bukkit.getScoreboardCriteria("killedByTeam.yellow");
    /**
     * Increments automatically when a player is killed by a player on the white team.
     */
    public static final Criteria KILLED_BY_TEAM_WHITE = Bukkit.getScoreboardCriteria("killedByTeam.white");

    /**
     * Get the name of this criteria (its unique id).
     *
     * @return the name
     */
    @NotNull
    public String getName();

    /**
     * Get whether or not this criteria is read only. If read only, scoreboards with this criteria
     * cannot have their scores changed.
     *
     * @return true if read only, false otherwise
     */
    public boolean isReadOnly();

    /**
     * Get the {@link RenderType} used by default for this criteria.
     *
     * @return the default render type
     */
    @NotNull
    public RenderType getDefaultRenderType();

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
     */
    @NotNull
    public static Criteria statistic(@NotNull Statistic statistic, @NotNull Material material) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        Preconditions.checkArgument(material != null, "material must not be null");

        Type type = statistic.getType();
        Preconditions.checkArgument(type == Type.BLOCK || type == Type.ITEM, "statistic type must be either BLOCK or ITEM, given %s", type);
        Preconditions.checkArgument(type != Type.BLOCK || material.isBlock(), "statistic type is BLOCK but got non-block Material, %s", material);
        Preconditions.checkArgument(type != Type.ITEM || material.isItem(), "statistic type is ITEM but got non-item Material, %s", material);

        // Good use case for a switch expression
        if (type == Type.BLOCK) {
            switch (statistic) {
                case MINE_BLOCK:
                    return Bukkit.getScoreboardCriteria("minecraft.mined:minecraft." + material.getKey().getKey());
                default:
                    break;
            }
        } else if (type == Type.ITEM) {
            switch (statistic) {
                case BREAK_ITEM:
                    return Bukkit.getScoreboardCriteria("minecraft.broken:minecraft." + material.getKey().getKey());
                case CRAFT_ITEM:
                    return Bukkit.getScoreboardCriteria("minecraft.crafted:minecraft." + material.getKey().getKey());
                case USE_ITEM:
                    return Bukkit.getScoreboardCriteria("minecraft.used:minecraft." + material.getKey().getKey());
                case PICKUP:
                    return Bukkit.getScoreboardCriteria("minecraft.picked_up:minecraft." + material.getKey().getKey());
                case DROP:
                    return Bukkit.getScoreboardCriteria("minecraft.dropped:minecraft." + material.getKey().getKey());
                default:
                    break;
            }
        }

        return statistic(statistic); // Fallback to a regular statistic
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
     */
    @NotNull
    public static Criteria statistic(@NotNull Statistic statistic, @NotNull EntityType entityType) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        Preconditions.checkArgument(entityType != null, "entityType must not be null");
        Preconditions.checkArgument(statistic.getType() == Type.ENTITY, "statistic type must be ENTITY, given %s", statistic.getType());

        switch (statistic) {
            case KILL_ENTITY:
                return Bukkit.getScoreboardCriteria("minecraft.killed:minecraft." + entityType.getKey().getKey());
            case ENTITY_KILLED_BY:
                return Bukkit.getScoreboardCriteria("minecraft.killed_by:minecraft." + entityType.getKey().getKey());
            default:
                break;
        }

        return statistic(statistic); // Fallback to a regular statistic
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
     */
    @NotNull
    public static Criteria statistic(@NotNull Statistic statistic) {
        Preconditions.checkArgument(statistic != null, "statistic must not be null");
        return Bukkit.getScoreboardCriteria("minecraft.custom:minecraft." + statistic.getKey().getKey());
    }

    /**
     * Get (or create) a new {@link Criteria} by its name.
     *
     * @param name the criteria name
     * @return the created criteria
     */
    @NotNull
    public static Criteria create(@NotNull String name) {
        return Bukkit.getScoreboardCriteria(name);
    }

}
