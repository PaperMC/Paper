package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;
// CraftBukkit end

public class BlockPortal extends BlockHalfTransparant {

    public BlockPortal(int i) {
        super(i, "portal", Material.PORTAL, false);
        this.b(true);
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.worldProvider.d() && random.nextInt(2000) < world.difficulty) {
            int l;

            for (l = j; !world.w(i, l, k) && l > 0; --l) {
                ;
            }

            if (l > 0 && !world.u(i, l + 1, k)) {
                Entity entity = ItemMonsterEgg.a(world, 57, (double) i + 0.5D, (double) l + 1.1D, (double) k + 0.5D);

                if (entity != null) {
                    entity.portalCooldown = entity.ac();
                }
            }
        }
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        return null;
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        float f;
        float f1;

        if (iblockaccess.getTypeId(i - 1, j, k) != this.id && iblockaccess.getTypeId(i + 1, j, k) != this.id) {
            f = 0.125F;
            f1 = 0.5F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        } else {
            f = 0.5F;
            f1 = 0.125F;
            this.a(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public boolean o_(World world, int i, int j, int k) {
        byte b0 = 0;
        byte b1 = 0;

        if (world.getTypeId(i - 1, j, k) == Block.OBSIDIAN.id || world.getTypeId(i + 1, j, k) == Block.OBSIDIAN.id) {
            b0 = 1;
        }

        if (world.getTypeId(i, j, k - 1) == Block.OBSIDIAN.id || world.getTypeId(i, j, k + 1) == Block.OBSIDIAN.id) {
            b1 = 1;
        }

        if (b0 == b1) {
            return false;
        } else {
            // CraftBukkit start
            java.util.Collection<org.bukkit.block.Block> blocks = new java.util.HashSet<org.bukkit.block.Block>();
            org.bukkit.World bworld = world.getWorld();
            // CraftBukkit end

            if (world.getTypeId(i - b0, j, k - b1) == 0) {
                i -= b0;
                k -= b1;
            }

            int l;
            int i1;

            for (l = -1; l <= 2; ++l) {
                for (i1 = -1; i1 <= 3; ++i1) {
                    boolean flag = l == -1 || l == 2 || i1 == -1 || i1 == 3;

                    if (l != -1 && l != 2 || i1 != -1 && i1 != 3) {
                        int j1 = world.getTypeId(i + b0 * l, j + i1, k + b1 * l);

                        if (flag) {
                            if (j1 != Block.OBSIDIAN.id) {
                                return false;
                            } else { // CraftBukkit
                                blocks.add(bworld.getBlockAt(i + b0 * l, j + i1, k + b1 * l)); // CraftBukkit
                            }
                        } else if (j1 != 0 && j1 != Block.FIRE.id) {
                            return false;
                        }
                    }
                }
            }

            // CraftBukkit start
            for (l = 0; l < 2; ++l) {
                for (i1 = 0; i1 < 3; ++i1) {
                    blocks.add(bworld.getBlockAt(i + b0 * l, j + i1, k + b1 * l));
                }
            }

            PortalCreateEvent event = new PortalCreateEvent(blocks, bworld, PortalCreateEvent.CreateReason.FIRE);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            for (l = 0; l < 2; ++l) {
                for (i1 = 0; i1 < 3; ++i1) {
                    world.setTypeIdAndData(i + b0 * l, j + i1, k + b1 * l, Block.PORTAL.id, 0, 2);
                }
            }

            return true;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        byte b0 = 0;
        byte b1 = 1;

        if (world.getTypeId(i - 1, j, k) == this.id || world.getTypeId(i + 1, j, k) == this.id) {
            b0 = 1;
            b1 = 0;
        }

        int i1;

        for (i1 = j; world.getTypeId(i, i1 - 1, k) == this.id; --i1) {
            ;
        }

        if (world.getTypeId(i, i1 - 1, k) != Block.OBSIDIAN.id) {
            world.setAir(i, j, k);
        } else {
            int j1;

            for (j1 = 1; j1 < 4 && world.getTypeId(i, i1 + j1, k) == this.id; ++j1) {
                ;
            }

            if (j1 == 3 && world.getTypeId(i, i1 + j1, k) == Block.OBSIDIAN.id) {
                boolean flag = world.getTypeId(i - 1, j, k) == this.id || world.getTypeId(i + 1, j, k) == this.id;
                boolean flag1 = world.getTypeId(i, j, k - 1) == this.id || world.getTypeId(i, j, k + 1) == this.id;

                if (flag && flag1) {
                    world.setAir(i, j, k);
                } else {
                    if ((world.getTypeId(i + b0, j, k + b1) != Block.OBSIDIAN.id || world.getTypeId(i - b0, j, k - b1) != this.id) && (world.getTypeId(i - b0, j, k - b1) != Block.OBSIDIAN.id || world.getTypeId(i + b0, j, k + b1) != this.id)) {
                        world.setAir(i, j, k);
                    }
                }
            } else {
                world.setAir(i, j, k);
            }
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

            entity.ab();
        }
    }
}
