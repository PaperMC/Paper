package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.ExperienceOrbMergeEvent;
import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.HandlerList;

public class PaperExperienceOrbMergeEvent extends CraftEntityEvent implements ExperienceOrbMergeEvent {

    private final ExperienceOrb mergeTarget;
    private final ExperienceOrb mergeSource;

    private boolean cancelled;

    public PaperExperienceOrbMergeEvent(final ExperienceOrb mergeTarget, final ExperienceOrb mergeSource) {
        super(mergeTarget);
        this.mergeTarget = mergeTarget;
        this.mergeSource = mergeSource;
    }

    @Override
    public ExperienceOrb getMergeTarget() {
        return this.mergeTarget;
    }

    @Override
    public ExperienceOrb getMergeSource() {
        return this.mergeSource;
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
