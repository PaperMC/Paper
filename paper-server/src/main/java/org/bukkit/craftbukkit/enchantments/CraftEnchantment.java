package org.bukkit.craftbukkit.enchantments;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import java.util.Locale;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class CraftEnchantment extends Enchantment implements Holderable<net.minecraft.world.item.enchantment.Enchantment> {

    public static Enchantment minecraftToBukkit(net.minecraft.world.item.enchantment.Enchantment minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ENCHANTMENT);
    }

    public static Enchantment minecraftHolderToBukkit(Holder<net.minecraft.world.item.enchantment.Enchantment> minecraft) {
        return CraftEnchantment.minecraftToBukkit(minecraft.value());
    }

    public static net.minecraft.world.item.enchantment.Enchantment bukkitToMinecraft(Enchantment bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.item.enchantment.Enchantment> bukkitToMinecraftHolder(Enchantment bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
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
        return CraftRegistry.get(RegistryKey.ENCHANTMENT, key, ApiVersion.CURRENT);
    }

    private final Holder<net.minecraft.world.item.enchantment.Enchantment> handle;

    public CraftEnchantment(Holder<net.minecraft.world.item.enchantment.Enchantment> holder) {
        this.handle = holder;
    }

    @Override
    public Holder<net.minecraft.world.item.enchantment.Enchantment> getHolder() {
        return this.handle;
    }

    @Override
    public NamespacedKey getKey() {
        return Holderable.super.getKey();
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
        return this.handle.is(EnchantmentTags.TREASURE); // Paper - use treasure tag
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
    public net.kyori.adventure.text.Component displayName(int level) {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(net.minecraft.world.item.enchantment.Enchantment.getFullname(this.handle, level));
    }

    @Override
    public String translationKey() {
        if (!(this.getHandle().description().getContents() instanceof final net.minecraft.network.chat.contents.TranslatableContents translatableContents)) {
            throw new UnsupportedOperationException("Description isn't translatable!"); // Paper
        }
        return translatableContents.getKey();
    }

    @Override
    public boolean isTradeable() {
        return this.handle.is(EnchantmentTags.TRADEABLE);
    }

    @Override
    public boolean isDiscoverable() {
        return this.handle.is(EnchantmentTags.IN_ENCHANTING_TABLE)
            || this.handle.is(EnchantmentTags.ON_RANDOM_LOOT)
            || this.handle.is(EnchantmentTags.ON_MOB_SPAWN_EQUIPMENT)
            || this.handle.is(EnchantmentTags.TRADEABLE)
            || this.handle.is(EnchantmentTags.ON_TRADED_EQUIPMENT);
    }

    @Override
    public int getMinModifiedCost(int level) {
        return this.getHandle().definition().minCost().calculate(level);
    }

    @Override
    public int getMaxModifiedCost(int level) {
        return this.getHandle().definition().maxCost().calculate(level);
    }

    @Override
    public int getAnvilCost() {
        return this.getHandle().definition().anvilCost();
    }

    @Override
    public io.papermc.paper.enchantments.EnchantmentRarity getRarity() {
        throw new UnsupportedOperationException("Enchantments don't have a rarity anymore in 1.20.5+.");
    }

    @Override
    public float getDamageIncrease(int level, org.bukkit.entity.EntityCategory entityCategory) {
        throw new UnsupportedOperationException("Enchantments are based on complex effect maps since 1.21, cannot compute a simple damage increase");
    }

    @Override
    public float getDamageIncrease(int level, org.bukkit.entity.EntityType entityType) {
        throw new UnsupportedOperationException("Enchantments are based on complex effect maps since 1.21, cannot compute a simple damage increase");
    }

    @Override
    public java.util.Set<org.bukkit.inventory.EquipmentSlotGroup> getActiveSlotGroups() {
        return this.getHandle().definition().slots().stream()
            .map(org.bukkit.craftbukkit.CraftEquipmentSlot::getSlotGroup)
            .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public net.kyori.adventure.text.Component description() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.handle.value().description());
    }

    @Override
    public io.papermc.paper.registry.set.RegistryKeySet<org.bukkit.inventory.ItemType> getSupportedItems() {
        return io.papermc.paper.registry.set.PaperRegistrySets.convertToApi(io.papermc.paper.registry.RegistryKey.ITEM, this.handle.value().getSupportedItems());
    }

    @Override
    public io.papermc.paper.registry.set.RegistryKeySet<org.bukkit.inventory.ItemType> getPrimaryItems() {
        final java.util.Optional<net.minecraft.core.HolderSet<net.minecraft.world.item.Item>> primaryItems = this.handle.value().definition().primaryItems();
        return primaryItems.map(holders -> io.papermc.paper.registry.set.PaperRegistrySets.convertToApi(io.papermc.paper.registry.RegistryKey.ITEM, holders)).orElse(null);
    }

    @Override
    public int getWeight() {
        return this.handle.value().getWeight();
    }

    @Override
    public io.papermc.paper.registry.set.RegistryKeySet<org.bukkit.enchantments.Enchantment> getExclusiveWith() {
        return io.papermc.paper.registry.set.PaperRegistrySets.convertToApi(io.papermc.paper.registry.RegistryKey.ENCHANTMENT, this.handle.value().exclusiveSet());
    }

    @Override
    public String getTranslationKey() {
        return Util.makeDescriptionId("enchantment", this.handle.unwrapKey().get().location());
    }

    @Override
    public boolean equals(Object other) {
        return Holderable.super.implEquals(other);
    }

    @Override
    public int hashCode() {
        return Holderable.super.implHashCode();
    }

    @Override
    public String toString() {
        return Holderable.super.implToString();
    }
}
