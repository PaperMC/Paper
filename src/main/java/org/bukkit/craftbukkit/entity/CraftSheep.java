package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySheep;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Sheep;

public class CraftSheep extends CraftAnimals implements Sheep {

    public CraftSheep(CraftServer server, EntitySheep entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftSheep";
    }

    public DyeColor getColor() {
        EntitySheep entity = (EntitySheep) getHandle();
        return DyeColor.getByData((byte) entity.e_());
    }

    public void setColor(DyeColor color) {
        EntitySheep entity = (EntitySheep) getHandle();
        entity.a(color.getData());
    }

}
