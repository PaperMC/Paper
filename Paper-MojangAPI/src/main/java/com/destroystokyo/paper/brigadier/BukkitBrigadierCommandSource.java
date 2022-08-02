package com.destroystokyo.paper.brigadier;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated For removal, use the new brigadier api.
 */
@Deprecated(forRemoval = true) // Paper
public interface BukkitBrigadierCommandSource {

    @Nullable
    Entity getBukkitEntity();

    @Nullable
    World getBukkitWorld();

    @Nullable
    Location getBukkitLocation();

    CommandSender getBukkitSender();
}
