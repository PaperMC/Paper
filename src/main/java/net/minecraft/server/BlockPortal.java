package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.entity.EntityPortalEnterEvent; // CraftBukkit

public class BlockPortal extends BlockHalfTransparent {

    public static final int[][] a = new int[][] { new int[0], { 3, 1}, { 2, 0}};

    public BlockPortal() {
        super("portal", Material.PORTAL, false);
        this.a(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.worldProvider.d() && world.getGameRules().getBoolean("doMobSpawning") && random.nextInt(2000) < world.difficulty.a()) {
            int l;

            for (l = j; !World.a((IBlockAccess) world, i, l, k) && l > 0; --l) {
                ;
            }

            if (l > 0 && !world.getType(i, l + 1, k).r()) {
                Entity entity = ItemMonsterEgg.a(world, 57, (double) i + 0.5D, (double) l + 1.1D, (double) k + 0.5D);

                if (entity != null) {
                    entity.portalCooldown = entity.ai();
                }
            }
        }
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = b(iblockaccess.getData(i, j, k));

        if (l == 0) {
            if (iblockaccess.getType(i - 1, j, k) != this && iblockaccess.getType(i + 1, j, k) != this) {
                l = 2;
            } else {
                l = 1;
            }

            if (iblockaccess instanceof World && !((World) iblockaccess).isStatic) {
                ((World) iblockaccess).setData(i, j, k, l, 2);
            }
        }

        float f = 0.125F;
        float f1 = 0.125F;

        if (l == 1) {
            f = 0.5F;
        }

        if (l == 2) {
            f1 = 0.5F;
        }

        this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
    }

    public boolean d() {
        return false;
    }

    public boolean e(World world, int i, int j, int k) {
        PortalCreator portalcreator = new PortalCreator(world, i, j, k, 1);
        PortalCreator portalcreator1 = new PortalCreator(world, i, j, k, 2);

        if (portalcreator.b() && PortalCreator.a(portalcreator) == 0) {
            // CraftBukkit start - return portalcreator
            return portalcreator.c();
            // return true;
        } else if (portalcreator1.b() && PortalCreator.a(portalcreator1) == 0) {
            return portalcreator1.c();
            // return true;
            // CraftBukkit end
        } else {
            return false;
        }
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        int l = b(world.getData(i, j, k));
        PortalCreator portalcreator = new PortalCreator(world, i, j, k, 1);
        PortalCreator portalcreator1 = new PortalCreator(world, i, j, k, 2);

        if (l == 1 && (!portalcreator.b() || PortalCreator.a(portalcreator) < PortalCreator.b(portalcreator) * PortalCreator.c(portalcreator))) {
            world.setTypeUpdate(i, j, k, Blocks.AIR);
        } else if (l == 2 && (!portalcreator1.b() || PortalCreator.a(portalcreator1) < PortalCreator.b(portalcreator1) * PortalCreator.c(portalcreator1))) {
            world.setTypeUpdate(i, j, k, Blocks.AIR);
        } else if (l == 0 && !portalcreator.b() && !portalcreator1.b()) {
            world.setTypeUpdate(i, j, k, Blocks.AIR);
        }
    }

    public int a(Random random) {
        return 0;
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (entity.vehicle == null && entity.passenger == null) {
            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), i, j, k));
            world.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end

            entity.ah();
        }
    }

    public static int b(int i) {
        return i & 3;
    }
}
