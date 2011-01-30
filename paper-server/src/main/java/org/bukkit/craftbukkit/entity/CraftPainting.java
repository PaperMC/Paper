package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityPainting;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftEntity implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftPainting";
    }

}
