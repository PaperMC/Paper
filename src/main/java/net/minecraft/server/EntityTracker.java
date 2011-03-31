package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EntityTracker {

    private Set a = new HashSet();
    private EntityList b = new EntityList();
    private MinecraftServer c;
    private int d;

    public EntityTracker(MinecraftServer minecraftserver) {
        this.c = minecraftserver;
        this.d = minecraftserver.f.a();
    }

    // CraftBukkit -- synchronized
    public synchronized void a(Entity entity) {
        if (entity instanceof EntityPlayer) {
            this.a(entity, 512, 2);
            EntityPlayer entityplayer = (EntityPlayer) entity;
            Iterator iterator = this.a.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                if (entitytrackerentry.a != entityplayer) {
                    entitytrackerentry.b(entityplayer);
                }
            }
        } else if (entity instanceof EntityFish) {
            this.a(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            this.a(entity, 64, 5, true);
        } else if (entity instanceof EntitySnowball) {
            this.a(entity, 64, 5, true);
        } else if (entity instanceof EntityEgg) {
            this.a(entity, 64, 5, true);
        } else if (entity instanceof EntityItem) {
            this.a(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecart) {
            this.a(entity, 160, 5, true);
        } else if (entity instanceof EntityBoat) {
            this.a(entity, 160, 5, true);
        } else if (entity instanceof EntitySquid) {
            this.a(entity, 160, 3, true);
        } else if (entity instanceof IAnimal) {
            this.a(entity, 160, 3);
        } else if (entity instanceof EntityTNTPrimed) {
            this.a(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingSand) {
            this.a(entity, 160, 20, true);
        } else if (entity instanceof EntityPainting) {
            this.a(entity, 160, Integer.MAX_VALUE, false);
        }
    }

    public void a(Entity entity, int i, int j) {
        this.a(entity, i, j, false);
    }

    // CraftBukkit -- synchronized
    public synchronized void a(Entity entity, int i, int j, boolean flag) {
        if (i > this.d) {
            i = this.d;
        }

        if (this.b.b(entity.id)) {
            throw new IllegalStateException("Entity is already tracked!");
        } else {
            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, j, flag);

            this.a.add(entitytrackerentry);
            this.b.a(entity.id, entitytrackerentry);
            // CraftBukkit
            entitytrackerentry.b(entity.world.d); // CraftBukkit
        }
    }

    // CraftBukkit -- synchronized
    public synchronized void b(Entity entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;
            Iterator iterator = this.a.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                entitytrackerentry.a(entityplayer);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.b.d(entity.id);

        if (entitytrackerentry1 != null) {
            this.a.remove(entitytrackerentry1);
            entitytrackerentry1.a();
        }
    }

    // CraftBukkit -- synchronized
    public synchronized void a() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            // CraftBukkit
            entitytrackerentry.a(entitytrackerentry.a.world.d);
            if (entitytrackerentry.m && entitytrackerentry.a instanceof EntityPlayer) {
                arraylist.add((EntityPlayer) entitytrackerentry.a);
            }
        }

        for (int i = 0; i < arraylist.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) arraylist.get(i);
            Iterator iterator1 = this.a.iterator();

            while (iterator1.hasNext()) {
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();

                if (entitytrackerentry1.a != entityplayer) {
                    entitytrackerentry1.b(entityplayer);
                }
            }
        }
    }

    // CraftBukkit -- synchronized
    public synchronized void a(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.b.a(entity.id);

        if (entitytrackerentry != null) {
            entitytrackerentry.a(packet);
        }
    }

    // CraftBukkit -- synchronized
    public synchronized void b(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.b.a(entity.id);

        if (entitytrackerentry != null) {
            entitytrackerentry.b(packet);
        }
    }

    // CraftBukkit -- synchronized
    public synchronized void a(EntityPlayer entityplayer) {
        Iterator iterator = this.a.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.c(entityplayer);
        }
    }
}
