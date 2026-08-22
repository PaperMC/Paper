package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.event.entity.PaperEntityZapEvent;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.PigZapEvent;

@Deprecated(since = "26.2")
public class CraftPigZapEvent extends PaperEntityZapEvent implements PigZapEvent {

    public CraftPigZapEvent(final Pig pig, final LightningStrike bolt, final PigZombie zombifiedPiglin) {
        super(pig, bolt, zombifiedPiglin);
    }

    @Override
    public Pig getEntity() {
        return (Pig) this.entity;
    }

    @Override
    public LightningStrike getLightning() {
        return this.getBolt();
    }

    @Override
    public PigZombie getPigZombie() {
        return (PigZombie) this.getReplacementEntity();
    }
}
