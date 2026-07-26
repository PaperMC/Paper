package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Illusioner;

public class CraftIllusioner extends CraftSpellcaster implements Illusioner, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.illager.Illusioner> { // Paper

    public CraftIllusioner(CraftServer server, net.minecraft.world.entity.monster.illager.Illusioner entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.illager.Illusioner getHandle() {
        return (net.minecraft.world.entity.monster.illager.Illusioner) this.entity;
    }
}
