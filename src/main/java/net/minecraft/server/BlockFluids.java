package net.minecraft.server;

import java.util.Random;

public abstract class BlockFluids extends Block {

    protected BlockFluids(int i, Material material) {
        super(i, (material == Material.LAVA ? 14 : 12) * 16 + 13, material);
        float f = 0.0F;
        float f1 = 0.0F;

        this.a(0.0F + f1, 0.0F + f, 0.0F + f1, 1.0F + f1, 1.0F + f, 1.0F + f1);
        this.a(true);
    }

    public static float d(int i) {
        if (i >= 8) {
            i = 0;
        }

        float f = (float) (i + 1) / 9.0F;

        return f;
    }

    public int a(int i) {
        return i != 0 && i != 1 ? this.textureId + 1 : this.textureId;
    }

    protected int g(World world, int i, int j, int k) {
        return world.getMaterial(i, j, k) != this.material ? -1 : world.getData(i, j, k);
    }

    protected int b(IBlockAccess iblockaccess, int i, int j, int k) {
        if (iblockaccess.getMaterial(i, j, k) != this.material) {
            return -1;
        } else {
            int l = iblockaccess.getData(i, j, k);

            if (l >= 8) {
                l = 0;
            }

            return l;
        }
    }

    public boolean b() {
        return false;
    }

    public boolean a() {
        return false;
    }

    public boolean a(int i, boolean flag) {
        return flag && i == 0;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        Material material = iblockaccess.getMaterial(i, j, k);

        return material == this.material ? false : (l == 1 ? true : (material == Material.ICE ? false : super.b(iblockaccess, i, j, k, l)));
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public int c() {
        return 4;
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }

    private Vec3D c(IBlockAccess iblockaccess, int i, int j, int k) {
        Vec3D vec3d = Vec3D.create(0.0D, 0.0D, 0.0D);
        int l = this.b(iblockaccess, i, j, k);

        for (int i1 = 0; i1 < 4; ++i1) {
            int j1 = i;
            int k1 = k;

            if (i1 == 0) {
                j1 = i - 1;
            }

            if (i1 == 1) {
                k1 = k - 1;
            }

            if (i1 == 2) {
                ++j1;
            }

            if (i1 == 3) {
                ++k1;
            }

            int l1 = this.b(iblockaccess, j1, j, k1);
            int i2;

            if (l1 < 0) {
                if (!iblockaccess.getMaterial(j1, j, k1).isSolid()) {
                    l1 = this.b(iblockaccess, j1, j - 1, k1);
                    if (l1 >= 0) {
                        i2 = l1 - (l - 8);
                        vec3d = vec3d.add((double) ((j1 - i) * i2), (double) ((j - j) * i2), (double) ((k1 - k) * i2));
                    }
                }
            } else if (l1 >= 0) {
                i2 = l1 - l;
                vec3d = vec3d.add((double) ((j1 - i) * i2), (double) ((j - j) * i2), (double) ((k1 - k) * i2));
            }
        }

        if (iblockaccess.getData(i, j, k) >= 8) {
            boolean flag = false;

            if (flag || this.b(iblockaccess, i, j, k - 1, 2)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i, j, k + 1, 3)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i - 1, j, k, 4)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i + 1, j, k, 5)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i, j + 1, k - 1, 2)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i, j + 1, k + 1, 3)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i - 1, j + 1, k, 4)) {
                flag = true;
            }

            if (flag || this.b(iblockaccess, i + 1, j + 1, k, 5)) {
                flag = true;
            }

            if (flag) {
                vec3d = vec3d.b().add(0.0D, -6.0D, 0.0D);
            }
        }

        vec3d = vec3d.b();
        return vec3d;
    }

    public void a(World world, int i, int j, int k, Entity entity, Vec3D vec3d) {
        Vec3D vec3d1 = this.c(world, i, j, k);

        vec3d.a += vec3d1.a;
        vec3d.b += vec3d1.b;
        vec3d.c += vec3d1.c;
    }

    public int d() {
        return this.material == Material.WATER ? 5 : (this.material == Material.LAVA ? 30 : 0);
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
    }

    public void onPlace(World world, int i, int j, int k) {
        this.i(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        this.i(world, i, j, k);
    }

    private void i(World world, int i, int j, int k) {
        if (world.getTypeId(i, j, k) == this.id) {
            if (this.material == Material.LAVA) {
                boolean flag = false;

                if (flag || world.getMaterial(i, j, k - 1) == Material.WATER) {
                    flag = true;
                }

                if (flag || world.getMaterial(i, j, k + 1) == Material.WATER) {
                    flag = true;
                }

                if (flag || world.getMaterial(i - 1, j, k) == Material.WATER) {
                    flag = true;
                }

                if (flag || world.getMaterial(i + 1, j, k) == Material.WATER) {
                    flag = true;
                }

                if (flag || world.getMaterial(i, j + 1, k) == Material.WATER) {
                    flag = true;
                }

                if (flag) {
                    int l = world.getData(i, j, k);

                    if (l == 0) {
                        world.setTypeId(i, j, k, Block.OBSIDIAN.id);
                    } else if (l <= 4) {
                        world.setTypeId(i, j, k, Block.COBBLESTONE.id);
                    }

                    this.fizz(world, i, j, k);
                }
            }
        }
    }

    protected void fizz(World world, int i, int j, int k) {
        world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), "random.fizz", 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

        for (int l = 0; l < 8; ++l) {
            world.a("largesmoke", (double) i + Math.random(), (double) j + 1.2D, (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
        }
    }
}
