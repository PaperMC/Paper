package io.papermc.paper.event.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.CopperGolem;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Specialization of {@link ItemTransportingEntityValidateTargetEvent} only
 * called for {@link CopperGolem}s. This event may be more convenient for
 * certain use cases, but if capturing all interactions is desired
 * (i.e. for a protection plugin), the parent event should be used instead.
 */
@NullMarked
public class CopperGolemValidateTargetEvent extends ItemTransportingEntityValidateTargetEvent {

    @ApiStatus.Internal
    public CopperGolemValidateTargetEvent(
        final CopperGolem copperGolem,
        final Block block
    ) {
        super(copperGolem, block);
    }

    @Override
    public CopperGolem getEntity() {
        return (CopperGolem) super.getEntity();
    }
}
