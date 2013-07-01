package net.minecraft.server;

import java.util.List;
import java.util.Random;

import org.bukkit.event.entity.EntityPortalEnterEvent; // CraftBukkit

public class BlockEnderPortal extends BlockContainer {

    public static boolean a;

    protected BlockEnderPortal(int i, Material material) {
        super(i, material);
        this.a(1.0F);
    }

    public TileEntity b(World world) {
        return new TileEntityEnderPortal();
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        float f = 0.0625F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List list, Entity entity) {}

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (entity.vehicle == null && entity.passenger == null && !world.isStatic) {
            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), i, j, k));
            world.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
            entity.b(1);
        }
    }

    public int d() {
        return -1;
    }

    public void onPlace(World world, int i, int j, int k) {
        if (!a) {
            if (world.worldProvider.dimension != 0) {
                world.setAir(i, j, k);
            }
        }
    }
}
