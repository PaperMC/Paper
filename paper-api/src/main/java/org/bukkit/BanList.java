package org.bukkit;

import java.util.Date;
import java.util.Set;

/**
 * A ban list, containing bans of type {@link org.bukkit.BanList.Type}
 */
public interface BanList {
    /**
     * Gets a {@link BanEntry} by target.
     *
     * @param target Entry parameter to search for
     * @return BanEntry for the submitted query, or null if none found
     */
    public BanEntry getBanEntry(String target);

    /**
     * Adds a ban to the ban list. If a previous ban exists, this will overwrite the previous
     * entry.
     *
     * @param target The target of the ban
     * @param reason Reason for the ban. If null, the implementation default is assumed
     * @param expires Expiration Date of the ban. If null, "infinity" is assumed
     * @param source Source of the ban. If null, the implementation default is assumed
     * @return The BanEntry of the added ban
     */
    public BanEntry addBan(String target, String reason, Date expires, String source);

    /**
     * Gets a set containing every {@link BanEntry} in the BanList.
     *
     * @return an immutable set containing every BanEntry tracked by the BanList
     */
    public Set<BanEntry> getBanEntries();

    /**
     * Gets if a {@link BanEntry} exists for the target, indicating ban status
     *
     * @param target Entry target to lookup
     * @return true if a {@link BanEntry} exists for the name, indicating ban status
     */
    public boolean isBanned(String target);

    /**
     * Removes the specified target from the list, therefore indicating a "not banned" status.
     *
     * @param target The target to remove from the list
     */
    public void pardon(String target);

    /**
     * Represents the various types a {@link BanList} may track.
     */
    public enum Type {
        /**
         * Banned player names
         */
        NAME,
        /**
         * Banned player IP addresses
         */
        IP;
    }

}
