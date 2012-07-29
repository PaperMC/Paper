package org.bukkit.craftbukkit;

import java.util.Random;

import net.minecraft.server.Block;
import net.minecraft.server.MathHelper;
import net.minecraft.server.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.World.Environment;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalTravelAgent implements TravelAgent {

    private Random random = new Random();

    private int searchRadius = 128;
    private int creationRadius = 14; // 16 -> 14
    private boolean canCreatePortal = true;

    public PortalTravelAgent() { }

    public Location findOrCreate(Location location) {
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        worldServer.chunkProviderServer.forceChunkLoad = true;
        // Attempt to find a Portal.
        Location resultLocation = this.findPortal(location);
        // If a Portal cannot be found we will attempt to create one.
        if (resultLocation == null) {
            // Attempt to create a portal, return if it was successful or not.
            if (this.canCreatePortal && this.createPortal(location)) {
                // Now find that portals location.
                resultLocation = this.findPortal(location);
            } else {
                // Fallback onto the original location.
                resultLocation = location;
            }
        }
        worldServer.chunkProviderServer.forceChunkLoad = false;
        // Return our resulting portal location.
        return resultLocation;
    }

    public Location findPortal(Location location) {
        net.minecraft.server.World world = ((CraftWorld) location.getWorld()).getHandle();

        if (location.getWorld().getEnvironment() == Environment.THE_END) {
            int i = MathHelper.floor(location.getBlockX());
            int j = MathHelper.floor(location.getBlockY()) - 1;
            int k = MathHelper.floor(location.getBlockZ());
            byte b0 = 1;
            byte b1 = 0;

            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                        int k1 = i + i1 * b0 + l * b1;
                        int l1 = j + j1;
                        int i2 = k + i1 * b1 - l * b0;
                        boolean flag = j1 < 0;

                        if (world.getTypeId(k1, l1, i2) != (flag ? Block.OBSIDIAN.id : 0)) {
                            return null;
                        }
                    }
                }
            }

            return location;
        }

        // short short1 = 128;
        double d0 = -1.0D;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = location.getBlockX();
        int i1 = location.getBlockZ();

        double d1;

        for (int j1 = l - this.searchRadius; j1 <= l + this.searchRadius; ++j1) {
            double d2 = (double) j1 + 0.5D - location.getX();

            for (int k1 = i1 - this.searchRadius; k1 <= i1 + this.searchRadius; ++k1) {
                double d3 = (double) k1 + 0.5D - location.getZ();

                for (int l1 = world.L() - 1; l1 >= 0; --l1) {
                    if (world.getTypeId(j1, l1, k1) == Block.PORTAL.id) {
                        while (world.getTypeId(j1, l1 - 1, k1) == Block.PORTAL.id) {
                            --l1;
                        }

                        d1 = (double) l1 + 0.5D - location.getY();
                        double d4 = d2 * d2 + d1 * d1 + d3 * d3;

                        if (d0 < 0.0D || d4 < d0) {
                            d0 = d4;
                            i = j1;
                            j = l1;
                            k = k1;
                        }
                    }
                }
            }
        }

        if (d0 >= 0.0D) {
            double d5 = (double) i + 0.5D;
            double d6 = (double) j + 0.5D;

            d1 = (double) k + 0.5D;
            if (world.getTypeId(i - 1, j, k) == Block.PORTAL.id) {
                d5 -= 0.5D;
            }

            if (world.getTypeId(i + 1, j, k) == Block.PORTAL.id) {
                d5 += 0.5D;
            }

            if (world.getTypeId(i, j, k - 1) == Block.PORTAL.id) {
                d1 -= 0.5D;
            }

            if (world.getTypeId(i, j, k + 1) == Block.PORTAL.id) {
                d1 += 0.5D;
            }

            return new Location(location.getWorld(), d5, d6, d1, location.getYaw(), location.getPitch());
        } else {
            return null;
        }
    }

    public boolean createPortal(Location location) {
        net.minecraft.server.World world = ((CraftWorld) location.getWorld()).getHandle();

        if (location.getWorld().getEnvironment() == Environment.THE_END) {
            int i = MathHelper.floor(location.getBlockX());
            int j = MathHelper.floor(location.getBlockY()) - 1;
            int k = MathHelper.floor(location.getBlockZ());
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

            return true;
        }

        // byte b0 = 16;
        double d0 = -1.0D;
        int i = location.getBlockX();
        int j = location.getBlockY();
        int k = location.getBlockZ();
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.random.nextInt(4);

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

        for (i2 = i - this.creationRadius; i2 <= i + this.creationRadius; ++i2) {
            d1 = (double) i2 + 0.5D - location.getX();

            for (j2 = k - this.creationRadius; j2 <= k + this.creationRadius; ++j2) {
                d2 = (double) j2 + 0.5D - location.getZ();

                label271:
                for (l2 = world.L() - 1; l2 >= 0; --l2) {
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
                                    for (j4 = -1; j4 < 5; ++j4) {
                                        i4 = i2 + (k3 - 1) * j3 + l3 * i3;
                                        k4 = l2 + j4;
                                        int l4 = j2 + (k3 - 1) * i3 - l3 * j3;

                                        if (j4 < 0 && !world.getMaterial(i4, k4, l4).isBuildable() || j4 >= 0 && !world.isEmpty(i4, k4, l4)) {
                                            continue label271;
                                        }
                                    }
                                }
                            }

                            d3 = (double) l2 + 0.5D - location.getY();
                            d4 = d1 * d1 + d3 * d3 + d2 * d2;
                            if (d0 < 0.0D || d4 < d0) {
                                d0 = d4;
                                l = i2;
                                i1 = l2 + 1;
                                j1 = j2;
                                k1 = k2 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (i2 = i - this.creationRadius; i2 <= i + this.creationRadius; ++i2) {
                d1 = (double) i2 + 0.5D - location.getX();

                for (j2 = k - this.creationRadius; j2 <= k + this.creationRadius; ++j2) {
                    d2 = (double) j2 + 0.5D - location.getZ();

                    label219:
                    for (l2 = world.L() - 1; l2 >= 0; --l2) {
                        if (world.isEmpty(i2, l2, j2)) {
                            while (l2 > 0 && world.isEmpty(i2, l2 - 1, j2)) {
                                --l2;
                            }

                            for (k2 = l1; k2 < l1 + 2; ++k2) {
                                j3 = k2 % 2;
                                i3 = 1 - j3;

                                for (l3 = 0; l3 < 4; ++l3) {
                                    for (k3 = -1; k3 < 5; ++k3) {
                                        j4 = i2 + (l3 - 1) * j3;
                                        i4 = l2 + k3;
                                        k4 = j2 + (l3 - 1) * i3;
                                        if (k3 < 0 && !world.getMaterial(j4, i4, k4).isBuildable() || k3 >= 0 && !world.isEmpty(j4, i4, k4)) {
                                            continue label219;
                                        }
                                    }
                                }

                                d3 = (double) l2 + 0.5D - location.getY();
                                d4 = d1 * d1 + d3 * d3 + d2 * d2;
                                if (d0 < 0.0D || d4 < d0) {
                                    d0 = d4;
                                    l = i2;
                                    i1 = l2 + 1;
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

        // CraftBukkit start - portal create event
        java.util.ArrayList<org.bukkit.block.Block> blocks = new java.util.ArrayList<org.bukkit.block.Block>();
        // Find out what blocks the portal is going to modify, duplicated from below
        CraftWorld craftWorld = ((WorldServer) world).getWorld();

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > world.L() - 10) {
                i1 = world.L() - 10;
            }

            j5 = i1;

            for (l2 = -1; l2 <= 1; ++l2) {
                for (k2 = 1; k2 < 3; ++k2) {
                    for (j3 = -1; j3 < 3; ++j3) {
                        i3 = i5 + (k2 - 1) * k5 + l2 * l5;
                        l3 = j5 + j3;
                        k3 = j2 + (k2 - 1) * l5 - l2 * k5;
                        org.bukkit.block.Block b = craftWorld.getBlockAt(i3, l3, k3);
                        if (!blocks.contains(b)) {
                            blocks.add(b);
                        }
                    }
                }
            }
        }

        for (l2 = 0; l2 < 4; ++l2) {
            for (k2 = 0; k2 < 4; ++k2) {
                for (j3 = -1; j3 < 4; ++j3) {
                    i3 = i5 + (k2 - 1) * k5;
                    l3 = j5 + j3;
                    k3 = j2 + (k2 - 1) * l5;
                    org.bukkit.block.Block b = craftWorld.getBlockAt(i3, l3, k3);
                    if (!blocks.contains(b)) {
                        blocks.add(b);
                    }
                }
            }
        }

        PortalCreateEvent event = new PortalCreateEvent(blocks, (org.bukkit.World) craftWorld, PortalCreateEvent.CreateReason.OBC_DESTINATION);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        // CraftBukkit end

        if (d0 < 0.0D) {
            if (i1 < 70) {
                i1 = 70;
            }

            if (i1 > 118) {
                i1 = 118;
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

    public TravelAgent setSearchRadius(int radius) {
        this.searchRadius = radius;
        return this;
    }

    public int getSearchRadius() {
        return this.searchRadius;
    }

    public TravelAgent setCreationRadius(int radius) {
        this.creationRadius = radius < 2 ? 0 : radius - 2;
        return this;
    }

    public int getCreationRadius() {
        return this.creationRadius;
    }

    public boolean getCanCreatePortal() {
        return this.canCreatePortal;
    }

    public void setCanCreatePortal(boolean create) {
        this.canCreatePortal = create;
    }
}
