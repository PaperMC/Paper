package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeSwimEvent;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;

public class PaperSlimeSwimEvent extends PaperSlimeWanderEvent implements SlimeSwimEvent {

    public PaperSlimeSwimEvent(final AbstractCubeMob cubeMob) {
        super(cubeMob);
    }
}
