package io.papermc.paper.plugin.provider.configuration.type;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

// Record components used for deserialization!!!!
@ConfigSerializable
public record PermissionConfiguration(
    PermissionDefault defaultPerm,
    List<Permission> permissions) {
}
