package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PiglinBrute;

public class CraftPiglinBrute extends CraftPiglinAbstract implements PiglinBrute {

    public CraftPiglinBrute(CraftServer server, EntityPiglinBrute entity) {
        super(server, entity);
    }

    @Override
    public EntityPiglinBrute getHandle() {
        return (EntityPiglinBrute) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPiglinBrute";
    }
}
