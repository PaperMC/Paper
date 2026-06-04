package org.bukkit.craftbukkit.entity;

import io.papermc.paper.entity.PaperBucketable;
import io.papermc.paper.entity.PaperShearable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SulfurCube;

public class CraftSulfurCube extends CraftAbstractCubeMob implements SulfurCube, PaperShearable, PaperBucketable {
    public CraftSulfurCube(final CraftServer server, final net.minecraft.world.entity.monster.cubemob.SulfurCube entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.SulfurCube getHandle() {
        return (net.minecraft.world.entity.monster.cubemob.SulfurCube) this.entity;
    }
}
