package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFox;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fox;

public class CraftFox extends CraftAnimals implements Fox {

    public CraftFox(CraftServer server, EntityFox entity) {
        super(server, entity);
    }

    @Override
    public EntityFox getHandle() {
        return (EntityFox) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.FOX;
    }

    @Override
    public String toString() {
        return "CraftFox";
    }
}
