package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.HandlerList;

public class PaperExperienceOrbMergeEvent extends CraftEntityEvent implements ExperienceOrbMergeEvent {

    private final ExperienceOrb target;
    private final ExperienceOrb source;

    private boolean cancelled;

    public PaperExperienceOrbMergeEvent(final ExperienceOrb target, final ExperienceOrb source) {
        super(target);
        this.target = target;
        this.source = source;
    }

    public PaperExperienceOrbMergeEvent(
        final net.minecraft.world.entity.ExperienceOrb target, final net.minecraft.world.entity.ExperienceOrb source
    ) {
        this((ExperienceOrb) target.getBukkitEntity(), (ExperienceOrb) source.getBukkitEntity());
    }

    @Override
    public ExperienceOrb getMergeTarget() {
        return this.target;
    }

    @Override
    public ExperienceOrb getMergeSource() {
        return this.source;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return ExperienceOrbMergeEvent.getHandlerList();
    }
}
