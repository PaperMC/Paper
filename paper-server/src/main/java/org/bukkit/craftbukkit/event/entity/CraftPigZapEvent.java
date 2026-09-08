package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.event.entity.PaperEntityZapEvent;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.PigZapEvent;

@Deprecated(since = "26.2")
public class CraftPigZapEvent extends PaperEntityZapEvent implements PigZapEvent {

    public CraftPigZapEvent(final net.minecraft.world.entity.animal.pig.Pig pig, final LightningBolt bolt, final ZombifiedPiglin zombifiedPiglin) {
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
