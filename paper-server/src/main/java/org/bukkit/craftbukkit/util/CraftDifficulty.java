package org.bukkit.craftbukkit.util;

import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftDifficulty {
    public static org.bukkit.Difficulty toBukkit(net.minecraft.world.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> org.bukkit.Difficulty.EASY;
            case HARD -> org.bukkit.Difficulty.HARD;
            case NORMAL -> org.bukkit.Difficulty.NORMAL;
            case PEACEFUL -> org.bukkit.Difficulty.PEACEFUL;
        };
    }

    public static net.minecraft.world.Difficulty toMinecraft(org.bukkit.Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> net.minecraft.world.Difficulty.EASY;
            case HARD -> net.minecraft.world.Difficulty.HARD;
            case NORMAL -> net.minecraft.world.Difficulty.NORMAL;
            case PEACEFUL -> net.minecraft.world.Difficulty.PEACEFUL;
        };
    }
}
