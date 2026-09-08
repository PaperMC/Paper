package io.papermc.paper.event.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.end.EnderDragonFight;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.boss.CraftDragonBattle;
import org.bukkit.craftbukkit.event.block.CraftBlockFormEvent;
import org.bukkit.event.HandlerList;

public class PaperDragonEggFormEvent extends CraftBlockFormEvent implements DragonEggFormEvent {

    private final DragonBattle dragonBattle;

    public PaperDragonEggFormEvent(final Block block, final BlockState newState, final DragonBattle dragonBattle) {
        super(block, newState);
        this.dragonBattle = dragonBattle;
    }

    public PaperDragonEggFormEvent(final Level level, final BlockPos pos, final EnderDragonFight dragonFight) {
        final Block block = CraftBlock.at(level, pos);
        final CraftBlockState newState = CraftBlockStates.snapshotOfSimpleBlock(block, Blocks.DRAGON_EGG.defaultBlockState());
        this(block, newState, new CraftDragonBattle(dragonFight));
    }

    @Override
    public DragonBattle getDragonBattle() {
        return this.dragonBattle;
    }

    @Override
    public HandlerList getHandlers() {
        return DragonEggFormEvent.getHandlerList();
    }
}
