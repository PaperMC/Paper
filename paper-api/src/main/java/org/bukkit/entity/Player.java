package org.bukkit.entity;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.PlayerGiveResult;
import io.papermc.paper.math.Position;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import org.bukkit.BanEntry;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Input;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.ServerLinks;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.WeatherType;
import org.bukkit.WorldBorder;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.ban.IpBanList;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.conversations.Conversable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a player, connected or not
 */
@NullMarked
public interface Player extends HumanEntity, Conversable, OfflinePlayer, PluginMessageRecipient, net.kyori.adventure.identity.Identified, net.kyori.adventure.bossbar.BossBarViewer, com.destroystokyo.paper.network.NetworkClient { // Paper

    // Paper start
    @Override
    default net.kyori.adventure.identity.Identity identity() {
        return net.kyori.adventure.identity.Identity.identity(this.getUniqueId());
    }

    /**
     * Gets an unmodifiable view of all known currently active bossbars.
     * <p>
     * <b>This currently only returns bossbars shown to the player via
     * {@link #showBossBar(net.kyori.adventure.bossbar.BossBar)} and does not contain bukkit
     * {@link org.bukkit.boss.BossBar} instances shown to the player.</b>
     *
     * @return an unmodifiable view of all known currently active bossbars
     * @since 4.14.0
     */
    @Override
    @org.jetbrains.annotations.UnmodifiableView Iterable<? extends net.kyori.adventure.bossbar.BossBar> activeBossBars();

    /**
     * Gets the "friendly" name to display of this player.
     *
     * @return the display name
     */
    net.kyori.adventure.text.Component displayName();

    /**
     * Sets the "friendly" name to display of this player.
     *
     * @param displayName the display name to set
     */
    void displayName(final net.kyori.adventure.text.@Nullable Component displayName);
    // Paper end

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName();

    /**
     * Gets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @return the friendly name
     * @deprecated in favour of {@link #displayName()}
     */
    @Deprecated // Paper
    public String getDisplayName();

    /**
     * Sets the "friendly" name to display of this player. This may include
     * color.
     * <p>
     * Note that this name will not be displayed in game, only in chat and
     * places defined by plugins.
     *
     * @param name The new display name.
     * @deprecated in favour of {@link #displayName(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setDisplayName(@Nullable String name);

    // Paper start
    /**
     * Sets the name that is shown on the in-game player list.
     * <p>
     * If the value is null, the name will be identical to {@link #getName()}.
     *
     * @param name new player list name
     */
    void playerListName(net.kyori.adventure.text.@Nullable Component name);

    /**
     * Gets the name that is shown on the in-game player list.
     *
     * @return the player list name
     */
    net.kyori.adventure.text.Component playerListName();

    /**
     * Gets the currently displayed player list header for this player.
     *
     * @return player list header or null
     */
    net.kyori.adventure.text.@Nullable Component playerListHeader();

    /**
     * Gets the currently displayed player list footer for this player.
     *
     * @return player list footer or null
     */
    net.kyori.adventure.text.@Nullable Component playerListFooter();
    // Paper end
    /**
     * Gets the name that is shown on the player list.
     *
     * @return the player list name
     * @deprecated in favour of {@link #playerListName()}
     */
    @Deprecated // Paper
    public String getPlayerListName();

    /**
     * Sets the name that is shown on the in-game player list.
     * <p>
     * If the value is null, the name will be identical to {@link #getName()}.
     *
     * @param name new player list name
     * @deprecated in favour of {@link #playerListName(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setPlayerListName(@Nullable String name);

    /**
     * Gets the relative order that the player is shown on the player list.
     *
     * @return the player list order
     */
    public int getPlayerListOrder();

    /**
     * Sets the relative order that the player is shown on the in-game player
     * list.
     *
     * @param order new player list order, must be positive
     */
    public void setPlayerListOrder(int order);

    /**
     * Gets the currently displayed player list header for this player.
     *
     * @return player list header or null
     * @deprecated in favour of {@link #playerListHeader()}
     */
    @Deprecated // Paper
    @Nullable
    public String getPlayerListHeader();

    /**
     * Gets the currently displayed player list footer for this player.
     *
     * @return player list header or null
     * @deprecated in favour of {@link #playerListFooter()}
     */
    @Deprecated // Paper
    @Nullable
    public String getPlayerListFooter();

    /**
     * Sets the currently displayed player list header for this player.
     *
     * @param header player list header, null for empty
     * @deprecated in favour of {@link #sendPlayerListHeader(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setPlayerListHeader(@Nullable String header);

    /**
     * Sets the currently displayed player list footer for this player.
     *
     * @param footer player list footer, null for empty
     * @deprecated in favour of {@link #sendPlayerListFooter(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setPlayerListFooter(@Nullable String footer);

    /**
     * Sets the currently displayed player list header and footer for this
     * player.
     *
     * @param header player list header, null for empty
     * @param footer player list footer, null for empty
     * @deprecated in favour of {@link #sendPlayerListHeaderAndFooter(net.kyori.adventure.text.Component, net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void setPlayerListHeaderFooter(@Nullable String header, @Nullable String footer);

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
    @Nullable
    public InetSocketAddress getAddress();

    // Paper start - Add API to get player's proxy address
    /**
     * Gets the socket address of this player's proxy
     *
     * @return the player's proxy address, null if the server doesn't have Proxy Protocol enabled, or the player didn't connect to an HAProxy instance
     */
    @Nullable
    public InetSocketAddress getHAProxyAddress();
    // Paper end - Add API to get player's proxy address

    /**
     * Gets if this connection has been transferred from another server.
     *
     * @return true if the connection has been transferred
     */
    public boolean isTransferred();

    /**
     * Retrieves a cookie from this player.
     *
     * @param key the key identifying the cookie
     * @return a {@link CompletableFuture} that will be completed when the
     * Cookie response is received or otherwise available. If the cookie is not
     * set in the client, the {@link CompletableFuture} will complete with a
     * null value.
     */
    CompletableFuture<byte @Nullable []> retrieveCookie(NamespacedKey key);

    /**
     * Stores a cookie in this player's client.
     *
     * @param key the key identifying the cookie
     * @param value the data to store in the cookie
     * @throws IllegalStateException if a cookie cannot be stored at this time
     */
    void storeCookie(NamespacedKey key, byte[] value);

    /**
     * Requests this player to connect to a different server specified by host
     * and port.
     *
     * @param host the host of the server to transfer to
     * @param port the port of the server to transfer to
     * @throws IllegalStateException if a transfer cannot take place at this
     * time
     */
    void transfer(String host, int port);

    /**
     * Sends this sender a message raw
     *
     * @param message Message to be displayed
     */
    @Override
    public void sendRawMessage(String message);

    /**
     * Kicks player with custom kick message.
     *
     * @param message kick message
     * @deprecated in favour of {@link #kick(net.kyori.adventure.text.Component)}
     */
    @Deprecated // Paper
    public void kickPlayer(@Nullable String message);

    /**
     * Kicks the player with the default kick message.
     *
     * @see #kick(net.kyori.adventure.text.Component)
     */
    default void kick() {
        class Holder {
            public static final Component DEFAULT_KICK_MESSAGE = Component.translatable("multiplayer.disconnect.kicked");
        }
        this.kick(Holder.DEFAULT_KICK_MESSAGE);
    }

    /**
     * Kicks player with custom kick message.
     *
     * @param message kick message
     */
    default void kick(final net.kyori.adventure.text.@Nullable Component message) {
        this.kick(message, org.bukkit.event.player.PlayerKickEvent.Cause.PLUGIN);
    }

    /**
     * Kicks player with custom kick message and cause.
     *
     * @param message kick message
     * @param cause kick cause
     */
    void kick(final net.kyori.adventure.text.@Nullable Component message, org.bukkit.event.player.PlayerKickEvent.Cause cause);

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer); // Paper - fix ban list API

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Instant expires, @Nullable String source, boolean kickPlayer); // Paper - fix ban list API

    /**
     * Adds this user to the {@link ProfileBanList}. If a previous ban exists, this will
     * update the entry.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param duration the duration how long the ban lasts, or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public <E extends BanEntry<? super com.destroystokyo.paper.profile.PlayerProfile>> E ban(@Nullable String reason, @Nullable Duration duration, @Nullable String source, boolean kickPlayer); // Paper - fix ban list API

    /**
     * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
     * update the entry. If {@link #getAddress()} is null this method will throw an exception.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Date expires, @Nullable String source, boolean kickPlayer);

    /**
     * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
     * update the entry. If {@link #getAddress()} is null this method will throw an exception.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param expires date for the ban's expiration (unban), or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Instant expires, @Nullable String source, boolean kickPlayer);

    /**
     * Adds this user's current IP address to the {@link IpBanList}. If a previous ban exists, this will
     * update the entry. If {@link #getAddress()} is null this method will throw an exception.
     *
     * @param reason reason for the ban, null indicates implementation default
     * @param duration the duration how long the ban lasts, or null to imply
     *     forever
     * @param source source of the ban, null indicates implementation default
     * @param kickPlayer if the player need to be kick
     *
     * @return the entry for the newly created ban, or the entry for the
     *     (updated) previous ban
     */
    @Nullable
    public BanEntry<InetAddress> banIp(@Nullable String reason, @Nullable Duration duration, @Nullable String source, boolean kickPlayer);

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
     * Returns true if the entity is supported by a block.
     *
     * This value is a state updated by the client after each movement.
     *
     * @return True if entity is on ground.
     * @deprecated This value is controlled only by the client and is therefore
     * unreliable and vulnerable to spoofing and/or desync depending on the
     * context/time which it is accessed
     */
    @Override
    @Deprecated(since = "1.16.1")
    public boolean isOnGround();

    /**
     * Returns if the player is in sneak mode
     *
     * @return true if player is in sneak mode
     */
    @Override // Paper
    public boolean isSneaking();

    /**
     * Sets the sneak mode the player
     *
     * @param sneak true if player should appear sneaking
     */
    @Override // Paper
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
     * other information into the &lt;uuid&gt;.dat file, in the
     * &lt;level-name&gt;/playerdata/ folder.
     */
    public void saveData();

    /**
     * Loads the players current location, health, inventory, motion, and
     * other information from the &lt;uuid&gt;.dat file, in the
     * &lt;level-name&gt;/playerdata/ folder.
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
     * Gets the Location where the player will spawn at, {@code null} if they
     * don't have a valid respawn point.
     * <br>
     * Unlike offline players, the location if found will be loaded to validate by default.
     *
     * @return respawn location if exists, otherwise {@code null}.
     * @see #getRespawnLocation(boolean) for more fine-grained control over chunk loading and validation behaviour.
     */
    @Override
    default @Nullable Location getRespawnLocation() {
        return this.getRespawnLocation(true);
    }

    /**
     * Sets the Location where the player will spawn at their bed.
     *
     * @param location where to set the respawn location
     *
     * @see #setRespawnLocation(Location)
     * @deprecated Misleading name. This method sets the player's respawn
     * location more generally and is not limited to beds.
     */
    @Deprecated(since = "1.20.4")
    default void setBedSpawnLocation(@Nullable Location location) {
        this.setBedSpawnLocation(location, false);
    }

    /**
     * Sets the Location where the player will respawn.
     *
     * @param location where to set the respawn location
     */
    default void setRespawnLocation(@Nullable Location location) {
        this.setRespawnLocation(location, false);
    }

    /**
     * Sets the Location where the player will spawn at their bed.
     *
     * @param location where to set the respawn location
     * @param force whether to forcefully set the respawn location even if a
     *     valid bed is not present
     *
     * @see #setRespawnLocation(Location, boolean)
     * @deprecated Misleading name. This method sets the player's respawn
     * location more generally and is not limited to beds.
     */
    @Deprecated(since = "1.20.4")
    default void setBedSpawnLocation(@Nullable Location location, boolean force) {
        this.setRespawnLocation(location, force);
    }

    /**
     * Sets the Location where the player will respawn.
     *
     * @param location where to set the respawn location
     * @param force whether to forcefully set the respawn location even if a
     *     valid respawn point is not present
     */
    public void setRespawnLocation(@Nullable Location location, boolean force);

    /**
     * Gets the ender pearls currently associated with this entity.
     * <p>
     * The returned list will not be directly linked to the entity's current
     * pearls, and no guarantees are made as to its mutability.
     *
     * @return collection of entities corresponding to current pearls.
     */
    @ApiStatus.Experimental
    public Collection<EnderPearl> getEnderPearls();

    /**
     * Gets the current movement input, as last provided by the player.
     * <br>
     * <b>Note: that this may not always be consistent with the current movement
     * of the player.</b>
     *
     * @return current input
     */
    public Input getCurrentInput();

    /**
     * Play a note for the player at a location. <br>
     * This <i>will</i> work with cake.
     *
     * @param loc The location to play the note
     * @param instrument The instrument ID.
     * @param note The note ID.
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    default void playNote(Location loc, byte instrument, byte note) {
        this.playNote(loc, Instrument.getByType(instrument), new Note(note));
    }

    /**
     * Play a note for the player at a location. <br>
     * This <i>will</i> work with cake.
     * <p>
     * This method will fail silently when called with {@link Instrument#CUSTOM_HEAD}.
     * @param loc The location to play the note
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
    default void playSound(Location location, Sound sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null. No
     * sound will be heard by the player if their client does not have the
     * respective sound for the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(Location location, String sound, float volume, float pitch) {
        this.playSound(location, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch);

    /**
     * Play a sound for a player at the location.
     * <p>
     * This function will fail silently if Location or Sound are null. No sound
     * will be heard by the player if their client does not have the respective
     * sound for the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch);

    /**
     * Play a sound for a player at the location. For sounds with multiple
     * variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Location or Sound are null.
     *
     * @param location The location to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a sound for a player at the location. For sounds with multiple
     * variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Location or Sound are null. No sound
     * will be heard by the player if their client does not have the respective
     * sound for the value passed.
     *
     * @param location The location to play the sound
     * @param sound The internal sound name to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a sound for a player at the location of the entity.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(Entity entity, Sound sound, float volume, float pitch) {
        this.playSound(entity, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a sound for a player at the location of the entity.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    default void playSound(Entity entity, String sound, float volume, float pitch) {
        this.playSound(entity, sound, SoundCategory.MASTER, volume, pitch);
    }

    /**
     * Play a sound for a player at the location of the entity.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Entity entity, Sound sound, SoundCategory category, float volume, float pitch);

    /**
     * Play a sound for a player at the location of the entity.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     */
    public void playSound(Entity entity, String sound, SoundCategory category, float volume, float pitch);

    /**
     * Play a sound for a player at the location of the entity. For sounds with
     * multiple variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    public void playSound(Entity entity, Sound sound, SoundCategory category, float volume, float pitch, long seed);

    /**
     * Play a sound for a player at the location of the entity. For sounds with
     * multiple variations passing the same seed will always play the same variation.
     * <p>
     * This function will fail silently if Entity or Sound are null.
     *
     * @param entity The entity to play the sound
     * @param sound The sound to play
     * @param category The category of the sound
     * @param volume The volume of the sound
     * @param pitch The pitch of the sound
     * @param seed The seed for the sound
     */
    public void playSound(Entity entity, String sound, SoundCategory category, float volume, float pitch, long seed);

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     */
    default void stopSound(Sound sound) {
        this.stopSound(sound, null);
    }

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     */
    default void stopSound(String sound) {
        this.stopSound(sound, null);
    }

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     * @param category the category of the sound
     */
    default void stopSound(Sound sound, @Nullable SoundCategory category) {
        this.stopSound(sound.getKey().getKey(), category);
    }

    /**
     * Stop the specified sound from playing.
     *
     * @param sound the sound to stop
     * @param category the category of the sound
     */
    public void stopSound(String sound, @Nullable SoundCategory category);

    /**
     * Stop the specified sound category from playing.
     *
     * @param category the sound category to stop
     */
    public void stopSound(SoundCategory category);

    /**
     * Stop all sounds from playing.
     */
    public void stopAllSounds();

    /**
     * Plays an effect to just this player.
     *
     * @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public void playEffect(Location loc, Effect effect, int data);

    /**
     * Plays an effect to just this player.
     *
     * @param <T> the data based on the type of the effect
     * @param loc the location to play the effect at
     * @param effect the {@link Effect}
     * @param data a data bit needed for some effects
     */
    public <T> void playEffect(Location loc, Effect effect, @Nullable T data);

    /**
     * Force this player to break a Block using the item in their main hand.
     *
     * This method will respect enchantments, handle item durability (if
     * applicable) and drop experience and the correct items according to the
     * tool/item in the player's hand.
     * <p>
     * Note that this method will call a {@link BlockBreakEvent}, meaning that
     * this method may not be successful in breaking the block if the event was
     * cancelled by a third party plugin. Care should be taken if running this
     * method in a BlockBreakEvent listener as recursion may be possible if it
     * is invoked on the same {@link Block} being broken in the event.
     * <p>
     * Additionally, a {@link BlockDropItemEvent} is called for the items
     * dropped by this method (if successful).
     * <p>
     * The block must be in the same world as the player.
     *
     * @param block the block to break
     *
     * @return true if the block was broken, false if the break failed
     */
    public boolean breakBlock(Block block);

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param material The new block
     * @param data The block data
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public void sendBlockChange(Location loc, Material material, byte data);

    /**
     * Send a block change. This fakes a block change packet for a user at a
     * certain location. This will not actually change the world in any way.
     *
     * @param loc The location of the changed block
     * @param block The new block
     */
    public void sendBlockChange(Location loc, BlockData block);

    /**
     * Send a multi-block change. This fakes a block change packet for a user
     * at multiple locations. This will not actually change the world in any
     * way.
     * <p>
     * This method may send multiple packets to the client depending on the
     * blocks in the collection. A packet must be sent for each chunk section
     * modified, meaning one packet for each 16x16x16 block area. Even if only
     * one block is changed in two different chunk sections, two packets will
     * be sent.
     * <p>
     * Additionally, this method cannot guarantee the functionality of changes
     * being sent to the player in chunks not loaded by the client. It is the
     * responsibility of the caller to ensure that the client is within range
     * of the changed blocks or to handle any side effects caused as a result.
     *
     * @param blocks the block states to send to the player
     */
    public void sendBlockChanges(Collection<BlockState> blocks);

    /**
     * Send a multi-block change. This fakes a block change packet for a user
     * at multiple locations. This will not actually change the world in any
     * way.
     * <p>
     * This method may send multiple packets to the client depending on the
     * blocks in the collection. A packet must be sent for each chunk section
     * modified, meaning one packet for each 16x16x16 block area. Even if only
     * one block is changed in two different chunk sections, two packets will
     * be sent.
     * <p>
     * Additionally, this method cannot guarantee the functionality of changes
     * being sent to the player in chunks not loaded by the client. It is the
     * responsibility of the caller to ensure that the client is within range
     * of the changed blocks or to handle any side effects caused as a result.
     *
     * @param blocks the block states to send to the player
     * @param suppressLightUpdates whether or not light updates should be
     * suppressed when updating the blocks on the client
     * @deprecated suppressLightUpdates is not functional in versions greater
     * than 1.19.4
     */
    @Deprecated(since = "1.20")
    default void sendBlockChanges(Collection<BlockState> blocks, boolean suppressLightUpdates) {
        this.sendBlockChanges(blocks);
    }

    /**
     * Send block damage. This fakes block break progress at a certain location
     * sourced by this player. This will not actually change the block's break
     * progress in any way.
     *
     * @param loc the location of the damaged block
     * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
     * 1.0 is the most damaged
     */
    default void sendBlockDamage(Location loc, float progress) {
        this.sendBlockDamage(loc, progress, this.getEntityId());
    }

    // Paper start
    /**
     * Send multiple block changes. This fakes a multi block change packet for each
     * chunk section that a block change occurs. This will not actually change the world in any way.
     *
     * @param blockChanges A map of the positions you want to change to their new block data
     */
    void sendMultiBlockChange(Map<? extends io.papermc.paper.math.Position, BlockData> blockChanges);

    /**
     * Send multiple block changes. This fakes a multi block change packet for each
     * chunk section that a block change occurs. This will not actually change the world in any way.
     *
     * @param blockChanges A map of the positions you want to change to their new block data
     * @param suppressLightUpdates Whether to suppress light updates or not
     * @deprecated suppressLightUpdates is no longer available in 1.20+, use {@link #sendMultiBlockChange(Map)}
     */
    @Deprecated
    default void sendMultiBlockChange(Map<? extends io.papermc.paper.math.Position, BlockData> blockChanges, boolean suppressLightUpdates) {
        this.sendMultiBlockChange(blockChanges);
    }
    // Paper end

    /**
     * Send block damage. This fakes block break progress at a certain location
     * sourced by the provided entity. This will not actually change the block's
     * break progress in any way.
     * <p>
     * At the same location for each unique damage source sent to the player, a
     * separate damage overlay will be displayed with the given progress. This allows
     * for block damage at different progress from multiple entities at once.
     *
     * @param loc the location of the damaged block
     * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
     * 1.0 is the most damaged
     * @param source the entity to which the damage belongs
     */
    public void sendBlockDamage(Location loc, float progress, Entity source);

    /**
     * Send block damage. This fakes block break progress at a certain location
     * sourced by the provided entity id. This will not actually change the block's
     * break progress in any way.
     * <p>
     * At the same location for each unique damage source sent to the player, a
     * separate damage overlay will be displayed with the given progress. This allows
     * for block damage at different progress from multiple entities at once.
     *
     * @param loc the location of the damaged block
     * @param progress the progress from 0.0 - 1.0 where 0 is no damage and
     * 1.0 is the most damaged
     * @param sourceId the entity id of the entity to which the damage belongs.
     * Can be an id that does not associate directly with an existing or loaded entity.
     */
    public void sendBlockDamage(Location loc, float progress, int sourceId);

    /**
     * Send an equipment change for the target entity. This will not
     * actually change the entity's equipment in any way.
     *
     * @param entity the entity whose equipment to change
     * @param slot the slot to change
     * @param item the item to which the slot should be changed, or null to set
     * it to air
     */
    public void sendEquipmentChange(LivingEntity entity, EquipmentSlot slot, @Nullable ItemStack item);

    /**
     * Send multiple equipment changes for the target entity. This will not
     * actually change the entity's equipment in any way.
     *
     * @param entity the entity whose equipment to change
     * @param items the slots to change, where the values are the items to which
     * the slot should be changed. null values will set the slot to air, empty map is not allowed
     */
    public void sendEquipmentChange(LivingEntity entity, Map<EquipmentSlot, @Nullable ItemStack> items);

    // Paper start
    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated
    default void sendSignChange(Location loc, java.util.@Nullable List<? extends net.kyori.adventure.text.Component> lines) throws IllegalArgumentException {
        this.sendSignChange(loc, lines, DyeColor.BLACK);
    }

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @param dyeColor the color of the sign
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if dyeColor is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated
    default void sendSignChange(Location loc, java.util.@Nullable List<? extends net.kyori.adventure.text.Component> lines, DyeColor dyeColor) throws IllegalArgumentException {
        this.sendSignChange(loc, lines, dyeColor, false);
    }

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @param hasGlowingText whether the text of the sign should glow as if dyed with a glowing ink sac
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if dyeColor is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated
    default void sendSignChange(Location loc, java.util.@Nullable List<? extends net.kyori.adventure.text.Component> lines, boolean hasGlowingText) throws IllegalArgumentException {
        this.sendSignChange(loc, lines, DyeColor.BLACK, hasGlowingText);
    }

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.Material, byte)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @param dyeColor the color of the sign
     * @param hasGlowingText whether the text of the sign should glow as if dyed with a glowing ink sac
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if dyeColor is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated
    void sendSignChange(Location loc, java.util.@Nullable List<? extends net.kyori.adventure.text.Component> lines, DyeColor dyeColor, boolean hasGlowingText)
        throws IllegalArgumentException;
    // Paper end

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.block.data.BlockData)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     * <p>
     * To change all attributes of a sign, including the back Side, use
     * {@link #sendBlockUpdate(org.bukkit.Location, org.bukkit.block.TileState)}.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated // Paper
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines) throws IllegalArgumentException;

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.block.data.BlockData)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     * <p>
     * To change all attributes of a sign, including the back Side, use
     * {@link #sendBlockUpdate(org.bukkit.Location, org.bukkit.block.TileState)}.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @param dyeColor the color of the sign
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if dyeColor is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated // Paper
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines, DyeColor dyeColor) throws IllegalArgumentException;

    /**
     * Send a sign change. This fakes a sign change packet for a user at
     * a certain location. This will not actually change the world in any way.
     * This method will use a sign at the location's block or a faked sign
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.block.data.BlockData)}.
     * <p>
     * If the client does not have a sign at the given location it will
     * display an error message to the user.
     * <p>
     * To change all attributes of a sign, including the back Side, use
     * {@link #sendBlockUpdate(org.bukkit.Location, org.bukkit.block.TileState)}.
     *
     * @param loc the location of the sign
     * @param lines the new text on the sign or null to clear it
     * @param dyeColor the color of the sign
     * @param hasGlowingText if the sign's text should be glowing
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if dyeColor is null
     * @throws IllegalArgumentException if lines is non-null and has a length less than 4
     * @deprecated Use {@link #sendBlockUpdate(Location, TileState)} by creating a new virtual
     * {@link org.bukkit.block.Sign} block state via {@link BlockData#createBlockState()}
     * (constructed e.g. via {@link Material#createBlockData()})
     */
    @Deprecated // Paper
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines, DyeColor dyeColor, boolean hasGlowingText) throws IllegalArgumentException;

    /**
     * Send a TileState change. This fakes a TileState change for a user at
     * the given location. This will not actually change the world in any way.
     * This method will use a TileState at the location's block or a faked TileState
     * sent via
     * {@link #sendBlockChange(org.bukkit.Location, org.bukkit.block.data.BlockData)}.
     * <p>
     * If the client does not have an appropriate tile at the given location it
     * may display an error message to the user.
     * <p>
     * {@link BlockData#createBlockState()} can be used to create a {@link BlockState}.
     *
     * @param loc the location of the sign
     * @param tileState the tile state
     * @throws IllegalArgumentException if location is null
     * @throws IllegalArgumentException if tileState is null
     */
    public void sendBlockUpdate(Location loc, TileState tileState) throws IllegalArgumentException;

    /**
     * Change a potion effect for the target entity. This will not actually
     * change the entity's potion effects in any way.
     * <p>
     * <b>Note:</b> Sending an effect change to a player for themselves may
     * cause unexpected behavior on the client. Effects sent this way will also
     * not be removed when their timer reaches 0, they can be removed with
     * {@link #sendPotionEffectChangeRemove(LivingEntity, PotionEffectType)}
     *
     * @param entity the entity whose potion effects to change
     * @param effect the effect to change
     */
    public void sendPotionEffectChange(LivingEntity entity, PotionEffect effect);

    /**
     * Remove a potion effect for the target entity. This will not actually
     * change the entity's potion effects in any way.
     * <p>
     * <b>Note:</b> Sending an effect change to a player for themselves may
     * cause unexpected behavior on the client.
     *
     * @param entity the entity whose potion effects to change
     * @param type the effect type to remove
     */
    public void sendPotionEffectChangeRemove(LivingEntity entity, PotionEffectType type);

    /**
     * Render a map and send it to the player in its entirety. This may be
     * used when streaming the map in the normal manner is not desirable.
     *
     * @param map The map to be sent
     */
    public void sendMap(MapView map);

    /**
     * Shows the player the win screen that normally is only displayed after one kills the ender dragon
     * and exits the end for the first time.
     * In vanilla, the win screen starts with a poem and then continues with the credits but its content can be
     * changed by using a resource pack.
     * <br>
     * Calling this method does not change the value of {@link #hasSeenWinScreen()}.
     * That means that the win screen is still displayed to a player if they leave the end for the first time, even though
     * they have seen it before because this method was called.
     * Note this method does not make the player invulnerable, which is normally expected when viewing credits.
     *
     * @see #hasSeenWinScreen()
     * @see #setHasSeenWinScreen(boolean)
     * @see <a href="https://minecraft.wiki/wiki/End_Poem#Technical_details">https://minecraft.wiki/wiki/End_Poem#Technical_details</a>
     */
    public void showWinScreen();

    /**
     * Returns whether this player has seen the win screen before.
     * When a player leaves the end the win screen is shown to them if they have not seen it before.
     *
     * @return Whether this player has seen the win screen before
     * @see #setHasSeenWinScreen(boolean)
     * @see #showWinScreen()
     * @see <a href="https://minecraft.wiki/wiki/End_Poem">https://minecraft.wiki/wiki/End_Poem</a>
     */
    public boolean hasSeenWinScreen();

    /**
     * Changes whether this player has seen the win screen before.
     * When a player leaves the end the win screen is shown to them if they have not seen it before.
     *
     * @param hasSeenWinScreen Whether this player has seen the win screen before
     * @see #hasSeenWinScreen()
     * @see #showWinScreen()
     * @see <a href="https://minecraft.wiki/wiki/End_Poem">https://minecraft.wiki/wiki/End_Poem</a>
     */
    public void setHasSeenWinScreen(boolean hasSeenWinScreen);

    /**
     * Permanently Bans the Profile and IP address currently used by the player.
     *
     * @param reason Reason for ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    // For reference, Bukkit defines this as nullable, while they impl isn't, we'll follow API.
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerFull(@Nullable String reason) {
        return this.banPlayerFull(reason, null, null);
    }

    /**
     * Permanently Bans the Profile and IP address currently used by the player.
     *
     * @param reason Reason for ban
     * @param source Source of ban, or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerFull(@Nullable String reason, @Nullable String source) {
        return this.banPlayerFull(reason, null, source);
    }

    /**
     * Bans the Profile and IP address currently used by the player.
     *
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerFull(@Nullable String reason, java.util.@Nullable Date expires) {
        return this.banPlayerFull(reason, expires, null);
    }

    /**
     * Bans the Profile and IP address currently used by the player.
     *
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @param source Source of the ban, or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerFull(@Nullable String reason, java.util.@Nullable Date expires, @Nullable String source) {
        this.banPlayer(reason, expires, source);
        return this.banPlayerIP(reason, expires, source, true);
    }

    /**
     * Permanently Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     *
     * @param reason Reason for ban
     * @param kickPlayer Whether or not to kick the player afterwards
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, boolean kickPlayer) {
        return this.banPlayerIP(reason, null, null, kickPlayer);
    }

    /**
     * Permanently Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for ban
     * @param source Source of ban, or null for default
     * @param kickPlayer Whether or not to kick the player afterwards
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable String source, boolean kickPlayer) {
        return this.banPlayerIP(reason, null, source, kickPlayer);
    }

    /**
     * Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @param kickPlayer Whether or not to kick the player afterwards
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, java.util.@Nullable Date expires, boolean kickPlayer) {
        return this.banPlayerIP(reason, expires, null, kickPlayer);
    }

    /**
     * Permanently Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     *
     * @param reason Reason for ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason) {
        return this.banPlayerIP(reason, null, null);
    }

    /**
     * Permanently Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for ban
     * @param source Source of ban, or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, @Nullable String source) {
        return this.banPlayerIP(reason, null, source);
    }

    /**
     * Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, java.util.@Nullable Date expires) {
        return this.banPlayerIP(reason, expires, null);
    }

    /**
     * Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @param source Source of the ban or null for default
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, java.util.@Nullable Date expires, @Nullable String source) {
        return this.banPlayerIP(reason, expires, source, true);
    }

    /**
     * Bans the IP address currently used by the player.
     * Does not ban the Profile, use {@link #banPlayerFull(String, java.util.Date, String)}
     * @param reason Reason for Ban
     * @param expires When to expire the ban
     * @param source Source of the ban or null for default
     * @param kickPlayer if the targeted player should be kicked
     * @return Ban Entry
     * @deprecated use {@link #ban(String, Date, String)} and {@link #banIp(String, Date, String, boolean)}
     */
    @Deprecated(since = "1.20.4")
    public default org.bukkit.@Nullable BanEntry banPlayerIP(@Nullable String reason, java.util.@Nullable Date expires, @Nullable String source, boolean kickPlayer) {
        org.bukkit.BanEntry banEntry = org.bukkit.Bukkit.getServer().getBanList(org.bukkit.BanList.Type.IP).addBan(getAddress().getAddress().getHostAddress(), reason, expires, source);
        if (kickPlayer && isOnline()) {
            this.getPlayer().kickPlayer(reason);
        }

        return banEntry;
    }

    /**
     * Sends an Action Bar message to the client.
     *
     * Use Section symbols for legacy color codes to send formatting.
     *
     * @param message The message to send
     * @deprecated use {@link #sendActionBar(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void sendActionBar(String message);

    /**
     * Sends an Action Bar message to the client.
     *
     * Use supplied alternative character to the section symbol to represent legacy color codes.
     *
     * @param alternateChar Alternate symbol such as '&amp;'
     * @param message The message to send
     * @deprecated use {@link #sendActionBar(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void sendActionBar(char alternateChar, String message);

    /**
     * Sends an Action Bar message to the client.
     *
     * @param message The components to send
     * @deprecated use {@link #sendActionBar(net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void sendActionBar(net.md_5.bungee.api.chat.BaseComponent... message);

    /**
     * Sends the component to the player
     *
     * @param component the components to send
     * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
     */
    @Override
    @Deprecated
    public default void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
        this.spigot().sendMessage(component);
    }

    /**
     * Sends an array of components as a single message to the player
     *
     * @param components the components to send
     * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
     */
    @Override
    @Deprecated
    public default void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
        this.spigot().sendMessage(components);
    }

    /**
     * Sends an array of components as a single message to the specified screen position of this player
     *
     * @deprecated This is unlikely the API you want to use. See {@link #sendActionBar(String)} for a more proper Action Bar API. This deprecated API may send unsafe items to the client.
     * @param position the screen position
     * @param components the components to send
     */
    @Deprecated
    public default void sendMessage(net.md_5.bungee.api.ChatMessageType position, net.md_5.bungee.api.chat.BaseComponent... components) {
        this.spigot().sendMessage(position, components);
    }

    /**
     * Set the text displayed in the player list header and footer for this player
     *
     * @param header content for the top of the player list
     * @param footer content for the bottom of the player list
     * @deprecated in favour of {@link #sendPlayerListHeaderAndFooter(net.kyori.adventure.text.Component, net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.BaseComponent @Nullable [] header, net.md_5.bungee.api.chat.BaseComponent @Nullable [] footer);

    /**
     * Set the text displayed in the player list header and footer for this player
     *
     * @param header content for the top of the player list
     * @param footer content for the bottom of the player list
     * @deprecated in favour of {@link #sendPlayerListHeaderAndFooter(net.kyori.adventure.text.Component, net.kyori.adventure.text.Component)}
     */
    @Deprecated
    public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.@Nullable BaseComponent header, net.md_5.bungee.api.chat.@Nullable BaseComponent footer);

    /**
     * Update the times for titles displayed to the player
     *
     * @param fadeInTicks  ticks to fade-in
     * @param stayTicks    ticks to stay visible
     * @param fadeOutTicks ticks to fade-out
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks);

    /**
     * Update the subtitle of titles displayed to the player
     *
     * @param subtitle Subtitle to set
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent[] subtitle);

    /**
     * Update the subtitle of titles displayed to the player
     *
     * @param subtitle Subtitle to set
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent subtitle);

    /**
     * Show the given title to the player, along with the last subtitle set, using the last set times
     *
     * @param title Title to set
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void showTitle(net.md_5.bungee.api.chat.@Nullable BaseComponent[] title);

    /**
     * Show the given title to the player, along with the last subtitle set, using the last set times
     *
     * @param title Title to set
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void showTitle(net.md_5.bungee.api.chat.@Nullable BaseComponent title);

    /**
     * Show the given title and subtitle to the player using the given times
     *
     * @param title        big text
     * @param subtitle     little text under it
     * @param fadeInTicks  ticks to fade-in
     * @param stayTicks    ticks to stay visible
     * @param fadeOutTicks ticks to fade-out
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void showTitle(net.md_5.bungee.api.chat.@Nullable BaseComponent[] title, net.md_5.bungee.api.chat.@Nullable BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks);

    /**
     * Show the given title and subtitle to the player using the given times
     *
     * @param title        big text
     * @param subtitle     little text under it
     * @param fadeInTicks  ticks to fade-in
     * @param stayTicks    ticks to stay visible
     * @param fadeOutTicks ticks to fade-out
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    public void showTitle(net.md_5.bungee.api.chat.@Nullable BaseComponent title, net.md_5.bungee.api.chat.@Nullable BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks);

    /**
     * Show the title to the player, overriding any previously displayed title.
     *
     * <p>This method overrides any previous title, use {@link #updateTitle(com.destroystokyo.paper.Title)} to change the existing one.</p>
     *
     * @param title the title to send
     * @throws NullPointerException if the title is null
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    void sendTitle(com.destroystokyo.paper.Title title);

    /**
     * Show the title to the player, overriding any previously displayed title.
     *
     * <p>This method doesn't override previous titles, but changes their values.</p>
     *
     * @param title the title to send
     * @throws NullPointerException if title is null
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated
    void updateTitle(com.destroystokyo.paper.Title title);

    /**
     * Hide any title that is currently visible to the player
     *
     * @deprecated use {@link #clearTitle()}
     */
    @Deprecated
    public void hideTitle();
    // Paper end

    /**
     * Send a hurt animation. This fakes incoming damage towards the player from
     * the given yaw relative to the player's direction.
     *
     * @param yaw the yaw in degrees relative to the player's direction where 0
     * is in front of the player, 90 is to the right, 180 is behind, and 270 is
     * to the left
     */
    public void sendHurtAnimation(float yaw);

    /**
     * Sends the given server links to the player.
     *
     * @param links links to send
     */
    public void sendLinks(ServerLinks links);

    /**
     * Add custom chat completion suggestions shown to the player while typing a
     * message.
     *
     * @param completions the completions to send
     */
    public void addCustomChatCompletions(Collection<String> completions);

    /**
     * Remove custom chat completion suggestions shown to the player while
     * typing a message.
     *
     * Online player names cannot be removed with this method. This will affect
     * only custom completions added by {@link #addCustomChatCompletions(Collection)}
     * or {@link #setCustomChatCompletions(Collection)}.
     *
     * @param completions the completions to remove
     */
    public void removeCustomChatCompletions(Collection<String> completions);

    /**
     * Set the list of chat completion suggestions shown to the player while
     * typing a message.
     * <p>
     * If completions were set previously, this method will remove them all and
     * replace them with the provided completions.
     *
     * @param completions the completions to set
     */
    public void setCustomChatCompletions(Collection<String> completions);

    /**
     * Forces an update of the player's entire inventory.
     */
    // @ApiStatus.Internal // Paper - is valid API
    public void updateInventory();

    /**
     * Gets this player's previous {@link GameMode}
     *
     * @return Previous game mode or null
     */
    @Nullable
    public GameMode getPreviousGameMode();

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
    @Nullable
    public WeatherType getPlayerWeather();

    /**
     * Restores the normal condition where the player's weather is controlled
     * by server conditions.
     */
    public void resetPlayerWeather();

    // Paper start
    /**
     * Gives the player the amount of experience specified.
     *
     * @param amount Exp amount to give
     */
    public default void giveExp(int amount) {
        giveExp(amount, false);
    }
    /**
     * Gets the player's cooldown between picking up experience orbs.
     *
     * @return The cooldown in ticks
     */
    public int getExpCooldown();

    /**
     * Sets the player's cooldown between picking up experience orbs.
     *
     * <strong>Note:</strong> Setting this to 0 allows the player to pick up
     * instantly, but setting this to a negative value will cause the player to
     * be unable to pick up xp-orbs.
     *
     * Calling this Method will result in {@link PlayerExpCooldownChangeEvent}
     * being called.
     *
     * @param ticks The cooldown in ticks
     */
    public void setExpCooldown(int ticks);

    /**
     * Gives the player the amount of experience specified.
     *
     * @param amount Exp amount to give
     * @param applyMending Mend players items with mending, with same behavior as picking up orbs. calls {@link #applyMending(int)}
     */
    public void giveExp(int amount, boolean applyMending);

    /**
     * Applies the mending effect to any items just as picking up an orb would.
     *
     * Can also be called with {@link #giveExp(int, boolean)} by passing true to applyMending
     *
     * @param amount Exp to apply
     * @return the remaining experience
     */
    public int applyMending(int amount);
    // Paper end

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
     * Gets the players total experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time and is not currently displayed to the client.
     *
     * @return Current total experience points
     */
    public int getTotalExperience();

    /**
     * Sets the players current experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time and is not currently displayed to the client.
     *
     * @param exp New total experience points
     */
    public void setTotalExperience(int exp);
    // Paper start
    /**
     * Gets the players total amount of experience points he collected to reach the current level and level progress.
     *
     * <p>This method differs from {@link #getTotalExperience()} in that this method always returns an
     * up-to-date value that reflects the players{@link #getLevel() level} and {@link #getExp() level progress}</p>
     *
     * @return Current total experience points
     * @see #getLevel()
     * @see #getExp()
     * @see #setExperienceLevelAndProgress(int)
     */
    @org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int calculateTotalExperiencePoints();

    /**
     * Updates the players level and level progress to that what would be reached when the total amount of experience
     * had been collected.
     *
     * <p>This method differs from {@link #setTotalExperience(int)} in that this method actually updates the
     * {@link #getLevel() level} and {@link #getExp() level progress} so that a subsequent call of
     * {@link #calculateTotalExperiencePoints()} yields the same amount of points that have been set</p>
     *
     * @param totalExperience New total experience points
     * @see #setLevel(int)
     * @see #setExp(float)
     * @see #calculateTotalExperiencePoints()
     */
    void setExperienceLevelAndProgress(@org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int totalExperience);

    /**
     * Gets the total amount of experience points that are needed to reach the next level from zero progress towards it.
     *
     * <p>Can be used with {@link #getExp()} to calculate the current points for the current level and alike</p>
     *
     * @return The required experience points
     * @see #getExp()
     */
    int getExperiencePointsNeededForNextLevel();
    // Paper end

    /**
     * Send an experience change.
     *
     * This fakes an experience change packet for a user. This will not actually
     * change the experience points in any way.
     *
     * @param progress Experience progress percentage (between 0.0 and 1.0)
     * @see #setExp(float)
     */
    public void sendExperienceChange(float progress);

    /**
     * Send an experience change.
     *
     * This fakes an experience change packet for a user. This will not actually
     * change the experience points in any way.
     *
     * @param progress New experience progress percentage (between 0.0 and 1.0)
     * @param level New experience level
     *
     * @see #setExp(float)
     * @see #setLevel(int)
     */
    public void sendExperienceChange(float progress, int level);

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

    // Paper start - flying fall damage
    /**
     * Allows you to enable fall damage while {@link #getAllowFlight()} is {@code true}
     *
     * @param flyingFallDamage Enables fall damage when {@link #getAllowFlight()} is {@code true}
     */
    public void setFlyingFallDamage(net.kyori.adventure.util.TriState flyingFallDamage);

    /**
     * Allows you to get if fall damage is enabled while {@link #getAllowFlight()} is {@code true}
     *
     * @return A tristate of whether fall damage is enabled, not set, or disabled when {@link #getAllowFlight()} is {@code true}
     */
    public net.kyori.adventure.util.TriState hasFlyingFallDamage();
    // Paper end

    /**
     * Hides a player from this player
     *
     * @param player Player to hide
     * @deprecated see {@link #hidePlayer(Plugin, Player)}
     */
    @Deprecated(since = "1.12.2")
    public void hidePlayer(Player player);

    /**
     * Hides a player from this player
     *
     * @param plugin Plugin that wants to hide the player
     * @param player Player to hide
     */
    default void hidePlayer(Plugin plugin, Player player) {
        this.hideEntity(plugin, player);
    }

    /**
     * Allows this player to see a player that was previously hidden
     *
     * @param player Player to show
     * @deprecated see {@link #showPlayer(Plugin, Player)}
     */
    @Deprecated(since = "1.12.2")
    public void showPlayer(Player player);

    /**
     * Allows this player to see a player that was previously hidden. If
     * another plugin had hidden the player too, then the player will
     * remain hidden until the other plugin calls this method too.
     *
     * @param plugin Plugin that wants to show the player
     * @param player Player to show
     */
    default void showPlayer(Plugin plugin, Player player) {
        this.showEntity(plugin, player);
    }

    /**
     * Checks to see if a player has been hidden from this player
     *
     * @param player Player to check
     * @return True if the provided player is not being hidden from this
     *     player
     */
    public boolean canSee(Player player);

    /**
     * Visually hides an entity from this player.
     *
     * @param plugin Plugin that wants to hide the entity
     * @param entity Entity to hide
     */
    public void hideEntity(Plugin plugin, Entity entity);

    /**
     * Allows this player to see an entity that was previously hidden. If
     * another plugin had hidden the entity too, then the entity will
     * remain hidden until the other plugin calls this method too.
     *
     * @param plugin Plugin that wants to show the entity
     * @param entity Entity to show
     */
    public void showEntity(Plugin plugin, Entity entity);

    /**
     * Checks to see if an entity has been visually hidden from this player.
     *
     * @param entity Entity to check
     * @return True if the provided entity is not being hidden from this
     *     player
     */
    public boolean canSee(Entity entity);

    // Paper start
    /**
     * Returns whether the {@code other} player is listed for {@code this}.
     *
     * @param other The other {@link Player} to check for listing.
     * @return True if the {@code other} player is listed for {@code this}.
     */
    boolean isListed(Player other);

    /**
     * Unlists the {@code other} player from the tablist.
     *
     * @param other The other {@link Player} to de-list.
     * @return True if the {@code other} player was listed.
     */
    boolean unlistPlayer(Player other);

    /**
     * Lists the {@code other} player.
     *
     * @param other The other {@link Player} to list.
     * @return True if the {@code other} player was not listed.
     * @throws IllegalStateException if this player can't see the other player
     * @see #canSee(Player)
     */
    boolean listPlayer(Player other);
    // Paper end

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
     * texture pack in the past, it will perform a file size check against
     * the response content to determine if the texture pack has changed and
     * needs to be downloaded again. When this request is sent for the very
     * first time from a given server, the client will first display a
     * confirmation GUI to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server textures on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>The request is sent with "null" as the hash. This might result
     *     in newer versions not loading the pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the texture
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long.
     * @deprecated Minecraft no longer uses textures packs. Instead you
     *     should use {@link #setResourcePack(UUID, String, byte[], net.kyori.adventure.text.Component, boolean)}.
     */
    @Deprecated(since = "1.7.2")
    default void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached the same
     * resource pack in the past, it will perform a file size check against
     * the response content to determine if the resource pack has changed and
     * needs to be downloaded again. When this request is sent for the very
     * first time from a given server, the client will first display a
     * confirmation GUI to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>The request is sent with empty string as the hash. This might result
     *     in newer versions not loading the pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     */
    @Deprecated // Paper - adventure
    default void setResourcePack(String url) {
        this.setResourcePack(url, (byte[]) null);
    }

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     */
    @Deprecated // Paper - adventure
    default void setResourcePack(String url, byte @Nullable [] hash) {
        this.setResourcePack(url, hash, false);
    }

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     */
    @Deprecated // Paper - adventure
    default void setResourcePack(String url, byte @Nullable [] hash, @Nullable String prompt) {
        this.setResourcePack(url, hash, prompt, false);
    }

    // Paper start
    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @see #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)
     */
    default void setResourcePack(final String url, final byte @Nullable [] hash, final net.kyori.adventure.text.@Nullable Component prompt) {
        this.setResourcePack(url, hash, prompt, false);
    }
    // Paper end

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     */
    @Deprecated // Paper - adventure
    default void setResourcePack(String url, byte @Nullable [] hash, boolean force) {
        this.setResourcePack(url, hash, (String) null, force);
    }

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     */
    @Deprecated // Paper
    public void setResourcePack(String url, byte @Nullable [] hash, @Nullable String prompt, boolean force);

    // Paper start
    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI with a custom prompt
     * to the player before proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @see #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)
     */
    default void setResourcePack(final String url, final byte @Nullable [] hash, final net.kyori.adventure.text.@Nullable Component prompt, final boolean force) {
        this.setResourcePack(UUID.nameUUIDFromBytes(url.getBytes(java.nio.charset.StandardCharsets.UTF_8)), url, hash, prompt, force);
    }
    // Paper end

    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param id Unique resource pack ID.
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @deprecated in favour of {@link #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)}
     */
    @Deprecated // Paper - adventure
    public void setResourcePack(UUID id, String url, byte @Nullable [] hash, @Nullable String prompt, boolean force);

    // Paper start
    /**
     * Request that the player's client download and switch resource packs.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically switch to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param uuid Unique resource pack ID.
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     * @see #sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest)
     */
    void setResourcePack(UUID uuid, String url, byte @Nullable [] hash, net.kyori.adventure.text.@Nullable Component prompt, boolean force);
    // Paper end

    // Paper start - more resource pack API
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
     *     case this method will have no effect on them.
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash A 40 character hexadecimal and lowercase SHA-1 digest of
     *     the resource pack file.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    default void setResourcePack(final String url, final String hash) {
        this.setResourcePack(url, hash, false);
    }

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
     *     case this method will have no effect on them.
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash A 40 character hexadecimal and lowercase SHA-1 digest of
     *     the resource pack file.
     * @param required Marks if the resource pack should be required by the client
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    default void setResourcePack(final String url, final String hash, final boolean required) {
        this.setResourcePack(url, hash, required, null);
    }

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
     *     case this method will have no effect on them.
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * </ul>
     *
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash A 40 character hexadecimal and lowercase SHA-1 digest of
     *     the resource pack file.
     * @param required Marks if the resource pack should be required by the client
     * @param resourcePackPrompt A Prompt to be displayed in the client request
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    default void setResourcePack(final String url, final String hash, final boolean required, final net.kyori.adventure.text.@Nullable Component resourcePackPrompt) {
        this.setResourcePack(UUID.nameUUIDFromBytes(url.getBytes(java.nio.charset.StandardCharsets.UTF_8)), url, hash, resourcePackPrompt, required);
    }

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
     *     case this method will have no effect on them.
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePacks(UUID, UUID...)} or {@link #clearResourcePacks()}.
     * </ul>
     *
     * @param uuid Unique resource pack ID.
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash A 40 character hexadecimal and lowercase SHA-1 digest of
     *     the resource pack file.
     * @param resourcePackPrompt A Prompt to be displayed in the client request
     * @param required Marks if the resource pack should be required by the client
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     */
    default void setResourcePack(final UUID uuid, final String url, final String hash, final net.kyori.adventure.text.@Nullable Component resourcePackPrompt, final boolean required) {
        this.sendResourcePacks(net.kyori.adventure.resource.ResourcePackRequest.resourcePackRequest()
            .required(required)
            .replace(true)
            .prompt(resourcePackPrompt)
            .packs(net.kyori.adventure.resource.ResourcePackInfo.resourcePackInfo(uuid, java.net.URI.create(url), hash))
        );
    }

    /**
     * Gets the most recent resource pack status from the player.
     *
     * @return the most recent status or null
     */
    org.bukkit.event.player.PlayerResourcePackStatusEvent.@Nullable Status getResourcePackStatus();

    /**
     * Gets the most recent pack hash from the player.
     *
     * @return the most recent hash or null
     * @deprecated This is no longer sent from the client and will always be null
     */
    @Deprecated(forRemoval = true, since = "1.13.2")
    @org.jetbrains.annotations.Contract("-> null")
    default @Nullable String getResourcePackHash() {
        return null;
    }

    /**
     * Gets if the last resource pack status from the player
     * was {@link org.bukkit.event.player.PlayerResourcePackStatusEvent.Status#SUCCESSFULLY_LOADED}.
     *
     * @return true if last status was successfully loaded
     */
    default boolean hasResourcePack() {
        return this.getResourcePackStatus() == org.bukkit.event.player.PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED;
    }
    // Paper end - more resource pack API

    /**
     * Request that the player's client download and include another resource pack.
     * <p>
     * The player's client will download the new resource pack asynchronously
     * in the background, and will automatically add to it once the
     * download is complete. If the client has downloaded and cached a
     * resource pack with the same hash in the past it will not download but
     * directly apply the cached pack. If the hash is null and the client has
     * downloaded and cached the same resource pack in the past, it will
     * perform a file size check against the response content to determine if
     * the resource pack has changed and needs to be downloaded again. When
     * this request is sent for the very first time from a given server, the
     * client will first display a confirmation GUI to the player before
     * proceeding with the download.
     * <p>
     * Notes:
     * <ul>
     * <li>Players can disable server resources on their client, in which
     *     case this method will have no effect on them. Use the
     *     {@link PlayerResourcePackStatusEvent} to figure out whether or not
     *     the player loaded the pack!
     * <li>To remove a resource pack you can use
     *     {@link #removeResourcePack(UUID)} or {@link #removeResourcePacks()}.
     * <li>The request is sent with empty string as the hash when the hash is
     *     not provided. This might result in newer versions not loading the
     *     pack correctly.
     * </ul>
     *
     * @param id Unique resource pack ID.
     * @param url The URL from which the client will download the resource
     *     pack. The string must contain only US-ASCII characters and should
     *     be encoded as per RFC 1738.
     * @param hash The sha1 hash sum of the resource pack file which is used
     *     to apply a cached version of the pack directly without downloading
     *     if it is available. Hast to be 20 bytes long!
     * @param prompt The optional custom prompt message to be shown to client.
     * @param force If true, the client will be disconnected from the server
     *     when it declines to use the resource pack.
     * @throws IllegalArgumentException Thrown if the URL is null.
     * @throws IllegalArgumentException Thrown if the URL is too long. The
     *     length restriction is an implementation specific arbitrary value.
     * @throws IllegalArgumentException Thrown if the hash is not 20 bytes
     *     long.
     */
    public void addResourcePack(UUID id, String url, byte @Nullable [] hash, @Nullable String prompt, boolean force);

    /**
     * Request that the player's client remove a resource pack sent by the
     * server.
     *
     * @param id the id of the resource pack.
     * @throws IllegalArgumentException If the ID is null.
     * @see #removeResourcePacks(UUID, UUID...)
     */
    public void removeResourcePack(UUID id);

    /**
     * Request that the player's client remove all loaded resource pack sent by
     * the server.
     * @see #clearResourcePacks()
     */
    public void removeResourcePacks();

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
     * Gets the {@link WorldBorder} visible to this Player, or null if viewing
     * the world's world border.
     *
     * @return the player's world border
     */
    @Nullable
    public WorldBorder getWorldBorder();

    /**
     * Sets the {@link WorldBorder} visible to this Player.
     *
     * @param border the border to set, or null to set to the world border of
     * the player's current world
     *
     * @throws UnsupportedOperationException if setting the border to that of
     * a world in which the player is not currently present.
     *
     * @see Server#createWorldBorder()
     */
    public void setWorldBorder(@Nullable WorldBorder border);

    /**
     * Send a health update to the player. This will adjust the health, food, and
     * saturation on the client and will not affect the player's actual values on
     * the server. As soon as any of these values change on the server, changes sent
     * by this method will no longer be visible.
     *
     * @param health the health. If 0.0, the client will believe it is dead
     * @param foodLevel the food level
     * @param saturation the saturation
     */
    public void sendHealthUpdate(double health, int foodLevel, float saturation);

    /**
     * Send a health update to the player using its known server values. This will
     * synchronize the health, food, and saturation on the client and therefore may
     * be useful when changing a player's maximum health attribute.
     */
    public void sendHealthUpdate();

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

    /**
     * Gets the entity which is followed by the camera when in
     * {@link GameMode#SPECTATOR}.
     *
     * @return the followed entity, or null if not in spectator mode or not
     * following a specific entity.
     */
    @Nullable
    public Entity getSpectatorTarget();

    /**
     * Sets the entity which is followed by the camera when in
     * {@link GameMode#SPECTATOR}.
     *
     * @param entity the entity to follow or null to reset
     * @throws IllegalStateException if the player is not in
     * {@link GameMode#SPECTATOR}
     */
    public void setSpectatorTarget(@Nullable Entity entity);

    /**
     * Sends a title and a subtitle message to the player. If either of these
     * values are null, they will not be sent and the display will remain
     * unchanged. If they are empty strings, the display will be updated as
     * such. If the strings contain a new line, only the first line will be
     * sent. The titles will be displayed with the client's default timings.
     *
     * @param title Title text
     * @param subtitle Subtitle text
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated(since = "1.8.7")
    public void sendTitle(@Nullable String title, @Nullable String subtitle);

    /**
     * Sends a title and a subtitle message to the player. If either of these
     * values are null, they will not be sent and the display will remain
     * unchanged. If they are empty strings, the display will be updated as
     * such. If the strings contain a new line, only the first line will be
     * sent. All timings values may take a value of -1 to indicate that they
     * will use the last value sent (or the defaults if no title has been
     * displayed).
     *
     * @param title Title text
     * @param subtitle Subtitle text
     * @param fadeIn time in ticks for titles to fade in. Defaults to 10.
     * @param stay time in ticks for titles to stay. Defaults to 70.
     * @param fadeOut time in ticks for titles to fade out. Defaults to 20.
     * @deprecated Use {@link #showTitle(net.kyori.adventure.title.Title)} or {@link #sendTitlePart(net.kyori.adventure.title.TitlePart, Object)}
     */
    @Deprecated // Paper - Adventure
    public void sendTitle(@Nullable String title, @Nullable String subtitle, int fadeIn, int stay, int fadeOut);

    /**
     * Resets the title displayed to the player. This will clear the displayed
     * title / subtitle and reset timings to their default values.
     */
    public void resetTitle();

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     */
    default void spawnParticle(Particle particle, Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     */
    default void spawnParticle(Particle particle, double x, double y, double z, int count) {
        this.spawnParticle(particle, x, y, z, count, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, Location location, int count, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, double x, double y, double z, int count, @Nullable T data) {
        this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    default void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     */
    default void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, @Nullable T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    default void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     */
    default void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     */
    default <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param location the location to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     * @param force whether to send the particle to the player in an extended
     *              range and encourage their client to render it regardless of
     *              settings
     */
    default <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    /**
     * Spawns the particle (the number of times specified by count)
     * at the target location. The position of each particle will be
     * randomized positively and negatively by the offset parameters
     * on each axis.
     *
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param particle the particle to spawn
     * @param x the position on the x axis to spawn at
     * @param y the position on the y axis to spawn at
     * @param z the position on the z axis to spawn at
     * @param count the number of particles
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     * @param force whether to send the particle to the player in an extended
     *              range and encourage their client to render it regardless of
     *              settings
     */
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, @Nullable T data, boolean force);

    /**
     * Return the player's progression on the specified advancement.
     *
     * @param advancement advancement
     * @return object detailing the player's progress
     */
    public AdvancementProgress getAdvancementProgress(Advancement advancement);

    /**
     * Get the player's current client side view distance.
     * <br>
     * Will default to the server view distance if the client has not yet
     * communicated this information,
     *
     * @return client view distance as above
     */
    public int getClientViewDistance();

    // Paper start
    /**
     * Gets the player's current locale.
     *
     * @return the player's locale
     */
    java.util.Locale locale();
    // Paper end
    /**
     * Gets the player's estimated ping in milliseconds.
     *
     * In Vanilla this value represents a weighted average of the response time
     * to application layer ping packets sent. This value does not represent the
     * network round trip time and as such may have less granularity and be
     * impacted by other sources. For these reasons it <b>should not</b> be used
     * for anti-cheat purposes. Its recommended use is only as a
     * <b>qualitative</b> indicator of connection quality (Vanilla uses it for
     * this purpose in the tab list).
     *
     * @return player ping
     */
    public int getPing();

    /**
     * Gets the player's current locale.
     *
     * The value of the locale String is not defined properly.
     * <br>
     * The vanilla Minecraft client will use lowercase language / country pairs
     * separated by an underscore, but custom resource packs may use any format
     * they wish.
     *
     * @return the player's locale
     * @deprecated in favour of {@link #locale()}
     */
    @Deprecated // Paper
    public String getLocale();

    // Paper start
    /**
     * Get whether the player can affect mob spawning
     *
     * @return if the player can affect mob spawning
     */
    public boolean getAffectsSpawning();

    /**
     * Set whether the player can affect mob spawning
     *
     * @param affects Whether the player can affect mob spawning
     */
    public void setAffectsSpawning(boolean affects);

    /**
     * Gets the view distance for this player
     *
     * @return the player's view distance
     * @see org.bukkit.World#getViewDistance()
     */
    public int getViewDistance();

    /**
     * Sets the view distance for this player
     *
     * @param viewDistance the player's view distance
     * @see org.bukkit.World#setViewDistance(int)
     */
    public void setViewDistance(int viewDistance);

    /**
     * Gets the simulation distance for this player
     *
     * @return the player's simulation distance
     */
    public int getSimulationDistance();

    /**
     * Sets the simulation distance for this player
     *
     * @param simulationDistance the player's new simulation distance
     */
    public void setSimulationDistance(int simulationDistance);

    /**
     * Gets the no-ticking view distance for this player.
     * <p>
     * No-tick view distance is the view distance where chunks will load, however the chunks and their entities will not
     * be set to tick.
     * </p>
     * @return The no-tick view distance for this player.
     * @deprecated Use {@link #getViewDistance()}
     */
    @Deprecated
    default int getNoTickViewDistance() {
        return this.getViewDistance();
    }

    /**
     * Sets the no-ticking view distance for this player.
     * <p>
     * No-tick view distance is the view distance where chunks will load, however the chunks and their entities will not
     * be set to tick.
     * </p>
     * @param viewDistance view distance in [2, 32] or -1
     * @deprecated Use {@link #setViewDistance(int)}
     */
    @Deprecated
    default void setNoTickViewDistance(int viewDistance) {
        this.setViewDistance(viewDistance);
    }

    /**
     * Gets the sending view distance for this player.
     * <p>
     * Sending view distance is the view distance where chunks will load in for players.
     * </p>
     * @return The sending view distance for this player.
     */
    public int getSendViewDistance();

    /**
     * Sets the sending view distance for this player.
     * <p>
     * Sending view distance is the view distance where chunks will load in for players.
     * </p>
     * @param viewDistance view distance in [2, 32] or -1
     */
    public void setSendViewDistance(int viewDistance);
    // Paper end

    /**
     * Update the list of commands sent to the client.
     * <br>
     * Generally useful to ensure the client has a complete list of commands
     * after permission changes are done.
     */
    public void updateCommands();

    /**
     * Open a {@link Material#WRITTEN_BOOK} for a Player
     *
     * @param book The book to open for this player
     */
    public void openBook(ItemStack book);

    /**
     * Open a Sign for editing by the Player.
     *
     * The Sign must be in the same world as the player.
     *
     * @param sign The sign to edit
     * @deprecated use {@link #openSign(Sign, Side)}
     */
    @Deprecated
    default void openSign(Sign sign) {
        this.openSign(sign, org.bukkit.block.sign.Side.FRONT);
    }

    /**
     * Open a Sign for editing by the Player.
     *
     * The Sign must be placed in the same world as the player.
     *
     * @param sign The sign to edit
     * @param side The side to edit
     */
    public void openSign(Sign sign, Side side);

    /**
     * Open a sign for editing by the player.
     * <p>
     * The sign must only be placed locally for the player, which can be done with {@link #sendBlockChange(Location, BlockData)} and {@link #sendBlockUpdate(Location, TileState)}.
     * A side-effect of this is that normal events, like {@link org.bukkit.event.block.SignChangeEvent} will not be called (unless there is an actual sign in the world).
     * Additionally, the client may enforce distance limits to the opened position.
     * </p>
     *
     * @param block The block where the client has a sign placed
     * @param side The side to edit
     * @see io.papermc.paper.event.packet.UncheckedSignChangeEvent
     */
    @ApiStatus.Experimental
    void openVirtualSign(Position block, Side side);

    /**
     * Shows the demo screen to the player, this screen is normally only seen in
     * the demo version of the game.
     * <br>
     * Servers can modify the text on this screen using a resource pack.
     */
    public void showDemoScreen();

    /**
     * Gets whether the player has the "Allow Server Listings" setting enabled.
     *
     * @return whether the player allows server listings
     */
    public boolean isAllowingServerListings();

    // Paper start
    @Override
    default net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowEntity> asHoverEvent(final java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowEntity> op) {
        return net.kyori.adventure.text.event.HoverEvent.showEntity(op.apply(net.kyori.adventure.text.event.HoverEvent.ShowEntity.of(this.getType().getKey(), this.getUniqueId(), this.displayName())));
    }
    // Paper end

    // Paper start - Player Profile API
    /**
     * Gets a copy of this players profile
     *
     * @return The players profile object
     */
    com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile();

    /**
     * Changes the PlayerProfile for this player. This will cause this player
     * to be re-registered to all clients that can currently see this player.
     * <p>
     * After executing this method, the player {@link java.util.UUID} won't
     * be swapped, only their name and profile properties.
     *
     * @param profile The new profile to use
     */
    void setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile profile);
    // Paper end - Player Profile API

    // Paper start - attack cooldown API
    /**
     * Returns the amount of ticks the current cooldown lasts
     *
     * @return Amount of ticks cooldown will last
     */
    float getCooldownPeriod();

    /**
     * Returns the percentage of attack power available based on the cooldown (zero to one).
     *
     * @param adjustTicks Amount of ticks to add to cooldown counter for this calculation
     * @return Percentage of attack power available
     */
    float getCooledAttackStrength(float adjustTicks);

    /**
     * Reset the cooldown counter to 0, effectively starting the cooldown period.
     */
    void resetCooldown();
    // Paper end - attack cooldown API

    // Paper start - client option API
    /**
     * @return the client option value of the player
     */
    <T> T getClientOption(com.destroystokyo.paper.ClientOption<T> option);
    // Paper end - client option API

    // Paper start - elytra boost API
    /**
     * Boost a Player that's {@link #isGliding()} using a {@link Firework}.
     * If the creation of the entity is cancelled, no boosting is done.
     * This method does not fire {@link com.destroystokyo.paper.event.player.PlayerElytraBoostEvent}.
     *
     * @param firework The {@link Material#FIREWORK_ROCKET} to boost the player with
     * @return The {@link Firework} boosting the Player or null if the spawning of the entity was cancelled
     * @throws IllegalArgumentException if {@link #isGliding()} is false
     * or if the {@code firework} isn't a {@link Material#FIREWORK_ROCKET}
     * @deprecated use {@link HumanEntity#fireworkBoost(ItemStack)} instead. Note that this method <b>does not</b>
     * check if the player is gliding or not.
     */
    default @Nullable Firework boostElytra(final ItemStack firework) {
        com.google.common.base.Preconditions.checkState(this.isGliding(), "Player must be gliding");
        return this.fireworkBoost(firework);
    }
    // Paper end - elytra boost API

    // Paper start - sendOpLevel API
    /**
     * Send a packet to the player indicating its operator status level.
     * <p>
     * <b>Note:</b> This will not persist across more than the current connection, and setting the player's operator
     * status as a later point <i>will</i> override the effects of this.
     *
     * @param level The level to send to the player. Must be in {@code [0, 4]}.
     * @throws IllegalArgumentException If the level is negative or greater than {@code 4} (i.e. not within {@code [0, 4]}).
     */
    void sendOpLevel(byte level);
    // Paper end - sendOpLevel API

    // Paper start - custom chat completions API
    /**
     * Adds custom chat completion suggestions that the client will
     * suggest when typing in chat.
     *
     * @param completions custom completions
     * @deprecated use {@link #addCustomChatCompletions(Collection)}
     */
    @Deprecated(since = "1.20.1")
    void addAdditionalChatCompletions(java.util.Collection<String> completions);

    /**
     * Removes custom chat completion suggestions that the client
     * suggests when typing in chat.
     *
     * Note: this only applies to previously added custom completions,
     * online player names are always suggested and cannot be removed.
     *
     * @param completions custom completions
     * @deprecated use {@link #addCustomChatCompletions(Collection)}
     */
    @Deprecated(since = "1.20.1")
    void removeAdditionalChatCompletions(java.util.Collection<String> completions);
    // Paper end - custom chat completions API

    // Spigot start
    public class Spigot extends Entity.Spigot {

        /**
         * Gets the connection address of this player, regardless of whether it
         * has been spoofed or not.
         *
         * @return the player's connection address
         */
        public InetSocketAddress getRawAddress() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Respawns the player if dead.
         */
        public void respawn() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Gets all players hidden with {@link #hidePlayer(org.bukkit.entity.Player)}.
         *
         * @return a Set with all hidden players
         */
        public java.util.Set<Player> getHiddenPlayers() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated // Paper
        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Deprecated // Paper
        @Override
        public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends the component to the specified screen position of this player
         *
         * @param position the screen position
         * @param component the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the specified screen position of this player
         *
         * @param position the screen position
         * @param components the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends the component to the specified screen position of this player
         *
         * @param position the screen position
         * @param sender the sender of the message
         * @param component the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, java.util.@Nullable UUID sender, net.md_5.bungee.api.chat.BaseComponent component) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * Sends an array of components as a single message to the specified screen position of this player
         *
         * @param position the screen position
         * @param sender the sender of the message
         * @param components the components to send
         * @deprecated use {@code sendMessage} methods that accept {@link net.kyori.adventure.text.Component}
         */
        @Deprecated // Paper
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, java.util.@Nullable UUID sender, net.md_5.bungee.api.chat.BaseComponent... components) {
            throw new UnsupportedOperationException("Not supported yet.");

        }

        // Paper start
        /**
         * @return the player's ping
         * @deprecated use {@link Player#getPing()}
         */
        @Deprecated
        public int getPing() {
            throw new UnsupportedOperationException( "Not supported yet." );
        }
        // Paper end
    }

    // Paper start - brand support
    /**
     * Returns player's client brand name. If the client didn't send this information, the brand name will be null.<br>
     * For the Notchian client this name defaults to <code>vanilla</code>. Some modified clients report other names such as <code>forge</code>.<br>
     * @return client brand name
     */
    @Nullable
    String getClientBrandName();
    // Paper end

    // Paper start - Teleport API
    /**
     * Sets the player's rotation.
     *
     * @param yaw the yaw
     * @param pitch the pitch
     */
    void setRotation(float yaw, float pitch);

    /**
     * Causes the player to look towards the given entity.
     *
     * @param entity Entity to look at
     * @param playerAnchor What part of the player should face the entity
     * @param entityAnchor What part of the entity the player should face
     */
    void lookAt(org.bukkit.entity.Entity entity, LookAnchor playerAnchor, LookAnchor entityAnchor);
    // Paper end - Teleport API

    // Paper start
    /**
     * Displays elder guardian effect with a sound
     *
     * @see #showElderGuardian(boolean)
     */
    default void showElderGuardian() {
        showElderGuardian(false);
    }

    /**
     * Displays elder guardian effect and optionally plays a sound
     *
     * @param silent whether sound should be silenced
     */
    void showElderGuardian(boolean silent);

    /**
     * Returns the player's cooldown in ticks until the next Warden warning can occur.
     *
     * @return ticks until next Warden warning can occur. 0 means there is no cooldown left.
     */
    int getWardenWarningCooldown();

    /**
     * Sets the player's cooldown in ticks until next Warden warning can occur.
     *
     * @param cooldown ticks until next Warden warning can occur. 0 means there is no cooldown left. Values less than 0 are set to 0.
     */
    void setWardenWarningCooldown(int cooldown);

    /**
     * Returns time since last Warden warning in ticks.
     *
     * @return ticks since last Warden warning
     */
    int getWardenTimeSinceLastWarning();

    /**
     * Sets time since last Warden warning in ticks.
     *
     * @param time ticks since last Warden warning
     */
    void setWardenTimeSinceLastWarning(int time);

    /**
     * Returns the player's current Warden warning level.
     *
     * @return current Warden warning level
     */
    int getWardenWarningLevel();

    /**
     * Sets the player's Warden warning level.
     * <p>
     * <b>Note:</b> This will not actually spawn the Warden.
     * Even if the warning level is over threshold, the player still needs to activate a Shrieker in order to summon the Warden.
     *
     * @param warningLevel player's Warden warning level. The warning level is internally limited to valid values.
     */
    void setWardenWarningLevel(int warningLevel);

    /**
     * Increases the player's Warden warning level if possible and not on cooldown.
     * <p>
     * <b>Note:</b> This will not actually spawn the Warden.
     * Even if the warning level is over threshold, the player still needs to activate a Shrieker in order to summon the Warden.
     */
    void increaseWardenWarningLevel();
    // Paper end

    // Paper start
    /**
     * The idle duration is reset when the player
     * sends specific action packets.
     * <p>
     * After the idle duration exceeds {@link org.bukkit.Bukkit#getIdleTimeout()}, the
     * player will be kicked for {@link org.bukkit.event.player.PlayerKickEvent.Cause#IDLING}.
     *
     * @return the current idle duration of this player
     */
    Duration getIdleDuration();

    /**
     * Resets this player's idle duration.
     * <p>
     * After the idle duration exceeds {@link org.bukkit.Bukkit#getIdleTimeout()}, the
     * player will be kicked for {@link org.bukkit.event.player.PlayerKickEvent.Cause#IDLING}.
     *
     * @see #getIdleDuration()
     */
    void resetIdleDuration();
    // Paper end

    // Paper start - Add chunk view API
    /**
     * Gets the set of chunk keys for all chunks that have been sent to the player.
     *
     * @return an immutable set of chunk keys
     * @apiNote currently marked as experimental to gather feedback regarding the returned set being an immutable copy
     * vs it potentially being an unmodifiable view of the set chunks.
     */
    @ApiStatus.Experimental
    java.util.@org.jetbrains.annotations.Unmodifiable Set<Long> getSentChunkKeys();

    /**
     * Gets the set of chunks that have been sent to the player.
     *
     * @return an immutable set of chunks
     * @apiNote currently marked as experimental to gather feedback regarding the returned set being an immutable copy
      * vs it potentially being an unmodifiable view of the set chunks.
     */
    @ApiStatus.Experimental
    java.util.@org.jetbrains.annotations.Unmodifiable Set<org.bukkit.Chunk> getSentChunks();

    /**
     * Checks if the player has been sent a specific chunk.
     *
     * @param chunk the chunk to check
     * @return true if the player has been sent the chunk, false otherwise
     */
    default boolean isChunkSent(org.bukkit.Chunk chunk) {
        return this.isChunkSent(chunk.getChunkKey());
    }

    /**
     * Checks if the player has been sent a specific chunk.
     *
     * @param chunkKey the chunk key to check
     * @return true if the player has been sent the chunk, false otherwise
     * @see org.bukkit.Chunk#getChunkKey()
     */
    boolean isChunkSent(long chunkKey);
    // Paper end

    @Override
    Spigot spigot();
    // Spigot end

    // Paper start - entity effect API
    /**
     * Plays an entity effect to this player for the target entity
     * <p>
     * If the effect is not applicable to this class of entity, it will not play.
     *
     * @param effect the entity effect
     * @param target the target entity
     */
    void sendEntityEffect(org.bukkit.EntityEffect effect, Entity target);
    // Paper end - entity effect API

    /**
     * Gives the player the items following full vanilla logic,
     * making the player drop the items that did not fit into
     * the inventory.
     *
     * @param items the items to give.
     * @return the result of this method, holding leftovers and spawned items.
     */
    default PlayerGiveResult give(final ItemStack ... items) {
        return this.give(List.of(items));
    }

    /**
     * Gives the player those items following full vanilla logic,
     * making the player drop the items that did not fit into
     * the inventory.
     *
     * @param items the items to give
     * @return the result of this method, holding leftovers and spawned items.
     */
    default PlayerGiveResult give(final Collection<ItemStack> items) {
        return this.give(items, true);
    }

    /**
     * Gives the player those items following full vanilla logic.
     *
     * @param items      the items to give
     * @param dropIfFull whether the player should drop items that
     *                   did not fit the inventory
     * @return the result of this method, holding leftovers and spawned items.
     */
    PlayerGiveResult give(Collection<ItemStack> items, boolean dropIfFull);

    /**
     * Get the score that shows in the death screen of the player.
     * <p>This amount is added to when the player gains experience.</p>
     *
     * @return Death screen score of player
     */
    int getDeathScreenScore();

    /**
     * Set the score that shows in the death screen of the player.
     * <p>This amount is added to when the player gains experience.</p>
     *
     * @param score New death screen score of player
     */
    void setDeathScreenScore(int score);

    /**
     * Gets the game connection for this player.
     *
     * @return the game connection
     */
    @ApiStatus.Experimental
    PlayerGameConnection getConnection();
}
