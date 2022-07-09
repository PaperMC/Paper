package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.EnumBlockSupport;
import org.bukkit.block.BlockSupport;

public final class CraftBlockSupport {

    private CraftBlockSupport() {
    }

    public static BlockSupport toBukkit(EnumBlockSupport support) {
        return switch (support) {
            case FULL -> BlockSupport.FULL;
            case CENTER -> BlockSupport.CENTER;
            case RIGID -> BlockSupport.RIGID;
            default -> throw new IllegalArgumentException("Unsupported EnumBlockSupport type: " + support + ". This is a bug.");
        };
    }

    public static EnumBlockSupport toNMS(BlockSupport support) {
        return switch (support) {
            case FULL -> EnumBlockSupport.FULL;
            case CENTER -> EnumBlockSupport.CENTER;
            case RIGID -> EnumBlockSupport.RIGID;
            default -> throw new IllegalArgumentException("Unsupported BlockSupport type: " + support + ". This is a bug.");
        };
    }
}
