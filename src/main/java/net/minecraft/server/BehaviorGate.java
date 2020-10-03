package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BehaviorGate<E extends EntityLiving> extends Behavior<E> {

    private final Set<MemoryModuleType<?>> b;
    private final BehaviorGate.Order c;
    private final BehaviorGate.Execution d;
    private final WeightedList<Behavior<? super E>> e = new WeightedList<>();

    public BehaviorGate(Map<MemoryModuleType<?>, MemoryStatus> map, Set<MemoryModuleType<?>> set, BehaviorGate.Order behaviorgate_order, BehaviorGate.Execution behaviorgate_execution, List<Pair<Behavior<? super E>, Integer>> list) {
        super(map);
        this.b = set;
        this.c = behaviorgate_order;
        this.d = behaviorgate_execution;
        list.forEach((pair) -> {
            this.e.a(pair.getFirst(), (Integer) pair.getSecond());
        });
    }

    @Override
    protected boolean b(WorldServer worldserver, E e0, long i) {
        return this.e.c().filter((behavior) -> {
            return behavior.a() == Behavior.Status.RUNNING;
        }).anyMatch((behavior) -> {
            return behavior.b(worldserver, e0, i);
        });
    }

    @Override
    protected boolean a(long i) {
        return false;
    }

    @Override
    protected void a(WorldServer worldserver, E e0, long i) {
        this.c.a(this.e);
        this.d.a(this.e, worldserver, e0, i);
    }

    @Override
    protected void d(WorldServer worldserver, E e0, long i) {
        this.e.c().filter((behavior) -> {
            return behavior.a() == Behavior.Status.RUNNING;
        }).forEach((behavior) -> {
            behavior.f(worldserver, e0, i);
        });
    }

    @Override
    protected void c(WorldServer worldserver, E e0, long i) {
        this.e.c().filter((behavior) -> {
            return behavior.a() == Behavior.Status.RUNNING;
        }).forEach((behavior) -> {
            behavior.g(worldserver, e0, i);
        });
        Set set = this.b;
        BehaviorController behaviorcontroller = e0.getBehaviorController();

        set.forEach(behaviorcontroller::removeMemory);
    }

    @Override
    public String toString() {
        Set<? extends Behavior<? super E>> set = (Set) this.e.c().filter((behavior) -> {
            return behavior.a() == Behavior.Status.RUNNING;
        }).collect(Collectors.toSet());

        return "(" + this.getClass().getSimpleName() + "): " + set;
    }

    static enum Execution {

        RUN_ONE {
            @Override
            public <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i) {
                weightedlist.c().filter((behavior) -> {
                    return behavior.a() == Behavior.Status.STOPPED;
                }).filter((behavior) -> {
                    return behavior.e(worldserver, e0, i);
                }).findFirst();
            }
        },
        TRY_ALL {
            @Override
            public <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i) {
                weightedlist.c().filter((behavior) -> {
                    return behavior.a() == Behavior.Status.STOPPED;
                }).forEach((behavior) -> {
                    behavior.e(worldserver, e0, i);
                });
            }
        };

        private Execution() {}

        public abstract <E extends EntityLiving> void a(WeightedList<Behavior<? super E>> weightedlist, WorldServer worldserver, E e0, long i);
    }

    static enum Order {

        ORDERED((weightedlist) -> {
        }), SHUFFLED(WeightedList::a);

        private final Consumer<WeightedList<?>> c;

        private Order(Consumer consumer) {
            this.c = consumer;
        }

        public void a(WeightedList<?> weightedlist) {
            this.c.accept(weightedlist);
        }
    }
}
