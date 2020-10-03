package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import java.util.function.Predicate;
// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class BehaviorAttackTargetForget<E extends EntityInsentient> extends Behavior<E> {

    private final Predicate<EntityLiving> b;

    public BehaviorAttackTargetForget(Predicate<EntityLiving> predicate) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
        this.b = predicate;
    }

    public BehaviorAttackTargetForget() {
        this((entityliving) -> {
            return false;
        });
    }

    protected void a(WorldServer worldserver, E e0, long i) {
        if (a((EntityLiving) e0)) {
            this.d(e0);
        } else if (this.c(e0)) {
            this.d(e0);
        } else if (this.a(e0)) {
            this.d(e0);
        } else if (!IEntitySelector.f.test(this.b(e0))) {
            this.d(e0);
        } else if (this.b.test(this.b(e0))) {
            this.d(e0);
        }
    }

    private boolean a(E e0) {
        return this.b(e0).world != e0.world;
    }

    private EntityLiving b(E e0) {
        return (EntityLiving) e0.getBehaviorController().getMemory(MemoryModuleType.ATTACK_TARGET).get();
    }

    private static <E extends EntityLiving> boolean a(E e0) {
        Optional<Long> optional = e0.getBehaviorController().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

        return optional.isPresent() && e0.world.getTime() - (Long) optional.get() > 200L;
    }

    private boolean c(E e0) {
        Optional<EntityLiving> optional = e0.getBehaviorController().getMemory(MemoryModuleType.ATTACK_TARGET);

        return optional.isPresent() && !((EntityLiving) optional.get()).isAlive();
    }

    private void d(E e0) {
        // CraftBukkit start
        EntityLiving old = e0.getBehaviorController().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
        EntityTargetEvent event = CraftEventFactory.callEntityTargetLivingEvent(e0, null, (old != null && !old.isAlive()) ? EntityTargetEvent.TargetReason.TARGET_DIED : EntityTargetEvent.TargetReason.FORGOT_TARGET);
        if (event.isCancelled()) {
            return;
        }
        if (event.getTarget() != null) {
            e0.getBehaviorController().setMemory(MemoryModuleType.ATTACK_TARGET, ((CraftLivingEntity) event.getTarget()).getHandle());
            return;
        }
        // CraftBukkit end
        e0.getBehaviorController().removeMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
