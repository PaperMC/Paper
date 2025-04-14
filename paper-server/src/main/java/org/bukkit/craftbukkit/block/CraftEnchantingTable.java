package org.bukkit.craftbukkit.block;

import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.EnchantingTable;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftEnchantingTable extends CraftBlockEntityState<EnchantingTableBlockEntity> implements EnchantingTable {

    public CraftEnchantingTable(World world, EnchantingTableBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftEnchantingTable(CraftEnchantingTable state, Location location) {
        super(state, location);
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        final EnchantingTableBlockEntity enchantingTable = this.getSnapshot();
        return enchantingTable.hasCustomName() ? io.papermc.paper.adventure.PaperAdventure.asAdventure(enchantingTable.getCustomName()) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }

    @Override
    public String getCustomName() {
        EnchantingTableBlockEntity enchant = this.getSnapshot();
        return enchant.hasCustomName() ? CraftChatMessage.fromComponent(enchant.getCustomName()) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public void applyTo(EnchantingTableBlockEntity blockEntity) {
        super.applyTo(blockEntity);

        if (!this.getSnapshot().hasCustomName()) {
            blockEntity.setCustomName(null);
        }
    }

    @Override
    public CraftEnchantingTable copy() {
        return new CraftEnchantingTable(this, null);
    }

    @Override
    public CraftEnchantingTable copy(Location location) {
        return new CraftEnchantingTable(this, location);
    }
}
