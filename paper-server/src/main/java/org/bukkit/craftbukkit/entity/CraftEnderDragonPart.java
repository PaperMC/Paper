package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityComplexPart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;

public class CraftEnderDragonPart extends CraftComplexPart implements EnderDragonPart {
    public CraftEnderDragonPart(CraftServer server, EntityComplexPart entity) {
        super(server, entity);
    }

    @Override
    public EnderDragon getParent() {
        return (EnderDragon) super.getParent();
    }

    @Override
    public EntityComplexPart getHandle() {
        return (EntityComplexPart) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragonPart";
    }
}
