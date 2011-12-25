package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.entity.Player;

/**
 * Called when a player completes the portaling process by standing in a portal
 */
@SuppressWarnings("serial")
public class PlayerPortalEvent extends PlayerTeleportEvent {

    protected boolean useTravelAgent = true;

    protected Player player;
    protected TravelAgent travelAgent;

    public PlayerPortalEvent(Player player, Location from, Location to, TravelAgent pta) {
        super(Type.PLAYER_PORTAL, player, from, to);
        this.travelAgent = pta;
    }

    public void useTravelAgent(boolean useTravelAgent) {
        this.useTravelAgent = useTravelAgent;
    }

    public boolean useTravelAgent() {
        return useTravelAgent;
    }

    public TravelAgent getPortalTravelAgent() {
        return this.travelAgent;
    }

    public void setPortalTravelAgent(TravelAgent travelAgent) {
        this.travelAgent = travelAgent;
    }
}