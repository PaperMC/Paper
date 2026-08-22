package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeTargetLivingEntityEvent;
import org.bukkit.entity.AbstractCubeMob;
import org.bukkit.entity.LivingEntity;

public class PaperSlimeTargetLivingEntityEvent extends PaperSlimePathfindEvent implements SlimeTargetLivingEntityEvent {

    private final LivingEntity target;

    public PaperSlimeTargetLivingEntityEvent(final AbstractCubeMob cubeMob, final LivingEntity target) {
        super(cubeMob);
        this.target = target;
    }

    @Override
    public LivingEntity getTarget() {
        return this.target;
    }
}
