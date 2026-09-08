package io.papermc.paper.event.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.Creature;
import org.bukkit.event.HandlerList;

public class PaperItemTransportingEntityValidateTargetEvent extends CraftEntityEvent implements ItemTransportingEntityValidateTargetEvent {

    private final Block block;
    private boolean allowed = true;

    public PaperItemTransportingEntityValidateTargetEvent(final Creature entity, final Block block) {
        super(entity);
        this.block = block;
    }

    public PaperItemTransportingEntityValidateTargetEvent(final PathfinderMob entity, final Level level, final BlockPos pos) {
        this((Creature) entity.getBukkitEntity(), CraftBlock.at(level, pos));
    }

    @Override
    public Creature getEntity() {
        return (Creature) this.entity;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public boolean isAllowed() {
        return this.allowed;
    }

    @Override
    public void setAllowed(final boolean allowed) {
        this.allowed = allowed;
    }

    @Override
    public HandlerList getHandlers() {
        return ItemTransportingEntityValidateTargetEvent.getHandlerList();
    }
}
