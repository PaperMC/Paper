package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

public class BlockPressurePlateBinary extends BlockPressurePlateAbstract {

    private EnumMobType a;

    protected BlockPressurePlateBinary(int i, String s, Material material, EnumMobType enummobtype) {
        super(i, s, material);
        this.a = enummobtype;
    }

    protected int d(int i) {
        return i > 0 ? 1 : 0;
    }

    protected int c(int i) {
        return i == 1 ? 15 : 0;
    }

    protected int e(World world, int i, int j, int k) {
        List list = null;

        if (this.a == EnumMobType.EVERYTHING) {
            list = world.getEntities((Entity) null, this.a(i, j, k));
        }

        if (this.a == EnumMobType.MOBS) {
            list = world.a(EntityLiving.class, this.a(i, j, k));
        }

        if (this.a == EnumMobType.PLAYERS) {
            list = world.a(EntityHuman.class, this.a(i, j, k));
        }

        if (list != null && !list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                // CraftBukkit start - Call interact event when turning on a pressure plate
                if (this.c(world.getData(i, j, k)) == 0) {
                    org.bukkit.World bworld = world.getWorld();
                    org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
                    org.bukkit.event.Cancellable cancellable;

                    if (entity instanceof EntityHuman) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, i, j, k, -1, null);
                    } else {
                        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(i, j, k));
                        manager.callEvent((EntityInteractEvent) cancellable);
                    }

                    // We only want to block turning the plate on if all events are cancelled
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                }
                // CraftBukkit end

                if (!entity.au()) {
                    return 15;
                }
            }
        }

        return 0;
    }
}
