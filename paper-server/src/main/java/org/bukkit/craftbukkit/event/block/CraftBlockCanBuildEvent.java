package org.bukkit.craftbukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class CraftBlockCanBuildEvent extends CraftBlockEvent implements BlockCanBuildEvent {

    private final Player player;
    private final EquipmentSlot hand;
    protected BlockData blockData;
    protected boolean buildable;

    public CraftBlockCanBuildEvent(final Block block, final @Nullable Player player, final BlockData blockData, final boolean canBuild, final EquipmentSlot hand) {
        super(block);
        this.player = player;
        this.buildable = canBuild;
        this.blockData = blockData;
        this.hand = hand;
    }

    @Override
    public @Nullable Player getPlayer() {
        return this.player;
    }

    @Override
    public Material getMaterial() {
        return this.blockData.getMaterial();
    }

    @Override
    public BlockData getBlockData() {
        return this.blockData.clone();
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public boolean isBuildable() {
        return this.buildable;
    }

    @Override
    public void setBuildable(final boolean buildable) {
        this.buildable = buildable;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockCanBuildEvent.getHandlerList();
    }
}
