package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityItemFrame;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;

public class CraftItemFrame extends CraftHanging implements ItemFrame {
    public CraftItemFrame(CraftServer server, EntityItemFrame entity) {
        super(server, entity);
    }

    @Override
    public EntityItemFrame getHandle() {
        return (EntityItemFrame) entity;
    }

    @Override
    public String toString() {
        return "CraftItemFrame";
    }

    public EntityType getType() {
        return EntityType.ITEM_FRAME;
    }
}
