package org.bukkit.craftbukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.LootContextParameterSets;
import net.minecraft.server.LootContextParameters;
import net.minecraft.server.LootTable;
import net.minecraft.server.LootTableInfo;
import net.minecraft.server.WorldServer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;

public class CraftLootTable implements org.bukkit.loot.LootTable {

    private final LootTable handle;
    private final NamespacedKey key;

    public CraftLootTable(NamespacedKey key, LootTable handle) {
        this.handle = handle;
        this.key = key;
    }

    public LootTable getHandle() {
        return handle;
    }

    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext context) {
        LootTableInfo nmsContext = convertContext(context);
        List<net.minecraft.server.ItemStack> nmsItems = handle.populateLoot(nmsContext);
        Collection<ItemStack> bukkit = new ArrayList<>(nmsItems.size());

        for (net.minecraft.server.ItemStack item : nmsItems) {
            if (item.isEmpty()) {
                continue;
            }
            bukkit.add(CraftItemStack.asBukkitCopy(item));
        }

        return bukkit;
    }

    @Override
    public void fillInventory(Inventory inventory, Random random, LootContext context) {
        LootTableInfo nmsContext = convertContext(context);
        CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory handle = craftInventory.getInventory();

        // TODO: When events are added, call event here w/ custom reason?
        getHandle().fillInventory(handle, nmsContext);
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    private LootTableInfo convertContext(LootContext context) {
        Location loc = context.getLocation();
        WorldServer handle = ((CraftWorld) loc.getWorld()).getHandle();

        LootTableInfo.Builder builder = new LootTableInfo.Builder(handle);
        // builder.luck(context.getLuck());

        if (context.getLootedEntity() != null) {
            Entity nmsLootedEntity = ((CraftEntity) context.getLootedEntity()).getHandle();
            builder.set(LootContextParameters.THIS_ENTITY, nmsLootedEntity);
            builder.set(LootContextParameters.DAMAGE_SOURCE, DamageSource.GENERIC);
            builder.set(LootContextParameters.POSITION, new BlockPosition(nmsLootedEntity));
        }

        if (context.getKiller() != null) {
            EntityHuman nmsKiller = ((CraftHumanEntity) context.getKiller()).getHandle();
            builder.set(LootContextParameters.KILLER_ENTITY, nmsKiller);
            // If there is a player killer, damage source should reflect that in case loot tables use that information
            builder.set(LootContextParameters.DAMAGE_SOURCE, DamageSource.playerAttack(nmsKiller));
        }

        return builder.build(LootContextParameterSets.EMPTY);
    }

    @Override
    public String toString() {
        return getKey().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof org.bukkit.loot.LootTable)) {
            return false;
        }

        org.bukkit.loot.LootTable table = (org.bukkit.loot.LootTable) obj;
        return table.getKey().equals(this.getKey());
    }
}
