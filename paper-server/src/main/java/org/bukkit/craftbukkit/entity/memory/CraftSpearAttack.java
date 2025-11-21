package org.bukkit.craftbukkit.entity.memory;

import net.minecraft.world.entity.ai.behavior.SpearAttack;

public class CraftSpearAttack {
    public static SpearAttack.SpearStatus statusFromBukkit(org.bukkit.entity.memory.SpearAttack.SpearStatus bukkit) {
        return switch (bukkit) {
            case RETREAT -> SpearAttack.SpearStatus.RETREAT;
            case APPROACH -> SpearAttack.SpearStatus.APPROACH;
            case CHARGING -> SpearAttack.SpearStatus.CHARGING;
        };
    }

    public static org.bukkit.entity.memory.SpearAttack.SpearStatus statusFromNms(SpearAttack.SpearStatus nms) {
        return switch (nms) {
            case RETREAT -> org.bukkit.entity.memory.SpearAttack.SpearStatus.RETREAT;
            case APPROACH -> org.bukkit.entity.memory.SpearAttack.SpearStatus.APPROACH;
            case CHARGING -> org.bukkit.entity.memory.SpearAttack.SpearStatus.CHARGING;
        };
    }
}
