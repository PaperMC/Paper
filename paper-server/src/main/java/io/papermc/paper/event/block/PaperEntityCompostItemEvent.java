package io.papermc.paper.event.block;

import io.papermc.paper.event.entity.EntityCompostItemEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.bukkit.entity.Entity;

public class PaperEntityCompostItemEvent extends PaperCompostItemEvent implements EntityCompostItemEvent {

    private final Entity entity;
    private boolean cancelled;

    public PaperEntityCompostItemEvent(
        final net.minecraft.world.entity.Entity entity, final LevelAccessor level, final BlockPos pos, final ItemStack item, final boolean willRaiseLevel
    ) {
        super(level, pos, item, willRaiseLevel);
        this.entity = entity.getBukkitEntity();
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
