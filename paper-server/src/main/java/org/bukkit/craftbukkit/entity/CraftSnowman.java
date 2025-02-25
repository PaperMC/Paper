package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.SnowGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Snowman;

public class CraftSnowman extends CraftGolem implements Snowman, com.destroystokyo.paper.entity.CraftRangedEntity<SnowGolem>, io.papermc.paper.entity.PaperShearable { // Paper

    public CraftSnowman(CraftServer server, SnowGolem entity) {
        super(server, entity);
    }

    @Override
    public SnowGolem getHandle() {
        return (SnowGolem) this.entity;
    }

    @Override
    public boolean isDerp() {
        return !this.getHandle().hasPumpkin();
    }

    @Override
    public void setDerp(boolean derpMode) {
        this.getHandle().setPumpkin(!derpMode);
    }
}
