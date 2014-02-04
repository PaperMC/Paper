package org.bukkit;

import java.util.Date;

/**
 * A single entry from the ban list. This may represent either a player ban or an
 * IP ban.<br />
 * Ban entries include the following properties:
 * <ul>
 *     <li><b>Target Name/IP Address</b> - The target name or IP address
 *     <li><b>Creation Date</b> - The creation date of the ban
 *     <li><b>Source</b> - The source of the ban, such as a player, console, plugin, etc
 *     <li><b>Expiration Date</b> - The expiration date of the ban
 *     <li><b>Reason</b> - The reason for the ban
 * </ul>
 * Unsaved information is not automatically written to the implementation's ban list, instead,
 * the {@link #save()} method must be called to write the changes to the ban list. If this ban
 * entry has expired (such as from an unban) and is no longer found in the list, the {@link #save()}
 * call will re-add it to the list, therefore banning the victim specified.
 */
public interface BanEntry {
    /**
     * Gets the target involved. This may be in the form of an IP or a player name.
     *
     * @return The target name or IP address
     */
    public String getTarget();

    /**
     * Gets the date this ban entry was created.
     *
     * @return The creation date
     */
    public Date getCreated();

    /**
     * Sets the date this ban entry was created.<br />
     * Use {@link #save()} to save the changes.
     *
     * @param created The new created date, cannot be null
     */
    public void setCreated(Date created);

    /**
     * Gets the source of this ban.<br />
     * A source is considered any String, although this is generally a player name.
     *
     * @return The source of the ban
     */
    public String getSource();

    /**
     * Sets the source of this ban.<br />
     * A source is considered any String, although this is generally a player name.<br />
     * Use {@link #save()} to save the changes.
     *
     * @param source The new source where null values become empty strings
     */
    public void setSource(String source);

    /**
     * Gets the date this ban expires on, or null for no defined end date.
     *
     * @return The expiration date
     */
    public Date getExpiration();

    /**
     * Sets the date this ban expires on. Null values are considered "infinite" bans.<br />
     * Use {@link #save()} to save the changes.
     *
     * @param expiry The new expiration date, or null to indicate an eternity
     */
    public void setExpiration(Date expiration);

    /**
     * Gets the reason for this ban.
     *
     * @return The ban reason or null if not set
     */
    public String getReason();

    /**
     * Sets the reason for this ban. Reasons must not be null.<br />
     * Use {@link #save()} to save the changes.
     *
     * @param reason The new reason, null values assume the implementation default
     */
    public void setReason(String reason);

    /**
     * Saves the ban entry, overwriting any previous data in the ban list.<br />
     * Saving the ban entry of an unbanned player will cause the player to be banned once again.
     */
    public void save();

}
