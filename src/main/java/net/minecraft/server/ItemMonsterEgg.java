package net.minecraft.server;

public class ItemMonsterEgg extends Item {

    public ItemMonsterEgg(int i) {
        super(i);
        this.e(1);
        this.a(true);
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (world.isStatic || itemstack.getData() == 48 || itemstack.getData() == 49 || itemstack.getData() == 63) { // CraftBukkit
            return true;
        } else {
            i += Facing.b[l];
            j += Facing.c[l];
            k += Facing.d[l];
            Entity entity = EntityTypes.a(itemstack.getData(), world);

            if (entity != null && entity instanceof EntityLiving) { // CraftBukkit

                if (!entityhuman.abilities.canInstantlyBuild) {
                    --itemstack.count;
                }

                entity.setPositionRotation((double) i + 0.5D, (double) j, (double) k + 0.5D, 0.0F, 0.0F);
                world.addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
            }

            return true;
        }
    }
}