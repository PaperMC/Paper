package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.loot.LootContextKey;
import io.papermc.paper.loot.PaperLootContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.Optionull;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.jspecify.annotations.Nullable;

public class CraftLootTable implements org.bukkit.loot.LootTable {

    public static org.bukkit.loot.LootTable minecraftToBukkit(Identifier minecraft) {
        return (minecraft == null) ? null : Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(minecraft));
    }

    public static org.bukkit.loot.LootTable minecraftToBukkit(ResourceKey<LootTable> minecraft) {
        return (minecraft == null) ? null : Bukkit.getLootTable(CraftLootTable.minecraftToBukkitKey(minecraft));
    }

    public static NamespacedKey minecraftToBukkitKey(ResourceKey<LootTable> minecraft) {
        return (minecraft == null) ? null : CraftNamespacedKey.fromMinecraft(minecraft.identifier());
    }

    public static ResourceKey<LootTable> bukkitToMinecraft(org.bukkit.loot.LootTable table) {
        return (table == null) ? null : CraftLootTable.bukkitKeyToMinecraft(table.getKey());
    }

    public static ResourceKey<LootTable> bukkitKeyToMinecraft(NamespacedKey key) {
        return (key == null) ? null : ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(key));
    }

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(NamespacedKey key, LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return this.handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        return this.populateLoot0(random, context);
    }

    @Override
    public Collection<ItemStack> populateLoot(LootContext context) {
        return this.populateLoot0(null, context);
    }

    public Collection<ItemStack> populateLoot0(@Nullable Random random, LootContext context) {
        Preconditions.checkArgument(context != null, "context cannot be null");
        Preconditions.checkArgument(context.getWorld() != null, "World in loot context cannot be null");
        if (random == null) {
            random = context.getRandom();
        }

        LootParams params = PaperLootContext.toVanilla(context, this.getHandle().getParamSet());
        List<net.minecraft.world.item.ItemStack> items = this.handle.getRandomItems(params, Optionull.map(random, RandomSourceWrapper::new));
        Collection<ItemStack> result = new ArrayList<>(items.size());

        for (net.minecraft.world.item.ItemStack item : items) {
            if (item.isEmpty()) {
                continue;
            }

            result.add(CraftItemStack.asBukkitCopy(item));
        }

        return result;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        this.fillInventory0(inventory, random, context);
    }

    @Override
    public void fillInventory(Inventory inventory, LootContext context) {
        this.fillInventory0(inventory, null, context);
    }

    public void fillInventory0(Inventory inventory, Random random, LootContext context) {
        Preconditions.checkArgument(inventory != null, "inventory cannot be null");
        Preconditions.checkArgument(context != null, "context cannot be null");
        Preconditions.checkArgument(context.getWorld() != null, "World in loot context cannot be null");
        if (random == null) {
            random = context.getRandom();
        }

        LootParams params = PaperLootContext.toVanilla(context, this.getHandle().getParamSet());
        Container container = ((CraftInventory) inventory).getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        this.getHandle().fill(container, params, Optionull.map(random, RandomSourceWrapper::new), true);
    }

    @Override
    public Set<LootContextKey> requiredContextKeys() {
        return PaperLootContext.Key.SET_CONVERTER.fromVanilla(this.getHandle().getParamSet().required());
    }

    @Override
    public Set<LootContextKey> allowedContextKeys() {
        return PaperLootContext.Key.SET_CONVERTER.fromVanilla(this.getHandle().getParamSet().allowed());
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof final org.bukkit.loot.LootTable table)) {
            return false;
        }

        return table.getKey().equals(this.key);
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }
}
