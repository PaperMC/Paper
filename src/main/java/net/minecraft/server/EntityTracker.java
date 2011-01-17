package net.minecraft.server;

import java.util.*;

public class EntityTracker {

    private Set a;
    private MCHashTable b;
    private MinecraftServer c;
    private int d;

    public EntityTracker(MinecraftServer minecraftserver) {
        a = ((Set) (new HashSet()));
        b = new MCHashTable();
        c = minecraftserver;
        d = minecraftserver.f.a();
    }

    public void a(Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            a(entity, 512, 2);
            EntityPlayerMP entityplayermp = (EntityPlayerMP) entity;
            Iterator iterator = a.iterator();

            do {
                if (!iterator.hasNext()) {
                    break;
                }
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                if (entitytrackerentry.a != entityplayermp) {
                    entitytrackerentry.b(entityplayermp);
                }
            } while (true);
        } else if (entity instanceof EntityFish) {
            a(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            a(entity, 64, 5, true);
        } else if (entity instanceof EntitySnowball) {
            a(entity, 64, 5, true);
        } else if (entity instanceof EntityEgg) {
            a(entity, 64, 5, true);
        } else if (entity instanceof EntityItem) {
            a(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecart) {
            a(entity, 160, 5, true);
        } else if (entity instanceof EntityBoat) {
            a(entity, 160, 5, true);
        } else if (entity instanceof EntitySquid) {
            a(entity, 160, 3, true);
        } else if (entity instanceof IAnimals) {
            a(entity, 160, 3);
        } else if (entity instanceof EntityTNTPrimed) {
            a(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingSand) {
            a(entity, 160, 20, true);
        } else if (entity instanceof EntityPainting) {
            a(entity, 160, 0x7fffffff, false);
        }
    }

    public void a(Entity entity, int i, int j) {
        a(entity, i, j, false);
    }

    public void a(Entity entity, int i, int j, boolean flag) {
        if (i > d) {
            i = d;
        }
        if (b.b(entity.g)) {
            throw new IllegalStateException("Entity is already tracked!");
        } else {
            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, j, flag);

            a.add(((entitytrackerentry)));
            b.a(entity.g, ((entitytrackerentry)));
            entitytrackerentry.b(c.e.d);
            return;
        }
    }

    public void b(Entity entity) {
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) entity;
            EntityTrackerEntry entitytrackerentry1;

            for (Iterator iterator = a.iterator(); iterator.hasNext(); entitytrackerentry1.a(entityplayermp)) {
                entitytrackerentry1 = (EntityTrackerEntry) iterator.next();
            }
        }
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) b.d(entity.g);

        if (entitytrackerentry != null) {
            a.remove(((entitytrackerentry)));
            entitytrackerentry.a();
        }
    }

    public void a() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = a.iterator();

        do {
            if (!iterator.hasNext()) {
                break;
            }
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.a(c.e.d);
            if (entitytrackerentry.m && (entitytrackerentry.a instanceof EntityPlayerMP)) {
                ((List) (arraylist)).add((((EntityPlayerMP) entitytrackerentry.a)));
            }
        } while (true);
        label0:
        for (int i = 0; i < ((List) (arraylist)).size(); i++) {
            EntityPlayerMP entityplayermp = (EntityPlayerMP) ((List) (arraylist)).get(i);
            Iterator iterator1 = a.iterator();

            do {
                if (!iterator1.hasNext()) {
                    continue label0;
                }
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();

                if (entitytrackerentry1.a != entityplayermp) {
                    entitytrackerentry1.b(entityplayermp);
                }
            } while (true);
        }
    }

    public void a(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) b.a(entity.g);

        if (entitytrackerentry != null) {
            entitytrackerentry.a(packet);
        }
    }

    public void b(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) b.a(entity.g);

        if (entitytrackerentry != null) {
            entitytrackerentry.b(packet);
        }
    }

    public void a(EntityPlayerMP entityplayermp) {
        EntityTrackerEntry entitytrackerentry;

        for (Iterator iterator = a.iterator(); iterator.hasNext(); entitytrackerentry.c(entityplayermp)) {
            entitytrackerentry = (EntityTrackerEntry) iterator.next();
        }
    }
}
