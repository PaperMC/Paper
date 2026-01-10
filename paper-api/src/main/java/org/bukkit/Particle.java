package org.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum Particle implements Keyed {
    POOF("poof"),
    EXPLOSION("explosion"),
    EXPLOSION_EMITTER("explosion_emitter"),
    FIREWORK("firework"),
    BUBBLE("bubble"),
    SPLASH("splash"),
    FISHING("fishing"),
    UNDERWATER("underwater"),
    CRIT("crit"),
    ENCHANTED_HIT("enchanted_hit"),
    SMOKE("smoke"),
    LARGE_SMOKE("large_smoke"),
    /**
     * Uses {@link Spell} as DataType
     */
    EFFECT("effect", Spell.class),
    /**
     * Uses {@link Spell} as DataType
     */
    INSTANT_EFFECT("instant_effect", Spell.class),
    /**
     * Uses {@link Color} as DataType (with alpha support)
     */
    ENTITY_EFFECT("entity_effect", Color.class),
    WITCH("witch"),
    DRIPPING_WATER("dripping_water"),
    DRIPPING_LAVA("dripping_lava"),
    ANGRY_VILLAGER("angry_villager"),
    HAPPY_VILLAGER("happy_villager"),
    MYCELIUM("mycelium"),
    NOTE("note"),
    PORTAL("portal"),
    ENCHANT("enchant"),
    FLAME("flame"),
    LAVA("lava"),
    CLOUD("cloud"),
    /**
     * Uses {@link DustOptions} as DataType
     */
    DUST("dust", DustOptions.class),
    ITEM_SNOWBALL("item_snowball"),
    ITEM_SLIME("item_slime"),
    HEART("heart"),
    /**
     * Uses {@link ItemStack} as DataType
     */
    ITEM("item", ItemStack.class),
    /**
     * Uses {@link BlockData} as DataType
     */
    BLOCK("block", BlockData.class),
    RAIN("rain"),
    ELDER_GUARDIAN("elder_guardian"),
    /**
     * Uses {@link Float} as DataType, for the power of the breath
     */
    DRAGON_BREATH("dragon_breath", Float.class),
    END_ROD("end_rod"),
    DAMAGE_INDICATOR("damage_indicator"),
    SWEEP_ATTACK("sweep_attack"),
    /**
     * Uses {@link BlockData} as DataType
     */
    FALLING_DUST("falling_dust", BlockData.class),
    TOTEM_OF_UNDYING("totem_of_undying"),
    SPIT("spit"),
    SQUID_INK("squid_ink"),
    BUBBLE_POP("bubble_pop"),
    CURRENT_DOWN("current_down"),
    BUBBLE_COLUMN_UP("bubble_column_up"),
    NAUTILUS("nautilus"),
    DOLPHIN("dolphin"),
    SNEEZE("sneeze"),
    CAMPFIRE_COSY_SMOKE("campfire_cosy_smoke"),
    CAMPFIRE_SIGNAL_SMOKE("campfire_signal_smoke"),
    COMPOSTER("composter"),
    /**
     * Uses {@link Color} as DataType
     */
    FLASH("flash", Color.class),
    FALLING_LAVA("falling_lava"),
    LANDING_LAVA("landing_lava"),
    FALLING_WATER("falling_water"),
    DRIPPING_HONEY("dripping_honey"),
    FALLING_HONEY("falling_honey"),
    LANDING_HONEY("landing_honey"),
    FALLING_NECTAR("falling_nectar"),
    SOUL_FIRE_FLAME("soul_fire_flame"),
    ASH("ash"),
    CRIMSON_SPORE("crimson_spore"),
    WARPED_SPORE("warped_spore"),
    SOUL("soul"),
    DRIPPING_OBSIDIAN_TEAR("dripping_obsidian_tear"),
    FALLING_OBSIDIAN_TEAR("falling_obsidian_tear"),
    LANDING_OBSIDIAN_TEAR("landing_obsidian_tear"),
    REVERSE_PORTAL("reverse_portal"),
    WHITE_ASH("white_ash"),
    /**
     * Uses {@link DustTransition} as DataType
     */
    DUST_COLOR_TRANSITION("dust_color_transition", DustTransition.class),
    /**
     * Uses {@link Vibration} as DataType
     */
    VIBRATION("vibration", Vibration.class),
    FALLING_SPORE_BLOSSOM("falling_spore_blossom"),
    SPORE_BLOSSOM_AIR("spore_blossom_air"),
    SMALL_FLAME("small_flame"),
    SNOWFLAKE("snowflake"),
    DRIPPING_DRIPSTONE_LAVA("dripping_dripstone_lava"),
    FALLING_DRIPSTONE_LAVA("falling_dripstone_lava"),
    DRIPPING_DRIPSTONE_WATER("dripping_dripstone_water"),
    FALLING_DRIPSTONE_WATER("falling_dripstone_water"),
    GLOW_SQUID_INK("glow_squid_ink"),
    GLOW("glow"),
    WAX_ON("wax_on"),
    WAX_OFF("wax_off"),
    ELECTRIC_SPARK("electric_spark"),
    SCRAPE("scrape"),
    SONIC_BOOM("sonic_boom"),
    SCULK_SOUL("sculk_soul"),
    /**
     * Uses {@link Float} as DataType, the angle in radians
     */
    SCULK_CHARGE("sculk_charge", Float.class),
    SCULK_CHARGE_POP("sculk_charge_pop"),
    /**
     * Uses {@link Integer} as DataType
     */
    SHRIEK("shriek", Integer.class),
    CHERRY_LEAVES("cherry_leaves"),
    PALE_OAK_LEAVES("pale_oak_leaves"),
    /**
     * Uses {@link Color} as DataType
     */
    TINTED_LEAVES("tinted_leaves", Color.class),
    EGG_CRACK("egg_crack"),
    DUST_PLUME("dust_plume"),
    WHITE_SMOKE("white_smoke"),
    GUST("gust"),
    SMALL_GUST("small_gust"),
    GUST_EMITTER_LARGE("gust_emitter_large"),
    GUST_EMITTER_SMALL("gust_emitter_small"),
    TRIAL_SPAWNER_DETECTION("trial_spawner_detection"),
    TRIAL_SPAWNER_DETECTION_OMINOUS("trial_spawner_detection_ominous"),
    VAULT_CONNECTION("vault_connection"),
    INFESTED("infested"),
    ITEM_COBWEB("item_cobweb"),
    /**
     * Uses {@link BlockData} as DataType
     */
    DUST_PILLAR("dust_pillar", BlockData.class),
    /**
     * Uses {@link BlockData} as DataType
     */
    BLOCK_CRUMBLE("block_crumble", BlockData.class),
    FIREFLY("firefly"),
    /**
     * Uses {@link Trail} as DataType
     */
    TRAIL("trail", Trail.class),
    OMINOUS_SPAWNING("ominous_spawning"),
    RAID_OMEN("raid_omen"),
    TRIAL_OMEN("trial_omen"),
    /**
     * Uses {@link BlockData} as DataType
     */
    BLOCK_MARKER("block_marker", BlockData.class),
    COPPER_FIRE_FLAME("copper_fire_flame"),
    ;

    private final NamespacedKey key;
    private final Class<?> dataType;
    // Paper - all particles are registered

    Particle(String key) {
        this(key, Void.class);
    }

    // Paper - all particles are registered

    Particle(String key, /*@NotNull*/ Class<?> data) {
        // Paper - all particles are registered
        if (key != null) {
            this.key = NamespacedKey.minecraft(key);
        } else {
            this.key = null;
        }
        dataType = data;
        // Paper - all particles are registered
    }

    /**
     * Returns the required data type for the particle
     * @return the required data type
     */
    @NotNull
    public Class<?> getDataType() {
        return dataType;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        if (key == null) {
            throw new UnsupportedOperationException("Cannot get key from legacy particle");
        }

        return key;
    }

    // Paper start - Particle API expansion
    /**
     * Creates a {@link com.destroystokyo.paper.ParticleBuilder}
     *
     * @return a {@link com.destroystokyo.paper.ParticleBuilder} for the particle
     */
    @NotNull
    public com.destroystokyo.paper.ParticleBuilder builder() {
        return new com.destroystokyo.paper.ParticleBuilder(this);
    }
    // Paper end

    /**
     * Options which can be applied to dust particles - a particle
     * color and size.
     */
    public static class DustOptions {

        private final Color color;
        private final float size;

        public DustOptions(@NotNull Color color, float size) {
            Preconditions.checkArgument(color != null, "color");
            this.color = color;
            this.size = size;
        }

        /**
         * The color of the particles to be displayed.
         *
         * @return particle color
         */
        @NotNull
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

    /**
     * Options which can be applied to a color transitioning dust particles.
     */
    public static class DustTransition extends DustOptions {

        private final Color toColor;

        public DustTransition(@NotNull Color fromColor, @NotNull Color toColor, float size) {
            super(fromColor, size);

            Preconditions.checkArgument(toColor != null, "toColor");
            this.toColor = toColor;
        }

        /**
         * The final of the particles to be displayed.
         *
         * @return final particle color
         */
        @NotNull
        public Color getToColor() {
            return toColor;
        }
    }

    /**
     * Options which can be applied to trail particles - a location, color and duration.
     */
    public static class Trail {

        private final Location target;
        private final Color color;
        private final int duration;

        public Trail(@NotNull Location target, @NotNull Color color, int duration) {
            this.target = target;
            this.color = color;
            this.duration = duration;
        }

        /**
         * The target of the particles to be displayed.
         *
         * @return particle target
         */
        @NotNull
        public Location getTarget() {
            return target;
        }

        /**
         * The color of the particles to be displayed.
         *
         * @return particle color
         */
        @NotNull
        public Color getColor() {
            return color;
        }

        /**
         * The duration of the trail to be displayed.
         *
         * @return trail duration
         */
        public int getDuration() {
            return duration;
        }
    }

    public static class Spell {

        private final Color color;
        private final float power;

        public Spell(@NotNull Color color, float power) {
            this.color = color;
            this.power = power;
        }

        /**
         * The color of the particles to be displayed.
         *
         * @return particle color
         */
        public @NotNull Color getColor() {
            return color;
        }

        /**
         * The power of the particles to be displayed.
         *
         * @return particle power
         */
        public float getPower() {
            return power;
        }
    }
}
