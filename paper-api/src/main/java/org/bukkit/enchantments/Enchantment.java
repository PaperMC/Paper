package org.bukkit.enchantments;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * The various type of enchantments that may be added to armour or weapons
 */
public abstract class Enchantment implements Keyed {
    /**
     * Provides protection against environmental damage
     */
    public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentWrapper("protection");

    /**
     * Provides protection against fire damage
     */
    public static final Enchantment PROTECTION_FIRE = new EnchantmentWrapper("fire_protection");

    /**
     * Provides protection against fall damage
     */
    public static final Enchantment PROTECTION_FALL = new EnchantmentWrapper("feather_falling");

    /**
     * Provides protection against explosive damage
     */
    public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentWrapper("blast_protection");

    /**
     * Provides protection against projectile damage
     */
    public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentWrapper("projectile_protection");

    /**
     * Decreases the rate of air loss whilst underwater
     */
    public static final Enchantment OXYGEN = new EnchantmentWrapper("respiration");

    /**
     * Increases the speed at which a player may mine underwater
     */
    public static final Enchantment WATER_WORKER = new EnchantmentWrapper("aqua_affinity");

    /**
     * Damages the attacker
     */
    public static final Enchantment THORNS = new EnchantmentWrapper("thorns");

    /**
     * Increases walking speed while in water
     */
    public static final Enchantment DEPTH_STRIDER = new EnchantmentWrapper("depth_strider");

    /**
     * Freezes any still water adjacent to ice / frost which player is walking on
     */
    public static final Enchantment FROST_WALKER = new EnchantmentWrapper("frost_walker");

    /**
     * Item cannot be removed
     */
    public static final Enchantment BINDING_CURSE = new EnchantmentWrapper("binding_curse");

    /**
     * Increases damage against all targets
     */
    public static final Enchantment DAMAGE_ALL = new EnchantmentWrapper("sharpness");

    /**
     * Increases damage against undead targets
     */
    public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWrapper("smite");

    /**
     * Increases damage against arthropod targets
     */
    public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWrapper("bane_of_arthropods");

    /**
     * All damage to other targets will knock them back when hit
     */
    public static final Enchantment KNOCKBACK = new EnchantmentWrapper("knockback");

    /**
     * When attacking a target, has a chance to set them on fire
     */
    public static final Enchantment FIRE_ASPECT = new EnchantmentWrapper("fire_aspect");

    /**
     * Provides a chance of gaining extra loot when killing monsters
     */
    public static final Enchantment LOOT_BONUS_MOBS = new EnchantmentWrapper("looting");

    /**
     * Increases damage against targets when using a sweep attack
     */
    public static final Enchantment SWEEPING_EDGE = new EnchantmentWrapper("sweeping");

    /**
     * Increases the rate at which you mine/dig
     */
    public static final Enchantment DIG_SPEED = new EnchantmentWrapper("efficiency");

    /**
     * Allows blocks to drop themselves instead of fragments (for example,
     * stone instead of cobblestone)
     */
    public static final Enchantment SILK_TOUCH = new EnchantmentWrapper("silk_touch");

    /**
     * Decreases the rate at which a tool looses durability
     */
    public static final Enchantment DURABILITY = new EnchantmentWrapper("unbreaking");

    /**
     * Provides a chance of gaining extra loot when destroying blocks
     */
    public static final Enchantment LOOT_BONUS_BLOCKS = new EnchantmentWrapper("fortune");

    /**
     * Provides extra damage when shooting arrows from bows
     */
    public static final Enchantment ARROW_DAMAGE = new EnchantmentWrapper("power");

    /**
     * Provides a knockback when an entity is hit by an arrow from a bow
     */
    public static final Enchantment ARROW_KNOCKBACK = new EnchantmentWrapper("punch");

    /**
     * Sets entities on fire when hit by arrows shot from a bow
     */
    public static final Enchantment ARROW_FIRE = new EnchantmentWrapper("flame");

    /**
     * Provides infinite arrows when shooting a bow
     */
    public static final Enchantment ARROW_INFINITE = new EnchantmentWrapper("infinity");

    /**
     * Decreases odds of catching worthless junk
     */
    public static final Enchantment LUCK = new EnchantmentWrapper("luck_of_the_sea");

    /**
     * Increases rate of fish biting your hook
     */
    public static final Enchantment LURE = new EnchantmentWrapper("lure");

    /**
     * Causes a thrown trident to return to the player who threw it
     */
    public static final Enchantment LOYALTY = new EnchantmentWrapper("loyalty");

    /**
     * Deals more damage to mobs that live in the ocean
     */
    public static final Enchantment IMPALING = new EnchantmentWrapper("impaling");

    /**
     * When it is rainy, launches the player in the direction their trident is thrown
     */
    public static final Enchantment RIPTIDE = new EnchantmentWrapper("riptide");

    /**
     * Strikes lightning when a mob is hit with a trident if conditions are
     * stormy
     */
    public static final Enchantment CHANNELING = new EnchantmentWrapper("channeling");

    /**
     * Allows mending the item using experience orbs
     */
    public static final Enchantment MENDING = new EnchantmentWrapper("mending");

    /**
     * Item disappears instead of dropping
     */
    public static final Enchantment VANISHING_CURSE = new EnchantmentWrapper("vanishing_curse");

    private static final Map<NamespacedKey, Enchantment> byKey = new HashMap<NamespacedKey, Enchantment>();
    private static final Map<String, Enchantment> byName = new HashMap<String, Enchantment>();
    private static boolean acceptingNew = true;
    private final NamespacedKey key;

    public Enchantment(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     * @deprecated enchantments are badly named, use {@link #getKey()}.
     */
    @Deprecated
    public abstract String getName();

    /**
     * Gets the maximum level that this Enchantment may become.
     *
     * @return Maximum level of the Enchantment
     */
    public abstract int getMaxLevel();

    /**
     * Gets the level that this Enchantment should start at
     *
     * @return Starting level of the Enchantment
     */
    public abstract int getStartLevel();

    /**
     * Gets the type of {@link ItemStack} that may fit this Enchantment.
     *
     * @return Target type of the Enchantment
     */
    public abstract EnchantmentTarget getItemTarget();

    /**
     * Checks if this enchantment is a treasure enchantment.
     * <br>
     * Treasure enchantments can only be received via looting, trading, or
     * fishing.
     *
     * @return true if the enchantment is a treasure enchantment
     */
    public abstract boolean isTreasure();

    /**
     * Checks if this enchantment is a cursed enchantment
     * <br>
     * Cursed enchantments are found the same way treasure enchantments are
     *
     * @return true if the enchantment is cursed
     * @deprecated cursed enchantments are no longer special. Will return true
     * only for {@link Enchantment#BINDING_CURSE} and
     * {@link Enchantment#VANISHING_CURSE}.
     */
    @Deprecated
    public abstract boolean isCursed();

    /**
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    public abstract boolean conflictsWith(Enchantment other);

    /**
     * Checks if this Enchantment may be applied to the given {@link
     * ItemStack}.
     * <p>
     * This does not check if it conflicts with any enchantments already
     * applied to the item.
     *
     * @param item Item to test
     * @return True if the enchantment may be applied, otherwise False
     */
    public abstract boolean canEnchantItem(ItemStack item);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Enchantment)) {
            return false;
        }
        final Enchantment other = (Enchantment) obj;
        if (!this.key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        return "Enchantment[" + key + ", " + getName() + "]";
    }

    /**
     * Registers an enchantment with the given ID and object.
     * <p>
     * Generally not to be used from within a plugin.
     *
     * @param enchantment Enchantment to register
     */
    public static void registerEnchantment(Enchantment enchantment) {
        if (byKey.containsKey(enchantment.key) || byName.containsKey(enchantment.getName())) {
            throw new IllegalArgumentException("Cannot set already-set enchantment");
        } else if (!isAcceptingRegistrations()) {
            throw new IllegalStateException("No longer accepting new enchantments (can only be done by the server implementation)");
        }

        byKey.put(enchantment.key, enchantment);
        byName.put(enchantment.getName(), enchantment);
    }

    /**
     * Checks if this is accepting Enchantment registrations.
     *
     * @return True if the server Implementation may add enchantments
     */
    public static boolean isAcceptingRegistrations() {
        return acceptingNew;
    }

    /**
     * Stops accepting any enchantment registrations
     */
    public static void stopAcceptingRegistrations() {
        acceptingNew = false;
    }

    /**
     * Gets the Enchantment at the specified key
     *
     * @param key key to fetch
     * @return Resulting Enchantment, or null if not found
     */
    public static Enchantment getByKey(NamespacedKey key) {
        return byKey.get(key);
    }

    /**
     * Gets the Enchantment at the specified name
     *
     * @param name Name to fetch
     * @return Resulting Enchantment, or null if not found
     * @deprecated enchantments are badly named, use {@link #getByKey(org.bukkit.NamespacedKey)}.
     */
    @Deprecated
    public static Enchantment getByName(String name) {
        return byName.get(name);
    }

    /**
     * Gets an array of all the registered {@link Enchantment}s
     *
     * @return Array of enchantments
     */
    public static Enchantment[] values() {
        return byName.values().toArray(new Enchantment[byName.size()]);
    }
}
