package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityEnderCrystal;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;

public class CraftEnderCrystal extends CraftEntity implements EnderCrystal {
    public CraftEnderCrystal(CraftServer server, EntityEnderCrystal entity) {
        super(server, entity);
    }

    @Override
    public boolean isShowingBottom() {
        return getHandle().k(); // PAIL: Rename isShowingBottom
    }

    @Override
    public void setShowingBottom(boolean showing) {
        getHandle().a(showing); // PAIL: Rename setShowingBottom
    }

    @Override
    public EntityEnderCrystal getHandle() {
        return (EntityEnderCrystal) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderCrystal";
    }

    public EntityType getType() {
        return EntityType.ENDER_CRYSTAL;
    }
}
