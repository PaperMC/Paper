package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.EntityPufferFish;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PufferFish;

public class CraftPufferFish extends CraftFish implements PufferFish {

    public CraftPufferFish(CraftServer server, EntityPufferFish entity) {
        super(server, entity);
    }

    @Override
    public EntityPufferFish getHandle() {
        return (EntityPufferFish) super.getHandle();
    }

    @Override
    public int getPuffState() {
        return getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        getHandle().setPuffState(state);
    }

    @Override
    public String toString() {
        return "CraftPufferFish";
    }
}
