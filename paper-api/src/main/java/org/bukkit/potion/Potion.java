package org.bukkit.potion;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a minecraft potion
 */
public class Potion {
    private boolean extended = false;
    private boolean splash = false;
    @Deprecated
    private Tier tier = Tier.ONE;
    private int level = 1;
    private final PotionType type;

    public Potion(PotionType type) {
        Validate.notNull(type, "type cannot be null");
        this.type = type;
    }

    @Deprecated
    public Potion(PotionType type, Tier tier) {
        this(type, tier == Tier.TWO ? 2 : 1);
        Validate.notNull(tier, "tier cannot be null");
    }

    @Deprecated
    public Potion(PotionType type, Tier tier, boolean splash) {
        this(type, tier == Tier.TWO ? 2 : 1, splash);
    }

    @Deprecated
    public Potion(PotionType type, Tier tier, boolean splash, boolean extended) {
        this(type, tier, splash);
        this.extended = extended;
    }

    public Potion(PotionType type, int level) {
        this(type);
        Validate.isTrue(level > 0 && level < 3, "Level must be 1 or 2");
        this.level = level;
    }

    public Potion(PotionType type, int level, boolean splash) {
        this(type, level);
        this.splash = splash;
    }

    public Potion(PotionType type, int level, boolean splash, boolean extended) {
        this(type, level, splash);
        this.extended = extended;
    }

    /**
     * Applies the effects of this potion to the given {@link ItemStack}. The
     * itemstack must be a potion.
     *
     * @param to
     *            The itemstack to apply to
     */
    public void apply(ItemStack to) {
        Validate.notNull(to, "itemstack cannot be null");
        if (to.getType() != Material.POTION)
            throw new IllegalArgumentException("given itemstack is not a potion");
        to.setDurability(toDamageValue());
    }

    /**
     * Applies the effects that would be applied by this potion to the given
     * {@link LivingEntity}.
     *
     * @see LivingEntity#addPotionEffects(Collection)
     * @param to
     *            The entity to apply the effects to
     */
    public void apply(LivingEntity to) {
        Validate.notNull(to, "entity cannot be null");
        to.addPotionEffects(getEffects());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Potion other = (Potion) obj;
        return extended == other.extended && splash == other.splash && level == other.level && type == other.type;
    }

    /**
     * Returns a collection of {@link PotionEffect}s that this {@link Potion}
     * would confer upon a {@link LivingEntity}.
     *
     * @see PotionBrewer#getEffectsFromDamage(int)
     * @see Potion#toDamageValue()
     * @return The effects that this potion applies
     */
    public Collection<PotionEffect> getEffects() {
        return getBrewer().getEffectsFromDamage(toDamageValue());
    }

    /**
     * Returns the level of this potion.
     *
     * @return The level of this potion
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the {@link Tier} of this potion.
     *
     * @return The tier of this potion
     */
    @Deprecated
    public Tier getTier() {
        return tier;
    }

    /**
     * Returns the {@link PotionType} of this potion.
     *
     * @return The type of this potion
     */
    public PotionType getType() {
        return type;
    }

    /**
     * Returns whether this potion has an extended duration.
     *
     * @return Whether this potion has extended duration
     */
    public boolean hasExtendedDuration() {
        return extended;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + level;
        result = prime * result + (extended ? 1231 : 1237);
        result = prime * result + (splash ? 1231 : 1237);
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * Returns whether this potion is a splash potion.
     *
     * @return Whether this is a splash potion
     */
    public boolean isSplash() {
        return splash;
    }

    /**
     * Set whether this potion has extended duration. This will cause the potion
     * to have roughly 8/3 more duration than a regular potion.
     *
     * @param isExtended
     *            Whether the potion should have extended duration
     */
    public void setHasExtendedDuration(boolean isExtended) {
        extended = isExtended;
    }

    /**
     * Sets whether this potion is a splash potion. Splash potions can be thrown
     * for a radius effect.
     *
     * @param isSplash
     *            Whether this is a splash potion
     */
    public void setSplash(boolean isSplash) {
        splash = isSplash;
    }

    /**
     * Sets the {@link Tier} of this potion.
     *
     * @param tier
     *            The new tier of this potion
     * @deprecated In favour of {@link #setLevel(int)}
     */
    @Deprecated
    public void setTier(Tier tier) {
        Validate.notNull(tier, "tier cannot be null");
        this.tier = tier;
        this.level = (tier == Tier.TWO ? 2 : 1);
    }

    /**
     * Sets the level of this potion.
     *
     * @param level
     *            The new level of this potion
     */
    public void setLevel(int level) {
        this.level = level;
        this.tier = level == 2 ? Tier.TWO : Tier.ONE;
    }

    /**
     * Converts this potion to a valid potion damage short, usable for potion
     * item stacks.
     *
     * @return The damage value of this potion
     */
    public short toDamageValue() {
        short damage = type == null ? 0 : (short) type.getDamageValue();
        damage |= level == 2 ? 0x20 : 0;
        if (splash) {
            damage |= SPLASH_BIT;
        }
        if (extended) {
            damage |= EXTENDED_BIT;
        }
        return damage;
    }

    /**
     * Converts this potion to an {@link ItemStack} with the specified amount
     * and a correct damage value.
     *
     * @param amount
     *            The amount of the ItemStack
     * @return The created ItemStack
     */
    public ItemStack toItemStack(int amount) {
        return new ItemStack(Material.POTION, amount, toDamageValue());
    }

    @Deprecated
    public enum Tier {
        ONE(0),
        TWO(0x20);

        private int damageBit;

        Tier(int bit) {
            damageBit = bit;
        }

        public int getDamageBit() {
            return damageBit;
        }

        public static Tier getByDamageBit(int damageBit) {
            for (Tier tier : Tier.values()) {
                if (tier.damageBit == damageBit)
                    return tier;
            }
            return null;
        }
    }

    private static PotionBrewer brewer;

    private static final int EXTENDED_BIT = 0x40;
    private static final int POTION_BIT = 0xF;
    private static final int SPLASH_BIT = 0x4000;
    private static final int TIER_BIT = 0x20;
    private static final int TIER_SHIFT = 5;

    public static Potion fromDamage(int damage) {
        PotionType type = PotionType.getByDamageValue(damage & POTION_BIT);
        int level = 1;
        level = (damage & TIER_BIT) >> TIER_SHIFT;
        return new Potion(type, level, (damage & SPLASH_BIT) > 0, (damage & EXTENDED_BIT) > 0);
    }

    public static Potion fromItemStack(ItemStack item) {
        Validate.notNull(item, "item cannot be null");
        if (item.getType() != Material.POTION)
            throw new IllegalArgumentException("item is not a potion");
        return fromDamage(item.getDurability());
    }

    /**
     * Returns an instance of {@link PotionBrewer}.
     *
     * @return An instance of PotionBrewer
     */
    public static PotionBrewer getBrewer() {
        return brewer;
    }

    /**
     * Sets the current instance of {@link PotionBrewer}. Generally not to be
     * used from within a plugin.
     *
     * @param other
     *            The new PotionBrewer
     */
    public static void setPotionBrewer(PotionBrewer other) {
        if (brewer != null)
            throw new IllegalArgumentException("brewer can only be set internally");
        brewer = other;
    }
}