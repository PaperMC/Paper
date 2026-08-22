package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeWanderEvent;
import org.bukkit.entity.AbstractCubeMob;

public class PaperSlimeWanderEvent extends PaperSlimePathfindEvent implements SlimeWanderEvent {

    public PaperSlimeWanderEvent(final AbstractCubeMob cubeMob) {
        super(cubeMob);
    }
}
