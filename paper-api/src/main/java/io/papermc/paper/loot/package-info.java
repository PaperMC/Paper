/**
 * Enhanced loot table API that provides safe and comprehensive loot generation capabilities.
 * <p>
 * This package replaces the broken {@link org.bukkit.loot} API with a new design that:
 * <ul>
 *   <li>Properly supports all Minecraft loot context parameters</li>
 *   <li>Provides safe builders for creating loot contexts</li>
 *   <li>Offers comprehensive loot generation methods</li>
 *   <li>Is testable and extensible for plugins</li>
 *   <li>Follows Bukkit/Paper API design patterns</li>
 * </ul>
 * 
 * <h3>Usage Examples</h3>
 * 
 * <h4>Basic Loot Generation</h4>
 * <pre>{@code
 * // Get a loot table
 * LootTable lootTable = Bukkit.getLootTable(NamespacedKey.minecraft("chests/simple_dungeon"));
 * 
 * // Create a loot context
 * LootContext context = LootContextBuilder.create(location)
 *     .luck(2.0f)
 *     .build();
 * 
 * // Generate loot
 * LootGenerator generator = LootTableManager.getInstance().createGenerator(lootTable);
 * Collection<ItemStack> loot = generator.generateLoot(context);
 * }</pre>
 * 
 * <h4>Entity Death Loot</h4>
 * <pre>{@code
 * // Create context for entity death
 * LootContext context = LootContextBuilder.create(entity.getLocation())
 *     .lootedEntity(entity)
 *     .killer(player)
 *     .tool(player.getInventory().getItemInMainHand())
 *     .damageSource(NamespacedKey.minecraft("player"))
 *     .build();
 * 
 * // Generate loot
 * LootGenerator generator = LootTableManager.getInstance().createGenerator(entityLootTable);
 * Collection<ItemStack> drops = generator.generateLoot(context);
 * }</pre>
 * 
 * <h4>Block Breaking with Tool</h4>
 * <pre>{@code
 * // Create context for block breaking
 * LootContext context = LootContextBuilder.create(block.getLocation())
 *     .tool(player.getInventory().getItemInMainHand())
 *     .killer(player)
 *     .build();
 * 
 * // Fill inventory directly
 * LootGenerator generator = LootTableManager.getInstance().createGenerator(blockLootTable);
 * generator.fillInventory(chest.getInventory(), context);
 * }</pre>
 * 
 * @since 1.21.4
 */
@org.jetbrains.annotations.ApiStatus.Experimental
package io.papermc.paper.loot;
