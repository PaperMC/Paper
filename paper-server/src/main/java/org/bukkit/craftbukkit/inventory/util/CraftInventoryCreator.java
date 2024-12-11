package org.bukkit.craftbukkit.inventory.util;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class CraftInventoryCreator {

    public static final CraftInventoryCreator INSTANCE = new CraftInventoryCreator();
    //
    private final CraftCustomInventoryConverter DEFAULT_CONVERTER = new CraftCustomInventoryConverter();
    private final Map<InventoryType, InventoryConverter> converterMap = new HashMap<>();

    private CraftInventoryCreator() {
        this.converterMap.put(InventoryType.CHEST, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.DISPENSER, new CraftTileInventoryConverter.Dispenser());
        this.converterMap.put(InventoryType.DROPPER, new CraftTileInventoryConverter.Dropper());
        this.converterMap.put(InventoryType.FURNACE, new CraftTileInventoryConverter.Furnace());
        this.converterMap.put(InventoryType.WORKBENCH, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.ENCHANTING, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.BREWING, new CraftTileInventoryConverter.BrewingStand());
        this.converterMap.put(InventoryType.PLAYER, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.MERCHANT, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.ENDER_CHEST, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.ANVIL, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.SMITHING, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.BEACON, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.HOPPER, new CraftTileInventoryConverter.Hopper());
        this.converterMap.put(InventoryType.SHULKER_BOX, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.BARREL, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.BLAST_FURNACE, new CraftTileInventoryConverter.BlastFurnace());
        this.converterMap.put(InventoryType.LECTERN, new CraftTileInventoryConverter.Lectern());
        this.converterMap.put(InventoryType.SMOKER, new CraftTileInventoryConverter.Smoker());
        this.converterMap.put(InventoryType.LOOM, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.CARTOGRAPHY, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.GRINDSTONE, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.STONECUTTER, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.SMITHING_NEW, this.DEFAULT_CONVERTER);
        this.converterMap.put(InventoryType.CRAFTER, new CraftTileInventoryConverter.Crafter());
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return this.converterMap.get(type).createInventory(holder, type);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        return this.converterMap.get(type).createInventory(holder, type, title);
    }

    public Inventory createInventory(InventoryHolder holder, int size) {
        return this.DEFAULT_CONVERTER.createInventory(holder, size);
    }

    public Inventory createInventory(InventoryHolder holder, int size, String title) {
        return this.DEFAULT_CONVERTER.createInventory(holder, size, title);
    }

    public interface InventoryConverter {

        Inventory createInventory(InventoryHolder holder, InventoryType type);

        Inventory createInventory(InventoryHolder holder, InventoryType type, String title);
    }
}
