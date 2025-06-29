package io.papermc.paper.loot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

/**
 * Implementation of LootGenerator.
 * This class delegates to the underlying LootTable while providing the new API.
 */
class LootGeneratorImpl implements LootGenerator {
    
    private final LootTable lootTable;
    
    LootGeneratorImpl(LootTable lootTable) {
        if (lootTable == null) {
            throw new IllegalArgumentException("LootTable cannot be null");
        }
        this.lootTable = lootTable;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    @Override
    public Collection<ItemStack> generateLoot(LootContext context) {
        return generateLoot(context, null);
    }

    @Override
    public Collection<ItemStack> generateLoot(LootContext context, Random random) {
        if (context == null) {
            throw new IllegalArgumentException("LootContext cannot be null");
        }
        
        // Convert our new LootContext to the old one for now
        org.bukkit.loot.LootContext oldContext = convertToOldContext(context);
        
        // Use the old API method (which we've deprecated)
        @SuppressWarnings("deprecation")
        Collection<ItemStack> result = lootTable.populateLoot(random, oldContext);
        
        return result;
    }

    @Override
    public void fillInventory(Inventory inventory, LootContext context) {
        fillInventory(inventory, context, null);
    }

    @Override
    public void fillInventory(Inventory inventory, LootContext context, Random random) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory cannot be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("LootContext cannot be null");
        }
        
        // Convert our new LootContext to the old one for now
        org.bukkit.loot.LootContext oldContext = convertToOldContext(context);
        
        // Use the old API method (which we've deprecated)
        @SuppressWarnings("deprecation")
        Collection<ItemStack> items = lootTable.populateLoot(random, oldContext);
        
        // Fill the inventory with the generated items
        for (ItemStack item : items) {
            if (item != null && !item.getType().isAir()) {
                inventory.addItem(item);
            }
        }
    }

    @Override
    public ItemStack generateSingleLoot(LootContext context) {
        return generateSingleLoot(context, null);
    }

    @Override
    public ItemStack generateSingleLoot(LootContext context, Random random) {
        Collection<ItemStack> loot = generateLoot(context, random);
        return loot.isEmpty() ? null : loot.iterator().next();
    }

    @Override
    public boolean canGenerateWith(LootContext context) {
        if (context == null) {
            throw new IllegalArgumentException("LootContext cannot be null");
        }
        
        // Basic validation - ensure location is set
        return context.getLocation() != null && context.getLocation().getWorld() != null;
    }

    @Override
    public Collection<String> getRequiredContextTypes() {
        // For now, return basic required parameters
        // In a full implementation, this would analyze the loot table structure
        return Collections.singletonList("location");
    }

    @Override
    public Collection<String> getOptionalContextTypes() {
        // For now, return common optional parameters
        // In a full implementation, this would analyze the loot table structure
        return java.util.Arrays.asList("luck", "killer", "killed_entity", "tool", "damage_source", "explosion_radius");
    }
    
    /**
     * Converts the new LootContext to the old format.
     * This is a temporary bridge until the server implementation is updated.
     */
    private org.bukkit.loot.LootContext convertToOldContext(LootContext context) {
        org.bukkit.loot.LootContext.Builder builder = new org.bukkit.loot.LootContext.Builder(context.getLocation());
        
        if (context.getLuck() != 0.0f) {
            builder.luck(context.getLuck());
        }
        
        if (context.getLootedEntity() != null) {
            builder.lootedEntity(context.getLootedEntity());
        }
        
        if (context.getKiller() != null) {
            builder.killer(context.getKiller());
        }
        
        return builder.build();
    }
}
