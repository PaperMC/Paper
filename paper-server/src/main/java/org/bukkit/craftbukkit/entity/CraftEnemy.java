package org.bukkit.craftbukkit.entity;

import org.bukkit.entity.Enemy;

public interface CraftEnemy extends Enemy {

    net.minecraft.world.entity.monster.Enemy getHandle();
}
