package org.bukkit.craftbukkit.inventory.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class CraftInventoryCreator {

    public static final CraftInventoryCreator INSTANCE = new CraftInventoryCreator();

    private final CraftCustomInventoryConverter defaultConverter = new CraftCustomInventoryConverter();
    private final Map<InventoryType, InventoryConverter> converterMap = new HashMap<>();

    private CraftInventoryCreator() {
        this.converterMap.put(InventoryType.CHEST, this.defaultConverter);
        this.converterMap.put(InventoryType.DISPENSER, new CraftBlockInventoryConverter.Dispenser());
        this.converterMap.put(InventoryType.DROPPER, new CraftBlockInventoryConverter.Dropper());
        this.converterMap.put(InventoryType.FURNACE, new CraftBlockInventoryConverter.Furnace());
        this.converterMap.put(InventoryType.WORKBENCH, this.defaultConverter);
        this.converterMap.put(InventoryType.ENCHANTING, this.defaultConverter);
        this.converterMap.put(InventoryType.BREWING, new CraftBlockInventoryConverter.BrewingStand());
        this.converterMap.put(InventoryType.PLAYER, this.defaultConverter);
        this.converterMap.put(InventoryType.MERCHANT, this.defaultConverter);
        this.converterMap.put(InventoryType.ENDER_CHEST, this.defaultConverter);
        this.converterMap.put(InventoryType.ANVIL, this.defaultConverter);
        this.converterMap.put(InventoryType.SMITHING, this.defaultConverter);
        this.converterMap.put(InventoryType.BEACON, this.defaultConverter);
        this.converterMap.put(InventoryType.HOPPER, new CraftBlockInventoryConverter.Hopper());
        this.converterMap.put(InventoryType.SHULKER_BOX, this.defaultConverter);
        this.converterMap.put(InventoryType.BARREL, this.defaultConverter);
        this.converterMap.put(InventoryType.BLAST_FURNACE, new CraftBlockInventoryConverter.BlastFurnace());
        this.converterMap.put(InventoryType.LECTERN, new CraftBlockInventoryConverter.Lectern());
        this.converterMap.put(InventoryType.SMOKER, new CraftBlockInventoryConverter.Smoker());
        this.converterMap.put(InventoryType.LOOM, this.defaultConverter);
        this.converterMap.put(InventoryType.CARTOGRAPHY, this.defaultConverter);
        this.converterMap.put(InventoryType.GRINDSTONE, this.defaultConverter);
        this.converterMap.put(InventoryType.STONECUTTER, this.defaultConverter);
        this.converterMap.put(InventoryType.SMITHING_NEW, this.defaultConverter);
        this.converterMap.put(InventoryType.CRAFTER, new CraftBlockInventoryConverter.Crafter());
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return this.converterMap.get(type).createInventory(holder, type);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type, net.kyori.adventure.text.Component title) {
        return this.converterMap.get(type).createInventory(holder, type, title);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        return this.converterMap.get(type).createInventory(holder, type, title);
    }

    public Inventory createInventory(InventoryHolder holder, int size) {
        return this.defaultConverter.createInventory(holder, size);
    }

    public Inventory createInventory(InventoryHolder holder, int size, net.kyori.adventure.text.Component title) {
        return this.defaultConverter.createInventory(holder, size, title);
    }

    public Inventory createInventory(InventoryHolder holder, int size, String title) {
        return this.defaultConverter.createInventory(holder, size, title);
    }

    public interface InventoryConverter {

        Inventory createInventory(InventoryHolder holder, InventoryType type);

        Inventory createInventory(InventoryHolder holder, InventoryType type, net.kyori.adventure.text.Component title);

        Inventory createInventory(InventoryHolder holder, InventoryType type, String title);
    }
}
