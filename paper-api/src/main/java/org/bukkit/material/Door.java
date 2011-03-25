package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents the different types of coals.
 * @author sunkid
 */
public class Door extends MaterialData {
       public Door(final int type) {
            super(type);
        }

        public Door(final Material type) {
            super(type);
        }

        public Door(final int type, final byte data) {
            super(type, data);
        }

        public Door(final Material type, final byte data) {
            super(type, data);
        }

        /**
         * Check to see if the door is open.
         * @return true if the door has swung counterclockwise around its hinge.
         */
        public boolean isOpen() {
            return ((getData() & 0x4) == 0x4);
        }
        
        /**
         * @return whether this is the top half of the door
         */
        public boolean isTopHalf() {
            return ((getData() & 0x8) == 0x8);
        }
        
        /**
         * @return the location of the hinges
         */
        public BlockFace getHingeCorner() {
            byte d = getData();
            if ((d & 0x3) == 0x3) {
                return BlockFace.NORTH_WEST;
            } else if ((d & 0x1) == 0x1) {
                return BlockFace.SOUTH_EAST;
            } else if ((d & 0x2) == 0x2) {
                return BlockFace.SOUTH_WEST;
            }
            
            return BlockFace.NORTH_EAST;
        }
}
