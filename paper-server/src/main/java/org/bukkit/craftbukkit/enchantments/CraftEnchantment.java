package org.bukkit.craftbukkit.enchantments;

import net.minecraft.core.IRegistry;
import net.minecraft.world.item.enchantment.EnchantmentBinding;
import net.minecraft.world.item.enchantment.EnchantmentVanishing;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {
    private final net.minecraft.world.item.enchantment.Enchantment target;

    public CraftEnchantment(net.minecraft.world.item.enchantment.Enchantment target) {
        super(CraftNamespacedKey.fromMinecraft(IRegistry.ENCHANTMENT.getKey(target)));
        this.target = target;
    }

    @Override
    public int getMaxLevel() {
        return target.getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return target.getMinLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        switch (target.category) {
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
        case VANISHABLE:
            return EnchantmentTarget.VANISHABLE;
        default:
            return null;
        }
    }

    @Override
    public boolean isTreasure() {
        return target.isTreasureOnly();
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
        switch (IRegistry.ENCHANTMENT.getId(target)) {
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
            return "SOUL_SPEED";
        case 12:
            return "DAMAGE_ALL";
        case 13:
            return "DAMAGE_UNDEAD";
        case 14:
            return "DAMAGE_ARTHROPODS";
        case 15:
            return "KNOCKBACK";
        case 16:
            return "FIRE_ASPECT";
        case 17:
            return "LOOT_BONUS_MOBS";
        case 18:
            return "SWEEPING_EDGE";
        case 19:
            return "DIG_SPEED";
        case 20:
            return "SILK_TOUCH";
        case 21:
            return "DURABILITY";
        case 22:
            return "LOOT_BONUS_BLOCKS";
        case 23:
            return "ARROW_DAMAGE";
        case 24:
            return "ARROW_KNOCKBACK";
        case 25:
            return "ARROW_FIRE";
        case 26:
            return "ARROW_INFINITE";
        case 27:
            return "LUCK";
        case 28:
            return "LURE";
        case 29:
            return "LOYALTY";
        case 30:
            return "IMPALING";
        case 31:
            return "RIPTIDE";
        case 32:
            return "CHANNELING";
        case 33:
            return "MULTISHOT";
        case 34:
            return "QUICK_CHARGE";
        case 35:
            return "PIERCING";
        case 36:
            return "MENDING";
        case 37:
            return "VANISHING_CURSE";
        default:
            return "UNKNOWN_ENCHANT_" + IRegistry.ENCHANTMENT.getId(target);
        }
    }

    public static net.minecraft.world.item.enchantment.Enchantment getRaw(Enchantment enchantment) {
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
        return !target.isCompatibleWith(ench.target);
    }

    public net.minecraft.world.item.enchantment.Enchantment getHandle() {
        return target;
    }
}
