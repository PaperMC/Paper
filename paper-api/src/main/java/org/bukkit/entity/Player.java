package org.bukkit.entity;

import java.net.InetSocketAddress;
import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.map.MapView;

/**
 * Represents a player, connected or not
 *
 */
public interface Player extends HumanEntity, CommandSender, OfflinePlayer {
    /**
     * Gets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @return the friendly name
     */
    public String getDisplayName();

    /**
     * Sets the "friendly" name to display of this player. This may include color.
     *
     * Note that this name will not be displayed in game, only in chat and places
     * defined by plugins
     *
     * @param name
     */
    public void setDisplayName(String name);

    /**
     * Set the target of the player's compass.
     *
     * @param loc
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
     * @return true if player is in sneak mode
     */
    public boolean isSneaking();

    /**
     * Sets the sneak mode the player
     * @param sneak true if player should appear sneaking
     */
    public void setSneaking(boolean sneak);

    /**
     * Saves the players current location, health, inventory, motion, and other information into the username.dat file, in the world/player folder
     */
    public void saveData();

    /**
     * Loads the players current location, health, inventory, motion, and other information from the username.dat file, in the world/player folder
     *
     * Note: This will overwrite the players current inventory, health, motion, etc, with the state from the saved dat file.
     */
    public void loadData();

    /**
     * Sets whether the player is ignored as not sleeping. If everyone is
     * either sleeping or has this flag set, then time will advance to the
     * next day. If everyone has this flag set but no one is actually in bed,
     * then nothing will happen.
     *
     * @param isSleeping
     */
    public void setSleepingIgnored(boolean isSleeping);

    /**
     * Returns whether the player is sleeping ignored.
     *
     * @return
     */
    public boolean isSleepingIgnored();

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *
     * @param loc
     * @param instrument
     * @param note
     */
    public void playNote(Location loc, byte instrument, byte note);

    /**
     * Play a note for a player at a location. This requires a note block
     * at the particular location (as far as the client is concerned). This
     * will not work without a note block. This will not work with cake.
     *
     * @param loc
     * @param instrument
     * @param note
     */
    public void playNote(Location loc, Instrument instrument, Note note);

    /**
     * Plays an effect to just this player.
     *
     * @param loc the player to play the effect for
     * @param effect the {@link Effect}
     * @param data a data bit needed for the RECORD_PLAY, SMOKE, and STEP_SOUND sounds
     */
    public void playEffect(Location loc, Effect effect, int data);

    /**
     * Send a block change. This fakes a block change packet for a user at
     * a certain location. This will not actually change the world in any way.
     *
     * @param loc
     * @param material
     * @param data
     */
    public void sendBlockChange(Location loc, Material material, byte data);

    /**
     * Send a chunk change. This fakes a chunk change packet for a user at
     * a certain location. The updated cuboid must be entirely within a single
     * chunk.  This will not actually change the world in any way.
     *
     * At least one of the dimensions of the cuboid must be even. The size of the
     * data buffer must be 2.5*sx*sy*sz and formatted in accordance with the Packet51
     * format.
     *
     * @param loc The location of the cuboid
     * @param sx The x size of the cuboid
     * @param sy The y size of the cuboid
     * @param sz The z size of the cuboid
     * @param data The data to be sent
     *
     * @return true if the chunk change packet was sent
     */
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data);

    /**
     * Send a block change. This fakes a block change packet for a user at
     * a certain location. This will not actually change the world in any way.
     *
     * @param loc
     * @param material
     * @param data
     */
    public void sendBlockChange(Location loc, int material, byte data);
    
    /**
     * Render a map and send it to the player in its entirety. This may be used
     * when streaming the map in the normal manner is not desirbale.
     * 
     * @pram map The map to be sent
     */
    public void sendMap(MapView map);

    /**
     * Forces an update of the player's entire inventory.
     *
     * @deprecated This method should not be relied upon as it is a temporary work-around for a larger, more complicated issue.
     */
    @Deprecated
    public void updateInventory();

    /**
     * Awards this player the given achievement
     *
     * @param achievement Achievement to award
     */
    public void awardAchievement(Achievement achievement);

    /**
     * Increments the given statistic for this player
     *
     * @param statistic Statistic to increment
     */
    public void incrementStatistic(Statistic statistic);

    /**
     * Increments the given statistic for this player
     *
     * @param statistic Statistic to increment
     * @param amount Amount to increment this statistic by
     */
    public void incrementStatistic(Statistic statistic, int amount);

    /**
     * Increments the given statistic for this player for the given material
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     */
    public void incrementStatistic(Statistic statistic, Material material);

    /**
     * Increments the given statistic for this player for the given material
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @param amount Amount to increment this statistic by
     */
    public void incrementStatistic(Statistic statistic, Material material, int amount);

    /**
     * Sets the current time on the player's client. When relative is true the player's time
     * will be kept synchronized to its world time with the specified offset.
     *
     * When using non relative time the player's time will stay fixed at the specified time parameter. It's up to
     * the caller to continue updating the player's time. To restore player time to normal use resetPlayerTime().
     *
     * @param time The current player's perceived time or the player's time offset from the server time.
     * @param relative When true the player time is kept relative to its world time.
     */
    public void setPlayerTime(long time, boolean relative);

    /**
     * Returns the player's current timestamp.
     *
     * @return
     */
    public long getPlayerTime();

    /**
     * Returns the player's current time offset relative to server time, or the current player's fixed time
     * if the player's time is absolute.
     *
     * @return
     */
    public long getPlayerTimeOffset();

    /**
     * Returns true if the player's time is relative to the server time, otherwise the player's time is absolute and
     * will not change its current time unless done so with setPlayerTime().
     *
     * @return true if the player's time is relative to the server time.
     */
    public boolean isPlayerTimeRelative();

    /**
     * Restores the normal condition where the player's time is synchronized with the server time.
     * Equivalent to calling setPlayerTime(0, true).
     */
    public void resetPlayerTime();

}
