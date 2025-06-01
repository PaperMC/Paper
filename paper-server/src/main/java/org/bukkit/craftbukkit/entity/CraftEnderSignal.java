package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.item.Items;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.EnderSignal;
import org.bukkit.inventory.ItemStack;

public class CraftEnderSignal extends CraftEntity implements EnderSignal {
    public CraftEnderSignal(CraftServer server, EyeOfEnder entity) {
        super(server, entity);
    }

    @Override
    public EyeOfEnder getHandle() {
        return (EyeOfEnder) this.entity;
    }

    @Override
    public Location getTargetLocation() {
        return CraftLocation.toBukkit(this.getHandle().target, this.getWorld(), this.getHandle().getYRot(), this.getHandle().getXRot());
    }

    @Override
    public void setTargetLocation(Location location) {
        // Paper start - Change EnderEye target without changing other things
        this.setTargetLocation(location, true);
    }

    @Override
    public void setTargetLocation(Location location, boolean update) {
        // Paper end - Change EnderEye target without changing other things
        Preconditions.checkArgument(this.getWorld().equals(location.getWorld()), "Cannot target EnderSignal across worlds");
        this.getHandle().signalTo(CraftLocation.toVec3(location), update); // Paper - Change EnderEye target without changing other things
    }

    @Override
    public boolean getDropItem() {
        return this.getHandle().surviveAfterDeath;
    }

    @Override
    public void setDropItem(boolean shouldDropItem) {
        this.getHandle().surviveAfterDeath = shouldDropItem;
    }

    @Override
    public ItemStack getItem() {
        return CraftItemStack.asBukkitCopy(this.getHandle().getItem());
    }

    @Override
    public void setItem(ItemStack item) {
        this.getHandle().setItem(item != null ? CraftItemStack.asNMSCopy(item) : Items.ENDER_EYE.getDefaultInstance());
    }

    @Override
    public int getDespawnTimer() {
        return this.getHandle().life;
    }

    @Override
    public void setDespawnTimer(int time) {
        this.getHandle().life = time;
    }
}
