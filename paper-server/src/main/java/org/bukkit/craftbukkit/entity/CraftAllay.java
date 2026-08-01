package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.allay.Allay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.inventory.Inventory;

public class CraftAllay extends CraftCreature implements org.bukkit.entity.Allay {

    public CraftAllay(CraftServer server, Allay entity) {
        super(server, entity);
    }

    @Override
    public Allay getHandle() {
        return (Allay) this.entity;
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().getInventory());
    }

    @Override
    public boolean canDuplicate() {
        return this.getHandle().canDuplicate();
    }

    @Override
    public void setCanDuplicate(boolean canDuplicate) {
        this.getHandle().setCanDuplicate(canDuplicate);
    }

    @Override
    public long getDuplicationCooldown() {
        return this.getHandle().duplicationCooldown;
    }

    @Override
    public void setDuplicationCooldown(long l) {
        this.getHandle().duplicationCooldown = l;
    }

    @Override
    public void resetDuplicationCooldown() {
        this.getHandle().resetDuplicationCooldown();
    }

    @Override
    public boolean isDancing() {
        return this.getHandle().isDancing();
    }

    @Override
    public void startDancing(Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        Preconditions.checkArgument(location.getBlock().getType() == Material.JUKEBOX, "The Block at the location needs to be a jukebox");
        this.getHandle().setJukeboxPlaying(CraftLocation.toBlockPos(location), true);
    }

    @Override
    public void startDancing() {
        this.getHandle().forceDancing = true;
        this.getHandle().setDancing(true);
    }

    @Override
    public void stopDancing() {
        this.getHandle().forceDancing = false;
        this.getHandle().jukeboxPos = null;
        this.getHandle().setDancing(false);
    }

    @Override
    public org.bukkit.entity.Allay duplicateAllay() {
        Allay allay = this.getHandle().duplicateAllay();
        return (allay != null) ? (org.bukkit.entity.Allay) allay.getBukkitEntity() : null;
    }

    public Location getJukebox() {
        BlockPos jukeboxPos = this.getHandle().jukeboxPos;
        return (jukeboxPos != null) ? CraftLocation.toBukkit(jukeboxPos, this.getWorld()) : null;
    }
}
