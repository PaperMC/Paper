package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.boss.DragonBattle;
import org.bukkit.craftbukkit.event.block.CraftBlockFormEvent;
import org.bukkit.event.HandlerList;

public class PaperDragonEggFormEvent extends CraftBlockFormEvent implements DragonEggFormEvent {

    private final DragonBattle dragonBattle;

    public PaperDragonEggFormEvent(final Block block, final BlockState newState, final DragonBattle dragonBattle) {
        super(block, newState);
        this.dragonBattle = dragonBattle;
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
