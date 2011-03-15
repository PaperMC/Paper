package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

/**
 * Represents the different types of coals.
 * @author sunkid
 */
public class Coal extends MaterialData {
       public Coal(final int type) {
            super(type);
        }

        public Coal(final Material type) {
            super(type);
        }

        public Coal(final int type, final byte data) {
            super(type, data);
        }

        public Coal(final Material type, final byte data) {
            super(type, data);
        }

        /**
         * Gets the current type of this coal
         *
         * @return CoalType of this coal
         */
        public CoalType getType() {
            return CoalType.getByData(getData());
        }

        /**
         * Sets the type of this coal
         *
         * @param type New type of this coal
         */
        public void setSpecies(CoalType type) {
            setData(type.getData());
        }
}
