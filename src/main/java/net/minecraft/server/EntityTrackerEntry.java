package net.minecraft.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
// CraftBukkit end

public class EntityTrackerEntry {

    private static final Logger p = LogManager.getLogger();
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
    private double q;
    private double r;
    private double s;
    private boolean isMoving;
    private boolean u;
    private int v;
    private Entity w;
    private boolean x;
    public boolean n;
    public Set trackedPlayers = new HashSet();

    public EntityTrackerEntry(Entity entity, int i, int j, boolean flag) {
        this.tracker = entity;
        this.b = i;
        this.c = j;
        this.u = flag;
        this.xLoc = MathHelper.floor(entity.locX * 32.0D);
        this.yLoc = MathHelper.floor(entity.locY * 32.0D);
        this.zLoc = MathHelper.floor(entity.locZ * 32.0D);
        this.yRot = MathHelper.d(entity.yaw * 256.0F / 360.0F);
        this.xRot = MathHelper.d(entity.pitch * 256.0F / 360.0F);
        this.i = MathHelper.d(entity.getHeadRotation() * 256.0F / 360.0F);
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry) object).tracker.getId() == this.tracker.getId() : false;
    }

    public int hashCode() {
        return this.tracker.getId();
    }

    public void track(List list) {
        this.n = false;
        if (!this.isMoving || this.tracker.e(this.q, this.r, this.s) > 16.0D) {
            this.q = this.tracker.locX;
            this.r = this.tracker.locY;
            this.s = this.tracker.locZ;
            this.isMoving = true;
            this.n = true;
            this.scanPlayers(list);
        }

        if (this.w != this.tracker.vehicle || this.tracker.vehicle != null && this.m % 60 == 0) {
            this.w = this.tracker.vehicle;
            this.broadcast(new PacketPlayOutAttachEntity(0, this.tracker, this.tracker.vehicle));
        }

        if (this.tracker instanceof EntityItemFrame /*&& this.m % 10 == 0*/) { // CraftBukkit - Moved below, should always enter this block
            EntityItemFrame i3 = (EntityItemFrame) this.tracker;
            ItemStack i4 = i3.getItem();

            if (this.m % 10 == 0 && i4 != null && i4.getItem() instanceof ItemWorldMap) { // CraftBukkit - Moved this.m % 10 logic here so item frames do not enter the other blocks
                WorldMap i6 = Items.MAP.getSavedMap(i4, this.tracker.world);
                Iterator i7 = this.trackedPlayers.iterator(); // CraftBukkit

                while (i7.hasNext()) {
                    EntityHuman i8 = (EntityHuman) i7.next();
                    EntityPlayer i9 = (EntityPlayer) i8;

                    i6.a(i9, i4);
                    Packet j0 = Items.MAP.c(i4, this.tracker.world, i9);

                    if (j0 != null) {
                        i9.playerConnection.sendPacket(j0);
                    }
                }
            }

            this.b();
        } else if (this.m % this.c == 0 || this.tracker.am || this.tracker.getDataWatcher().a()) {
            int i;
            int j;

            if (this.tracker.vehicle == null) {
                ++this.v;
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
                    if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128 && this.v <= 400 && !this.x) {
                        if (flag && flag1) {
                            object = new PacketPlayOutRelEntityMoveLook(this.tracker.getId(), (byte) j1, (byte) k1, (byte) l1, (byte) l, (byte) i1);
                        } else if (flag) {
                            object = new PacketPlayOutRelEntityMove(this.tracker.getId(), (byte) j1, (byte) k1, (byte) l1);
                        } else if (flag1) {
                            object = new PacketPlayOutEntityLook(this.tracker.getId(), (byte) l, (byte) i1);
                        }
                    } else {
                        this.v = 0;
                        // CraftBukkit start - Refresh list of who can see a player before sending teleport packet
                        if (this.tracker instanceof EntityPlayer) {
                            this.scanPlayers(new java.util.ArrayList(this.trackedPlayers));
                        }
                        // CraftBukkit end
                        object = new PacketPlayOutEntityTeleport(this.tracker.getId(), i, j, k, (byte) l, (byte) i1);
                    }
                }

                if (this.u) {
                    double d0 = this.tracker.motX - this.j;
                    double d1 = this.tracker.motY - this.k;
                    double d2 = this.tracker.motZ - this.l;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > d3 * d3 || d4 > 0.0D && this.tracker.motX == 0.0D && this.tracker.motY == 0.0D && this.tracker.motZ == 0.0D) {
                        this.j = this.tracker.motX;
                        this.k = this.tracker.motY;
                        this.l = this.tracker.motZ;
                        this.broadcast(new PacketPlayOutEntityVelocity(this.tracker.getId(), this.j, this.k, this.l));
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

                this.x = false;
            } else {
                i = MathHelper.d(this.tracker.yaw * 256.0F / 360.0F);
                j = MathHelper.d(this.tracker.pitch * 256.0F / 360.0F);
                boolean flag2 = Math.abs(i - this.yRot) >= 4 || Math.abs(j - this.xRot) >= 4;

                if (flag2) {
                    this.broadcast(new PacketPlayOutEntityLook(this.tracker.getId(), (byte) i, (byte) j));
                    this.yRot = i;
                    this.xRot = j;
                }

                this.xLoc = this.tracker.at.a(this.tracker.locX);
                this.yLoc = MathHelper.floor(this.tracker.locY * 32.0D);
                this.zLoc = this.tracker.at.a(this.tracker.locZ);
                this.b();
                this.x = true;
            }

            i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
            if (Math.abs(i - this.i) >= 4) {
                this.broadcast(new PacketPlayOutEntityHeadRotation(this.tracker, (byte) i));
                this.i = i;
            }

            this.tracker.am = false;
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
                this.broadcastIncludingSelf((Packet) (new PacketPlayOutEntityVelocity(this.tracker)));
            }
            // CraftBukkit end

            this.tracker.velocityChanged = false;
        }
    }

    private void b() {
        DataWatcher datawatcher = this.tracker.getDataWatcher();

        if (datawatcher.a()) {
            this.broadcastIncludingSelf(new PacketPlayOutEntityMetadata(this.tracker.getId(), datawatcher, false));
        }

        if (this.tracker instanceof EntityLiving) {
            AttributeMapServer attributemapserver = (AttributeMapServer) ((EntityLiving) this.tracker).bc();
            Set set = attributemapserver.b();

            if (!set.isEmpty()) {
                // CraftBukkit start - Send scaled max health
                if (this.tracker instanceof EntityPlayer) {
                    ((EntityPlayer) this.tracker).getBukkitEntity().injectScaledMaxHealth(set, false);
                }
                // CraftBukkit end
                this.broadcastIncludingSelf(new PacketPlayOutUpdateAttributes(this.tracker.getId(), set));
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

            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.getId()));
        }
    }

    public void a(EntityPlayer entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.getId()));
            this.trackedPlayers.remove(entityplayer);
        }
    }

    public void updatePlayer(EntityPlayer entityplayer) {
        if (entityplayer != this.tracker) {
            double d0 = entityplayer.locX - (double) (this.xLoc / 32);
            double d1 = entityplayer.locZ - (double) (this.zLoc / 32);

            if (d0 >= (double) (-this.b) && d0 <= (double) this.b && d1 >= (double) (-this.b) && d1 <= (double) this.b) {
                if (!this.trackedPlayers.contains(entityplayer) && (this.d(entityplayer) || this.tracker.o)) {
                    // CraftBukkit start
                    if (this.tracker instanceof EntityPlayer) {
                        Player player = ((EntityPlayer) this.tracker).getBukkitEntity();
                        if (!entityplayer.getBukkitEntity().canSee(player)) {
                            return;
                        }
                    }

                    entityplayer.removeQueue.remove(Integer.valueOf(this.tracker.getId()));
                    // CraftBukkit end

                    this.trackedPlayers.add(entityplayer);
                    Packet packet = this.c();

                    entityplayer.playerConnection.sendPacket(packet);
                    if (!this.tracker.getDataWatcher().d()) {
                        entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(this.tracker.getId(), this.tracker.getDataWatcher(), true));
                    }

                    if (this.tracker instanceof EntityLiving) {
                        AttributeMapServer attributemapserver = (AttributeMapServer) ((EntityLiving) this.tracker).bc();
                        Collection collection = attributemapserver.c();

                        // CraftBukkit start - If sending own attributes send scaled health instead of current maximum health
                        if (this.tracker.getId() == entityplayer.getId()) {
                            ((EntityPlayer) this.tracker).getBukkitEntity().injectScaledMaxHealth(collection, false);
                        }
                        // CraftBukkit end
                        if (!collection.isEmpty()) {
                            entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateAttributes(this.tracker.getId(), collection));
                        }
                    }

                    this.j = this.tracker.motX;
                    this.k = this.tracker.motY;
                    this.l = this.tracker.motZ;
                    if (this.u && !(packet instanceof PacketPlayOutSpawnEntityLiving)) {
                        entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityVelocity(this.tracker.getId(), this.tracker.motX, this.tracker.motY, this.tracker.motZ));
                    }

                    if (this.tracker.vehicle != null) {
                        entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this.tracker, this.tracker.vehicle));
                    }

                    // CraftBukkit start
                    if (this.tracker.passenger != null) {
                        entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(0, this.tracker.passenger, this.tracker));
                    }
                    // CraftBukkit end

                    if (this.tracker instanceof EntityInsentient && ((EntityInsentient) this.tracker).getLeashHolder() != null) {
                        entityplayer.playerConnection.sendPacket(new PacketPlayOutAttachEntity(1, this.tracker, ((EntityInsentient) this.tracker).getLeashHolder()));
                    }

                    if (this.tracker instanceof EntityLiving) {
                        for (int i = 0; i < 5; ++i) {
                            ItemStack itemstack = ((EntityLiving) this.tracker).getEquipment(i);

                            if (itemstack != null) {
                                entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityEquipment(this.tracker.getId(), i, itemstack));
                            }
                        }
                    }

                    if (this.tracker instanceof EntityHuman) {
                        EntityHuman entityhuman = (EntityHuman) this.tracker;

                        if (entityhuman.isSleeping()) {
                            entityplayer.playerConnection.sendPacket(new PacketPlayOutBed(entityhuman, MathHelper.floor(this.tracker.locX), MathHelper.floor(this.tracker.locY), MathHelper.floor(this.tracker.locZ)));
                        }
                    }

                    // CraftBukkit start - Fix for nonsensical head yaw
                    this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F); // tracker.ao() should be getHeadRotation
                    this.broadcast(new PacketPlayOutEntityHeadRotation(this.tracker, (byte) i));
                    // CraftBukkit end

                    if (this.tracker instanceof EntityLiving) {
                        EntityLiving entityliving = (EntityLiving) this.tracker;
                        Iterator iterator = entityliving.getEffects().iterator();

                        while (iterator.hasNext()) {
                            MobEffect mobeffect = (MobEffect) iterator.next();

                            entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityEffect(this.tracker.getId(), mobeffect));
                        }
                    }
                }
            } else if (this.trackedPlayers.contains(entityplayer)) {
                this.trackedPlayers.remove(entityplayer);
                entityplayer.removeQueue.add(Integer.valueOf(this.tracker.getId()));
            }
        }
    }

    private boolean d(EntityPlayer entityplayer) {
        return entityplayer.r().getPlayerChunkMap().a(entityplayer, this.tracker.ai, this.tracker.ak);
    }

    public void scanPlayers(List list) {
        for (int i = 0; i < list.size(); ++i) {
            this.updatePlayer((EntityPlayer) list.get(i));
        }
    }

    private Packet c() {
        if (this.tracker.dead) {
            // CraftBukkit start - Remove useless error spam, just return
            // p.warn("Fetching addPacket for removed entity");
            return null;
            // CraftBukkit end
        }

        if (this.tracker instanceof EntityItem) {
            return new PacketPlayOutSpawnEntity(this.tracker, 2, 1);
        } else if (this.tracker instanceof EntityPlayer) {
            return new PacketPlayOutNamedEntitySpawn((EntityHuman) this.tracker);
        } else if (this.tracker instanceof EntityMinecartAbstract) {
            EntityMinecartAbstract entityminecartabstract = (EntityMinecartAbstract) this.tracker;

            return new PacketPlayOutSpawnEntity(this.tracker, 10, entityminecartabstract.m());
        } else if (this.tracker instanceof EntityBoat) {
            return new PacketPlayOutSpawnEntity(this.tracker, 1);
        } else if (!(this.tracker instanceof IAnimal) && !(this.tracker instanceof EntityEnderDragon)) {
            if (this.tracker instanceof EntityFishingHook) {
                EntityHuman entityhuman = ((EntityFishingHook) this.tracker).owner;

                return new PacketPlayOutSpawnEntity(this.tracker, 90, entityhuman != null ? entityhuman.getId() : this.tracker.getId());
            } else if (this.tracker instanceof EntityArrow) {
                Entity entity = ((EntityArrow) this.tracker).shooter;

                return new PacketPlayOutSpawnEntity(this.tracker, 60, entity != null ? entity.getId() : this.tracker.getId());
            } else if (this.tracker instanceof EntitySnowball) {
                return new PacketPlayOutSpawnEntity(this.tracker, 61);
            } else if (this.tracker instanceof EntityPotion) {
                return new PacketPlayOutSpawnEntity(this.tracker, 73, ((EntityPotion) this.tracker).getPotionValue());
            } else if (this.tracker instanceof EntityThrownExpBottle) {
                return new PacketPlayOutSpawnEntity(this.tracker, 75);
            } else if (this.tracker instanceof EntityEnderPearl) {
                return new PacketPlayOutSpawnEntity(this.tracker, 65);
            } else if (this.tracker instanceof EntityEnderSignal) {
                return new PacketPlayOutSpawnEntity(this.tracker, 72);
            } else if (this.tracker instanceof EntityFireworks) {
                return new PacketPlayOutSpawnEntity(this.tracker, 76);
            } else {
                PacketPlayOutSpawnEntity packetplayoutspawnentity;

                if (this.tracker instanceof EntityFireball) {
                    EntityFireball entityfireball = (EntityFireball) this.tracker;

                    packetplayoutspawnentity = null;
                    byte b0 = 63;

                    if (this.tracker instanceof EntitySmallFireball) {
                        b0 = 64;
                    } else if (this.tracker instanceof EntityWitherSkull) {
                        b0 = 66;
                    }

                    if (entityfireball.shooter != null) {
                        packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, b0, ((EntityFireball) this.tracker).shooter.getId());
                    } else {
                        packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, b0, 0);
                    }

                    packetplayoutspawnentity.d((int) (entityfireball.dirX * 8000.0D));
                    packetplayoutspawnentity.e((int) (entityfireball.dirY * 8000.0D));
                    packetplayoutspawnentity.f((int) (entityfireball.dirZ * 8000.0D));
                    return packetplayoutspawnentity;
                } else if (this.tracker instanceof EntityEgg) {
                    return new PacketPlayOutSpawnEntity(this.tracker, 62);
                } else if (this.tracker instanceof EntityTNTPrimed) {
                    return new PacketPlayOutSpawnEntity(this.tracker, 50);
                } else if (this.tracker instanceof EntityEnderCrystal) {
                    return new PacketPlayOutSpawnEntity(this.tracker, 51);
                } else if (this.tracker instanceof EntityFallingBlock) {
                    EntityFallingBlock entityfallingblock = (EntityFallingBlock) this.tracker;

                    return new PacketPlayOutSpawnEntity(this.tracker, 70, Block.b(entityfallingblock.f()) | entityfallingblock.data << 16);
                } else if (this.tracker instanceof EntityPainting) {
                    return new PacketPlayOutSpawnEntityPainting((EntityPainting) this.tracker);
                } else if (this.tracker instanceof EntityItemFrame) {
                    EntityItemFrame entityitemframe = (EntityItemFrame) this.tracker;

                    packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, 71, entityitemframe.direction);
                    packetplayoutspawnentity.a(MathHelper.d((float) (entityitemframe.x * 32)));
                    packetplayoutspawnentity.b(MathHelper.d((float) (entityitemframe.y * 32)));
                    packetplayoutspawnentity.c(MathHelper.d((float) (entityitemframe.z * 32)));
                    return packetplayoutspawnentity;
                } else if (this.tracker instanceof EntityLeash) {
                    EntityLeash entityleash = (EntityLeash) this.tracker;

                    packetplayoutspawnentity = new PacketPlayOutSpawnEntity(this.tracker, 77);
                    packetplayoutspawnentity.a(MathHelper.d((float) (entityleash.x * 32)));
                    packetplayoutspawnentity.b(MathHelper.d((float) (entityleash.y * 32)));
                    packetplayoutspawnentity.c(MathHelper.d((float) (entityleash.z * 32)));
                    return packetplayoutspawnentity;
                } else if (this.tracker instanceof EntityExperienceOrb) {
                    return new PacketPlayOutSpawnEntityExperienceOrb((EntityExperienceOrb) this.tracker);
                } else {
                    throw new IllegalArgumentException("Don\'t know how to add " + this.tracker.getClass() + "!");
                }
            }
        } else {
            this.i = MathHelper.d(this.tracker.getHeadRotation() * 256.0F / 360.0F);
            return new PacketPlayOutSpawnEntityLiving((EntityLiving) this.tracker);
        }
    }

    public void clear(EntityPlayer entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            this.trackedPlayers.remove(entityplayer);
            entityplayer.removeQueue.add(Integer.valueOf(this.tracker.getId()));
        }
    }
}
