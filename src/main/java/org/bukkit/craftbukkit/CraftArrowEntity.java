package org.bukkit.craftbukkit;

import org.bukkit.ArrowEntity;
import net.minecraft.server.EntityArrow;

/**
 * Represents an arrow.
 * 
 * @author sk89q
 */
public class CraftArrowEntity extends CraftEntity implements ArrowEntity {
    CraftArrowEntity(CraftServer server, EntityArrow entity) {
        super(server, entity);
    }
}
