package org.bukkit.craftbukkit.enchantments;

import net.minecraft.server.EnchantmentBinding;
import net.minecraft.server.EnchantmentVanishing;
import net.minecraft.server.IRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {
    private final net.minecraft.server.Enchantment target;

    public CraftEnchantment(net.minecraft.server.Enchantment target) {
        super(CraftNamespacedKey.fromMinecraft(IRegistry.ENCHANTMENT.getKey(target)));
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return target.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return target.getStartLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        switch (target.itemTarget) {
        case ALL:
            return EnchantmentTarget.ALL;
        case ARMOR:
            return EnchantmentTarget.ARMOR;
        case ARMOR_FEET:
            return EnchantmentTarget.ARMOR_FEET;
        case ARMOR_HEAD:
            return EnchantmentTarget.ARMOR_HEAD;
        case ARMOR_LEGS:
            return EnchantmentTarget.ARMOR_LEGS;
        case ARMOR_CHEST:
            return EnchantmentTarget.ARMOR_TORSO;
        case DIGGER:
            return EnchantmentTarget.TOOL;
        case WEAPON:
            return EnchantmentTarget.WEAPON;
        case BOW:
            return EnchantmentTarget.BOW;
        case FISHING_ROD:
            return EnchantmentTarget.FISHING_ROD;
        case BREAKABLE:
            return EnchantmentTarget.BREAKABLE;
        case WEARABLE:
            return EnchantmentTarget.WEARABLE;
        case TRIDENT:
            return EnchantmentTarget.TRIDENT;
        case CROSSBOW:
            return EnchantmentTarget.CROSSBOW;
        default:
            return null;
        }
    }

    @Override
    public boolean isTreasure() {
        return target.isTreasure();
    }

    @Override
    public boolean isCursed() {
        return target instanceof EnchantmentBinding || target instanceof EnchantmentVanishing;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return target.canEnchant(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        // PAIL: migration paths
        switch (IRegistry.ENCHANTMENT.a(target)) {
        case 0:
            return "PROTECTION_ENVIRONMENTAL";
        case 1:
            return "PROTECTION_FIRE";
        case 2:
            return "PROTECTION_FALL";
        case 3:
            return "PROTECTION_EXPLOSIONS";
        case 4:
            return "PROTECTION_PROJECTILE";
        case 5:
            return "OXYGEN";
        case 6:
            return "WATER_WORKER";
        case 7:
            return "THORNS";
        case 8:
            return "DEPTH_STRIDER";
        case 9:
            return "FROST_WALKER";
        case 10:
            return "BINDING_CURSE";
        case 11:
            return "DAMAGE_ALL";
        case 12:
            return "DAMAGE_UNDEAD";
        case 13:
            return "DAMAGE_ARTHROPODS";
        case 14:
            return "KNOCKBACK";
        case 15:
            return "FIRE_ASPECT";
        case 16:
            return "LOOT_BONUS_MOBS";
        case 17:
            return "SWEEPING_EDGE";
        case 18:
            return "DIG_SPEED";
        case 19:
            return "SILK_TOUCH";
        case 20:
            return "DURABILITY";
        case 21:
            return "LOOT_BONUS_BLOCKS";
        case 22:
            return "ARROW_DAMAGE";
        case 23:
            return "ARROW_KNOCKBACK";
        case 24:
            return "ARROW_FIRE";
        case 25:
            return "ARROW_INFINITE";
        case 26:
            return "LUCK";
        case 27:
            return "LURE";
        case 28:
            return "LOYALTY";
        case 29:
            return "IMPALING";
        case 30:
            return "RIPTIDE";
        case 31:
            return "CHANNELING";
        case 32:
            return "MULTISHOT";
        case 33:
            return "QUICK_CHARGE";
        case 34:
            return "PIERCING";
        case 35:
            return "MENDING";
        case 36:
            return "VANISHING_CURSE";
        default:
            return "UNKNOWN_ENCHANT_" + IRegistry.ENCHANTMENT.a(target);
        }
    }

    public static net.minecraft.server.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper) enchantment).getEnchantment();
        }

        if (enchantment instanceof CraftEnchantment) {
            return ((CraftEnchantment) enchantment).target;
        }

        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        if (other instanceof EnchantmentWrapper) {
            other = ((EnchantmentWrapper) other).getEnchantment();
        }
        if (!(other instanceof CraftEnchantment)) {
            return false;
        }
        CraftEnchantment ench = (CraftEnchantment) other;
        return !target.isCompatible(ench.target);
    }

    public net.minecraft.server.Enchantment getHandle() {
        return target;
    }
}
