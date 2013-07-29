package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

public class EntityTracker {

    private final WorldServer world;
    private Set b = new HashSet();
    public IntHashMap trackedEntities = new IntHashMap(); // CraftBukkit - private -> public
    private int d;

    public EntityTracker(WorldServer worldserver) {
        this.world = worldserver;
        this.d = worldserver.getMinecraftServer().getPlayerList().a();
    }

    public void track(Entity entity) {
        if (entity instanceof EntityPlayer) {
            this.addEntity(entity, 512, 2);
            EntityPlayer entityplayer = (EntityPlayer) entity;
            Iterator iterator = this.b.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                if (entitytrackerentry.tracker != entityplayer) {
                    entitytrackerentry.updatePlayer(entityplayer);
                }
            }
        } else if (entity instanceof EntityFishingHook) {
            this.addEntity(entity, 64, 5, true);
        } else if (entity instanceof EntityArrow) {
            this.addEntity(entity, 64, 20, false);
        } else if (entity instanceof EntitySmallFireball) {
            this.addEntity(entity, 64, 10, false);
        } else if (entity instanceof EntityFireball) {
            this.addEntity(entity, 64, 10, false);
        } else if (entity instanceof EntitySnowball) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderPearl) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityEnderSignal) {
            this.addEntity(entity, 64, 4, true);
        } else if (entity instanceof EntityEgg) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityPotion) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityThrownExpBottle) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityFireworks) {
            this.addEntity(entity, 64, 10, true);
        } else if (entity instanceof EntityItem) {
            this.addEntity(entity, 64, 20, true);
        } else if (entity instanceof EntityMinecartAbstract) {
            this.addEntity(entity, 80, 3, true);
        } else if (entity instanceof EntityBoat) {
            this.addEntity(entity, 80, 3, true);
        } else if (entity instanceof EntitySquid) {
            this.addEntity(entity, 64, 3, true);
        } else if (entity instanceof EntityWither) {
            this.addEntity(entity, 80, 3, false);
        } else if (entity instanceof EntityBat) {
            this.addEntity(entity, 80, 3, false);
        } else if (entity instanceof IAnimal) {
            this.addEntity(entity, 80, 3, true);
        } else if (entity instanceof EntityEnderDragon) {
            this.addEntity(entity, 160, 3, true);
        } else if (entity instanceof EntityTNTPrimed) {
            this.addEntity(entity, 160, 10, true);
        } else if (entity instanceof EntityFallingBlock) {
            this.addEntity(entity, 160, 20, true);
        } else if (entity instanceof EntityHanging) {
            this.addEntity(entity, 160, Integer.MAX_VALUE, false);
        } else if (entity instanceof EntityExperienceOrb) {
            this.addEntity(entity, 160, 20, true);
        } else if (entity instanceof EntityEnderCrystal) {
            this.addEntity(entity, 256, Integer.MAX_VALUE, false);
        }
    }

    public void addEntity(Entity entity, int i, int j) {
        this.addEntity(entity, i, j, false);
    }

    public void addEntity(Entity entity, int i, int j, boolean flag) {
        if (i > this.d) {
            i = this.d;
        }

        try {
            if (this.trackedEntities.b(entity.id)) {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entity, i, j, flag);

            this.b.add(entitytrackerentry);
            this.trackedEntities.a(entity.id, entitytrackerentry);
            entitytrackerentry.scanPlayers(this.world.players);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Adding entity to track");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Entity To Track");

            crashreportsystemdetails.a("Tracking range", (i + " blocks"));
            crashreportsystemdetails.a("Update interval", (Callable) (new CrashReportEntityTrackerUpdateInterval(this, j)));
            entity.a(crashreportsystemdetails);
            CrashReportSystemDetails crashreportsystemdetails1 = crashreport.a("Entity That Is Already Tracked");

            ((EntityTrackerEntry) this.trackedEntities.get(entity.id)).tracker.a(crashreportsystemdetails1);

            try {
                throw new ReportedException(crashreport);
            } catch (ReportedException reportedexception) {
                System.err.println("\"Silently\" catching entity tracking error.");
                reportedexception.printStackTrace();
            }
        }
    }

    public void untrackEntity(Entity entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entity;
            Iterator iterator = this.b.iterator();

            while (iterator.hasNext()) {
                EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

                entitytrackerentry.a(entityplayer);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) this.trackedEntities.d(entity.id);

        if (entitytrackerentry1 != null) {
            this.b.remove(entitytrackerentry1);
            entitytrackerentry1.a();
        }
    }

    public void updatePlayers() {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.track(this.world.players);
            if (entitytrackerentry.n && entitytrackerentry.tracker instanceof EntityPlayer) {
                arraylist.add((EntityPlayer) entitytrackerentry.tracker);
            }
        }

        for (int i = 0; i < arraylist.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) arraylist.get(i);
            Iterator iterator1 = this.b.iterator();

            while (iterator1.hasNext()) {
                EntityTrackerEntry entitytrackerentry1 = (EntityTrackerEntry) iterator1.next();

                if (entitytrackerentry1.tracker != entityplayer) {
                    entitytrackerentry1.updatePlayer(entityplayer);
                }
            }
        }
    }

    public void a(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntities.get(entity.id);

        if (entitytrackerentry != null) {
            entitytrackerentry.broadcast(packet);
        }
    }

    public void sendPacketToEntity(Entity entity, Packet packet) {
        EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) this.trackedEntities.get(entity.id);

        if (entitytrackerentry != null) {
            entitytrackerentry.broadcastIncludingSelf(packet);
        }
    }

    public void untrackPlayer(EntityPlayer entityplayer) {
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            entitytrackerentry.clear(entityplayer);
        }
    }

    public void a(EntityPlayer entityplayer, Chunk chunk) {
        Iterator iterator = this.b.iterator();

        while (iterator.hasNext()) {
            EntityTrackerEntry entitytrackerentry = (EntityTrackerEntry) iterator.next();

            if (entitytrackerentry.tracker != entityplayer && entitytrackerentry.tracker.aj == chunk.x && entitytrackerentry.tracker.al == chunk.z) {
                entitytrackerentry.updatePlayer(entityplayer);
            }
        }
    }
}
