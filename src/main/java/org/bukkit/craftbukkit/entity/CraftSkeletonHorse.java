package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityHorseSkeleton;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.SkeletonHorse;

public class CraftSkeletonHorse extends CraftAbstractHorse implements SkeletonHorse {

    public CraftSkeletonHorse(CraftServer server, EntityHorseSkeleton entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftSkeletonHorse";
    }

    @Override
    public EntityType getType() {
        return EntityType.SKELETON_HORSE;
    }

    @Override
    public Variant getVariant() {
        return Variant.SKELETON_HORSE;
    }

    // Paper start
    @Override
    public EntityHorseSkeleton getHandle() {
        return (EntityHorseSkeleton) super.getHandle();
    }

    @Override
    public int getTrapTime() {
        return getHandle().getTrapTime();
    }

    @Override
    public boolean isTrap() {
        return getHandle().isTrap();
    }

    @Override
    public void setTrap(boolean trap) {
        getHandle().setTrap(trap);
    }
    // Paper end
}
