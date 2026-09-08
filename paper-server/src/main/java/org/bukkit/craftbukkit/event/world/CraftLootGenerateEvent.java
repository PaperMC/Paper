package org.bukkit.craftbukkit.event.world;

import io.papermc.paper.util.MCUtil;
import java.util.Collection;
import java.util.List;
import net.minecraft.Optionull;
import net.minecraft.world.Container;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

public class CraftLootGenerateEvent extends CraftWorldEvent implements LootGenerateEvent {

    private final @Nullable Entity entity;
    private final @Nullable InventoryHolder inventoryHolder;
    private final LootTable lootTable;
    private final LootContext lootContext;
    private final List<ItemStack> loot;
    private final boolean plugin;

    private boolean cancelled;

    public CraftLootGenerateEvent(
        final World world,
        final @Nullable Entity entity,
        final @Nullable InventoryHolder inventoryHolder,
        final LootTable lootTable,
        final LootContext lootContext,
        final List<ItemStack> items,
        final boolean plugin
    ) {
        super(world);
        this.entity = entity;
        this.inventoryHolder = inventoryHolder;
        this.lootTable = lootTable;
        this.lootContext = lootContext;
        this.loot = items;
        this.plugin = plugin;
    }

    public CraftLootGenerateEvent(
        final net.minecraft.world.level.storage.loot.LootTable lootTable,
        final Container container,
        final net.minecraft.world.level.storage.loot.LootContext lootContext,
        final List<net.minecraft.world.item.ItemStack> loot,
        final boolean plugin
    ) {
        this(
            lootContext.getLevel().getWorld(),
            Optionull.map(lootContext.getOptionalParameter(LootContextParams.THIS_ENTITY), net.minecraft.world.entity.Entity::getBukkitEntity),
            container.getOwner(),
            lootTable.craftLootTable,
            CraftLootTable.convertContext(lootContext),
            MCUtil.mutableTransform(loot, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy),
            plugin
        );
    }

    @Override
    public @Nullable Entity getEntity() {
        return this.entity;
    }

    @Override
    public @Nullable InventoryHolder getInventoryHolder() {
        return this.inventoryHolder;
    }

    @Override
    public LootTable getLootTable() {
        return this.lootTable;
    }

    @Override
    public LootContext getLootContext() {
        return this.lootContext;
    }

    @Override
    public List<ItemStack> getLoot() {
        return this.loot;
    }

    @Override
    public void setLoot(final @Nullable Collection<ItemStack> loot) {
        this.loot.clear();
        if (loot != null) {
            this.loot.addAll(loot);
        }
    }

    @Override
    public boolean isPlugin() {
        return this.plugin;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return LootGenerateEvent.getHandlerList();
    }
}
