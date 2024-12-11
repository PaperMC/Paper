package io.papermc.paper.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Leashable;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;

public interface PaperLeashable extends io.papermc.paper.entity.Leashable {

    Leashable getHandle();

    @Override
    default boolean isLeashed() {
        return this.getHandle().getLeashHolder() != null;
    }

    @Override
    default Entity getLeashHolder() throws IllegalStateException {
        Preconditions.checkState(this.isLeashed(), "Entity not leashed");
        return this.getHandle().getLeashHolder().getBukkitEntity();
    }

    private boolean unleash() {
        if (!this.isLeashed()) {
            return false;
        }

        this.getHandle().removeLeash();
        return true;
    }

    @Override
    default boolean setLeashHolder(Entity holder) {
        if (this.getHandle() instanceof net.minecraft.world.entity.Entity entity && entity.generation) {
            return false;
        }

        if (holder == null) {
            return this.unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        this.unleash();
        this.getHandle().setLeashedTo(((CraftEntity) holder).getHandle(), true);
        return true;
    }
}
