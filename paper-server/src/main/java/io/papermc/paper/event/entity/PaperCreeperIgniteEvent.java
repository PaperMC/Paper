package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.CreeperIgniteEvent;
import org.bukkit.entity.Creeper;
import org.checkerframework.checker.index.qual.Positive;

public class PaperCreeperIgniteEvent extends PaperEntityIgniteEvent implements CreeperIgniteEvent {

    private boolean ignited;

    public PaperCreeperIgniteEvent(final Creeper creeper, final boolean ignited) {
        super(creeper, creeper.getMaxFuseTicks());
        this.ignited = ignited;
    }

    @Override
    public Creeper getEntity() {
        return (Creeper) this.entity;
    }

    @Override
    public boolean isIgnited() {
        return this.ignited;
    }

    @Override
    public void setIgnited(final boolean ignited) {
        this.ignited = ignited;
    }

    @Override
    public @Positive int getFuseTime() {
        return this.getEntity().getMaxFuseTicks();
    }

    @Override
    public void setFuseTime(final @Positive int ticks) {
        this.getEntity().setMaxFuseTicks(ticks);
    }
}
