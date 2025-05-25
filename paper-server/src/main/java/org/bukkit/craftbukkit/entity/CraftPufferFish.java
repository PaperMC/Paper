package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.Pufferfish;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PufferFish;

public class CraftPufferFish extends CraftFish implements PufferFish {

    public CraftPufferFish(CraftServer server, Pufferfish entity) {
        super(server, entity);
    }

    @Override
    public Pufferfish getHandle() {
        return (Pufferfish) this.entity;
    }

    @Override
    public int getPuffState() {
        return this.getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        this.getHandle().setPuffState(state);
    }
}
