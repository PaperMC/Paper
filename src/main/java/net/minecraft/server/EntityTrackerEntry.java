package net.minecraft.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
// CraftBukkit end

public class EntityTrackerEntry {

    public Entity tracker;
    public int b;
    public int c;
    public int xLoc;
    public int yLoc;
    public int zLoc;
    public int yRot;
    public int xRot;
    public int i;
    public double j;
    public double k;
    public double l;
    public int m;
    private double p;
    private double q;
    private double r;
    private boolean s;
    private boolean isMoving;
    private int u;
    private Entity v;
    private boolean w;
    public boolean n;
    public Set trackedPlayers = new HashSet();

    public EntityTrackerEntry(Entity entity, int i, int j, boolean flag) {
        this.tracker = entity;
        this.b = i;
        this.c = j;
        this.isMoving = flag;
        this.xLoc = MathHelper.floor(entity.locX * 32.0D);
        this.yLoc = MathHelper.floor(entity.locY * 32.0D);
        this.zLoc = MathHelper.floor(entity.locZ * 32.0D);
        this.yRot = MathHelper.d(entity.yaw * 256.0F / 360.0F);
        this.xRot = MathHelper.d(entity.pitch * 256.0F / 360.0F);
        this.i = MathHelper.d(entity.getHeadRotation() * 256.0F / 360.0F);
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry) object).tracker.id == this.tracker.id : false;
    }

    public int hashCode() {
        return this.tracker.id;
    }

    public void track(List list) {
        this.n = false;
        if (!this.s || this.tracker.e(this.p, this.q, this.r) > 16.0D) {
            this.p = this.tracker.locX;
            this.q = this.tracker.locY;
            this.r = this.tracker.locZ;
            this.s = true;
            this.n = true;
            this.scanPlayers(list);
        }

        if (this.v != this.tracker.vehicle || this.tracker.vehicle != null && this.m % 60 == 0) {
            this.v = this.tracker.vehicle;
            this.broadcast(new Packet39AttachEntity(0, this.tracker, this.tracker.vehicle));
        }

        if (this.tracker instanceof EntityItemFrame /*&& this.m % 10 == 0*/) { // CraftBukkit - Moved below, should always enter this block
            EntityItemFrame i3 = (EntityItemFrame) this.tracker;
            ItemStack i4 = i3.getItem();

            if (this.m % 10 == 0 && i4 != null && i4.getItem() instanceof ItemWorldMap) { // CraftBukkit - Moved this.m % 10 logic here so item frames do not enter the other blocks
                WorldMap i6 = Item.MAP.getSavedMap(i4, this.tracker.world);
                Iterator i7 = this.trackedPlayers.iterator(); // CraftBukkit

                while (i7.hasNext()) {
                    EntityHuman i8 = (EntityHuman) i7.next();
                    EntityPlayer i9 = (EntityPlayer) i8;

                    i6.a(i9, i4);
                    if (i9.playerConnection.lowPriorityCount() <= 5) {
                        Packet j0 = Item.MAP.c(i4, this.tracker.world, i9);

                        if (j0 != null) {
                            i9.playerConnection.sendPacket(j0);
                        }
                    }
                }
            }

            this.b();
        } else if (this.m % this.c == 0 || this.tracker.an || this.tracker.getDataWatcher().a()) {
            int i;
            int j;

            if (this.tracker.vehicle == null) {
                ++this.u;
                i = this.tracker.at.a(this.tracker.locX);
                j = MathHelper.floor(this.tracker.locY * 32.0D);
                int k = this.tracker.at.a(this.tracker.locZ);
                int l = MathHelper.d(this.tracker.yaw * 256.0F / 360.0F);
                int i1 = MathHelper.d(this.tracker.pitch * 256.0F / 360.0F);
                int j1 = i - this.xLoc;
                int k1 = j - this.yLoc;
                int l1 = k - this.zLoc;
                Object object = null;
                boolean flag = Math.abs(j1) >= 4 || Math.abs(k1) >= 4 || Math.abs(l1) >= 4 || this.m % 60 == 0;
                boolean flag1 = Math.abs(l - this.yRot) >= 4 || Math.abs(i1 - this.xRot) >= 4;

                // CraftBukkit start - Code moved from below
                if (flag) {
                    this.xLoc = i;
                    this.yLoc = j;
                    this.zLoc = k;
                }

                if (flag1) {
                    this.yRot = l;
                    this.xRot = i1;
                }
                // CraftBukkit end

                if (this.m > 0 || this.tracker instanceof EntityArrow) {
                    if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128 && this.u <= 400 && !this.w) {
                        if (flag && flag1) {
                            object = new Packet33RelEntityMoveLook(this.tracker.id, (byte) j1, (byte) k1, (byte) l1, (byte) l, (byte) i1);
                        } else if (flag) {
                            object = new Packet31RelEntityMove(this.tracker.id, (byte) j1, (byte) k1, (byte) l1);
                        } else if (flag1) {
                            object = new Packet32EntityLook(this.tracker.id, (byte) l, (byte) i1);
                        }
                    } else {
                        this.u = 0;
                        // CraftBukkit start - Refresh list of who can see a player before sending teleport packet
                        if (this.tracker instanceof EntityPlayer) {
                            this.scanPlayers(new java.util.ArrayList(this.trackedPlayers));
                        }
                        // CraftBukkit end
                        object = new Packet34EntityTeleport(this.tracker.id, i, j, k, (byte) l, (byte) i1);
                    }
                }

                if (this.isMoving) {
                    double d0 = this.tracker.motX - this.j;
                    double d1 = this.tracker.motY - this.k;
                    double d2 = this.tracker.motZ - this.l;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > d3 * d3 || d4 > 0.0D && this.tracker.motX == 0.0D && this.tracker.motY == 0.0D && this.tracker.motZ == 0.0D) {
                        this.j = this.tracker.motX;
                        this.k = this.tracker.motY;
                        this.l = this.tracker.motZ;
                        this.broadcast(new Packet28EntityVelocity(this.tracker.id, this.j, this.k, this.l));
                    }
                }

                if (object != null) {
                    this.broadcast((Packet) object);
                }

                this.b();
                /* CraftBukkit start - Code moved up
                if (flag) {
                    this.xLoc = i;
                    this.yLoc = j;
                    this.zLoc = k;
                }

                if (flag1) {
                    this.yRot = l;
                    this.xRot = i1;
                }
                // CraftBukkit end */

                this.w = false;
            } else {
                i = MathHelper.d(this.tracker.yaw * 256.0F / 360.0F);
                j = MathHelper.d(this.tracker.pitch * 256.0F / 360.0F);
                boolean flag2 = Math.abs(i - this.yRot) >= 4 || Math.abs(j - this.xRot) >= 4;

                if (flag2) {
                    this.broadcast(new Packet32EntityLook(this.tracker.id, (byte) i, (byte) j));
                    this.yRot = i;
                    this.xRot = j;
                }

                this.xLoc = this.tracker.at.a(this.tracker.locX);
                this.yLoc = MathHelper.floor(this.tracker.locY * 32.0D);
                this.zLoc = this.tracker.at.a(this.tracker.locZ);
                this.b();
                this.w = true;
            }

            i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
            if (Math.abs(i - this.i) >= 4) {
                this.broadcast(new Packet35EntityHeadRotation(this.tracker.id, (byte) i));
                this.i = i;
            }

            this.tracker.an = false;
        }

        ++this.m;
        if (this.tracker.velocityChanged) {
            // CraftBukkit start - Create PlayerVelocity event
            boolean cancelled = false;

            if (this.tracker instanceof EntityPlayer) {
                Player player = (Player) this.tracker.getBukkitEntity();
                org.bukkit.util.Vector velocity = player.getVelocity();

                PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity);
                this.tracker.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    cancelled = true;
                } else if (!velocity.equals(event.getVelocity())) {
                    player.setVelocity(velocity);
                }
            }

            if (!cancelled) {
                this.broadcastIncludingSelf((Packet) (new Packet28EntityVelocity(this.tracker)));
            }
            // CraftBukkit end

            this.tracker.velocityChanged = false;
        }
    }

    private void b() {
        DataWatcher datawatcher = this.tracker.getDataWatcher();

        if (datawatcher.a()) {
            this.broadcastIncludingSelf(new Packet40EntityMetadata(this.tracker.id, datawatcher, false));
        }

        if (this.tracker instanceof EntityLiving) {
            AttributeMapServer attributemapserver = (AttributeMapServer) ((EntityLiving) this.tracker).aX();
            Set set = attributemapserver.b();

            if (!set.isEmpty()) {
                // CraftBukkit start - Send scaled max health
                if (this.tracker instanceof EntityPlayer) {
                    ((EntityPlayer) this.tracker).getBukkitEntity().injectScaledMaxHealth(set, false);
                }
                // CraftBukkit end
                this.broadcastIncludingSelf(new Packet44UpdateAttributes(this.tracker.id, set));
            }

            set.clear();
        }
    }

    public void broadcast(Packet packet) {
        Iterator iterator = this.trackedPlayers.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            entityplayer.playerConnection.sendPacket(packet);
        }
    }

    public void broadcastIncludingSelf(Packet packet) {
        this.broadcast(packet);
        if (this.tracker instanceof EntityPlayer) {
            ((EntityPlayer) this.tracker).playerConnection.sendPacket(packet);
        }
    }

    public void a() {
        Iterator iterator = this.trackedPlayers.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.id));
        }
    }

    public void a(EntityPlayer entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.id));
            this.trackedPlayers.remove(entityplayer);
        }
    }

    public void updatePlayer(EntityPlayer entityplayer) {
        if (entityplayer != this.tracker) {
            double d0 = entityplayer.locX - (double) (this.xLoc / 32);
            double d1 = entityplayer.locZ - (double) (this.zLoc / 32);

            if (d0 >= (double) (-this.b) && d0 <= (double) this.b && d1 >= (double) (-this.b) && d1 <= (double) this.b) {
                if (!this.trackedPlayers.contains(entityplayer) && (this.d(entityplayer) || this.tracker.p)) {
                    // CraftBukkit start
                    if (this.tracker instanceof EntityPlayer) {
                        Player player = ((EntityPlayer) this.tracker).getBukkitEntity();
                        if (!entityplayer.getBukkitEntity().canSee(player)) {
                            return;
                        }
                    }

                    entityplayer.removeQueue.remove(Integer.valueOf(this.tracker.id));
                    // CraftBukkit end

                    this.trackedPlayers.add(entityplayer);
                    Packet packet = this.c();

                    entityplayer.playerConnection.sendPacket(packet);
                    if (!this.tracker.getDataWatcher().d()) {
                        entityplayer.playerConnection.sendPacket(new Packet40EntityMetadata(this.tracker.id, this.tracker.getDataWatcher(), true));
                    }

                    if (this.tracker instanceof EntityLiving) {
                        AttributeMapServer attributemapserver = (AttributeMapServer) ((EntityLiving) this.tracker).aX();
                        Collection collection = attributemapserver.c();

                        // CraftBukkit start - If sending own attributes send scaled health instead of current maximum health
                        if (this.tracker.id == entityplayer.id) {
                            ((EntityPlayer) this.tracker).getBukkitEntity().injectScaledMaxHealth(collection, false);
                        }
                        // CraftBukkit end
                        if (!collection.isEmpty()) {
                            entityplayer.playerConnection.sendPacket(new Packet44UpdateAttributes(this.tracker.id, collection));
                        }
                    }

                    this.j = this.tracker.motX;
                    this.k = this.tracker.motY;
                    this.l = this.tracker.motZ;
                    if (this.isMoving && !(packet instanceof Packet24MobSpawn)) {
                        entityplayer.playerConnection.sendPacket(new Packet28EntityVelocity(this.tracker.id, this.tracker.motX, this.tracker.motY, this.tracker.motZ));
                    }

                    if (this.tracker.vehicle != null) {
                        entityplayer.playerConnection.sendPacket(new Packet39AttachEntity(0, this.tracker, this.tracker.vehicle));
                    }

                    // CraftBukkit start
                    if (this.tracker.passenger != null) {
                        entityplayer.playerConnection.sendPacket(new Packet39AttachEntity(0, this.tracker.passenger, this.tracker));
                    }
                    // CraftBukkit end

                    if (this.tracker instanceof EntityInsentient && ((EntityInsentient) this.tracker).getLeashHolder() != null) {
                        entityplayer.playerConnection.sendPacket(new Packet39AttachEntity(1, this.tracker, ((EntityInsentient) this.tracker).getLeashHolder()));
                    }

                    if (this.tracker instanceof EntityLiving) {
                        for (int i = 0; i < 5; ++i) {
                            ItemStack itemstack = ((EntityLiving) this.tracker).getEquipment(i);

                            if (itemstack != null) {
                                entityplayer.playerConnection.sendPacket(new Packet5EntityEquipment(this.tracker.id, i, itemstack));
                            }
                        }
                    }

                    if (this.tracker instanceof EntityHuman) {
                        EntityHuman entityhuman = (EntityHuman) this.tracker;

                        if (entityhuman.isSleeping()) {
                            entityplayer.playerConnection.sendPacket(new Packet17EntityLocationAction(this.tracker, 0, MathHelper.floor(this.tracker.locX), MathHelper.floor(this.tracker.locY), MathHelper.floor(this.tracker.locZ)));
                        }
                    }

                    // CraftBukkit start - Fix for nonsensical head yaw
                    this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F); // tracker.ao() should be getHeadRotation
                    this.broadcast(new Packet35EntityHeadRotation(this.tracker.id, (byte) i));
                    // CraftBukkit end

                    if (this.tracker instanceof EntityLiving) {
                        EntityLiving entityliving = (EntityLiving) this.tracker;
                        Iterator iterator = entityliving.getEffects().iterator();

                        while (iterator.hasNext()) {
                            MobEffect mobeffect = (MobEffect) iterator.next();

                            entityplayer.playerConnection.sendPacket(new Packet41MobEffect(this.tracker.id, mobeffect));
                        }
                    }
                }
            } else if (this.trackedPlayers.contains(entityplayer)) {
                this.trackedPlayers.remove(entityplayer);
                entityplayer.removeQueue.add(Integer.valueOf(this.tracker.id));
            }
        }
    }

    private boolean d(EntityPlayer entityplayer) {
        return entityplayer.p().getPlayerChunkMap().a(entityplayer, this.tracker.aj, this.tracker.al);
    }

    public void scanPlayers(List list) {
        for (int i = 0; i < list.size(); ++i) {
            this.updatePlayer((EntityPlayer) list.get(i));
        }
    }

    private Packet c() {
        if (this.tracker.dead) {
            // CraftBukkit start - Remove useless error spam, just return
            // this.tracker.world.getLogger().warning("Fetching addPacket for removed entity");
            return null;
            // CraftBukkit end
        }

        if (this.tracker instanceof EntityItem) {
            return new Packet23VehicleSpawn(this.tracker, 2, 1);
        } else if (this.tracker instanceof EntityPlayer) {
            return new Packet20NamedEntitySpawn((EntityHuman) this.tracker);
        } else if (this.tracker instanceof EntityMinecartAbstract) {
            EntityMinecartAbstract entityminecartabstract = (EntityMinecartAbstract) this.tracker;

            return new Packet23VehicleSpawn(this.tracker, 10, entityminecartabstract.getType());
        } else if (this.tracker instanceof EntityBoat) {
            return new Packet23VehicleSpawn(this.tracker, 1);
        } else if (!(this.tracker instanceof IAnimal) && !(this.tracker instanceof EntityEnderDragon)) {
            if (this.tracker instanceof EntityFishingHook) {
                EntityHuman entityhuman = ((EntityFishingHook) this.tracker).owner;

                return new Packet23VehicleSpawn(this.tracker, 90, entityhuman != null ? entityhuman.id : this.tracker.id);
            } else if (this.tracker instanceof EntityArrow) {
                Entity entity = ((EntityArrow) this.tracker).shooter;

                return new Packet23VehicleSpawn(this.tracker, 60, entity != null ? entity.id : this.tracker.id);
            } else if (this.tracker instanceof EntitySnowball) {
                return new Packet23VehicleSpawn(this.tracker, 61);
            } else if (this.tracker instanceof EntityPotion) {
                return new Packet23VehicleSpawn(this.tracker, 73, ((EntityPotion) this.tracker).getPotionValue());
            } else if (this.tracker instanceof EntityThrownExpBottle) {
                return new Packet23VehicleSpawn(this.tracker, 75);
            } else if (this.tracker instanceof EntityEnderPearl) {
                return new Packet23VehicleSpawn(this.tracker, 65);
            } else if (this.tracker instanceof EntityEnderSignal) {
                return new Packet23VehicleSpawn(this.tracker, 72);
            } else if (this.tracker instanceof EntityFireworks) {
                return new Packet23VehicleSpawn(this.tracker, 76);
            } else {
                Packet23VehicleSpawn packet23vehiclespawn;

                if (this.tracker instanceof EntityFireball) {
                    EntityFireball entityfireball = (EntityFireball) this.tracker;

                    packet23vehiclespawn = null;
                    byte b0 = 63;

                    if (this.tracker instanceof EntitySmallFireball) {
                        b0 = 64;
                    } else if (this.tracker instanceof EntityWitherSkull) {
                        b0 = 66;
                    }

                    if (entityfireball.shooter != null) {
                        packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, b0, ((EntityFireball) this.tracker).shooter.id);
                    } else {
                        packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, b0, 0);
                    }

                    packet23vehiclespawn.e = (int) (entityfireball.dirX * 8000.0D);
                    packet23vehiclespawn.f = (int) (entityfireball.dirY * 8000.0D);
                    packet23vehiclespawn.g = (int) (entityfireball.dirZ * 8000.0D);
                    return packet23vehiclespawn;
                } else if (this.tracker instanceof EntityEgg) {
                    return new Packet23VehicleSpawn(this.tracker, 62);
                } else if (this.tracker instanceof EntityTNTPrimed) {
                    return new Packet23VehicleSpawn(this.tracker, 50);
                } else if (this.tracker instanceof EntityEnderCrystal) {
                    return new Packet23VehicleSpawn(this.tracker, 51);
                } else if (this.tracker instanceof EntityFallingBlock) {
                    EntityFallingBlock entityfallingblock = (EntityFallingBlock) this.tracker;

                    return new Packet23VehicleSpawn(this.tracker, 70, entityfallingblock.id | entityfallingblock.data << 16);
                } else if (this.tracker instanceof EntityPainting) {
                    return new Packet25EntityPainting((EntityPainting) this.tracker);
                } else if (this.tracker instanceof EntityItemFrame) {
                    EntityItemFrame entityitemframe = (EntityItemFrame) this.tracker;

                    packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 71, entityitemframe.direction);
                    packet23vehiclespawn.b = MathHelper.d((float) (entityitemframe.x * 32));
                    packet23vehiclespawn.c = MathHelper.d((float) (entityitemframe.y * 32));
                    packet23vehiclespawn.d = MathHelper.d((float) (entityitemframe.z * 32));
                    return packet23vehiclespawn;
                } else if (this.tracker instanceof EntityLeash) {
                    EntityLeash entityleash = (EntityLeash) this.tracker;

                    packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 77);
                    packet23vehiclespawn.b = MathHelper.d((float) (entityleash.x * 32));
                    packet23vehiclespawn.c = MathHelper.d((float) (entityleash.y * 32));
                    packet23vehiclespawn.d = MathHelper.d((float) (entityleash.z * 32));
                    return packet23vehiclespawn;
                } else if (this.tracker instanceof EntityExperienceOrb) {
                    return new Packet26AddExpOrb((EntityExperienceOrb) this.tracker);
                } else {
                    throw new IllegalArgumentException("Don\'t know how to add " + this.tracker.getClass() + "!");
                }
            }
        } else {
            this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
            return new Packet24MobSpawn((EntityLiving) this.tracker);
        }
    }

    public void clear(EntityPlayer entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            this.trackedPlayers.remove(entityplayer);
            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.id));
        }
    }
}
