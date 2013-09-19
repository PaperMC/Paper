package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityLeash extends EntityHanging {

    public EntityLeash(World world) {
        super(world);
    }

    public EntityLeash(World world, int i, int j, int k) {
        super(world, i, j, k, 0);
        this.setPosition((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D);
    }

    protected void a() {
        super.a();
    }

    public void setDirection(int i) {}

    public int d() {
        return 9;
    }

    public int e() {
        return 9;
    }

    public void b(Entity entity) {}

    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {}

    public void a(NBTTagCompound nbttagcompound) {}

    public boolean c(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.aZ();
        boolean flag = false;
        double d0;
        List list;
        Iterator iterator;
        EntityInsentient entityinsentient;

        if (itemstack != null && itemstack.id == Item.LEASH.id && !this.world.isStatic) {
            d0 = 7.0D;
            list = this.world.a(EntityInsentient.class, AxisAlignedBB.a().a(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + d0, this.locZ + d0));
            if (list != null) {
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityinsentient = (EntityInsentient) iterator.next();
                    if (entityinsentient.bH() && entityinsentient.getLeashHolder() == entityhuman) {
                        // CraftBukkit start
                        if (CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, this, entityhuman).isCancelled()) {
                            ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet39AttachEntity(1, entityinsentient, entityinsentient.getLeashHolder()));
                            continue;
                        }
                        // CraftBukkit end
                        entityinsentient.setLeashHolder(this, true);
                        flag = true;
                    }
                }
            }
        }

        if (!this.world.isStatic && !flag) {
            // CraftBukkit start - Move below
            // this.die();
            boolean die = true;
            // CraftBukkit end
            if (true || entityhuman.abilities.canInstantlyBuild) { // CraftBukkit - Process for non-creative as well
                d0 = 7.0D;
                list = this.world.a(EntityInsentient.class, AxisAlignedBB.a().a(this.locX - d0, this.locY - d0, this.locZ - d0, this.locX + d0, this.locY + d0, this.locZ + d0));
                if (list != null) {
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityinsentient = (EntityInsentient) iterator.next();
                        if (entityinsentient.bH() && entityinsentient.getLeashHolder() == this) {
                            // CraftBukkit start
                            if (CraftEventFactory.callPlayerUnleashEntityEvent(entityinsentient, entityhuman).isCancelled()) {
                                die = false;
                                continue;
                            }
                            entityinsentient.unleash(true, !entityhuman.abilities.canInstantlyBuild); // false -> survival mode boolean
                            // CraftBukkit end
                        }
                    }
                }
            }
            // CraftBukkit start
            if (die) {
                this.die();
            }
            // CraftBukkit end
        }

        return true;
    }

    public boolean survives() {
        int i = this.world.getTypeId(this.x, this.y, this.z);

        return Block.byId[i] != null && Block.byId[i].d() == 11;
    }

    public static EntityLeash a(World world, int i, int j, int k) {
        EntityLeash entityleash = new EntityLeash(world, i, j, k);

        entityleash.p = true;
        world.addEntity(entityleash);
        return entityleash;
    }

    public static EntityLeash b(World world, int i, int j, int k) {
        List list = world.a(EntityLeash.class, AxisAlignedBB.a().a((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D));
        Object object = null;

        if (list != null) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLeash entityleash = (EntityLeash) iterator.next();

                if (entityleash.x == i && entityleash.y == j && entityleash.z == k) {
                    return entityleash;
                }
            }
        }

        return null;
    }
}
