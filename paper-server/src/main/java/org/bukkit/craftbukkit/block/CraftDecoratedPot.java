package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.DecoratedPot;
import org.bukkit.craftbukkit.inventory.CraftInventoryDecoratedPot;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.inventory.DecoratedPotInventory;

public class CraftDecoratedPot extends CraftBlockEntityState<DecoratedPotBlockEntity> implements DecoratedPot {

    public CraftDecoratedPot(World world, DecoratedPotBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftDecoratedPot(CraftDecoratedPot state, Location location) {
        super(state, location);
    }

    @Override
    public DecoratedPotInventory getSnapshotInventory() {
        return new CraftInventoryDecoratedPot(this.getSnapshot());
    }

    @Override
    public DecoratedPotInventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryDecoratedPot(this.getBlockEntity());
    }

    // Paper start - expose loot table
    @Override
    public void setLootTable(final org.bukkit.loot.LootTable table) {
        this.setLootTable(table, this.getSeed());
    }

    @Override
    public void setLootTable(org.bukkit.loot.LootTable table, long seed) {
        this.getSnapshot().setLootTable(org.bukkit.craftbukkit.CraftLootTable.bukkitToMinecraft(table), seed);
    }

    @Override
    public org.bukkit.loot.LootTable getLootTable() {
        return org.bukkit.craftbukkit.CraftLootTable.minecraftToBukkit(this.getSnapshot().getLootTable());
    }

    @Override
    public void setSeed(final long seed) {
        this.getSnapshot().setLootTableSeed(seed);
    }

    @Override
    public long getSeed() {
        return this.getSnapshot().getLootTableSeed();
    }
    // Paper end - expose loot table

    @Override
    public void setSherd(Side face, Material sherd) {
        Preconditions.checkArgument(face != null, "face must not be null");
        Preconditions.checkArgument(sherd == null || sherd == Material.BRICK || Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(sherd), "sherd is not a valid sherd material: %s", sherd);

        Optional<Item> sherdItem = (sherd != null) ? Optional.of(CraftItemType.bukkitToMinecraft(sherd)) : Optional.of(Items.BRICK);
        PotDecorations decorations = this.getSnapshot().getDecorations();

        switch (face) {
            case BACK -> this.getSnapshot().decorations = new PotDecorations(sherdItem, decorations.left(), decorations.right(), decorations.front());
            case LEFT -> this.getSnapshot().decorations = new PotDecorations(decorations.back(), sherdItem, decorations.right(), decorations.front());
            case RIGHT -> this.getSnapshot().decorations = new PotDecorations(decorations.back(), decorations.left(), sherdItem, decorations.front());
            case FRONT -> this.getSnapshot().decorations = new PotDecorations(decorations.back(), decorations.left(), decorations.right(), sherdItem);
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        }
    }

    @Override
    public Material getSherd(Side face) {
        Preconditions.checkArgument(face != null, "face must not be null");

        PotDecorations decorations = this.getSnapshot().getDecorations();
        Optional<Item> sherdItem = switch (face) {
            case BACK -> decorations.back();
            case LEFT -> decorations.left();
            case RIGHT -> decorations.right();
            case FRONT -> decorations.front();
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        };

        return CraftItemType.minecraftToBukkit(sherdItem.orElse(Items.BRICK));
    }

    @Override
    public Map<Side, Material> getSherds() {
        PotDecorations decorations = this.getSnapshot().getDecorations();

        Map<Side, Material> sherds = new EnumMap<>(Side.class);
        sherds.put(Side.BACK, CraftItemType.minecraftToBukkit(decorations.back().orElse(Items.BRICK)));
        sherds.put(Side.LEFT, CraftItemType.minecraftToBukkit(decorations.left().orElse(Items.BRICK)));
        sherds.put(Side.RIGHT, CraftItemType.minecraftToBukkit(decorations.right().orElse(Items.BRICK)));
        sherds.put(Side.FRONT, CraftItemType.minecraftToBukkit(decorations.front().orElse(Items.BRICK)));
        return sherds;
    }

    @Override
    public List<Material> getShards() {
        return this.getSnapshot().getDecorations().ordered().stream().map(CraftItemType::minecraftToBukkit).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CraftDecoratedPot copy() {
        return new CraftDecoratedPot(this, null);
    }

    @Override
    public CraftDecoratedPot copy(Location location) {
        return new CraftDecoratedPot(this, location);
    }
}
