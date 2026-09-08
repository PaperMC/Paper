package io.papermc.paper.event.entity;

import net.minecraft.world.entity.projectile.FishingHook;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.FishHook;
import org.bukkit.event.HandlerList;

public class PaperFishHookStateChangeEvent extends CraftEntityEvent implements FishHookStateChangeEvent {

    private final FishHook.HookState newHookState;

    public PaperFishHookStateChangeEvent(final FishHook entity, final FishHook.HookState newHookState) {
        super(entity);
        this.newHookState = newHookState;
    }

    public PaperFishHookStateChangeEvent(final FishingHook entity, final FishHook.HookState newHookState) {
        this((FishHook) entity.getBukkitEntity(), newHookState);
    }

    @Override
    public FishHook.HookState getNewHookState() {
        return this.newHookState;
    }

    @Override
    public FishHook getEntity() {
        return (FishHook) this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return FishHookStateChangeEvent.getHandlerList();
    }
}
