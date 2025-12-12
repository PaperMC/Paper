package org.bukkit;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.profile.PlayerProfile;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a reference to a player identity and the data belonging to a
 * player that is stored on the disk and can, thus, be retrieved without the
 * player needing to be online.
 */
@NullMarked
public interface OfflinePlayer extends ServerOperator, AnimalTamer, ConfigurationSerializable, io.papermc.paper.persistence.PersistentDataViewHolder, PlayerHeadObjectContents.SkinSource { // Paper - Add Offline PDC API

    /**
     * Checks if this player is currently online
     *
     * It should be noted that this will return true if any instance of this player is
     * online! This instance may have disconnected. If you wish to check if this specific
     * instance of the player is still online, see {@link OfflinePlayer#isConnected()}.
     *
     * @return true if they are online
     */
    public boolean isOnline();

    // Paper start
    /**
     * Checks whether the connection to this player is still valid. This will return
     * true as long as this specific instance of the player is still connected. This
     * will return false after this instance has disconnected, even if the same player
     * has reconnected since.
     *
     * @return true if this player instance is connected
     */
    public boolean isConnected();
    // Paper end

    /**
     * Returns the name of this player
     * <p>
     * Names are no longer unique past a single game session. For persistent storage
     * it is recommended that you use {@link #getUniqueId()} instead.
     *
     * @return Player name or null if we have not seen a name for this player yet
     */
    @Override
    @Nullable
    public String getName();

    /**
     * Returns the UUID of this player
     *
     * @return Player UUID
     */
    @Override
    public UUID getUniqueId();

    /**
     * Gets a copy of the player's profile.
     * <p>
     * If the player is online, the returned profile will be complete.
     * Otherwise, only the unique id is guaranteed to be present. You can use
     * {@link PlayerProfile#update()} to complete the returned profile.
     *
     * @return the player's profile
     */
    com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile(); // Paper

    /**
     * Checks if this player has had their profile banned.
     *
     * @return true if banned, otherwise false
     */
    public boolean isBanned();
    // Paper start
    /**
     * Permanently Bans this player from the server
     *
     * @param reason Reason for Ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)}
     */
    @Deprecated(since = "1.20.4")
    public default BanEntry banPlayer(@Nullable String reason) {
        return banPlayer(reason, null, null);
    }

    /**
     * Permanently Bans this player from the server
     * @param reason Reason for Ban
     * @param source Source of the ban, or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)}
     */
    @Deprecated(since = "1.20.4")
    public default BanEntry banPlayer(@Nullable String reason, @Nullable String source) {
        return banPlayer(reason, null, source);
    }

    /**
     * Bans this player from the server
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)}
     */
    @Deprecated(since = "1.20.4")
    public default BanEntry banPlayer(@Nullable String reason, java.util.@Nullable Date expires) {
        return banPlayer(reason, expires, null);
    }

    /**
     * Bans this player from the server
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @param source Source of the ban or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)}
     */
    @Deprecated(since = "1.20.4")
    public default BanEntry banPlayer(@Nullable String reason, java.util.@Nullable Date expires, @Nullable String source) {
        return banPlayer(reason, expires, source, true);
    }

    /**
     * @deprecated use {@link #ban(String, Date, String)}
     */
    @Deprecated(since = "1.20.4")
    public default BanEntry banPlayer(@Nullable String reason, java.util.@Nullable Date expires, @Nullable String source, boolean kickIfOnline) {
        BanEntry banEntry = Bukkit.getServer().getBanList(BanList.Type.NAME).addBan(getName(), reason, expires, source);
        if (kickIfOnline && isOnline()) {
            getPlayer().kickPlayer(reason);
        }
        return banEntry;
    }
    // Paper end

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Date expires, @Nullable String source); // Paper - fix ban list API

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires instant for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Instant expires, @Nullable String source); // Paper - fix ban list API

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param duration how long the ban last, or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Duration duration, @Nullable String source); // Paper - fix ban list API

    /**
     * Checks if this player is whitelisted or not
     *
     * @return true if whitelisted
     */
    public boolean isWhitelisted();

    /**
     * Sets if this player is whitelisted or not
     *
     * @param value true if whitelisted
     */
    public void setWhitelisted(boolean value);

    /**
     * Gets a {@link Player} object that this represents, if there is one
     * <p>
     * If the player is online, this will return that player. Otherwise,
     * it will return null.
     *
     * @return Online player
     */
    @Nullable
    public Player getPlayer();

    /**
     * Gets the first date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of first log-in for this player, or 0
     */
    public long getFirstPlayed();

    /**
     * Gets the last date and time that this player was witnessed on this
     * server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return Date of last log-in for this player, or 0
     * @deprecated The API contract is ambiguous and the implementation may or may not return the correct value given this API ambiguity. It is instead recommended use {@link #getLastLogin()} or {@link #getLastSeen()} depending on your needs.
     */
    @Deprecated
    public long getLastPlayed();

    /**
     * Checks if this player has played on this server before.
     *
     * @return True if the player has played before, otherwise false
     */
    public boolean hasPlayedBefore();

    /**
     * Gets the Location where the player will spawn at their bed, null if
     * they have not slept in one or their current bed spawn is invalid.
     *
     * @return Bed Spawn Location if bed exists, otherwise null.
     *
     * @see #getRespawnLocation()
     * @deprecated Misleading name. This method also returns the location of
     * respawn anchors.
     */
    @Nullable
    @Deprecated(since = "1.20.4")
    default Location getBedSpawnLocation() {
        return this.getRespawnLocation();
    }

    /**
     * Gets the last date and time that this player logged into the server.
     * <p>
     * If the player has never played before, this will return 0. Otherwise,
     * it will be the amount of milliseconds since midnight, January 1, 1970
     * UTC.
     *
     * @return last login time
     */
    public long getLastLogin();

    /**
     * Gets the last date and time that this player was seen on the server.
     * <p>
     * If the player has never played before, this will return 0. If the
     * player is currently online, this will return the current time.
     * Otherwise it will be the amount of milliseconds since midnight,
     * January 1, 1970 UTC.
     *
     * @return last seen time
     */
    public long getLastSeen();

    /**
     * Decrements the given stat for this player.
     * <p>
     * This is equivalent to the following code: {@code decrementStatistic(Statistic, 1)}
     *
     * @param statistic the stat to decrement
     * @throws IllegalArgumentException if the stat would have a negative value after decrementing it
     */
    default void decrementStatistic(final io.papermc.paper.statistic.Statistic<?> statistic) {
        this.decrementStatistic(statistic, 1);
    }

    /**
     * Increments the given stat for this player.
     * <p>
     * This is equivalent to the following code: {@code incrementStatistic(Statistic, 1)}
     *
     * @param statistic the stat to increment
     */
    default void incrementStatistic(final io.papermc.paper.statistic.Statistic<?> statistic) {
        this.incrementStatistic(statistic, 1);
    }

    /**
     * Decrements the given stat for this player.
     *
     * @param statistic the stat to decrement
     * @param amount the value to decrement by
     * @throws IllegalArgumentException if amount is negative
     * @throws IllegalArgumentException if the stat would have a negative value after decrementing it
     */
    void decrementStatistic(final io.papermc.paper.statistic.Statistic<?> statistic, final int amount);

    /**
     * Increments the given stat for this player.
     *
     * @param statistic the stat to increment
     * @param amount the amount to increment by
     * @throws IllegalArgumentException if amount is negative
     */
    void incrementStatistic(io.papermc.paper.statistic.Statistic<?> statistic, int amount);

    /**
     * Sets the given stat for this player.
     *
     * @param statistic the stat to set
     * @param newAmount the value to set the stat to
     * @throws IllegalArgumentException if the amount is negative
     */
    void setStatistic(io.papermc.paper.statistic.Statistic<?> statistic, int newAmount);

    /**
     * Gets the given stat for this player.
     *
     * @param statistic the stat to get
     * @return the amount for the stat
     */
    int getStatistic(io.papermc.paper.statistic.Statistic<?> statistic);

    /**
     * Get the formatted value for this stat. This is how the stat might
     * appear in the client's statistic window
     *
     * @param statistic the stat to get the formatted value for
     * @return the formatted value
     */
    String getFormattedValue(io.papermc.paper.statistic.Statistic<?> statistic);

    /**
     * Gets the Location where the player will spawn at, {@code null} if they
     * don't have a valid respawn point.
     * <br>
     * Unlike online players, the location if found will not be loaded by default.
     *
     * @return respawn location if exists, otherwise {@code null}.
     * @see #getRespawnLocation(boolean) for more fine-grained control over chunk loading and validation behaviour.
     */
    default @Nullable Location getRespawnLocation() {
        return this.getRespawnLocation(false); // keep old behavior for offline players
    }

    /**
     * Gets the Location where the player will spawn at, {@code null} if they
     * don't have a valid respawn point.
     *
     * @param loadLocationAndValidate load the expected respawn location to retrieve the exact position of the spawn
     *                                block and check if this position is still valid or not. Loading the location
     *                                will induce a sync chunk load and must hence be used with caution.
     * @return respawn location if exists, otherwise {@code null}.
     */
    @Nullable Location getRespawnLocation(boolean loadLocationAndValidate);

    //<editor-fold desc="deprecated statistic methods" defaultstate="collapsed">
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
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        this.incrementStatistic(statistic.toModern(null, null));
    }

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
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        this.decrementStatistic(statistic.toModern(null, null));
    }

    /**
     * Increments the given statistic for this player.
     *
     * @param statistic Statistic to increment
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     * @throws IllegalArgumentException if amount isn't positive
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        this.incrementStatistic(statistic.toModern(null, null), amount);
    }

    /**
     * Decrements the given statistic for this player.
     *
     * @param statistic Statistic to decrement
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     * @throws IllegalArgumentException if amount isn't positive
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        this.decrementStatistic(statistic.toModern(null, null), amount);
    }

    /**
     * Sets the given statistic for this player.
     *
     * @param statistic Statistic to set
     * @param newValue The value to set this statistic to
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if newValue is negative
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     * @deprecated use {@link #setStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        this.setStatistic(statistic.toModern(null, null), newValue);
    }

    /**
     * Gets the value of the given statistic for this player.
     *
     * @param statistic Statistic to check
     * @return the value of the given statistic
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if the statistic requires an
     *     additional parameter
     * @deprecated use {@link #getStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default int getStatistic(Statistic statistic) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        return this.getStatistic(statistic.toModern(null, null));
    }

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
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.incrementStatistic(statistic.toModern(null, material));
    }

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
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.decrementStatistic(statistic.toModern(null, material));
    }

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
     * @deprecated use {@link #getStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        return this.getStatistic(statistic.toModern(null, material));
    }

    /**
     * Increments the given statistic for this player for the given material.
     *
     * @param statistic Statistic to increment
     * @param material Material to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount isn't positive
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.incrementStatistic(statistic.toModern(null, material), amount);
    }

    /**
     * Decrements the given statistic for this player for the given material.
     *
     * @param statistic Statistic to decrement
     * @param material Material to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if material is null
     * @throws IllegalArgumentException if amount isn't positive
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.decrementStatistic(statistic.toModern(null, material), amount);
    }

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
     * @deprecated use {@link #setStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(material != null, "Material cannot be null");
        this.setStatistic(statistic.toModern(null, material), newValue);
    }

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
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        this.incrementStatistic(statistic.toModern(entityType, null));
    }

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
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        this.decrementStatistic(statistic.toModern(entityType, null));
    }

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
     * @deprecated use {@link #getStatistic(io.papermc.paper.statistic.Statistic)}
     */
    @Deprecated(since = "1.21.11")
    default int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        return this.getStatistic(statistic.toModern(entityType, null));
    }

    /**
     * Increments the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to increment
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to increment this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount isn't positive
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     * @deprecated use {@link #incrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        this.incrementStatistic(statistic.toModern(entityType, null), amount);
    }

    /**
     * Decrements the given statistic for this player for the given entity.
     *
     * @param statistic Statistic to decrement
     * @param entityType EntityType to offset the statistic with
     * @param amount Amount to decrement this statistic by
     * @throws IllegalArgumentException if statistic is null
     * @throws IllegalArgumentException if entityType is null
     * @throws IllegalArgumentException if amount isn't positive
     * @throws IllegalArgumentException if the given parameter is not valid
     *     for the statistic
     * @throws IllegalArgumentException if the statistic would have a negative value after decrementing it
     * @deprecated use {@link #decrementStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        this.decrementStatistic(statistic.toModern(entityType, null), amount);
    }

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
     * @deprecated use {@link #setStatistic(io.papermc.paper.statistic.Statistic, int)}
     */
    @Deprecated(since = "1.21.11")
    default void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        Preconditions.checkArgument(statistic != null, "Statistic cannot be null");
        Preconditions.checkArgument(entityType != null, "EntityType cannot be null");
        this.setStatistic(statistic.toModern(entityType, null),  newValue);
    }
    //</editor-fold>

    /**
     * Gets the player's last death location.
     *
     * @return the last death location if it exists, otherwise null.
     */
    @Nullable
    public Location getLastDeathLocation();

    /**
     * Gets the player's current location.
     *
     * @return the player's location, {@code null} if player hasn't ever played
     * before.
     */
    @Nullable
    public Location getLocation();
    // Paper start - add pdc to offline player
    /**
     * Yields a view of the persistent data container for this offline player.
     * In case this {@link OfflinePlayer} instance was created for an offline player, the returned view will wrap the persistent
     * data on disk.
     * <p>
     * As such, this method as well as queries to the {@link io.papermc.paper.persistence.PersistentDataContainerView}
     * may produce blocking IO requests to read the requested data from disk.
     * Caution in its usage is hence advised.
     *
     * @return the persistent data container view
     * @see io.papermc.paper.persistence.PersistentDataViewHolder#getPersistentDataContainer()
     */
    @Override
    io.papermc.paper.persistence.PersistentDataContainerView getPersistentDataContainer();
    // Paper end - add pdc to offline player

    @Override
    default void applySkinToPlayerHeadContents(final PlayerHeadObjectContents.@NonNull Builder builder) {
        builder.id(this.getUniqueId());
    }
}
