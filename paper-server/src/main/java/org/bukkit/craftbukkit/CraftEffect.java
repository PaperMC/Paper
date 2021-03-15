package org.bukkit.craftbukkit;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        switch (effect) {
        case VILLAGER_PLANT_GROW:
            datavalue = (Integer) data;
            break;
        case POTION_BREAK:
            datavalue = ((Potion) data).toDamageValue() & 0x3F;
            break;
        case INSTANT_POTION_BREAK:
            datavalue = ((Color) data).asRGB();
            break;
        case RECORD_PLAY:
            Validate.isTrue(data == Material.AIR || ((Material) data).isRecord(), "Invalid record type!");
            datavalue = Item.getId(CraftMagicNumbers.getItem((Material) data));
            break;
        case SMOKE:
            switch ((BlockFace) data) {
            case DOWN:
            // SPIGOT-6318: Fallback value for the old directions
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case SELF:
                datavalue = 0;
                break;
            case UP:
                datavalue = 1;
                break;
            case NORTH:
                datavalue = 2;
                break;
            case SOUTH:
                datavalue = 3;
                break;
            case WEST:
                datavalue = 4;
                break;
            case EAST:
                datavalue = 5;
                break;
            default:
                throw new IllegalArgumentException("Bad smoke direction!");
            }
            break;
        case STEP_SOUND:
            Validate.isTrue(((Material) data).isBlock(), "Material is not a block!");
            datavalue = Block.getCombinedId(CraftMagicNumbers.getBlock((Material) data).getBlockData());
            break;
        default:
            datavalue = 0;
        }
        return datavalue;
    }
}
