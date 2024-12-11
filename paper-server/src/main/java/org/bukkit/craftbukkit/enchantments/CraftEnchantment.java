package org.bukkit.craftbukkit.enchantments;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment implements Handleable<net.minecraft.world.item.enchantment.Enchantment> {

    public static Enchantment minecraftToBukkit(net.minecraft.world.item.enchantment.Enchantment minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ENCHANTMENT, Registry.ENCHANTMENT);
    }

    public static Enchantment minecraftHolderToBukkit(Holder<net.minecraft.world.item.enchantment.Enchantment> minecraft) {
        return CraftEnchantment.minecraftToBukkit(minecraft.value());
    }

    public static net.minecraft.world.item.enchantment.Enchantment bukkitToMinecraft(Enchantment bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.enchantment.Enchantment> bukkitToMinecraftHolder(Enchantment bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.ENCHANTMENT);
    }

    public static String bukkitToString(Enchantment bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return bukkit.getKey().toString();
    }

    public static Enchantment stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        // We currently do not have any version-dependent remapping, so we can use current version
        // First convert from when only the names where saved
        string = FieldRename.convertEnchantmentName(ApiVersion.CURRENT, string);
        string = string.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);

        // Now also convert from when keys where saved
        return CraftRegistry.get(Registry.ENCHANTMENT, key, ApiVersion.CURRENT);
    }

    private final NamespacedKey key;
    private final Holder<net.minecraft.world.item.enchantment.Enchantment> handle;

    public CraftEnchantment(NamespacedKey key, net.minecraft.world.item.enchantment.Enchantment handle) {
        this.key = key;
        this.handle = CraftRegistry.getMinecraftRegistry(Registries.ENCHANTMENT).wrapAsHolder(handle);
    }

    @Override
    public net.minecraft.world.item.enchantment.Enchantment getHandle() {
        return this.handle.value();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public int getMaxLevel() {
        return this.getHandle().getMaxLevel();
    }

    @Override
    public int getStartLevel() {
        return this.getHandle().getMinLevel();
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        throw new UnsupportedOperationException("Method no longer applicable. Use Tags instead.");
    }

    @Override
    public boolean isTreasure() {
        return !this.handle.is(EnchantmentTags.IN_ENCHANTING_TABLE);
    }

    @Override
    public boolean isCursed() {
        return this.handle.is(EnchantmentTags.CURSE);
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return this.getHandle().canEnchant(CraftItemStack.asNMSCopy(item));
    }

    @Override
    public String getName() {
        // PAIL: migration paths
        if (!this.getKey().getNamespace().equals(NamespacedKey.MINECRAFT)) {
            return this.getKey().toString();
        }
        String keyName = this.getKey().getKey().toUpperCase(Locale.ROOT);
        return switch (keyName) {
            case "PROTECTION" -> "PROTECTION_ENVIRONMENTAL";
            case "FIRE_PROTECTION" -> "PROTECTION_FIRE";
            case "FEATHER_FALLING" -> "PROTECTION_FALL";
            case "BLAST_PROTECTION" -> "PROTECTION_EXPLOSIONS";
            case "PROJECTILE_PROTECTION" -> "PROTECTION_PROJECTILE";
            case "RESPIRATION" -> "OXYGEN";
            case "AQUA_AFFINITY" -> "WATER_WORKER";
            case "SHARPNESS" -> "DAMAGE_ALL";
            case "SMITE" -> "DAMAGE_UNDEAD";
            case "BANE_OF_ARTHROPODS" -> "DAMAGE_ARTHROPODS";
            case "LOOTING" -> "LOOT_BONUS_MOBS";
            case "EFFICIENCY" -> "DIG_SPEED";
            case "UNBREAKING" -> "DURABILITY";
            case "FORTUNE" -> "LOOT_BONUS_BLOCKS";
            case "POWER" -> "ARROW_DAMAGE";
            case "PUNCH" -> "ARROW_KNOCKBACK";
            case "FLAME" -> "ARROW_FIRE";
            case "INFINITY" -> "ARROW_INFINITE";
            case "LUCK_OF_THE_SEA" -> "LUCK";
            default -> keyName;
        };
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
        return !net.minecraft.world.item.enchantment.Enchantment.areCompatible(this.handle, ench.handle);
    }

    @Override
    public String getTranslationKey() {
        return Util.makeDescriptionId("enchantment", this.handle.unwrapKey().get().location());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CraftEnchantment)) {
            return false;
        }

        return this.getKey().equals(((Enchantment) other).getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    @Override
    public String toString() {
        return "CraftEnchantment[" + this.getKey() + "]";
    }
}
