package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

// CraftBukkit end

import java.util.*;

public class Explosion {

    public boolean a;
    private Random h;
    private World i;
    public double b;
    public double c;
    public double d;
    public Entity e;
    public float f;
    public Set g;

    public Explosion(World world, Entity entity, double d1, double d2, double d3, float f1) {
        a = false;
        h = new Random();
        g = ((Set) (new HashSet()));
        i = world;
        e = entity;
        f = f1;
        b = d1;
        c = d2;
        d = d3;
    }

    public void a() {
        float f1 = f;
        int j = 16;

        for (int k = 0; k < j; k++) {
            for (int i1 = 0; i1 < j; i1++) {
                label0:
                for (int k1 = 0; k1 < j; k1++) {
                    if (k != 0 && k != j - 1 && i1 != 0 && i1 != j - 1 && k1 != 0 && k1 != j - 1) {
                        continue;
                    }
                    double d1 = ((float) k / ((float) j - 1.0F)) * 2.0F - 1.0F;
                    double d2 = ((float) i1 / ((float) j - 1.0F)) * 2.0F - 1.0F;
                    double d3 = ((float) k1 / ((float) j - 1.0F)) * 2.0F - 1.0F;
                    double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);

                    d1 /= d4;
                    d2 /= d4;
                    d3 /= d4;
                    float f2 = f * (0.7F + i.l.nextFloat() * 0.6F);
                    double d5 = b;
                    double d7 = c;
                    double d9 = d;
                    float f3 = 0.3F;

                    do {
                        if (f2 <= 0.0F) {
                            continue label0;
                        }
                        int j2 = MathHelper.b(d5);
                        int k2 = MathHelper.b(d7);
                        int l2 = MathHelper.b(d9);
                        int i3 = i.a(j2, k2, l2);

                        if (i3 > 0) {
                            f2 -= (Block.m[i3].a(e) + 0.3F) * f3;
                        }
                        if (f2 > 0.0F) {
                            g.add(((new ChunkPosition(j2, k2, l2))));
                        }
                        d5 += d1 * (double) f3;
                        d7 += d2 * (double) f3;
                        d9 += d3 * (double) f3;
                        f2 -= f3 * 0.75F;
                    } while (true);
                }
            }
        }

        f *= 2.0F;
        int l = MathHelper.b(b - (double) f - 1.0D);
        int j1 = MathHelper.b(b + (double) f + 1.0D);
        int l1 = MathHelper.b(c - (double) f - 1.0D);
        int j3 = MathHelper.b(c + (double) f + 1.0D);
        int k3 = MathHelper.b(d - (double) f - 1.0D);
        int l3 = MathHelper.b(d + (double) f + 1.0D);
        List list = i.b(e, AxisAlignedBB.b(l, l1, k3, j1, j3, l3));
        Vec3D vec3d = Vec3D.b(b, c, d);

        for (int i4 = 0; i4 < list.size(); i4++) {
            Entity entity = (Entity) list.get(i4);
            double d11 = entity.e(b, c, d) / (double) f;

            if (d11 <= 1.0D) {
                double d6 = entity.p - b;
                double d8 = entity.q - c;
                double d10 = entity.r - d;
                double d12 = MathHelper.a(d6 * d6 + d8 * d8 + d10 * d10);

                d6 /= d12;
                d8 /= d12;
                d10 /= d12;
                double d13 = i.a(vec3d, entity.z);
                double d14 = (1.0D - d11) * d13;

                // CraftBukkit start - explosion damage hook
                CraftServer server = ((WorldServer) i).getServer();
                org.bukkit.entity.Entity damagee = null;
                DamageCause damageType;
                int damageDone = (int) (((d14 * d14 + d14) / 2D) * 8D * (double) f + 1.0D);
                
                if(entity instanceof EntityLiving) {
                    damagee = entity.getBukkitEntity();
                }

                if (e == null) { // Block explosion
                    // Craftbukkit TODO: get the x/y/z of the tnt block?
                    damageType = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
                    EntityDamageByBlockEvent edbbe = new EntityDamageByBlockEvent(null, damagee, damageType, damageDone);
                    server.getPluginManager().callEvent(edbbe);
                    if (!edbbe.isCancelled()) {
                        entity.a(e, edbbe.getDamage());
                    }
                } else {
                    CraftEntity damager = null;
                    //TODO org.bukkit.entity.Entity damager = e.getBukkitEntity();
                    if (e instanceof EntityPlayerMP) {
                        // not possible in normal operations
                        damager = new CraftPlayer(server, (EntityPlayerMP) e);
                    } else if (e instanceof EntityLiving) {
                        damager = new CraftLivingEntity(server, (EntityLiving) e);
                    }
                    damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
                                        
                    EntityDamageByEntityEvent edbbe = new EntityDamageByEntityEvent(damager, damagee, damageType, damageDone);
                    server.getPluginManager().callEvent(edbbe);

                    if (!edbbe.isCancelled()) {
                        entity.a(e, edbbe.getDamage());
                    }
                }
                // Craftbukkit end

                double d15 = d14;

                entity.s += d6 * d15;
                entity.t += d8 * d15;
                entity.u += d10 * d15;
            }
        }

        f = f1;
        ArrayList arraylist = new ArrayList();

        ((List) (arraylist)).addAll(((java.util.Collection) (g)));
        if (a) {
            for (int j4 = ((List) (arraylist)).size() - 1; j4 >= 0; j4--) {
                ChunkPosition chunkposition = (ChunkPosition) ((List) (arraylist)).get(j4);
                int i2 = chunkposition.a;
                int k4 = chunkposition.b;
                int l4 = chunkposition.c;
                int i5 = i.a(i2, k4, l4);
                int j5 = i.a(i2, k4 - 1, l4);

                if (i5 == 0 && Block.o[j5] && h.nextInt(3) == 0) {
                    i.e(i2, k4, l4, Block.ar.bi);
                }
            }
        }
    }

    public void b() {
        i.a(b, c, d, "random.explode", 4F, (1.0F + (i.l.nextFloat() - i.l.nextFloat()) * 0.2F) * 0.7F);
        ArrayList arraylist = new ArrayList();

        ((List) (arraylist)).addAll(((java.util.Collection) (g)));

        // Craftbukkit start
        CraftServer servr = ((WorldServer) i).getServer();
        CraftWorld wrld = ((WorldServer) i).getWorld();
        org.bukkit.entity.Entity splode;
        if (this.e instanceof EntityCreeper) {
            splode = new CraftLivingEntity(servr, (EntityLiving) e);
        } else {
            splode = (org.bukkit.entity.Entity) this.e;
        }
        ArrayList blocklist = new ArrayList();
        for (int j = ((List) (arraylist)).size() - 1; j >= 0; j--) {
            ChunkPosition cpos = (ChunkPosition) ((List) (arraylist)).get(j);
            org.bukkit.craftbukkit.block.CraftBlock blox = new org.bukkit.craftbukkit.block.CraftBlock(wrld, cpos.a, cpos.b, cpos.c, wrld.getBlockTypeIdAt(cpos.a, cpos.b, cpos.c), wrld.getBlockAt(cpos.a, cpos.b, cpos.c).getData());
            if (!blox.getType().equals(org.bukkit.Material.AIR)) {
                blocklist.add(blox);
            }
        }

        EntityExplodeEvent eee = new EntityExplodeEvent(EntityExplodeEvent.Type.ENTITY_EXPLODE, splode, blocklist);
        servr.getPluginManager().callEvent(eee);
        if (!(eee.isCancelled())) {
            // Notch start
            for (int j = ((List) (arraylist)).size() - 1; j >= 0; j--) {
                ChunkPosition chunkposition = (ChunkPosition) ((List) (arraylist)).get(j);
                int k = chunkposition.a;
                int l = chunkposition.b;
                int i1 = chunkposition.c;
                int j1 = i.a(k, l, i1);

                for (int k1 = 0; k1 < 1; k1++) {
                    double d1 = (float) k + i.l.nextFloat();
                    double d2 = (float) l + i.l.nextFloat();
                    double d3 = (float) i1 + i.l.nextFloat();
                    double d4 = d1 - b;
                    double d5 = d2 - c;
                    double d6 = d3 - d;
                    double d7 = MathHelper.a(d4 * d4 + d5 * d5 + d6 * d6);

                    d4 /= d7;
                    d5 /= d7;
                    d6 /= d7;
                    double d8 = 0.5D / (d7 / (double) f + 0.10000000000000001D);

                    d8 *= i.l.nextFloat() * i.l.nextFloat() + 0.3F;
                    d4 *= d8;
                    d5 *= d8;
                    d6 *= d8;
                    i.a("explode", (d1 + b * 1.0D) / 2D, (d2 + c * 1.0D) / 2D, (d3 + d * 1.0D) / 2D, d4, d5, d6);
                    i.a("smoke", d1, d2, d3, d4, d5, d6);
                }

                if (j1 > 0) {
                    Block.m[j1].a(i, k, l, i1, i.b(k, l, i1), 0.3F);
                    i.e(k, l, i1, 0);
                    Block.m[j1].a_(i, k, l, i1);
                }
            }
            // Notch end
        }
        //Craftbukkit end
    }
}
