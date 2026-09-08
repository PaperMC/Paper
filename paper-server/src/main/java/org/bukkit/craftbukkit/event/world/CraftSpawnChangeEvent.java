package org.bukkit.craftbukkit.event.world;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.SpawnChangeEvent;

public class CraftSpawnChangeEvent extends CraftWorldEvent implements SpawnChangeEvent {

    private final Location previousLocation;

    public CraftSpawnChangeEvent(final World world, final Location previousLocation) {
        super(world);
        this.previousLocation = previousLocation;
    }

    public CraftSpawnChangeEvent(final Level level, final LevelData.RespawnData respawnData) {
        this(level.getWorld(), CraftLocation.toBukkit(respawnData.pos(), level.getWorld(), respawnData.yaw(), respawnData.pitch()));
    }

    @Override
    public Location getPreviousLocation() {
        return this.previousLocation.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return SpawnChangeEvent.getHandlerList();
    }
}
