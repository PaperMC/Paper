package org.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public enum Particle {
    EXPLOSION_NORMAL,
    EXPLOSION_LARGE,
    EXPLOSION_HUGE,
    FIREWORKS_SPARK,
    WATER_BUBBLE,
    WATER_SPLASH,
    WATER_WAKE,
    SUSPENDED,
    SUSPENDED_DEPTH,
    CRIT,
    CRIT_MAGIC,
    SMOKE_NORMAL,
    SMOKE_LARGE,
    SPELL,
    SPELL_INSTANT,
    SPELL_MOB,
    SPELL_MOB_AMBIENT,
    SPELL_WITCH,
    DRIP_WATER,
    DRIP_LAVA,
    VILLAGER_ANGRY,
    VILLAGER_HAPPY,
    TOWN_AURA,
    NOTE,
    PORTAL,
    ENCHANTMENT_TABLE,
    FLAME,
    LAVA,
    CLOUD,
    REDSTONE(DustOptions.class),
    SNOWBALL,
    SNOW_SHOVEL,
    SLIME,
    HEART,
    BARRIER,
    ITEM_CRACK(ItemStack.class),
    BLOCK_CRACK(BlockData.class),
    BLOCK_DUST(BlockData.class),
    WATER_DROP,
    MOB_APPEARANCE,
    DRAGON_BREATH,
    END_ROD,
    DAMAGE_INDICATOR,
    SWEEP_ATTACK,
    FALLING_DUST(BlockData.class),
    TOTEM,
    SPIT,
    SQUID_INK,
    BUBBLE_POP,
    CURRENT_DOWN,
    BUBBLE_COLUMN_UP,
    NAUTILUS,
    DOLPHIN,
    // ----- Legacy Separator -----
    LEGACY_BLOCK_CRACK(MaterialData.class),
    LEGACY_BLOCK_DUST(MaterialData.class),
    LEGACY_FALLING_DUST(MaterialData.class);

    private final Class<?> dataType;

    Particle() {
        dataType = Void.class;
    }

    Particle(Class<?> data) {
        dataType = data;
    }

    /**
     * Returns the required data type for the particle
     * @return the required data type
     */
    public Class<?> getDataType() {
        return dataType;
    }

    /**
     * Options which can be applied to redstone dust particles - a particle
     * color and size.
     */
    public static class DustOptions {

        private final Color color;
        private final float size;

        public DustOptions(Color color, float size) {
            Preconditions.checkArgument(color != null, "color");
            this.color = color;
            this.size = size;
        }

        /**
         * The color of the particles to be displayed.
         *
         * @return particle color
         */
        public Color getColor() {
            return color;
        }

        /**
         * Relative size of the particle.
         *
         * @return relative particle size
         */
        public float getSize() {
            return size;
        }
    }
}
