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
        converterMap.put(InventoryType.CHEST, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.DISPENSER, new CraftTileInventoryConverter.Dispenser());
        converterMap.put(InventoryType.DROPPER, new CraftTileInventoryConverter.Dropper());
        converterMap.put(InventoryType.FURNACE, new CraftTileInventoryConverter.Furnace());
        converterMap.put(InventoryType.WORKBENCH, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ENCHANTING, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BREWING, new CraftTileInventoryConverter.BrewingStand());
        converterMap.put(InventoryType.PLAYER, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.MERCHANT, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ENDER_CHEST, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.ANVIL, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.SMITHING, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BEACON, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.HOPPER, new CraftTileInventoryConverter.Hopper());
        converterMap.put(InventoryType.SHULKER_BOX, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BARREL, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.BLAST_FURNACE, new CraftTileInventoryConverter.BlastFurnace());
        converterMap.put(InventoryType.LECTERN, new CraftTileInventoryConverter.Lectern());
        converterMap.put(InventoryType.SMOKER, new CraftTileInventoryConverter.Smoker());
        converterMap.put(InventoryType.LOOM, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.CARTOGRAPHY, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.GRINDSTONE, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.STONECUTTER, DEFAULT_CONVERTER);
        converterMap.put(InventoryType.SMITHING_NEW, DEFAULT_CONVERTER);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return converterMap.get(type).createInventory(holder, type);
    }

    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        return converterMap.get(type).createInventory(holder, type, title);
    }

    public Inventory createInventory(InventoryHolder holder, int size) {
        return DEFAULT_CONVERTER.createInventory(holder, size);
    }

    public Inventory createInventory(InventoryHolder holder, int size, String title) {
        return DEFAULT_CONVERTER.createInventory(holder, size, title);
    }

    public interface InventoryConverter {

        Inventory createInventory(InventoryHolder holder, InventoryType type);

        Inventory createInventory(InventoryHolder holder, InventoryType type, String title);
    }
}
