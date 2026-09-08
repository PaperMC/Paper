package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeWanderEvent;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;

public class PaperSlimeWanderEvent extends PaperSlimePathfindEvent implements SlimeWanderEvent {

    public PaperSlimeWanderEvent(final AbstractCubeMob cubeMob) {
        super(cubeMob);
    }
}
