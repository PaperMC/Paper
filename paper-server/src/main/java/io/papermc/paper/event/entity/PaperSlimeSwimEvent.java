package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeSwimEvent;
import org.bukkit.entity.AbstractCubeMob;

public class PaperSlimeSwimEvent extends PaperSlimeWanderEvent implements SlimeSwimEvent {

    public PaperSlimeSwimEvent(final AbstractCubeMob cubeMob) {
        super(cubeMob);
    }
}
