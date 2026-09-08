package io.papermc.paper.event.entity;

import com.destroystokyo.paper.event.entity.EntityZapEvent;
import java.util.Collections;
import net.minecraft.world.entity.LightningBolt;
import org.bukkit.craftbukkit.event.entity.CraftEntityTransformEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.HandlerList;

public class PaperEntityZapEvent extends CraftEntityTransformEvent implements EntityZapEvent {

    private final LightningStrike bolt;

    public PaperEntityZapEvent(final Entity entity, final LightningStrike bolt, final Entity replacementEntity) {
        super(entity, Collections.singletonList(replacementEntity), TransformReason.LIGHTNING);
        this.bolt = bolt;
    }

    public PaperEntityZapEvent(final net.minecraft.world.entity.Entity entity, final LightningBolt bolt, final net.minecraft.world.entity.Entity replacementEntity) {
        super(entity, replacementEntity, TransformReason.LIGHTNING);
        this.bolt = (LightningStrike) bolt.getBukkitEntity();
    }

    @Override
    public LightningStrike getBolt() {
        return this.bolt;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityZapEvent.getHandlerList();
    }
}
