package org.bukkit.craftbukkit.entity;

import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;

public class CraftEnderDragonPart extends CraftComplexPart implements EnderDragonPart {
    public CraftEnderDragonPart(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public EnderDragon getParent() {
        return (EnderDragon)super.getParent();
    }
}
