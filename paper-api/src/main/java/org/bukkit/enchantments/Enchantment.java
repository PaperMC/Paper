package org.bukkit.enchantments;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

/**
 * The various type of enchantments that may be added to armour or weapons
 */
public abstract class Enchantment {
    /**
     * Provides protection against environmental damage
     */
    public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentWrapper(0);

    /**
     * Provides protection against fire damage
     */
    public static final Enchantment PROTECTION_FIRE = new EnchantmentWrapper(1);

    /**
     * Provides protection against fall damage
     */
    public static final Enchantment PROTECTION_FALL = new EnchantmentWrapper(2);

    /**
     * Provides protection against explosive damage
     */
    public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentWrapper(3);

    /**
     * Provides protection against projectile damage
     */
    public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentWrapper(4);

    /**
     * Decreases the rate of air loss whilst underwater
     */
    public static final Enchantment OXYGEN = new EnchantmentWrapper(5);

    /**
     * Increases the speed at which a player may mine underwater
     */
    public static final Enchantment WATER_WORKER = new EnchantmentWrapper(6);

    /**
     * Increases damage against all targets
     */
    public static final Enchantment DAMAGE_ALL = new EnchantmentWrapper(16);

    /**
     * Increases damage against undead targets
     */
    public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWrapper(17);

    /**
     * Increases damage against arthropod targets
     */
    public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWrapper(18);

    /**
     * All damage to other targets will knock them back when hit
     */
    public static final Enchantment KNOCKBACK = new EnchantmentWrapper(19);

    /**
     * When attacking a target, has a chance to set them on fire
     */
    public static final Enchantment FIRE_ASPECT = new EnchantmentWrapper(20);

    /**
     * Provides a chance of gaining extra loot when killing monsters
     */
    public static final Enchantment LOOT_BONUS_MOBS = new EnchantmentWrapper(21);

    /**
     * Increases the rate at which you mine/dig
     */
    public static final Enchantment DIG_SPEED = new EnchantmentWrapper(32);

    /**
     * Allows blocks to drop themselves instead of fragments (for example, stone instead of cobblestone)
     */
    public static final Enchantment SILK_TOUCH = new EnchantmentWrapper(33);

    /**
     * Decreases the rate at which a tool looses durability
     */
    public static final Enchantment DURABILITY = new EnchantmentWrapper(34);

    /**
     * Provides a chance of gaining extra loot when destroying blocks
     */
    public static final Enchantment LOOT_BONUS_BLOCKS = new EnchantmentWrapper(35);

    /**
     * Provides extra damage when shooting arrows from bows
     */
    public static final Enchantment ARROW_DAMAGE = new EnchantmentWrapper(48);

    /**
     * Provides a knockback when an entity is hit by an arrow from a bow
     */
    public static final Enchantment ARROW_KNOCKBACK = new EnchantmentWrapper(49);

    /**
     * Sets entities on fire when hit by arrows shot from a bow
     */
    public static final Enchantment ARROW_FIRE = new EnchantmentWrapper(50);

    /**
     * Provides infinite arrows when shooting a bow
     */
    public static final Enchantment ARROW_INFINITE = new EnchantmentWrapper(51);

    private static final Map<Integer, Enchantment> byId = new HashMap<Integer, Enchantment>();
    private static final Map<String, Enchantment> byName = new HashMap<String, Enchantment>();
    private static boolean acceptingNew = true;
    private final int id;

    public Enchantment(int id) {
        this.id = id;
    }

    /**
     * Gets the unique ID of this enchantment
     *
     * @return Unique ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     */
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
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    public abstract boolean conflictsWith(Enchantment other);

    /**
     * Checks if this Enchantment may be applied to the given {@link ItemStack}.
     * This does not check if it conflicts with any enchantmentds already applied to the item.
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
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Enchantment[" + id + ", " + getName() + "]";
    }

    /**
     * Registers an enchantment with the given ID and object.
     * <p />
     * Generally not to be used from within a plugin.
     *
     * @param enchantment Enchantment to register
     */
    public static void registerEnchantment(Enchantment enchantment) {
        if (byId.containsKey(enchantment.id) || byName.containsKey(enchantment.getName())) {
            throw new IllegalArgumentException("Cannot set already-set enchantment");
        } else if (!isAcceptingRegistrations()) {
            throw new IllegalStateException("No longer accepting new enchantments (can only be done by the server implementation)");
        }

        byId.put(enchantment.id, enchantment);
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
     * Gets the Enchantment at the specified ID
     *
     * @param id ID to fetch
     * @return Resulting Enchantment, or null if not found
     */
    public static Enchantment getById(int id) {
        return byId.get(id);
    }

    /**
     * Gets the Enchantment at the specified name
     *
     * @param name Name to fetch
     * @return Resulting Enchantment, or null if not found
     */
    public static Enchantment getByName(String name) {
        return byName.get(name);
    }

    /**
     * Gets an array of all the registered {@link Enchantment}s
     *
     * @return Array of enchantments
     */
    public static Enchantment[] values() {
        return byId.values().toArray(new Enchantment[byId.size()]);
    }
}
