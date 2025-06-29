package io.papermc.paper.loot;

import com.google.common.base.Preconditions;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Server-side implementation of LootGenerator.
 */
public class PaperLootGeneratorImpl implements LootGenerator {
    
    private final org.bukkit.loot.LootTable bukkitLootTable;
    private final LootTable nmsLootTable;
    
    public PaperLootGeneratorImpl(org.bukkit.loot.LootTable lootTable) {
        this.bukkitLootTable = Preconditions.checkNotNull(lootTable, "LootTable cannot be null");
        this.nmsLootTable = ((CraftLootTable) lootTable).getHandle();
    }

    @Override
    public org.bukkit.loot.LootTable getLootTable() {
        return bukkitLootTable;
    }

    @Override
    public Collection<ItemStack> generateLoot(LootContext context) {
        return generateLoot(context, null);
    }

    @Override
    public Collection<ItemStack> generateLoot(LootContext context, Random random) {
        Preconditions.checkNotNull(context, "LootContext cannot be null");
        
        if (!canGenerateWith(context)) {
            throw new IllegalArgumentException("LootContext is missing required parameters for this loot table");
        }
        
        // Convert to NMS LootParams
        LootParams nmsContext = ((PaperLootContextImpl) context).toNmsLootParams(nmsLootTable.getParamSet());
        
        // Generate loot using NMS
        List<net.minecraft.world.item.ItemStack> nmsItems = nmsLootTable.getRandomItems(
            nmsContext, 
            random != null ? new RandomSourceWrapper(random) : null
        );
        
        // Convert back to Bukkit items
        Collection<ItemStack> bukkitItems = new ArrayList<>(nmsItems.size());
        for (net.minecraft.world.item.ItemStack nmsItem : nmsItems) {
            if (!nmsItem.isEmpty()) {
                bukkitItems.add(CraftItemStack.asBukkitCopy(nmsItem));
            }
        }
        
        return bukkitItems;
    }

    @Override
    public void fillInventory(Inventory inventory, LootContext context) {
        fillInventory(inventory, context, null);
    }

    @Override
    public void fillInventory(Inventory inventory, LootContext context, Random random) {
        Preconditions.checkNotNull(inventory, "Inventory cannot be null");
        Preconditions.checkNotNull(context, "LootContext cannot be null");
        
        if (!canGenerateWith(context)) {
            throw new IllegalArgumentException("LootContext is missing required parameters for this loot table");
        }
        
        // Convert to NMS LootParams
        LootParams nmsContext = ((PaperLootContextImpl) context).toNmsLootParams(nmsLootTable.getParamSet());
        
        // Fill inventory using NMS
        CraftInventory craftInventory = (CraftInventory) inventory;
        net.minecraft.world.Container nmsContainer = craftInventory.getInventory();
        
        nmsLootTable.fill(
            nmsContainer, 
            nmsContext, 
            random != null ? new RandomSourceWrapper(random) : null,
            true // respectStackCount
        );
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
        Preconditions.checkNotNull(context, "LootContext cannot be null");
        
        // Check basic requirements
        if (context.getLocation() == null || context.getLocation().getWorld() == null) {
            return false;
        }
        
        // For now, assume all contexts are valid
        // In a full implementation, this would check the loot table's parameter requirements
        return true;
    }

    @Override
    public Collection<String> getRequiredContextTypes() {
        // Analyze the NMS loot table's required parameters
        Collection<String> required = new ArrayList<>();
        
        // Always required
        required.add("location");
        
        // Check what the loot table actually requires
        if (nmsLootTable.getParamSet().required().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY)) {
            required.add("killed_entity");
        }
        if (nmsLootTable.getParamSet().required().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY)) {
            required.add("killer");
        }
        if (nmsLootTable.getParamSet().required().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL)) {
            required.add("tool");
        }
        
        return Collections.unmodifiableCollection(required);
    }

    @Override
    public Collection<String> getOptionalContextTypes() {
        // Analyze the NMS loot table's optional parameters
        Collection<String> optional = new ArrayList<>();
        
        // Check what the loot table allows
        if (nmsLootTable.getParamSet().allowed().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.THIS_ENTITY)) {
            optional.add("killed_entity");
        }
        if (nmsLootTable.getParamSet().allowed().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.ATTACKING_ENTITY)) {
            optional.add("killer");
        }
        if (nmsLootTable.getParamSet().allowed().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.TOOL)) {
            optional.add("tool");
        }
        if (nmsLootTable.getParamSet().allowed().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE)) {
            optional.add("damage_source");
        }
        if (nmsLootTable.getParamSet().allowed().contains(net.minecraft.world.level.storage.loot.parameters.LootContextParams.EXPLOSION_RADIUS)) {
            optional.add("explosion_radius");
        }
        
        // Always optional
        optional.add("luck");
        
        return Collections.unmodifiableCollection(optional);
    }
}
