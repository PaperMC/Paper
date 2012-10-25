package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
/*
 * HEAR ME, HE WHOM WISHES TO COMMAND THE UPDATERING
 *
 * FOR SOME REASON WE HAVE OUR OWN PORTAL TRAVEL AGENT IN OBC, WE NEED TO UPDATE
 * THAT WITH ANY NEW UPDATES TO THIS FILE. THIS FILE LEFT HERE AS A REMINDER.
 *
 */
// CraftBukkit end

public class PortalTravelAgent {

    private Random a = new Random();

    public PortalTravelAgent() {}

    public void a(World world, Entity entity, double d0, double d1, double d2, float f) {
        if (world.worldProvider.dimension != 1) {
            if (!this.b(world, entity, d0, d1, d2, f)) {
                this.a(world, entity);
                this.b(world, entity, d0, d1, d2, f);
            }
        } else {
            int i = MathHelper.floor(entity.locX);
            int j = MathHelper.floor(entity.locY) - 1;
            int k = MathHelper.floor(entity.locZ);
            byte b0 = 1;
            byte b1 = 0;

            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                        int k1 = i + i1 * b0 + l * b1;
                        int l1 = j + j1;
                        int i2 = k + i1 * b1 - l * b0;
                        boolean flag = j1 < 0;

                        world.setTypeId(k1, l1, i2, flag ? Block.OBSIDIAN.id : 0);
                    }
                }
            }

            entity.setPositionRotation((double) i, (double) j, (double) k, entity.yaw, 0.0F);
            entity.motX = entity.motY = entity.motZ = 0.0D;
        }
    }

    public boolean b(World world, Entity entity, double d0, double d1, double d2, float f) {
        short short1 = 128;
        double d3 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = MathHelper.floor(entity.locX);
        int i1 = MathHelper.floor(entity.locZ);

        int j1;
        double d4;

        for (j1 = l - short1; j1 <= l + short1; ++j1) {
            double d5 = (double) j1 + 0.5D - entity.locX;

            for (int k1 = i1 - short1; k1 <= i1 + short1; ++k1) {
                double d6 = (double) k1 + 0.5D - entity.locZ;

                for (int l1 = world.O() - 1; l1 >= 0; --l1) {
                    if (world.getTypeId(j1, l1, k1) == Block.PORTAL.id) {
                        while (world.getTypeId(j1, l1 - 1, k1) == Block.PORTAL.id) {
                            --l1;
                        }

                        d4 = (double) l1 + 0.5D - entity.locY;
                        double d7 = d5 * d5 + d4 * d4 + d6 * d6;

                        if (d3 < 0.0D || d7 < d3) {
                            d3 = d7;
                            i = j1;
                            j = l1;
                            k = k1;
                        }
                    }
                }
            }
        }

        if (d3 < 0.0D) {
            return false;
        } else {
            double d8 = (double) i + 0.5D;
            double d9 = (double) j + 0.5D;

            d4 = (double) k + 0.5D;
            int i2 = -1;

            if (world.getTypeId(i - 1, j, k) == Block.PORTAL.id) {
                i2 = 2;
            }

            if (world.getTypeId(i + 1, j, k) == Block.PORTAL.id) {
                i2 = 0;
            }

            if (world.getTypeId(i, j, k - 1) == Block.PORTAL.id) {
                i2 = 3;
            }

            if (world.getTypeId(i, j, k + 1) == Block.PORTAL.id) {
                i2 = 1;
            }

            int j2 = entity.at();

            if (i2 > -1) {
                int k2 = Direction.h[i2];
                int l2 = Direction.a[i2];
                int i3 = Direction.b[i2];
                int j3 = Direction.a[k2];
                int k3 = Direction.b[k2];
                boolean flag = !world.isEmpty(i + l2 + j3, j, k + i3 + k3) || !world.isEmpty(i + l2 + j3, j + 1, k + i3 + k3);
                boolean flag1 = !world.isEmpty(i + l2, j, k + i3) || !world.isEmpty(i + l2, j + 1, k + i3);

                if (flag && flag1) {
                    i2 = Direction.f[i2];
                    k2 = Direction.f[k2];
                    l2 = Direction.a[i2];
                    i3 = Direction.b[i2];
                    j3 = Direction.a[k2];
                    k3 = Direction.b[k2];
                    j1 = i - j3;
                    d8 -= (double) j3;
                    int l3 = k - k3;

                    d4 -= (double) k3;
                    flag = !world.isEmpty(j1 + l2 + j3, j, l3 + i3 + k3) || !world.isEmpty(j1 + l2 + j3, j + 1, l3 + i3 + k3);
                    flag1 = !world.isEmpty(j1 + l2, j, l3 + i3) || !world.isEmpty(j1 + l2, j + 1, l3 + i3);
                }

                float f1 = 0.5F;
                float f2 = 0.5F;

                if (!flag && flag1) {
                    f1 = 1.0F;
                } else if (flag && !flag1) {
                    f1 = 0.0F;
                } else if (flag && flag1) {
                    f2 = 0.0F;
                }

                d8 += (double) ((float) j3 * f1 + f2 * (float) l2);
                d4 += (double) ((float) k3 * f1 + f2 * (float) i3);
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;
                float f6 = 0.0F;

                if (i2 == j2) {
                    f3 = 1.0F;
                    f4 = 1.0F;
                } else if (i2 == Direction.f[j2]) {
                    f3 = -1.0F;
                    f4 = -1.0F;
                } else if (i2 == Direction.g[j2]) {
                    f5 = 1.0F;
                    f6 = -1.0F;
                } else {
                    f5 = -1.0F;
                    f6 = 1.0F;
                }

                double d10 = entity.motX;
                double d11 = entity.motZ;

                entity.motX = d10 * (double) f3 + d11 * (double) f6;
                entity.motZ = d10 * (double) f5 + d11 * (double) f4;
                entity.yaw = f - (float) (j2 * 90) + (float) (i2 * 90);
            } else {
                entity.motX = entity.motY = entity.motZ = 0.0D;
            }

            entity.setPositionRotation(d8, d9, d4, entity.yaw, entity.pitch);
            return true;
        }
    }

    public boolean a(World world, Entity entity) {
        byte b0 = 16;
        double d0 = -1.0D;
        int i = MathHelper.floor(entity.locX);
        int j = MathHelper.floor(entity.locY);
        int k = MathHelper.floor(entity.locZ);
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.a.nextInt(4);

        int i2;
        double d1;
        int j2;
        double d2;
        int k2;
        int l2;
        int i3;
        int j3;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        double d3;
        double d4;

        for (i2 = i - b0; i2 <= i + b0; ++i2) {
            d1 = (double) i2 + 0.5D - entity.locX;

            for (j2 = k - b0; j2 <= k + b0; ++j2) {
                d2 = (double) j2 + 0.5D - entity.locZ;

                label274:
                for (l2 = world.O() - 1; l2 >= 0; --l2) {
                    if (world.isEmpty(i2, l2, j2)) {
                        while (l2 > 0 && world.isEmpty(i2, l2 - 1, j2)) {
                            --l2;
                        }

                        for (k2 = l1; k2 < l1 + 4; ++k2) {
                            j3 = k2 % 2;
                            i3 = 1 - j3;
                            if (k2 % 4 >= 2) {
                                j3 = -j3;
                                i3 = -i3;
                            }

                            for (l3 = 0; l3 < 3; ++l3) {
                                for (k3 = 0; k3 < 4; ++k3) {
                                    for (j4 = -1; j4 < 4; ++j4) {
                                        i4 = i2 + (k3 - 1) * j3 + l3 * i3;
                                        k4 = l2 + j4;
                                        int l4 = j2 + (k3 - 1) * i3 - l3 * j3;

                                        if (j4 < 0 && !world.getMaterial(i4, k4, l4).isBuildable() || j4 >= 0 && !world.isEmpty(i4, k4, l4)) {
                                            continue label274;
                                        }
                                    }
                                }
                            }

                            d3 = (double) l2 + 0.5D - entity.locY;
                            d4 = d1 * d1 + d3 * d3 + d2 * d2;
                            if (d0 < 0.0D || d4 < d0) {
                                d0 = d4;
                                l = i2;
                                i1 = l2;
                                j1 = j2;
                                k1 = k2 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (i2 = i - b0; i2 <= i + b0; ++i2) {
                d1 = (double) i2 + 0.5D - entity.locX;

                for (j2 = k - b0; j2 <= k + b0; ++j2) {
                    d2 = (double) j2 + 0.5D - entity.locZ;

                    label222:
                    for (l2 = world.O() - 1; l2 >= 0; --l2) {
                        if (world.isEmpty(i2, l2, j2)) {
                            while (l2 > 0 && world.isEmpty(i2, l2 - 1, j2)) {
                                --l2;
                            }

                            for (k2 = l1; k2 < l1 + 2; ++k2) {
                                j3 = k2 % 2;
                                i3 = 1 - j3;

                                for (l3 = 0; l3 < 4; ++l3) {
                                    for (k3 = -1; k3 < 4; ++k3) {
                                        j4 = i2 + (l3 - 1) * j3;
                                        i4 = l2 + k3;
                                        k4 = j2 + (l3 - 1) * i3;
                                        if (k3 < 0 && !world.getMaterial(j4, i4, k4).isBuildable() || k3 >= 0 && !world.isEmpty(j4, i4, k4)) {
                                            continue label222;
                                        }
                                    }
                                }

                                d3 = (double) l2 + 0.5D - entity.locY;
                                d4 = d1 * d1 + d3 * d3 + d2 * d2;
                                if (d0 < 0.0D || d4 < d0) {
                                    d0 = d4;
                                    l = i2;
                                    i1 = l2;
                                    j1 = j2;
                                    k1 = k2 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int i5 = l;
        int j5 = i1;

        j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;

        if (k1 % 4 >= 2) {
            k5 = -k5;
            l5 = -l5;
        }

        boolean flag;

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > world.O() - 10) {
                i1 = world.O() - 10;
            }

            j5 = i1;

            for (l2 = -1; l2 <= 1; ++l2) {
                for (k2 = 1; k2 < 3; ++k2) {
                    for (j3 = -1; j3 < 3; ++j3) {
                        i3 = i5 + (k2 - 1) * k5 + l2 * l5;
                        l3 = j5 + j3;
                        k3 = j2 + (k2 - 1) * l5 - l2 * k5;
                        flag = j3 < 0;
                        world.setTypeId(i3, l3, k3, flag ? Block.OBSIDIAN.id : 0);
                    }
                }
            }
        }

        for (l2 = 0; l2 < 4; ++l2) {
            world.suppressPhysics = true;

            for (k2 = 0; k2 < 4; ++k2) {
                for (j3 = -1; j3 < 4; ++j3) {
                    i3 = i5 + (k2 - 1) * k5;
                    l3 = j5 + j3;
                    k3 = j2 + (k2 - 1) * l5;
                    flag = k2 == 0 || k2 == 3 || j3 == -1 || j3 == 3;
                    world.setTypeId(i3, l3, k3, flag ? Block.OBSIDIAN.id : Block.PORTAL.id);
                }
            }

            world.suppressPhysics = false;

            for (k2 = 0; k2 < 4; ++k2) {
                for (j3 = -1; j3 < 4; ++j3) {
                    i3 = i5 + (k2 - 1) * k5;
                    l3 = j5 + j3;
                    k3 = j2 + (k2 - 1) * l5;
                    world.applyPhysics(i3, l3, k3, world.getTypeId(i3, l3, k3));
                }
            }
        }

        return true;
    }
}
