package org.bukkit.craftbukkit.event.data;

import org.bukkit.Location;

public record PortalEventResult(Location to, int searchRadius, int createRadius, boolean canCreatePortal) {
}
