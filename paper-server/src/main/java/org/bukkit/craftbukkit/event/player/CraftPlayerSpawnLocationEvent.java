package org.bukkit.craftbukkit.event.player;

import com.google.common.base.Preconditions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class CraftPlayerSpawnLocationEvent extends CraftPlayerEvent implements PlayerSpawnLocationEvent {

    private Location spawnLocation;

    public CraftPlayerSpawnLocationEvent(final Player player, final Location spawnLocation) {
        super(player);
        this.spawnLocation = spawnLocation;
    }

    public CraftPlayerSpawnLocationEvent(final ServerPlayer player, final Level level, final Vec3 pos, final Vec2 angle) {
        this(player.getBukkitEntity(), CraftLocation.toBukkit(pos, level, angle.x, angle.y));
    }

    @Override
    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    @Override
    public void setSpawnLocation(final Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "World cannot be null");
        this.spawnLocation = location.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerSpawnLocationEvent.getHandlerList();
    }
}
