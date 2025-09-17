package org.bukkit.craftbukkit.entity;

import io.papermc.paper.entity.PaperShearable;
import net.minecraft.world.entity.animal.AbstractGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.CopperGolem;

public class CraftCopperGolem extends CraftGolem implements CopperGolem, PaperShearable {
    public CraftCopperGolem(final CraftServer server, final AbstractGolem entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.coppergolem.CopperGolem getHandle() {
        return (net.minecraft.world.entity.animal.coppergolem.CopperGolem) super.getHandle();
    }
}
