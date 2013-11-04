package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.Location;
// CraftBukkit end

public class Explosion {

    public boolean a;
    public boolean b = true;
    private int i = 16;
    private Random j = new Random();
    private World world;
    public double posX;
    public double posY;
    public double posZ;
    public Entity source;
    public float size;
    public List blocks = new ArrayList();
    private Map l = new HashMap();
    public boolean wasCanceled = false; // CraftBukkit

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f) {
        this.world = world;
        this.source = entity;
        this.size = (float) Math.max(f, 0.0); // CraftBukkit - clamp bad values
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
    }

    public void a() {
        // CraftBukkit start
        if (this.size < 0.1F) {
            return;
        }
        // CraftBukkit end

        float f = this.size;
        HashSet hashset = new HashSet();

        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < this.i; ++i) {
            for (j = 0; j < this.i; ++j) {
                for (k = 0; k < this.i; ++k) {
                    if (i == 0 || i == this.i - 1 || j == 0 || j == this.i - 1 || k == 0 || k == this.i - 1) {
                        double d3 = (double) ((float) i / ((float) this.i - 1.0F) * 2.0F - 1.0F);
                        double d4 = (double) ((float) j / ((float) this.i - 1.0F) * 2.0F - 1.0F);
                        double d5 = (double) ((float) k / ((float) this.i - 1.0F) * 2.0F - 1.0F);
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);

                        d0 = this.posX;
                        d1 = this.posY;
                        d2 = this.posZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            int l = MathHelper.floor(d0);
                            int i1 = MathHelper.floor(d1);
                            int j1 = MathHelper.floor(d2);
                            Block block = this.world.getType(l, i1, j1);

                            if (block.getMaterial() != Material.AIR) {
                                float f3 = this.source != null ? this.source.a(this, this.world, l, i1, j1, block) : block.a(this.source);

                                f1 -= (f3 + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && (this.source == null || this.source.a(this, this.world, l, i1, j1, block, f1)) && i1 < 256 && i1 >= 0) { // CraftBukkit - don't wrap explosions
                                hashset.add(new ChunkPosition(l, i1, j1));
                            }

                            d0 += d3 * (double) f2;
                            d1 += d4 * (double) f2;
                            d2 += d5 * (double) f2;
                        }
                    }
                }
            }
        }

        this.blocks.addAll(hashset);
        this.size *= 2.0F;
        i = MathHelper.floor(this.posX - (double) this.size - 1.0D);
        j = MathHelper.floor(this.posX + (double) this.size + 1.0D);
        k = MathHelper.floor(this.posY - (double) this.size - 1.0D);
        int k1 = MathHelper.floor(this.posY + (double) this.size + 1.0D);
        int l1 = MathHelper.floor(this.posZ - (double) this.size - 1.0D);
        int i2 = MathHelper.floor(this.posZ + (double) this.size + 1.0D);
        List list = this.world.getEntities(this.source, AxisAlignedBB.a().a((double) i, (double) k, (double) l1, (double) j, (double) k1, (double) i2));
        Vec3D vec3d = this.world.getVec3DPool().create(this.posX, this.posY, this.posZ);

        for (int j2 = 0; j2 < list.size(); ++j2) {
            Entity entity = (Entity) list.get(j2);
            double d7 = entity.f(this.posX, this.posY, this.posZ) / (double) this.size;

            if (d7 <= 1.0D) {
                d0 = entity.locX - this.posX;
                d1 = entity.locY + (double) entity.getHeadHeight() - this.posY;
                d2 = entity.locZ - this.posZ;
                double d8 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                if (d8 != 0.0D) {
                    d0 /= d8;
                    d1 /= d8;
                    d2 /= d8;
                    double d9 = (double) this.world.a(vec3d, entity.boundingBox);
                    double d10 = (1.0D - d7) * d9;

                    // CraftBukkit start - Explosion damage hook
                    org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                    float damageDone = (float) ((int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) this.size + 1.0D));

                    if (damagee == null) {
                        // nothing was hurt
                    } else if (this.source == null) { // Block explosion (without an entity source; bed etc.)
                        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, damagee, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, damageDone);
                        Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            damagee.setLastDamageCause(event);
                            entity.damageEntity(DamageSource.explosion(this), (float) event.getDamage());
                            double d11 = EnchantmentProtection.a(entity, d10);

                            entity.motX += d0 * d11;
                            entity.motY += d1 * d11;
                            entity.motZ += d2 * d11;
                            if (entity instanceof EntityHuman) {
                                this.l.put((EntityHuman) entity, this.world.getVec3DPool().create(d0 * d10, d1 * d10, d2 * d10));
                            }
                        }
                    } else {
                        final org.bukkit.entity.Entity damager = this.source.getBukkitEntity();
                        final EntityDamageEvent.DamageCause damageCause;

                        if (damager instanceof org.bukkit.entity.TNTPrimed) {
                            damageCause = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
                        } else {
                            damageCause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
                        }

                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageCause, damageDone);
                        Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            entity.getBukkitEntity().setLastDamageCause(event);
                            entity.damageEntity(DamageSource.explosion(this), (float) event.getDamage());

                            entity.motX += d0 * d10;
                            entity.motY += d1 * d10;
                            entity.motZ += d2 * d10;
                            if (entity instanceof EntityHuman) {
                                this.l.put((EntityHuman) entity, this.world.getVec3DPool().create(d0 * d10, d1 * d10, d2 * d10));
                            }
                        }
                    }
                    // CraftBukkit end
                }
            }
        }

        this.size = f;
    }

    public void a(boolean flag) {
        this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
        if (this.size >= 2.0F && this.b) {
            this.world.addParticle("hugeexplosion", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
        } else {
            this.world.addParticle("largeexplode", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
        }

        Iterator iterator;
        ChunkPosition chunkposition;
        int i;
        int j;
        int k;
        Block block;

        if (this.b) {
            // CraftBukkit start
            org.bukkit.World bworld = this.world.getWorld();
            org.bukkit.entity.Entity explode = this.source == null ? null : this.source.getBukkitEntity();
            Location location = new Location(bworld, this.posX, this.posY, this.posZ);

            List<org.bukkit.block.Block> blockList = new ArrayList<org.bukkit.block.Block>();
            for (int i1 = this.blocks.size() - 1; i1 >= 0; i1--) {
                ChunkPosition cpos = (ChunkPosition) this.blocks.get(i1);
                org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.x, cpos.y, cpos.z);
                if (bblock.getType() != org.bukkit.Material.AIR) {
                    blockList.add(bblock);
                }
            }

            EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, 0.3F);
            this.world.getServer().getPluginManager().callEvent(event);

            this.blocks.clear();

            for (org.bukkit.block.Block bblock : event.blockList()) {
                ChunkPosition coords = new ChunkPosition(bblock.getX(), bblock.getY(), bblock.getZ());
                blocks.add(coords);
            }

            if (event.isCancelled()) {
                this.wasCanceled = true;
                return;
            }
            // CraftBukkit end

            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                chunkposition = (ChunkPosition) iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                block = this.world.getType(i, j, k);
                if (flag) {
                    double d0 = (double) ((float) i + this.world.random.nextFloat());
                    double d1 = (double) ((float) j + this.world.random.nextFloat());
                    double d2 = (double) ((float) k + this.world.random.nextFloat());
                    double d3 = d0 - this.posX;
                    double d4 = d1 - this.posY;
                    double d5 = d2 - this.posZ;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                    d7 *= (double) (this.world.random.nextFloat() * this.world.random.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.world.addParticle("explode", (d0 + this.posX * 1.0D) / 2.0D, (d1 + this.posY * 1.0D) / 2.0D, (d2 + this.posZ * 1.0D) / 2.0D, d3, d4, d5);
                    this.world.addParticle("smoke", d0, d1, d2, d3, d4, d5);
                }

                if (block.getMaterial() != Material.AIR) {
                    if (block.a(this)) {
                        // CraftBukkit - add yield
                        block.dropNaturally(this.world, i, j, k, this.world.getData(i, j, k), event.getYield(), 0);
                    }

                    this.world.setTypeAndData(i, j, k, Blocks.AIR, 0, 3);
                    block.wasExploded(this.world, i, j, k, this);
                }
            }
        }

        if (this.a) {
            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                chunkposition = (ChunkPosition) iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                block = this.world.getType(i, j, k);
                Block block1 = this.world.getType(i, j - 1, k);

                if (block.getMaterial() == Material.AIR && block1.j() && this.j.nextInt(3) == 0) {
                    // CraftBukkit start - Ignition by explosion
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(this.world, i, j, k, this).isCancelled()) {
                        this.world.setTypeUpdate(i, j, k, Blocks.FIRE);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public Map b() {
        return this.l;
    }

    public EntityLiving c() {
        return this.source == null ? null : (this.source instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.source).getSource() : (this.source instanceof EntityLiving ? (EntityLiving) this.source : null));
    }
}
