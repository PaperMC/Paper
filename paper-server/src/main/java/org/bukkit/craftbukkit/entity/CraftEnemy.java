package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.IMonster;
import org.bukkit.entity.Enemy;

public interface CraftEnemy extends Enemy {

    IMonster getHandle();
}
