package org.bukkit.inventory.meta;

import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a skull that can have an owner.
 */
public interface SkullMeta extends ItemMeta {

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     * @deprecated see {@link #getOwningPlayer()}.
     */
    @Deprecated(since = "1.12.1")
    @Nullable
    String getOwner();

    /**
     * Checks to see if the skull has an owner.
     *
     * @return true if the skull has an owner
     */
    boolean hasOwner();

    /**
     * Sets the owner of the skull.
     *
     * @param owner the new owner of the skull
     * @return true if the owner was successfully set
     * @deprecated see {@link #setOwningPlayer(org.bukkit.OfflinePlayer)}.
     */
    @Deprecated(since = "1.12.1")
    boolean setOwner(@Nullable String owner);

    // Paper start
    /**
     * Sets this skull to use the supplied Player Profile, which can include textures already prefilled.
     * @param profile The profile to set this Skull to use, or null to clear owner
     */
    void setPlayerProfile(@Nullable com.destroystokyo.paper.profile.PlayerProfile profile);

    /**
     * If the skull has an owner, per {@link #hasOwner()}, return the owners {@link com.destroystokyo.paper.profile.PlayerProfile}
     * @return The profile of the owner, if set
     */
    @Nullable com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile();
    // Paper end

    /**
     * Gets the owner of the skull.
     *
     * @return the owner if the skull
     */
    @Nullable
    OfflinePlayer getOwningPlayer();

    /**
     * Sets the owner of the skull.
     * <p>
     * Plugins should check that hasOwner() returns true before calling this
     * plugin.
     *
     * @param owner the new owner of the skull
     * @return true if the owner was successfully set
     */
    boolean setOwningPlayer(@Nullable OfflinePlayer owner);

    /**
     * Gets the profile of the player who owns the skull. This player profile
     * may appear as the texture depending on skull type.
     *
     * @return the profile of the owning player
     */
    @Nullable
    @Deprecated // Paper
    PlayerProfile getOwnerProfile();

    /**
     * Sets the profile of the player who owns the skull. This player profile
     * may appear as the texture depending on skull type.
     * <p>
     * The profile must contain both a unique id and a skin texture. If either
     * of these is missing, the profile must contain a name by which the server
     * will then attempt to look up the unique id and skin texture.
     *
     * @param profile the profile of the owning player
     * @throws IllegalArgumentException if the profile does not contain the
     * necessary information
     */
    @Deprecated // Paper
    void setOwnerProfile(@Nullable PlayerProfile profile);

    /**
     * Sets the sound to play if the skull is placed on a note block.
     * <br>
     * <strong>Note:</strong> This only works for player heads. For other heads,
     * see {@link org.bukkit.Instrument}.
     *
     * @param noteBlockSound the key of the sound to be played, or null
     */
    void setNoteBlockSound(@Nullable NamespacedKey noteBlockSound);

    /**
     * Gets the sound to play if the skull is placed on a note block.
     * <br>
     * <strong>Note:</strong> This only works for player heads. For other heads,
     * see {@link org.bukkit.Instrument}.
     *
     * @return the key of the sound, or null
     */
    @Nullable
    NamespacedKey getNoteBlockSound();

    @Override
    @NotNull
    SkullMeta clone();
}
