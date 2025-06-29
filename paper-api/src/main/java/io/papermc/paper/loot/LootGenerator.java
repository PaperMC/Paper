package io.papermc.paper.loot;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import java.util.Collection;
import java.util.Random;

/**
 * Enhanced loot generation interface that provides safe and comprehensive
 * loot table generation capabilities.
 * <p>
 * This interface replaces the problematic methods in {@link LootTable} and
 * provides better support for all types of loot generation with proper
 * context handling.
 * 
 * @since 1.21.4
 */
public interface LootGenerator {

    /**
     * Gets the underlying loot table.
     * 
     * @return the loot table
     */
    LootTable getLootTable();

    /**
     * Generates loot items using the provided context.
     * This method replaces {@link LootTable#populateLoot(Random, org.bukkit.loot.LootContext)}
     * with better context support.
     * 
     * @param context the loot context
     * @return a collection of generated items
     */
    Collection<ItemStack> generateLoot(LootContext context);

    /**
     * Generates loot items using the provided context and random source.
     * 
     * @param context the loot context
     * @param random the random source, or null to use default
     * @return a collection of generated items
     */
    Collection<ItemStack> generateLoot(LootContext context, Random random);

    /**
     * Fills an inventory with generated loot.
     * This method replaces {@link LootTable#fillInventory(Inventory, Random, org.bukkit.loot.LootContext)}
     * with better context support.
     * 
     * @param inventory the inventory to fill
     * @param context the loot context
     */
    void fillInventory(Inventory inventory, LootContext context);

    /**
     * Fills an inventory with generated loot using the provided random source.
     * 
     * @param inventory the inventory to fill
     * @param context the loot context
     * @param random the random source, or null to use default
     */
    void fillInventory(Inventory inventory, LootContext context, Random random);

    /**
     * Generates a single loot roll.
     * Useful for loot tables that should only generate one item.
     * 
     * @param context the loot context
     * @return the generated item, or null if no loot was generated
     */
    ItemStack generateSingleLoot(LootContext context);

    /**
     * Generates a single loot roll using the provided random source.
     * 
     * @param context the loot context
     * @param random the random source, or null to use default
     * @return the generated item, or null if no loot was generated
     */
    ItemStack generateSingleLoot(LootContext context, Random random);

    /**
     * Checks if this loot table can generate loot with the given context.
     * This validates that all required context parameters are present.
     * 
     * @param context the loot context to validate
     * @return true if the context is valid for this loot table
     */
    boolean canGenerateWith(LootContext context);

    /**
     * Gets the required context parameter types for this loot table.
     * This can be used to determine what parameters need to be set
     * in the loot context.
     * 
     * @return a collection of required parameter types
     */
    Collection<String> getRequiredContextTypes();

    /**
     * Gets the optional context parameter types for this loot table.
     * These parameters may affect loot generation but are not required.
     * 
     * @return a collection of optional parameter types
     */
    Collection<String> getOptionalContextTypes();
}
