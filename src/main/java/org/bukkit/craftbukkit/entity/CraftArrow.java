package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.Arrow;
import net.minecraft.server.EntityArrow;
import org.bukkit.craftbukkit.CraftServer;

/**
 * Represents an arrow.
 * 
 * @author sk89q
 */
public class CraftArrow extends CraftEntity implements Arrow {
    public CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }
}
