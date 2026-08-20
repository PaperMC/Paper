package org.bukkit.craftbukkit.event.block;

import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BellResonateEvent;

public class CraftBellResonateEvent extends CraftBlockEvent implements BellResonateEvent {

    private final List<LivingEntity> resonatedEntities;

    public CraftBellResonateEvent(final Block bell, final List<LivingEntity> resonatedEntities) {
        super(bell);
        this.resonatedEntities = resonatedEntities;
    }

    @Override
    public List<LivingEntity> getResonatedEntities() {
        return this.resonatedEntities;
    }

    @Override
    public HandlerList getHandlers() {
        return BellResonateEvent.getHandlerList();
    }
}
