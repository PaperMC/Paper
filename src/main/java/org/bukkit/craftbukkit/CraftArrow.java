package org.bukkit.craftbukkit;

import org.bukkit.entity.Arrow;
import net.minecraft.server.EntityArrow;

/**
 * Represents an arrow.
 * 
 * @author sk89q
 */
public class CraftArrow extends CraftEntity implements Arrow {
    CraftArrow(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }
}
