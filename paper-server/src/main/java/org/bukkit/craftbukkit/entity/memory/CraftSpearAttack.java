package org.bukkit.craftbukkit.entity.memory;

import net.minecraft.world.entity.ai.behavior.SpearAttack;

public class CraftSpearAttack {
    public static SpearAttack.SpearStatus toNms(org.bukkit.entity.memory.SpearAttack.SpearStatus bukkit) {
        return SpearAttack.SpearStatus.values()[bukkit.ordinal()];
    }

    public static org.bukkit.entity.memory.SpearAttack.SpearStatus fromNms(SpearAttack.SpearStatus nms) {
        return org.bukkit.entity.memory.SpearAttack.SpearStatus.values()[nms.ordinal()];
    }
}
