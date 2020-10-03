package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class BehaviorAttackTargetSet<E extends EntityInsentient> extends Behavior<E> {

    private final Predicate<E> b;
    private final Function<E, Optional<? extends EntityLiving>> c;

    public BehaviorAttackTargetSet(Predicate<E> predicate, Function<E, Optional<? extends EntityLiving>> function) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.b = predicate;
        this.c = function;
    }

    public BehaviorAttackTargetSet(Function<E, Optional<? extends EntityLiving>> function) {
        this((entityinsentient) -> {
            return true;
        }, function);
    }

    protected boolean a(WorldServer worldserver, E e0) {
        if (!this.b.test(e0)) {
            return false;
        } else {
            Optional<? extends EntityLiving> optional = (Optional) this.c.apply(e0);

            return optional.isPresent() && ((EntityLiving) optional.get()).isAlive();
        }
    }

    protected void a(WorldServer worldserver, E e0, long i) {
        (this.c.apply(e0)).ifPresent((entityliving) -> { // CraftBukkit - decompile error
            this.a(e0, entityliving);
        });
    }

    private void a(E e0, EntityLiving entityliving) {
        // CraftBukkit start
        EntityTargetEvent event = CraftEventFactory.callEntityTargetLivingEvent(e0, entityliving, (entityliving instanceof EntityPlayer) ? EntityTargetEvent.TargetReason.CLOSEST_PLAYER : EntityTargetEvent.TargetReason.CLOSEST_ENTITY);
        if (event.isCancelled()) {
            return;
        }
        entityliving = (event.getTarget() != null) ? ((CraftLivingEntity) event.getTarget()).getHandle() : null;
        // CraftBukkit end

        e0.getBehaviorController().setMemory(MemoryModuleType.ATTACK_TARGET, entityliving); // CraftBukkit - decompile error
        e0.getBehaviorController().removeMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
