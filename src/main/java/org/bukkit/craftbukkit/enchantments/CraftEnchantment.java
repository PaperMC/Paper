package org.bukkit.craftbukkit.enchantments;

import net.minecraft.server.Item;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment {
    private final net.minecraft.server.Enchantment target;

    public CraftEnchantment(net.minecraft.server.Enchantment target) {
        super(target.id);
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
        switch (target.slot) {
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
            case ARMOR_TORSO:
                return EnchantmentTarget.ARMOR_TORSO;
            case DIGGER:
                return EnchantmentTarget.TOOL;
            case WEAPON:
                return EnchantmentTarget.WEAPON;
            default:
                return null;
        }
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return target.slot.canEnchant(Item.byId[item.getTypeId()]);
    }

    @Override
    public String getName() {
        switch (target.id) {
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
            case 16:
                return "DAMAGE_ALL";
            case 17:
                return "DAMAGE_UNDEAD";
            case 18:
                return "DAMAGE_ARTHROPODS";
            case 19:
                return "KNOCKBACK";
            case 20:
                return "FIRE_ASPECT";
            case 21:
                return "LOOT_BONUS_MOBS";
            case 32:
                return "DIG_SPEED";
            case 33:
                return "SILK_TOUCH";
            case 34:
                return "DURABILITY";
            case 35:
                return "LOOT_BONUS_BLOCKS";
            default:
                return "UNKNOWN_ENCHANT_" + target.id;
        }
    }

    public static net.minecraft.server.Enchantment getRaw(Enchantment enchantment) {
        if (enchantment instanceof EnchantmentWrapper) {
            enchantment = ((EnchantmentWrapper)enchantment).getEnchantment();
        }

        if (enchantment instanceof CraftEnchantment) {
            return ((CraftEnchantment)enchantment).target;
        }

        return null;
    }
}
