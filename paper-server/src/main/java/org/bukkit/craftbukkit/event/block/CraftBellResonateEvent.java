package org.bukkit.craftbukkit.event.block;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BellResonateEvent;

public class CraftBellResonateEvent extends CraftBlockEvent implements BellResonateEvent {

    private final List<LivingEntity> resonatedEntities;

    public CraftBellResonateEvent(final Block bell, final List<LivingEntity> resonatedEntities) {
        super(bell);
        this.resonatedEntities = resonatedEntities;
    }

    public CraftBellResonateEvent(final Level level, final BlockPos pos, final List<net.minecraft.world.entity.LivingEntity> resonatedEntities) {
        this(
            CraftBlock.at(level, pos),
            MCUtil.mutableTransform(
                resonatedEntities,
                net.minecraft.world.entity.LivingEntity::getBukkitEntity, e -> ((CraftLivingEntity) e).getHandle()
            )
        );
    }

    @Override
    public List<LivingEntity> getResonatedEntities() {
        return this.resonatedEntities;
    }

    @Override
    public HandlerList getHandlers() {
        return BellResonateEvent.getHandlerList();
    }
}
