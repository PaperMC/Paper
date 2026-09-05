package org.bukkit.craftbukkit.entity;

import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Cushion;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftCushion extends CraftBlockAttachedEntity implements Cushion {

    public CraftCushion(CraftServer server, net.minecraft.world.entity.decoration.Cushion entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.decoration.Cushion getHandle() {
        return (net.minecraft.world.entity.decoration.Cushion) this.entity;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData((byte) this.getHandle().getColor().getId());
    }

    @Override
    public void setColor(DyeColor color) {
        this.getHandle().setColor(net.minecraft.world.item.DyeColor.byId(color.getWoolData()));
    }
}
