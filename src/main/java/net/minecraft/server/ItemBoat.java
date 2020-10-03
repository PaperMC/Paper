package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class ItemBoat extends Item {

    private static final Predicate<Entity> a = IEntitySelector.g.and(Entity::isInteractable);
    private final EntityBoat.EnumBoatType b;

    public ItemBoat(EntityBoat.EnumBoatType entityboat_enumboattype, Item.Info item_info) {
        super(item_info);
        this.b = entityboat_enumboattype;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        MovingObjectPositionBlock movingobjectpositionblock = a(world, entityhuman, RayTrace.FluidCollisionOption.ANY);

        if (movingobjectpositionblock.getType() == MovingObjectPosition.EnumMovingObjectType.MISS) {
            return InteractionResultWrapper.pass(itemstack);
        } else {
            Vec3D vec3d = entityhuman.f(1.0F);
            double d0 = 5.0D;
            List<Entity> list = world.getEntities(entityhuman, entityhuman.getBoundingBox().b(vec3d.a(5.0D)).g(1.0D), ItemBoat.a);

            if (!list.isEmpty()) {
                Vec3D vec3d1 = entityhuman.j(1.0F);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    AxisAlignedBB axisalignedbb = entity.getBoundingBox().g((double) entity.bf());

                    if (axisalignedbb.d(vec3d1)) {
                        return InteractionResultWrapper.pass(itemstack);
                    }
                }
            }

            if (movingobjectpositionblock.getType() == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
                EntityBoat entityboat = new EntityBoat(world, movingobjectpositionblock.getPos().x, movingobjectpositionblock.getPos().y, movingobjectpositionblock.getPos().z);

                entityboat.setType(this.b);
                entityboat.yaw = entityhuman.yaw;
                if (!world.getCubes(entityboat, entityboat.getBoundingBox().g(-0.1D))) {
                    return InteractionResultWrapper.fail(itemstack);
                } else {
                    if (!world.isClientSide) {
                        world.addEntity(entityboat);
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            itemstack.subtract(1);
                        }
                    }

                    entityhuman.b(StatisticList.ITEM_USED.b(this));
                    return InteractionResultWrapper.a(itemstack, world.s_());
                }
            } else {
                return InteractionResultWrapper.pass(itemstack);
            }
        }
    }
}
