package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Witch;

public class CraftWitch extends CraftRaider implements Witch, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.Witch> { // Paper
    public CraftWitch(CraftServer server, net.minecraft.world.entity.monster.Witch entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Witch getHandle() {
        return (net.minecraft.world.entity.monster.Witch) this.entity;
    }

    @Override
    public String toString() {
        return "CraftWitch";
    }

    @Override
    public boolean isDrinkingPotion() {
        return this.getHandle().isDrinkingPotion();
    }
}
