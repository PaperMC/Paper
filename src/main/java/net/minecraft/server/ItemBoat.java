package net.minecraft.server;

import java.util.List;

public class ItemBoat extends Item {

    public ItemBoat(int i) {
        super(i);
        this.maxStackSize = 1;
        this.a(CreativeModeTab.e);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        float f = 1.0F;
        float f1 = entityhuman.lastPitch + (entityhuman.pitch - entityhuman.lastPitch) * f;
        float f2 = entityhuman.lastYaw + (entityhuman.yaw - entityhuman.lastYaw) * f;
        double d0 = entityhuman.lastX + (entityhuman.locX - entityhuman.lastX) * (double) f;
        double d1 = entityhuman.lastY + (entityhuman.locY - entityhuman.lastY) * (double) f + 1.62D - (double) entityhuman.height;
        double d2 = entityhuman.lastZ + (entityhuman.locZ - entityhuman.lastZ) * (double) f;
        Vec3D vec3d = world.getVec3DPool().create(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3D vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        MovingObjectPosition movingobjectposition = world.rayTrace(vec3d, vec3d1, true);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            Vec3D vec3d2 = entityhuman.j(f);
            boolean flag = false;
            float f9 = 1.0F;
            List list = world.getEntities(entityhuman, entityhuman.boundingBox.a(vec3d2.c * d3, vec3d2.d * d3, vec3d2.e * d3).grow((double) f9, (double) f9, (double) f9));

            int i;

            for (i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.L()) {
                    float f10 = entity.Z();
                    AxisAlignedBB axisalignedbb = entity.boundingBox.grow((double) f10, (double) f10, (double) f10);

                    if (axisalignedbb.a(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return itemstack;
            } else {
                if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                    i = movingobjectposition.b;
                    int j = movingobjectposition.c;
                    int k = movingobjectposition.d;

                    // CraftBukkit start - Boat placement
                    org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(entityhuman, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, i, j, k, movingobjectposition.face, itemstack);

                    if (event.isCancelled()) {
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (world.getTypeId(i, j, k) == Block.SNOW.id) {
                        --j;
                    }

                    EntityBoat entityboat = new EntityBoat(world, (double) ((float) i + 0.5F), (double) ((float) j + 1.0F), (double) ((float) k + 0.5F));

                    entityboat.yaw = (float) (((MathHelper.floor((double) (entityhuman.yaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
                    if (!world.getCubes(entityboat, entityboat.boundingBox.grow(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                        return itemstack;
                    }

                    if (!world.isStatic) {
                        world.addEntity(entityboat);
                    }

                    if (!entityhuman.abilities.canInstantlyBuild) {
                        --itemstack.count;
                    }
                }

                return itemstack;
            }
        }
    }
}
