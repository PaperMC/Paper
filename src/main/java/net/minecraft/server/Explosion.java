package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// CraftBukkit start
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.Location;
// CraftBukkit end

public class Explosion {

    public boolean a = false;
    private Random h = new Random();
    private World i;
    public double b;
    public double c;
    public double d;
    public Entity e;
    public float f;
    public Set g = new HashSet();

    public boolean wasCanceled = false; // CraftBukkit

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f) {
        this.i = world;
        this.e = entity;
        this.f = f;
        this.b = d0;
        this.c = d1;
        this.d = d2;
    }

    public void a() {
        float f = this.f;
        byte b0 = 16;

        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < b0; ++i) {
            for (j = 0; j < b0; ++j) {
                for (k = 0; k < b0; ++k) {
                    if (i == 0 || i == b0 - 1 || j == 0 || j == b0 - 1 || k == 0 || k == b0 - 1) {
                        double d3 = (double) ((float) i / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d4 = (double) ((float) j / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d5 = (double) ((float) k / ((float) b0 - 1.0F) * 2.0F - 1.0F);
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.f * (0.7F + this.i.k.nextFloat() * 0.6F);

                        d0 = this.b;
                        d1 = this.c;
                        d2 = this.d;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            int l = MathHelper.b(d0);
                            int i1 = MathHelper.b(d1);
                            int j1 = MathHelper.b(d2);
                            int k1 = this.i.getTypeId(l, i1, j1);

                            if (k1 > 0) {
                                f1 -= (Block.byId[k1].a(this.e) + 0.3F) * f2;
                            }

                            if (f1 > 0.0F) {
                                this.g.add(new ChunkPosition(l, i1, j1));
                            }

                            d0 += d3 * (double) f2;
                            d1 += d4 * (double) f2;
                            d2 += d5 * (double) f2;
                        }
                    }
                }
            }
        }

        this.f *= 2.0F;
        i = MathHelper.b(this.b - (double) this.f - 1.0D);
        j = MathHelper.b(this.b + (double) this.f + 1.0D);
        k = MathHelper.b(this.c - (double) this.f - 1.0D);
        int l1 = MathHelper.b(this.c + (double) this.f + 1.0D);
        int i2 = MathHelper.b(this.d - (double) this.f - 1.0D);
        int j2 = MathHelper.b(this.d + (double) this.f + 1.0D);
        List list = this.i.b(this.e, AxisAlignedBB.b((double) i, (double) k, (double) i2, (double) j, (double) l1, (double) j2));
        Vec3D vec3d = Vec3D.b(this.b, this.c, this.d);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = (Entity) list.get(k2);
            double d7 = entity.e(this.b, this.c, this.d) / (double) this.f;

            if (d7 <= 1.0D) {
                d0 = entity.locX - this.b;
                d1 = entity.locY - this.c;
                d2 = entity.locZ - this.d;
                double d8 = (double) MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

                d0 /= d8;
                d1 /= d8;
                d2 /= d8;
                double d9 = (double) this.i.a(vec3d, entity.boundingBox);
                double d10 = (1.0D - d7) * d9;

                // CraftBukkit start - explosion damage hook
                CraftServer server = ((WorldServer) this.i).getServer();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                DamageCause damageType;
                int damageDone = (int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) this.f + 1.0D);

                if (damagee == null) {
                    // nothing was hurt
                } else if (e == null) { // Block explosion
                    // TODO: get the x/y/z of the tnt block?
                    // does this even get called ever? @see EntityTNTPrimed - not BlockTNT or whatever
                    damageType = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;

                    EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, damagee, damageType, damageDone);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        entity.a(this.e, event.getDamage());
                        entity.motX += d0 * d10;
                        entity.motY += d1 * d10;
                        entity.motZ += d2 * d10;
                    }
                } else {
                    org.bukkit.entity.Entity damager = this.e.getBukkitEntity();
                    damageType = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;

                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, damageDone);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        entity.a(this.e, event.getDamage());

                        entity.motX += d0 * d10;
                        entity.motY += d1 * d10;
                        entity.motZ += d2 * d10;
                    }
                }
                // CraftBukkit end
            }
        }

        this.f = f;
        ArrayList arraylist = new ArrayList();

        arraylist.addAll(this.g);
        if (this.a) {
            for (int l2 = arraylist.size() - 1; l2 >= 0; --l2) {
                ChunkPosition chunkposition = (ChunkPosition) arraylist.get(l2);
                int i3 = chunkposition.a;
                int j3 = chunkposition.b;
                int k3 = chunkposition.c;
                int l3 = this.i.getTypeId(i3, j3, k3);
                int i4 = this.i.getTypeId(i3, j3 - 1, k3);

                if (l3 == 0 && Block.o[i4] && this.h.nextInt(3) == 0) {
                    this.i.e(i3, j3, k3, Block.FIRE.id);
                }
            }
        }
    }

    public void b() {
        this.i.a(this.b, this.c, this.d, "random.explode", 4.0F, (1.0F + (this.i.k.nextFloat() - this.i.k.nextFloat()) * 0.2F) * 0.7F);
        ArrayList arraylist = new ArrayList();

        arraylist.addAll(this.g);

        // CraftBukkit start
        Server server = ((WorldServer) this.i).getServer();
        CraftWorld world = ((WorldServer) this.i).getWorld();
        org.bukkit.entity.Entity explode = (this.e == null) ? null : this.e.getBukkitEntity();
        Location location = new Location(world, this.b, this.c, this.d);

        List<org.bukkit.block.Block> blockList = new ArrayList<org.bukkit.block.Block>();
        for (int j = arraylist.size() - 1; j >= 0; j--) {
            ChunkPosition cpos = (ChunkPosition) arraylist.get(j);
            org.bukkit.block.Block block = world.getBlockAt(cpos.a, cpos.b, cpos.c);
            if (!block.getType().equals(org.bukkit.Material.AIR)) {
                blockList.add(block);
            }
        }

        EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList);
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            wasCanceled = true;
            return;
        }
        // CraftBukkit end

        for (int i = arraylist.size() - 1; i >= 0; --i) {
            ChunkPosition chunkposition = (ChunkPosition) arraylist.get(i);
            int j = chunkposition.a;
            int k = chunkposition.b;
            int l = chunkposition.c;
            int i1 = this.i.getTypeId(j, k, l);

            for (int j1 = 0; j1 < 1; ++j1) {
                double d0 = (double) ((float) j + this.i.k.nextFloat());
                double d1 = (double) ((float) k + this.i.k.nextFloat());
                double d2 = (double) ((float) l + this.i.k.nextFloat());
                double d3 = d0 - this.b;
                double d4 = d1 - this.c;
                double d5 = d2 - this.d;
                double d6 = (double) MathHelper.a(d3 * d3 + d4 * d4 + d5 * d5);

                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double) this.f + 0.1D);

                d7 *= (double) (this.i.k.nextFloat() * this.i.k.nextFloat() + 0.3F);
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                this.i.a("explode", (d0 + this.b * 1.0D) / 2.0D, (d1 + this.c * 1.0D) / 2.0D, (d2 + this.d * 1.0D) / 2.0D, d3, d4, d5);
                this.i.a("smoke", d0, d1, d2, d3, d4, d5);
            }

            if (i1 > 0) {
                // CraftBukkit
                Block.byId[i1].a(this.i, j, k, l, this.i.getData(j, k, l), event.getYield());
                this.i.e(j, k, l, 0);
                Block.byId[i1].c(this.i, j, k, l);
            }
        }
    }
}
