package net.minecraft.server;

public class ItemMonsterEgg extends Item {

    public ItemMonsterEgg(int i) {
        super(i);
        this.a(true);
        this.a(CreativeModeTab.f);
    }

    public String i(ItemStack itemstack) {
        String s = ("" + LocaleI18n.get(this.getName() + ".name")).trim();
        String s1 = EntityTypes.b(itemstack.getData());

        if (s1 != null) {
            s = s + " " + LocaleI18n.get("entity." + s1 + ".name");
        }

        return s;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (world.isStatic || itemstack.getData() == 48 || itemstack.getData() == 49 || itemstack.getData() == 63 || itemstack.getData() == 64) { // CraftBukkit
            return true;
        } else {
            int i1 = world.getTypeId(i, j, k);

            i += Facing.b[l];
            j += Facing.c[l];
            k += Facing.d[l];
            double d0 = 0.0D;

            if (l == 1 && Block.byId[i1] != null && Block.byId[i1].d() == 11) {
                d0 = 0.5D;
            }

            if (a(world, itemstack.getData(), (double) i + 0.5D, (double) j + d0, (double) k + 0.5D) != null && !entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            return true;
        }
    }

    public static Entity a(World world, int i, double d0, double d1, double d2) {
        if (!EntityTypes.a.containsKey(Integer.valueOf(i))) {
            return null;
        } else {
            Entity entity = null;

            for (int j = 0; j < 1; ++j) {
                entity = EntityTypes.a(i, world);
                if (entity != null && entity instanceof EntityLiving) {
                    EntityLiving entityliving = (EntityLiving) entity;

                    entity.setPositionRotation(d0, d1, d2, MathHelper.g(world.random.nextFloat() * 360.0F), 0.0F);
                    entityliving.az = entityliving.yaw;
                    entityliving.ax = entityliving.yaw;
                    entityliving.bG();
                    world.addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                    entityliving.aO();
                }
            }

            return entity;
        }
    }
}
