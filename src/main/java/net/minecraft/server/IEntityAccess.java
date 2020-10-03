package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public interface IEntityAccess {

    List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate);

    <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate);

    default <T extends Entity> List<T> b(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        return this.a(oclass, axisalignedbb, predicate);
    }

    List<? extends EntityHuman> getPlayers();

    default List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb) {
        return this.getEntities(entity, axisalignedbb, IEntitySelector.g);
    }

    default boolean a(@Nullable Entity entity, VoxelShape voxelshape) {
        if (voxelshape.isEmpty()) {
            return true;
        } else {
            Iterator iterator = this.getEntities(entity, voxelshape.getBoundingBox()).iterator();

            Entity entity1;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entity1 = (Entity) iterator.next();
            } while (entity1.dead || !entity1.i || entity != null && entity1.isSameVehicle(entity) || !VoxelShapes.c(voxelshape, VoxelShapes.a(entity1.getBoundingBox()), OperatorBoolean.AND));

            return false;
        }
    }

    default <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.a(oclass, axisalignedbb, IEntitySelector.g);
    }

    default <T extends Entity> List<T> b(Class<? extends T> oclass, AxisAlignedBB axisalignedbb) {
        return this.b(oclass, axisalignedbb, IEntitySelector.g);
    }

    default Stream<VoxelShape> c(@Nullable Entity entity, AxisAlignedBB axisalignedbb, Predicate<Entity> predicate) {
        if (axisalignedbb.a() < 1.0E-7D) {
            return Stream.empty();
        } else {
            AxisAlignedBB axisalignedbb1 = axisalignedbb.g(1.0E-7D);

            return this.getEntities(entity, axisalignedbb1, predicate.and((entity1) -> {
                boolean flag;

                if (entity1.getBoundingBox().c(axisalignedbb1)) {
                    label25:
                    {
                        if (entity == null) {
                            if (!entity1.aY()) {
                                break label25;
                            }
                        } else if (!entity.j(entity1)) {
                            break label25;
                        }

                        flag = true;
                        return flag;
                    }
                }

                flag = false;
                return flag;
            })).stream().map(Entity::getBoundingBox).map(VoxelShapes::a);
        }
    }

    @Nullable
    default EntityHuman a(double d0, double d1, double d2, double d3, @Nullable Predicate<Entity> predicate) {
        double d4 = -1.0D;
        EntityHuman entityhuman = null;
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman1 = (EntityHuman) iterator.next();

            if (predicate == null || predicate.test(entityhuman1)) {
                double d5 = entityhuman1.h(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    @Nullable
    default EntityHuman findNearbyPlayer(Entity entity, double d0) {
        return this.a(entity.locX(), entity.locY(), entity.locZ(), d0, false);
    }

    @Nullable
    default EntityHuman a(double d0, double d1, double d2, double d3, boolean flag) {
        Predicate<Entity> predicate = flag ? IEntitySelector.e : IEntitySelector.g;

        return this.a(d0, d1, d2, d3, predicate);
    }

    default boolean isPlayerNearby(double d0, double d1, double d2, double d3) {
        Iterator iterator = this.getPlayers().iterator();

        double d4;

        do {
            EntityHuman entityhuman;

            do {
                do {
                    if (!iterator.hasNext()) {
                        return false;
                    }

                    entityhuman = (EntityHuman) iterator.next();
                } while (!IEntitySelector.g.test(entityhuman));
            } while (!IEntitySelector.b.test(entityhuman));

            d4 = entityhuman.h(d0, d1, d2);
        } while (d3 >= 0.0D && d4 >= d3 * d3);

        return true;
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, entityliving, entityliving.locX(), entityliving.locY(), entityliving.locZ());
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, double d0, double d1, double d2) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, entityliving, d0, d1, d2);
    }

    @Nullable
    default EntityHuman a(PathfinderTargetCondition pathfindertargetcondition, double d0, double d1, double d2) {
        return (EntityHuman) this.a(this.getPlayers(), pathfindertargetcondition, (EntityLiving) null, d0, d1, d2);
    }

    @Nullable
    default <T extends EntityLiving> T a(Class<? extends T> oclass, PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2, AxisAlignedBB axisalignedbb) {
        return this.a(this.a(oclass, axisalignedbb, (Predicate) null), pathfindertargetcondition, entityliving, d0, d1, d2);
    }

    @Nullable
    default <T extends EntityLiving> T b(Class<? extends T> oclass, PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2, AxisAlignedBB axisalignedbb) {
        return this.a(this.b(oclass, axisalignedbb, (Predicate) null), pathfindertargetcondition, entityliving, d0, d1, d2);
    }

    @Nullable
    default <T extends EntityLiving> T a(List<? extends T> list, PathfinderTargetCondition pathfindertargetcondition, @Nullable EntityLiving entityliving, double d0, double d1, double d2) {
        double d3 = -1.0D;
        T t0 = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            T t1 = (EntityLiving) iterator.next();

            if (pathfindertargetcondition.a(entityliving, t1)) {
                double d4 = t1.h(d0, d1, d2);

                if (d3 == -1.0D || d4 < d3) {
                    d3 = d4;
                    t0 = t1;
                }
            }
        }

        return t0;
    }

    default List<EntityHuman> a(PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, AxisAlignedBB axisalignedbb) {
        List<EntityHuman> list = Lists.newArrayList();
        Iterator iterator = this.getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityHuman entityhuman = (EntityHuman) iterator.next();

            if (axisalignedbb.e(entityhuman.locX(), entityhuman.locY(), entityhuman.locZ()) && pathfindertargetcondition.a(entityliving, entityhuman)) {
                list.add(entityhuman);
            }
        }

        return list;
    }

    default <T extends EntityLiving> List<T> a(Class<? extends T> oclass, PathfinderTargetCondition pathfindertargetcondition, EntityLiving entityliving, AxisAlignedBB axisalignedbb) {
        List<T> list = this.a(oclass, axisalignedbb, (Predicate) null);
        List<T> list1 = Lists.newArrayList();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            T t0 = (EntityLiving) iterator.next();

            if (pathfindertargetcondition.a(entityliving, t0)) {
                list1.add(t0);
            }
        }

        return list1;
    }

    @Nullable
    default EntityHuman b(UUID uuid) {
        for (int i = 0; i < this.getPlayers().size(); ++i) {
            EntityHuman entityhuman = (EntityHuman) this.getPlayers().get(i);

            if (uuid.equals(entityhuman.getUniqueID())) {
                return entityhuman;
            }
        }

        return null;
    }
}
