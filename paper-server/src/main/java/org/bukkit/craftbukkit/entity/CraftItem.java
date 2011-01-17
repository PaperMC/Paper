package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityItem;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Item;

public class CraftItem extends CraftEntity implements Item {

    public CraftItem(CraftServer server, EntityItem entity) {
        super(server, entity);
    }

}
