package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.animal.AbstractFish;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Fish;

public class CraftFish extends CraftWaterMob implements Fish, io.papermc.paper.entity.PaperBucketable { // Paper - Bucketable API

    public CraftFish(CraftServer server, AbstractFish entity) {
        super(server, entity);
    }

    @Override
    public AbstractFish getHandle() {
        return (AbstractFish) this.entity;
    }
}
