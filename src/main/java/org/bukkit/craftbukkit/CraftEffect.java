package org.bukkit.craftbukkit;

import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;

public class CraftEffect {
    public static <T> int getDataValue(Effect effect, T data) {
        int datavalue;
        switch(effect) {
        case POTION_BREAK:
            datavalue = ((Potion) data).toDamageValue() & 0x3F;
            break;
        case RECORD_PLAY:
            Validate.isTrue(((Material) data).isRecord(), "Invalid record type!");
            datavalue = ((Material) data).getId();
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
            datavalue = ((Material) data).getId();
            break;
        default:
            datavalue = 0;
        }
        return datavalue;
    }
}
