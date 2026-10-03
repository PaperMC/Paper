package io.papermc.paper.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public final class PaperPermissions {

    public static final String PACKET_LIMITER_BYPASS = "paper.packet-limiter.bypass";

    private PaperPermissions() {
    }

    public static void registerPermissions() {
        DefaultPermissions.registerPermission(new Permission(
            PACKET_LIMITER_BYPASS,
            "Allows a trusted player to bypass Paper's packet limiter",
            PermissionDefault.OP
        ), false);
    }
}
