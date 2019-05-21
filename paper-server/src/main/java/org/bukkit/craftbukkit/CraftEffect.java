package org.bukkit.craftbukkit;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        switch(effect) {
        case VILLAGER_PLANT_GROW:
            datavalue = (Integer) data;
            break;
        case POTION_BREAK:
            datavalue = ((Potion) data).toDamageValue() & 0x3F;
            break;
        case RECORD_PLAY:
            Validate.isTrue(data == Material.AIR || ((Material) data).isRecord(), "Invalid record type!");
            datavalue = Item.getId(CraftMagicNumbers.getItem((Material) data));
            break;
        case SMOKE:
            switch((BlockFace) data) { // TODO: Verify (Where did these values come from...?)
            case SOUTH_EAST:
                datavalue = 0;
                break;
            case SOUTH:
                datavalue = 1;
                break;
            case SOUTH_WEST:
                datavalue = 2;
                break;
            case EAST:
                datavalue = 3;
                break;
            case UP:
            case SELF:
                datavalue = 4;
                break;
            case WEST:
                datavalue = 5;
                break;
            case NORTH_EAST:
                datavalue = 6;
                break;
            case NORTH:
                datavalue = 7;
                break;
            case NORTH_WEST:
                datavalue = 8;
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
