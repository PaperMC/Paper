package org.bukkit.potion;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

/**
 * Represents a minecraft potion
 */
public class Potion {
    private boolean extended = false;
    private boolean splash = false;
    private int level = 1;
    private int name = -1;
    private PotionType type;

    /**
     * Construct a new potion of the given type. Unless the type is {@link PotionType#WATER},
     * it will be level one, without extended duration. Don't use this constructor to create
     * a no-effect potion other than water bottle.
     * @param type The potion type
     * @see #Potion(int)
     */
    public Potion(PotionType type) {
        this.type = type;
        if (type != null) {
            this.name = type.getDamageValue();
        }
        if (type == null || type == PotionType.WATER) {
            this.level = 0;
        }
    }

    /** @deprecated In favour of {@link #Potion(PotionType, int)} */
    @SuppressWarnings("javadoc")
    @Deprecated
    public Potion(PotionType type, Tier tier) {
        this(type, tier == Tier.TWO ? 2 : 1);
        Validate.notNull(type, "Type cannot be null");
    }

    /** @deprecated In favour of {@link #Potion(PotionType, int, boolean)} */
    @SuppressWarnings("javadoc")
    @Deprecated
    public Potion(PotionType type, Tier tier, boolean splash) {
        this(type, tier == Tier.TWO ? 2 : 1, splash);
    }

    /** @deprecated In favour of {@link #Potion(PotionType, int, boolean, boolean)} */
    @SuppressWarnings("javadoc")
    @Deprecated
    public Potion(PotionType type, Tier tier, boolean splash, boolean extended) {
        this(type, tier, splash);
        this.extended = extended;
    }

    /**
     * Create a new potion of the given type and level.
     * @param type The type of potion.
     * @param level The potion's level.
     */
    public Potion(PotionType type, int level) {
        this(type);
        Validate.notNull(type, "Type cannot be null");
        Validate.isTrue(type != PotionType.WATER, "Water bottles don't have a level!");
        Validate.isTrue(level > 0 && level < 3, "Level must be 1 or 2");
        this.level = level;
    }

    /**
     * Create a new potion of the given type and level.
     * @param type The type of potion.
     * @param level The potion's level.
     * @param splash Whether it is a splash potion.
     * @deprecated In favour of using {@link #Potion(PotionType)} with {@link #splash()}.
     */
    @Deprecated
    public Potion(PotionType type, int level, boolean splash) {
        this(type, level);
        this.splash = splash;
    }

    /**
     * Create a new potion of the given type and level.
     * @param type The type of potion.
     * @param level The potion's level.
     * @param splash Whether it is a splash potion.
     * @param extended Whether it has an extended duration.
     * @deprecated In favour of using {@link #Potion(PotionType)} with {@link #extend()}
     * and possibly {@link #splash()}.
     */
    @Deprecated
    public Potion(PotionType type, int level, boolean splash, boolean extended) {
        this(type, level, splash);
        this.extended = extended;
    }

    /**
     * Create a potion with a specific name.
     * @param name The name index (0-63)
     */
    public Potion(int name) {
        this(PotionType.getByDamageValue(name & POTION_BIT));
        this.name = name & NAME_BIT;
        if ((name & POTION_BIT) == 0) {
            // If it's 0 it would've become PotionType.WATER, but it should actually be mundane potion
            this.type = null;
        }
    }

    /**
     * Chain this to the constructor to make the potion a splash potion.
     * @return The potion.
     */
    public Potion splash() {
        setSplash(true);
        return this;
    }

    /**
     * Chain this to the constructor to extend the potion's duration.
     * @return The potion.
     */
    public Potion extend() {
        setHasExtendedDuration(true);
        return this;
    }

    /**
     * Applies the effects of this potion to the given {@link ItemStack}. The
     * itemstack must be a potion.
     *
     * @param to The itemstack to apply to
     */
    public void apply(ItemStack to) {
        Validate.notNull(to, "itemstack cannot be null");
        Validate.isTrue(to.getType() == Material.POTION, "given itemstack is not a potion");
        to.setDurability(toDamageValue());
    }

    /**
     * Applies the effects that would be applied by this potion to the given
     * {@link LivingEntity}.
     *
     * @see LivingEntity#addPotionEffects(Collection)
     * @param to The entity to apply the effects to
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
        if (type == null) return ImmutableList.<PotionEffect>of();
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
        return level == 2 ? Tier.TWO : Tier.ONE;
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
     * @param isExtended Whether the potion should have extended duration
     */
    public void setHasExtendedDuration(boolean isExtended) {
        Validate.isTrue(type == null || !type.isInstant(), "Instant potions cannot be extended");
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
     * @param tier The new tier of this potion
     * @deprecated In favour of {@link #setLevel(int)}
     */
    @Deprecated
    public void setTier(Tier tier) {
        Validate.notNull(tier, "tier cannot be null");
        this.level = (tier == Tier.TWO ? 2 : 1);
    }

    /**
     * Sets the {@link PotionType} of this potion.
     *
     * @param type
     *            The new type of this potion
     */
    public void setType(PotionType type) {
        this.type = type;
    }

    /**
     * Sets the level of this potion.
     *
     * @param level The new level of this potion
     */
    public void setLevel(int level) {
        Validate.notNull(this.type, "No-effect potions don't have a level.");
        int max = type.getMaxLevel();
        Validate.isTrue(level > 0 && level <= max, "Level must be " + (max == 1 ? "" : "between 1 and ") + max + " for this potion");
        this.level = level;
    }

    /**
     * Converts this potion to a valid potion damage short, usable for potion
     * item stacks.
     *
     * @return The damage value of this potion
     */
    public short toDamageValue() {
        short damage;
        if (type == PotionType.WATER) {
            return 0;
        } else if (type == null) {
            // Without this, mundanePotion.toDamageValue() would return 0
            damage = (short) (name == 0 ? 8192 : name);
        } else {
            damage = (short) (level - 1);
            damage <<= TIER_SHIFT;
            damage |= (short) type.getDamageValue();
        }
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
     * @param amount The amount of the ItemStack
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
    private static final int NAME_BIT = 0x3F;

    public static Potion fromDamage(int damage) {
        PotionType type = PotionType.getByDamageValue(damage & POTION_BIT);
        Potion potion;
        if (type == null || (type == PotionType.WATER && damage != 0)) {
            potion = new Potion(damage & NAME_BIT);
        } else {
            int level = (damage & TIER_BIT) >> TIER_SHIFT;
            level++;
            potion = new Potion(type, level);
        }
        if ((damage & SPLASH_BIT) > 0) {
            potion = potion.splash();
        }
        if ((damage & EXTENDED_BIT) > 0) {
            potion = potion.extend();
        }
        return potion;
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
     * @param other The new PotionBrewer
     */
    public static void setPotionBrewer(PotionBrewer other) {
        if (brewer != null)
            throw new IllegalArgumentException("brewer can only be set internally");
        brewer = other;
    }

    public int getNameId() {
        return name;
    }
}