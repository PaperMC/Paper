package io.papermc.paper.event.entity;


import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class GhastShootFireballEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private int explosion_power = 1;


    public GhastShootFireballEvent(@NotNull Entity ghast) {
        super(ghast);
    }

    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public @NotNull int getExplosionPower(){
        return explosion_power;
    }

    public @NotNull void setExplosionPower(@NotNull int power){
        Preconditions.checkArgument((power < 127), "Fatal error! Power can not be greater than 127");
        Preconditions.checkArgument((power > 1), "Fatal error! Power can not be less than 1");

        explosion_power = power;
    }


}
