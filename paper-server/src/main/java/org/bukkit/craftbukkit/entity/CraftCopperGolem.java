package org.bukkit.craftbukkit.entity;

import io.papermc.paper.entity.PaperShearable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CopperGolem;

public class CraftCopperGolem extends CraftGolem implements CopperGolem, PaperShearable {
    public CraftCopperGolem(final CraftServer server, final net.minecraft.world.entity.animal.coppergolem.CopperGolem entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.coppergolem.CopperGolem getHandle() {
        return (net.minecraft.world.entity.animal.coppergolem.CopperGolem) this.entity;
    }
}
