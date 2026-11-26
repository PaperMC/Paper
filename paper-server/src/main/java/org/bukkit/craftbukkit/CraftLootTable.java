package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import io.papermc.paper.loot.LootContextKey;
import io.papermc.paper.loot.PaperLootContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.Optionull;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftLootTable implements org.bukkit.loot.LootTable {

    public static org.bukkit.loot.@Nullable LootTable minecraftToBukkit(final @Nullable Identifier minecraft) {
        return (minecraft == null) ? null : Bukkit.getLootTable(CraftNamespacedKey.fromMinecraft(minecraft));
    }

    public static org.bukkit.loot.@Nullable LootTable minecraftToBukkit(final @Nullable ResourceKey<LootTable> minecraft) {
        return (minecraft == null) ? null : Bukkit.getLootTable(CraftLootTable.minecraftToBukkitKey(minecraft));
    }

    public static @Nullable NamespacedKey minecraftToBukkitKey(final @Nullable ResourceKey<LootTable> minecraft) {
        return (minecraft == null) ? null : CraftNamespacedKey.fromMinecraft(minecraft.identifier());
    }

    public static @Nullable ResourceKey<LootTable> bukkitToMinecraft(final org.bukkit.loot.@Nullable LootTable table) {
        return (table == null) ? null : CraftLootTable.bukkitKeyToMinecraft(table.getKey());
    }

    public static @Nullable ResourceKey<LootTable> bukkitKeyToMinecraft(final @Nullable NamespacedKey key) {
        return (key == null) ? null : ResourceKey.create(Registries.LOOT_TABLE, CraftNamespacedKey.toMinecraft(key));
    }

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(final NamespacedKey key, final LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return this.handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(final LootContext context) {
        Preconditions.checkArgument(context != null, "context cannot be null");

        final Random random = context.getRandom();
        final LootParams params = PaperLootContext.toVanilla(context, this.getHandle().getParamSet());
        final List<net.minecraft.world.item.ItemStack> items = this.handle.getRandomItems(params, Optionull.map(random, RandomSourceWrapper::new));
        final Collection<ItemStack> result = new ArrayList<>(items.size());

        for (final net.minecraft.world.item.ItemStack item : items) {
            if (item.isEmpty()) {
                continue;
            }

            result.add(CraftItemStack.asBukkitCopy(item));
        }

        return result;
    }

    @Override
    public void fillInventory(final Inventory inventory, final LootContext context) {
        Preconditions.checkArgument(inventory != null, "inventory cannot be null");
        Preconditions.checkArgument(context != null, "context cannot be null");

        final Random random = context.getRandom();
        final LootParams params = PaperLootContext.toVanilla(context, this.getHandle().getParamSet());
        final Container container = ((CraftInventory) inventory).getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        this.getHandle().fill(container, params, Optionull.map(random, RandomSourceWrapper::new), true);
    }

    @Override
    public Set<LootContextKey> requiredContextKeys() {
        return exposedKeys(this.getHandle().getParamSet().required());
    }

    @Override
    public Set<LootContextKey> allowedContextKeys() {
        return exposedKeys(this.getHandle().getParamSet().allowed());
    }

    // some keys (e.g. container) are not exposed in the API, leave them out instead of failing
    private static Set<LootContextKey> exposedKeys(final Set<ContextKey<?>> keys) {
        return keys.stream()
            .map(PaperLootContext.Key.CONVERTER::fromVanilla)
            .filter(Objects::nonNull)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean isValidContext(final LootContext context) {
        Preconditions.checkArgument(context != null, "context cannot be null");
        // keys not allowed by the param set are dropped in PaperLootContext#toVanilla, so only the required keys matter
        for (final ContextKey<?> key : this.getHandle().getParamSet().required()) {
            final LootContextKey apiKey = PaperLootContext.Key.CONVERTER.fromVanilla(key);
            if (apiKey == null || !context.has(apiKey)) {
                return false; // keys not exposed in the API can never be supplied by plugins
            }
        }
        return true;
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
    public boolean equals(final Object obj) {
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
