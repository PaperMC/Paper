package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.SlimeChangeDirectionEvent;
import org.bukkit.entity.AbstractCubeMob;

public class PaperSlimeChangeDirectionEvent extends PaperSlimePathfindEvent implements SlimeChangeDirectionEvent {

    private float yaw;

    public PaperSlimeChangeDirectionEvent(final AbstractCubeMob cubeMob, final float yaw) {
        super(cubeMob);
        this.yaw = yaw;
    }

    @Override
    public float getNewYaw() {
        return this.yaw;
    }

    @Override
    public void setNewYaw(final float yaw) {
        this.yaw = yaw;
    }
}
