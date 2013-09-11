package org.bukkit;

/**
 * The Travel Agent handles the creation and the research of Nether and End
 * portals when Entities try to use one.
 * <p>
 * It is used in {@link org.bukkit.event.entity.EntityPortalEvent} and in
 * {@link org.bukkit.event.player.PlayerPortalEvent} to help developers
 * reproduce and/or modify Vanilla behaviour.
 */
public interface TravelAgent {

    /**
     * Set the Block radius to search in for available portals.
     *
     * @param radius the radius in which to search for a portal from the
     *     location
     * @return this travel agent
     */
    public TravelAgent setSearchRadius(int radius);

    /**
     * Gets the search radius value for finding an available portal.
     *
     * @return the currently set search radius
     */
    public int getSearchRadius();

    /**
     * Sets the maximum radius from the given location to create a portal.
     *
     * @param radius the radius in which to create a portal from the location
     * @return this travel agent
     */
    public TravelAgent setCreationRadius(int radius);

    /**
     * Gets the maximum radius from the given location to create a portal.
     *
     * @return the currently set creation radius
     */
    public int getCreationRadius();

    /**
     * Returns whether the TravelAgent will attempt to create a destination
     * portal or not.
     *
     * @return whether the TravelAgent should create a destination portal or
     *     not
     */
    public boolean getCanCreatePortal();

    /**
     * Sets whether the TravelAgent should attempt to create a destination
     * portal or not.
     *
     * @param create Sets whether the TravelAgent should create a destination
     *     portal or not
     */
    public void setCanCreatePortal(boolean create);

    /**
     * Attempt to find a portal near the given location, if a portal is not
     * found it will attempt to create one.
     *
     * @param location the location where the search for a portal should begin
     * @return the location of a portal which has been found or returns the
     *     location passed to the method if unsuccessful
     * @see #createPortal(Location)
     */
    public Location findOrCreate(Location location);

    /**
     * Attempt to find a portal near the given location.
     *
     * @param location the desired location of the portal
     * @return the location of the nearest portal to the location
     */
    public Location findPortal(Location location);

    /**
     * Attempt to create a portal near the given location.
     * <p>
     * In the case of a Nether portal teleportation, this will attempt to
     * create a Nether portal.
     * <p>
     * In the case of an Ender portal teleportation, this will (re-)create the
     * obsidian platform and clean blocks above it.
     *
     * @param location the desired location of the portal
     * @return true if a portal was successfully created
     */
    public boolean createPortal(Location location);
}
