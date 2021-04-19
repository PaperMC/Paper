package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.EntityShulker;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Shulker;

public class CraftShulker extends CraftGolem implements Shulker {

    public CraftShulker(CraftServer server, EntityShulker entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftShulker";
    }

    @Override
    public EntityType getType() {
        return EntityType.SHULKER;
    }

    @Override
    public EntityShulker getHandle() {
        return (EntityShulker) entity;
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.getByWoolData(getHandle().getDataWatcher().get(EntityShulker.COLOR));
    }

    @Override
    public void setColor(DyeColor color) {
        getHandle().getDataWatcher().set(EntityShulker.COLOR, (color == null) ? 16 : color.getWoolData());
    }

    @Override
    public float getPeek() {
        return (float) getHandle().eN() / 100; // PAIL rename getPeek
    }

    @Override
    public void setPeek(float value) {
        Preconditions.checkArgument(value >= 0 && value <= 1, "value needs to be in between or equal to 0 and 1");
        getHandle().a((int) (value * 100)); // PAIL rename setPeek
    }
}
