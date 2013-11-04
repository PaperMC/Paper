package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public abstract class BlockButtonAbstract extends Block {

    private final boolean a;

    protected BlockButtonAbstract(boolean flag) {
        super(Material.ORIENTABLE);
        this.a(true);
        this.a(CreativeModeTab.d);
        this.a = flag;
    }

    public AxisAlignedBB a(World world, int i, int j, int k) {
        return null;
    }

    public int a(World world) {
        return this.a ? 30 : 20;
    }

    public boolean c() {
        return false;
    }

    public boolean d() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 2 && world.getType(i, j, k + 1).r() ? true : (l == 3 && world.getType(i, j, k - 1).r() ? true : (l == 4 && world.getType(i + 1, j, k).r() ? true : l == 5 && world.getType(i - 1, j, k).r()));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.getType(i - 1, j, k).r() ? true : (world.getType(i + 1, j, k).r() ? true : (world.getType(i, j, k - 1).r() ? true : world.getType(i, j, k + 1).r()));
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        int j1 = world.getData(i, j, k);
        int k1 = j1 & 8;

        j1 &= 7;
        if (l == 2 && world.getType(i, j, k + 1).r()) {
            j1 = 4;
        } else if (l == 3 && world.getType(i, j, k - 1).r()) {
            j1 = 3;
        } else if (l == 4 && world.getType(i + 1, j, k).r()) {
            j1 = 2;
        } else if (l == 5 && world.getType(i - 1, j, k).r()) {
            j1 = 1;
        } else {
            j1 = this.e(world, i, j, k);
        }

        return j1 + k1;
    }

    private int e(World world, int i, int j, int k) {
        return world.getType(i - 1, j, k).r() ? 1 : (world.getType(i + 1, j, k).r() ? 2 : (world.getType(i, j, k - 1).r() ? 3 : (world.getType(i, j, k + 1).r() ? 4 : 1)));
    }

    public void doPhysics(World world, int i, int j, int k, Block block) {
        if (this.m(world, i, j, k)) {
            int l = world.getData(i, j, k) & 7;
            boolean flag = false;

            if (!world.getType(i - 1, j, k).r() && l == 1) {
                flag = true;
            }

            if (!world.getType(i + 1, j, k).r() && l == 2) {
                flag = true;
            }

            if (!world.getType(i, j, k - 1).r() && l == 3) {
                flag = true;
            }

            if (!world.getType(i, j, k + 1).r() && l == 4) {
                flag = true;
            }

            if (flag) {
                this.b(world, i, j, k, world.getData(i, j, k), 0);
                world.setAir(i, j, k);
            }
        }
    }

    private boolean m(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.b(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);

        this.b(l);
    }

    private void b(int i) {
        int j = i & 7;
        boolean flag = (i & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag) {
            f3 = 0.0625F;
        }

        if (j == 1) {
            this.a(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        } else if (j == 2) {
            this.a(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        } else if (j == 3) {
            this.a(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        } else if (j == 4) {
            this.a(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {}

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);

        if (k1 == 0) {
            return true;
        } else {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (k1 != 8) ? 15 : 0;
            int current = (k1 == 8) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (k1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, j1 + k1, 3);
            world.c(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
            this.a(world, i, j, k, j1);
            world.a(i, j, k, this, this.a(world));
            return true;
        }
    }

    public void remove(World world, int i, int j, int k, Block block, int l) {
        if ((l & 8) > 0) {
            int i1 = l & 7;

            this.a(world, i, j, k, i1);
        }

        super.remove(world, i, j, k, block, l);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0 ? 15 : 0;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return 0;
        } else {
            int j1 = i1 & 7;

            return j1 == 5 && l == 1 ? 15 : (j1 == 4 && l == 2 ? 15 : (j1 == 3 && l == 3 ? 15 : (j1 == 2 && l == 4 ? 15 : (j1 == 1 && l == 5 ? 15 : 0))));
        }
    }

    public boolean isPowerSource() {
        return true;
    }

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 15, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end

                if (this.a) {
                    this.n(world, i, j, k);
                } else {
                    world.setData(i, j, k, l & 7, 3);
                    int i1 = l & 7;

                    this.a(world, i, j, k, i1);
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
                    world.c(i, j, k, i, j, k);
                }
            }
        }
    }

    public void g() {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }

    public void a(World world, int i, int j, int k, Entity entity) {
        if (!world.isStatic) {
            if (this.a) {
                if ((world.getData(i, j, k) & 8) == 0) {
                    this.n(world, i, j, k);
                }
            }
        }
    }

    private void n(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        int i1 = l & 7;
        boolean flag = (l & 8) != 0;

        this.b(l);
        List list = world.a(EntityArrow.class, AxisAlignedBB.a().a((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ));
        boolean flag1 = !list.isEmpty();

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag != flag1 && flag1) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
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
            world.setData(i, j, k, i1 | 8, 3);
            this.a(world, i, j, k, i1);
            world.c(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag1 && flag) {
            world.setData(i, j, k, i1, 3);
            this.a(world, i, j, k, i1);
            world.c(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag1) {
            world.a(i, j, k, this, this.a(world));
        }
    }

    private void a(World world, int i, int j, int k, int l) {
        world.applyPhysics(i, j, k, this);
        if (l == 1) {
            world.applyPhysics(i - 1, j, k, this);
        } else if (l == 2) {
            world.applyPhysics(i + 1, j, k, this);
        } else if (l == 3) {
            world.applyPhysics(i, j, k - 1, this);
        } else if (l == 4) {
            world.applyPhysics(i, j, k + 1, this);
        } else {
            world.applyPhysics(i, j - 1, k, this);
        }
    }
}
