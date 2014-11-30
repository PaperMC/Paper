package org.bukkit.entity;

import java.net.InetSocketAddress;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Represents a player, connected or not
 */
public interface Player extends HumanEntity, Conversable, CommandSender, OfflinePlayer, PluginMessageRecipient {

    /**
     * Gets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @return the friendly name
     */
    public String getDisplayName();

    /**
     * Sets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @param name The new display name.
     */
    public void setDisplayName(String name);

    /**
     * Gets the name that is shown on the player list.
     *
     * @return the player list name
     */
    public String getPlayerListName();

    /**
     * Sets the name that is shown on the in-game player list.
     * <p>
     * The name cannot be longer than 16 characters, but {@link ChatColor} is
     * supported.
     * <p>
     * If the value is null, the name will be identical to {@link #getName()}.
     * <p>
     * This name is case sensitive and unique, two names with different casing
     * will appear as two different people. If a player joins afterwards with
     * a name that conflicts with a player's custom list name, the joining
     * player's player list name will have a random number appended to it (1-2
     * characters long in the default implementation). If the joining player's
     * name is 15 or 16 characters long, part of the name will be truncated at
     * the end to allow the addition of the two digits.
     *
     * @param name new player list name
     * @throws IllegalArgumentException if the name is already used by someone
     *     else
     * @throws IllegalArgumentException if the length of the name is too long
     */
    public void setPlayerListName(String name);

    /**
     * Set the target of the player's compass.
     *
     * @param loc Location to point to
     */
    public void setCompassTarget(Location loc);

    /**
     * Get the previously set compass target.
     *
     * @return location of the target
     */
    public Location getCompassTarget();

    /**
     * Gets the socket address of this player
     *
     * @return the player's address
     */
    public InetSocketAddress getAddress();

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    public void sendRawMessage(String message);

    /**
     * Kicks player with custom kick message.
     *
     * @param message kick message
     */
    public void kickPlayer(String message);

    /**
     * Says a message (or runs a command).
     *
     * @param msg message to print
     */
    public void chat(String msg);

    /**
     * Makes the player perform the given command
     *
     * @param command Command to perform
     * @return true if the command was successful, otherwise false
     */
    public boolean performCommand(String command);

    /**
     * Returns if the player is in sneak mode
     *
     * @return true if player is in sneak mode
     */
    public boolean isSneaking();

    /**
     * Sets the sneak mode the player
     *
     * @param sneak true if player should appear sneaking
     */
    public void setSneaking(boolean sneak);

    /**
     * Gets whether the player is sprinting or not.
     *
     * @return true if player is sprinting.
     */
    public boolean isSprinting();

    /**
     * Sets whether the player is sprinting or not.
     *
     * @param sprinting true if the player should be sprinting
     */
    public void setSprinting(boolean sprinting);

    /**
     * Saves the players current location, health, inventory, motion, and
     * other information into the username.dat file, in the world/player
     * folder
     */
    public void saveData();

    /**
     * Loads the players current location, health, inventory, motion, and
     * other information from the username.dat file, in the world/player
     * folder.
     * <p>
     * Note: This will overwrite the players current inventory, health,
     * motion, etc, with the state from the saved dat file.
     */
    public void loadData();

    /**
     * Sets whether the player is ignored as not sleeping. If everyone is
     * either sleeping or has this flag set, then time will advance to the
     * next day. If everyone has this flag set but no one is actually in bed,
     * then nothing will happen.
     *
     * @param isSleeping Whether to ignore.
     */
    public void setSleepingIgnored(boolean isSleeping);

    /**
     * Returns whether the player is sleeping ignored.
     *
     * @return Whether player is ignoring sleep.
     */
    public boolean isSleepingIgnored();

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *
     * @param loc The location of a note block.
     * @param instrument The instrument ID.
     * @param note The note ID.
     * @deprecated Magic value
     */
    @Deprecated
    public void playNote(Location loc, byte instrument, byte note);

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *
     * @param loc The location of a note block
     * @param instrument The instrument
     * @param note The note
     */
    public void playNote(Location loc, Instrument instrument, Note note);


    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Location location, Sound sound, float volume, float pitch);

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null. No
     * sound will be heard by the player if their client does not have the
     * respective sound for the value passed.
     *
     * @param location the location to play the sound
     * @param sound the internal sound name to play
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     * @deprecated Magic value
     */
    @Deprecated
    public void playSound(Location location, String sound, float volume, float pitch);

    /**
     * Plays an effect to just this player.
     *
     * @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @deprecated Magic value
     */
    @Deprecated
    public void playEffect(Location loc, Effect effect, int data);

    /**
     * Plays an effect to just this player.
     *
     * @param <T> the data based based on the type of the effect
     * @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    public <T> void playEffect(Location loc, Effect effect, T data);

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param material The new block
     * @param data The block data
     * @deprecated Magic value
     */
    @Deprecated
    public void sendBlockChange(Location loc, Material material, byte data);

    /**
     * Send a chunk change. This fakes a chunk change packet for a user at a
     * certain location. The updated cuboid must be entirely within a single
     * chunk. This will not actually change the world in any way.
     * <p>
     * At least one of the dimensions of the cuboid must be even. The size of
     * the data buffer must be 2.5*sx*sy*sz and formatted in accordance with
     * the Packet51 format.
     *
     * @param loc The location of the cuboid
     * @param sx The x size of the cuboid
     * @param sy The y size of the cuboid
     * @param sz The z size of the cuboid
     * @param data The data to be sent
     * @return true if the chunk change packet was sent
     * @deprecated Magic value
     */
    @Deprecated
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data);

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param material The new block ID
     * @param data The block data
     * @deprecated Magic value
     */
    @Deprecated
    public void sendBlockChange(Location loc, int material, byte data);

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via {@link #sendBlockChange(org.bukkit.Location, int, byte)} or
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     */
    public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException;

    /**
     * Render a map and send it to the player in its entirety. This may be
     * used when streaming the map in the normal manner is not desirable.
     *
     * @param map The map to be sent
     */
    public void sendMap(MapView map);

    /**
     * Forces an update of the player's entire inventory.
     *
     * @deprecated This method should not be relied upon as it is a temporary
     *     work-around for a larger, more complicated issue.
     */
    @Deprecated
    public void updateInventory();

    /**
     * Awards the given achievement and any parent achievements that the
     * player does not have.
     *
     * @param achievement Achievement to award
     * @throws IllegalArgumentException if achievement is null
     */
    public void awardAchievement(Achievement achievement);

    /**
     * Removes the given achievement and any children achievements that the
     * player has.
     *
     * @param achievement Achievement to remove
     * @throws IllegalArgumentException if achievement is null
     */
    public void removeAchievement(Achievement achievement);

    /**
     * Gets whether this player has the given achievement.
     *
     * @param achievement the achievement to check
     * @return whether the player has the achievement
     * @throws IllegalArgumentException if achievement is null
     */
    public boolean hasAchievement(Achievement achievement);

    /**
     * Increments the given statistic for this player.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, 1)</code>
     *
     * @param statistic Statistic to increment
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException;

    /**
     * Increments the given statistic for this player.
     *
     * @param statistic Statistic to increment
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player.
     *
     * @param statistic Statistic to decrement
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException;

    /**
     * Sets the given statistic for this player.
     *
     * @param statistic Statistic to set
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException;

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     */
    public int getStatistic(Statistic statistic) throws IllegalArgumentException;

    /**
     * Increments the given statistic for this player for the given material.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, Material, 1)</code>
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player for the given material.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, Material, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @param material Material to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @param material Material offset of the statistic
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

    /**
     * Increments the given statistic for this player for the given material.
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player for the given material.
     *
     * @param statistic Statistic to decrement
     * @param material Material to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException;

    /**
     * Sets the given statistic for this player for the given material.
     *
     * @param statistic Statistic to set
     * @param material Material to offset the statistic with
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException;

    /**
     * Increments the given statistic for this player for the given entity.
     * <p>
     * This is equivalent to the following code:
     * <code>incrementStatistic(Statistic, EntityType, 1)</code>
     *
     * @param statistic Statistic to increment
     * @param entityType EntityType to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player for the given entity.
     * <p>
     * This is equivalent to the following code:
     * <code>decrementStatistic(Statistic, EntityType, 1)</code>
     *
     * @param statistic Statistic to decrement
     * @param entityType EntityType to offset the statistic with
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @param entityType EntityType offset of the statistic
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

    /**
     * Increments the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to increment
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException;

    /**
     * Decrements the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to decrement
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount);

    /**
     * Sets the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to set
     * @param entityType EntityType to offset the statistic with
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     */
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue);

    /**
     * Sets the current time on the player's client. When relative is true the
     * player's time will be kept synchronized to its world time with the
     * specified offset.
     * <p>
     * When using non relative time the player's time will stay fixed at the
     * specified time parameter. It's up to the caller to continue updating
     * the player's time. To restore player time to normal use
     * resetPlayerTime().
     *
     * @param time The current player's perceived time or the player's time
     *     offset from the server time.
     * @param relative When true the player time is kept relative to its world
     *     time.
     */
    public void setPlayerTime(long time, boolean relative);

    /**
     * Returns the player's current timestamp.
     *
     * @return The player's time
     */
    public long getPlayerTime();

    /**
     * Returns the player's current time offset relative to server time, or
     * the current player's fixed time if the player's time is absolute.
     *
     * @return The player's time
     */
    public long getPlayerTimeOffset();

    /**
     * Returns true if the player's time is relative to the server time,
     * otherwise the player's time is absolute and will not change its current
     * time unless done so with setPlayerTime().
     *
     * @return true if the player's time is relative to the server time.
     */
    public boolean isPlayerTimeRelative();

    /**
     * Restores the normal condition where the player's time is synchronized
     * with the server time.
     * <p>
     * Equivalent to calling setPlayerTime(0, true).
     */
    public void resetPlayerTime();

    /**
     * Sets the type of weather the player will see.  When used, the weather
     * status of the player is locked until {@link #resetPlayerWeather()} is
     * used.
     *
     * @param type The WeatherType enum type the player should experience
     */
    public void setPlayerWeather(WeatherType type);

    /**
     * Returns the type of weather the player is currently experiencing.
     *
     * @return The WeatherType that the player is currently experiencing or
     *     null if player is seeing server weather.
     */
    public WeatherType getPlayerWeather();

    /**
     * Restores the normal condition where the player's weather is controlled
     * by server conditions.
     */
    public void resetPlayerWeather();

    /**
     * Gives the player the amount of experience specified.
     *
     * @param amount Exp amount to give
     */
    public void giveExp(int amount);

    /**
     * Gives the player the amount of experience levels specified. Levels can
     * be taken by specifying a negative amount.
     *
     * @param amount amount of experience levels to give or take
     */
    public void giveExpLevels(int amount);

    /**
     * Gets the players current experience points towards the next level.
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @return Current experience points
     */
    public float getExp();

    /**
     * Sets the players current experience points towards the next level
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @param exp New experience points
     */
    public void setExp(float exp);

    /**
     * Gets the players current experience level
     *
     * @return Current experience level
     */
    public int getLevel();

    /**
     * Sets the players current experience level
     *
     * @param level New experience level
     */
    public void setLevel(int level);

    /**
     * Gets the players total experience points
     *
     * @return Current total experience points
     */
    public int getTotalExperience();

    /**
     * Sets the players current experience level
     *
     * @param exp New experience level
     */
    public void setTotalExperience(int exp);

    /**
     * Gets the players current exhaustion level.
     * <p>
     * Exhaustion controls how fast the food level drops. While you have a
     * certain amount of exhaustion, your saturation will drop to zero, and
     * then your food will drop to zero.
     *
     * @return Exhaustion level
     */
    public float getExhaustion();

    /**
     * Sets the players current exhaustion level
     *
     * @param value Exhaustion level
     */
    public void setExhaustion(float value);

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated {@literal >} 0.
     *
     * @return Saturation level
     */
    public float getSaturation();

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     */
    public void setSaturation(float value);

    /**
     * Gets the players current food level
     *
     * @return Food level
     */
    public int getFoodLevel();

    /**
     * Sets the players current food level
     *
     * @param value New food level
     */
    public void setFoodLevel(int value);

    /**
     * Gets the Location where the player will spawn at their bed, null if
     * they have not slept in one or their current bed spawn is invalid.
     *
     * @return Bed Spawn Location if bed exists, otherwise null.
     */
    public Location getBedSpawnLocation();

    /**
     * Sets the Location where the player will spawn at their bed.
     *
     * @param location where to set the respawn location
     */
    public void setBedSpawnLocation(Location location);

    /**
     * Sets the Location where the player will spawn at their bed.
     *
     * @param location where to set the respawn location
     * @param force whether to forcefully set the respawn location even if a
     *     valid bed is not present
     */
    public void setBedSpawnLocation(Location location, boolean force);

    /**
     * Determines if the Player is allowed to fly via jump key double-tap like
     * in creative mode.
     *
     * @return True if the player is allowed to fly.
     */
    public boolean getAllowFlight();

    /**
     * Sets if the Player is allowed to fly via jump key double-tap like in
     * creative mode.
     *
     * @param flight If flight should be allowed.
     */
    public void setAllowFlight(boolean flight);

    /**
     * Hides a player from this player
     *
     * @param player Player to hide
     */
    public void hidePlayer(Player player);

    /**
     * Allows this player to see a player that was previously hidden
     *
     * @param player Player to show
     */
    public void showPlayer(Player player);

    /**
     * Checks to see if a player has been hidden from this player
     *
     * @param player Player to check
     * @return True if the provided player is not being hidden from this
     *     player
     */
    public boolean canSee(Player player);

    /**
     * Checks to see if this player is currently standing on a block. This
     * information may not be reliable, as it is a state provided by the
     * client, and may therefore not be accurate.
     *
     * @return True if the player standing on a solid block, else false.
     * @deprecated Inconsistent with {@link
     *     org.bukkit.entity.Entity#isOnGround()}
     */
    @Deprecated
    public boolean isOnGround();

    /**
     * Checks to see if this player is currently flying or not.
     *
     * @return True if the player is flying, else false.
     */
    public boolean isFlying();

    /**
     * Makes this player start or stop flying.
     *
     * @param value True to fly.
     */
    public void setFlying(boolean value);

    /**
     * Sets the speed at which a client will fly. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    public void setFlySpeed(float value) throws IllegalArgumentException;

    /**
     * Sets the speed at which a client will walk. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    public void setWalkSpeed(float value) throws IllegalArgumentException;

    /**
     * Gets the current allowed speed that a client can fly.
     *
     * @return The current allowed speed, from -1 to 1
     */
    public float getFlySpeed();

    /**
     * Gets the current allowed speed that a client can walk.
     *
     * @return The current allowed speed, from -1 to 1
     */
    public float getWalkSpeed();

    /**
     * Request that the player's client download and switch texture packs.
     * <p>
     * The player's client will download the new texture pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached the same
     * texture pack in the past, it will perform a quick timestamp check over
     * the network to determine if the texture pack has changed and needs to
     * be downloaded again. When this request is sent for the very first time
     * from a given server, the client will first display a confirmation GUI
     * to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server textures on their client, in which
     *     case this method will have no affect on them.
     * <li>There is no concept of resetting texture packs back to default
     *     within Minecraft, so players will have to relog to do so.
     * </ul>
     *
     * @param url The URL from which the client will download the texture
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long.
     * @deprecated Minecraft no longer uses textures packs. Instead you
     *     should use {@link #setResourcePack(String)}.
     */
    @Deprecated
    public void setTexturePack(String url);

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached the same
     * resource pack in the past, it will perform a quick timestamp check
     * over the network to determine if the resource pack has changed and
     * needs to be downloaded again. When this request is sent for the very
     * first time from a given server, the client will first display a
     * confirmation GUI to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no affect on them.
     * <li>There is no concept of resetting resource packs back to default
     *     within Minecraft, so players will have to relog to do so.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    public void setResourcePack(String url);

    /**
     * Gets the Scoreboard displayed to this player
     *
     * @return The current scoreboard seen by this player
     */
    public Scoreboard getScoreboard();

    /**
     * Sets the player's visible Scoreboard.
     *
     * @param scoreboard New Scoreboard for the player
     * @throws IllegalArgumentException if scoreboard is null
     * @throws IllegalArgumentException if scoreboard was not created by the
     *     {@link org.bukkit.scoreboard.ScoreboardManager scoreboard manager}
     * @throws IllegalStateException if this is a player that is not logged
     *     yet or has logged out
     */
    public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException;

    /**
     * Gets if the client is displayed a 'scaled' health, that is, health on a
     * scale from 0-{@link #getHealthScale()}.
     *
     * @return if client health display is scaled
     * @see Player#setHealthScaled(boolean)
     */
    public boolean isHealthScaled();

    /**
     * Sets if the client is displayed a 'scaled' health, that is, health on a
     * scale from 0-{@link #getHealthScale()}.
     * <p>
     * Displayed health follows a simple formula <code>displayedHealth =
     * getHealth() / getMaxHealth() * getHealthScale()</code>.
     *
     * @param scale if the client health display is scaled
     */
    public void setHealthScaled(boolean scale);

    /**
     * Sets the number to scale health to for the client; this will also
     * {@link #setHealthScaled(boolean) setHealthScaled(true)}.
     * <p>
     * Displayed health follows a simple formula <code>displayedHealth =
     * getHealth() / getMaxHealth() * getHealthScale()</code>.
     *
     * @param scale the number to scale health to
     * @throws IllegalArgumentException if scale is &lt;0
     * @throws IllegalArgumentException if scale is {@link Double#NaN}
     * @throws IllegalArgumentException if scale is too high
     */
    public void setHealthScale(double scale) throws IllegalArgumentException;

    /**
     * Gets the number that health is scaled to for the client.
     *
     * @return the number that health would be scaled to for the client if
     *     HealthScaling is set to true
     * @see Player#setHealthScale(double)
     * @see Player#setHealthScaled(boolean)
     */
    public double getHealthScale();
}
