package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.loot.LootContextKey;
import io.papermc.paper.loot.PaperLootContextKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
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
        Preconditions.checkArgument(context != null, "context cannot be null");
        Preconditions.checkArgument(context.getWorld() != null, "World in loot context cannot be null");

        LootParams params = convertContext(context, this.getHandle().getParamSet());
        List<net.minecraft.world.item.ItemStack> nmsItems = this.handle.getRandomItems(params, random == null ? null : new RandomSourceWrapper(random));
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.world.item.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) { // todo deprecate and use the random provided in the context?
        Preconditions.checkArgument(inventory != null, "inventory cannot be null");
        Preconditions.checkArgument(context != null, "context cannot be null");
        Preconditions.checkArgument(context.getWorld() != null, "World in loot context cannot be null");

        LootParams params = convertContext(context, this.getHandle().getParamSet());
        Container container = ((CraftInventory) inventory).getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        this.getHandle().fill(container, params, random == null ? null : new RandomSourceWrapper(random), true);
    }

    @Override
    public Set<LootContextKey> requiredContextKeys() {
        return this.getHandle().getParamSet().required().stream()
            .map(PaperLootContextKey.KEY_BRIDGE::get).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<LootContextKey> allowedContextKeys() {
        return this.getHandle().getParamSet().allowed().stream()
            .map(PaperLootContextKey.KEY_BRIDGE::get).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    private static LootParams convertContext(LootContext context, ContextKeySet contextKeySet) {
        final LootParams.Builder builder = new LootParams.Builder(((CraftWorld) context.getWorld()).getHandle())
            .withLuck(context.getLuck());
        context.getContextMap().forEach((key, value) -> {
            io.papermc.paper.loot.PaperLootContextKey.applyToBuilder(contextKeySet.allowed(), builder, key, value);
        });

        return builder.create(contextKeySet);
    }

    public static LootContext convertContext(net.minecraft.world.level.storage.loot.LootContext context) {
        final LootContext.Builder builder = new LootContext.Builder(context.getLevel().getWorld())
            .withRandom(new org.bukkit.craftbukkit.util.RandomSourceWrapper.RandomWrapper(context.getRandom()))
            .luck(context.getLuck());
        for (final ContextKey<?> key : io.papermc.paper.loot.PaperLootContextKey.KEY_BRIDGE.keySet()) { // not ideal
            if (context.hasParameter(key)) {
                io.papermc.paper.loot.PaperLootContextKey.applyToApiBuilder(builder, key, context.getParameter(key));
            }
        }
        return builder.build();
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
