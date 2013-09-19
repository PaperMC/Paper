package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

public class BlockTripwire extends Block {

    public BlockTripwire(int i) {
        super(i, Material.ORIENTABLE);
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        this.b(true);
    }

    public int a(World world) {
        return 10;
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 30;
    }

    public int getDropType(int i, Random random, int j) {
        return Item.STRING.id;
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        boolean flag = (i1 & 2) == 2;
        boolean flag1 = !world.w(i, j - 1, k);

        if (flag != flag1) {
            this.c(world, i, j, k, i1, 0);
            world.setAir(i, j, k);
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        boolean flag = (l & 4) == 4;
        boolean flag1 = (l & 2) == 2;

        if (!flag1) {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
        } else if (!flag) {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        } else {
            this.a(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        int l = world.w(i, j - 1, k) ? 0 : 2;

        world.setData(i, j, k, l, 3);
        this.d(world, i, j, k, l);
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        this.d(world, i, j, k, i1 | 1);
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        if (!world.isStatic) {
            if (entityhuman.by() != null && entityhuman.by().id == Item.SHEARS.id) {
                world.setData(i, j, k, l | 8, 4);
            }
        }
    }

    private void d(World world, int i, int j, int k, int l) {
        int i1 = 0;

        while (i1 < 2) {
            int j1 = 1;

            while (true) {
                if (j1 < 42) {
                    int k1 = i + Direction.a[i1] * j1;
                    int l1 = k + Direction.b[i1] * j1;
                    int i2 = world.getTypeId(k1, j, l1);

                    if (i2 == Block.TRIPWIRE_SOURCE.id) {
                        int j2 = world.getData(k1, j, l1) & 3;

                        if (j2 == Direction.f[i1]) {
                            Block.TRIPWIRE_SOURCE.a(world, k1, j, l1, i2, world.getData(k1, j, l1), true, j1, l);
                        }
                    } else if (i2 == Block.TRIPWIRE.id) {
                        ++j1;
                        continue;
                    }
                }

                ++i1;
                break;
            }
        }
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            if ((world.getData(i, j, k) & 1) != 1) {
                this.k(world, i, j, k);
            }
        }
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            if ((world.getData(i, j, k) & 1) == 1) {
                this.k(world, i, j, k);
            }
        }
    }

    private void k(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        boolean flag = (l & 1) == 1;
        boolean flag1 = false;
        List list = world.getEntities((Entity) null, AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ));

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (!entity.au()) {
                    flag1 = true;
                    break;
                }
            }
        }

        // CraftBukkit start - Call interact even when triggering connected tripwire
        if (flag != flag1 && flag1 && (world.getData(i, j, k) & 4) == 4) {
            org.bukkit.World bworld = world.getWorld();
            org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
            org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);
            boolean allowed = false;

            // If all of the events are cancelled block the tripwire trigger, else allow
            for (Object object : list) {
                if (object != null) {
                    org.bukkit.event.Cancellable cancellable;

                    if (object instanceof EntityHuman) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) object, org.bukkit.event.block.Action.PHYSICAL, i, j, k, -1, null);
                    } else if (object instanceof Entity) {
                        cancellable = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                        manager.callEvent((EntityInteractEvent) cancellable);
                    } else {
                        continue;
                    }

                    if (!cancellable.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag1 && !flag) {
            l |= 1;
        }

        if (!flag1 && flag) {
            l &= -2;
        }

        if (flag1 != flag) {
            world.setData(i, j, k, l, 3);
            this.d(world, i, j, k, l);
        }

        if (flag1) {
            world.a(i, j, k, this.id, this.a(world));
        }
    }
}
