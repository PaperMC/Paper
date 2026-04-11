package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.SupportType;
import org.bukkit.block.BlockSupport;

public final class CraftBlockSupport {

    private CraftBlockSupport() {
    }

    public static BlockSupport toBukkit(SupportType support) {
        return switch (support) {
            case FULL -> BlockSupport.FULL;
            case CENTER -> BlockSupport.CENTER;
            case RIGID -> BlockSupport.RIGID;
        };
    }

    public static SupportType toMinecraft(BlockSupport support) {
        return switch (support) {
            case FULL -> SupportType.FULL;
            case CENTER -> SupportType.CENTER;
            case RIGID -> SupportType.RIGID;
        };
    }
}
