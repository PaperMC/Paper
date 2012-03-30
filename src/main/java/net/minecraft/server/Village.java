package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Village {

    private final World world;
    private final List doors = new ArrayList();
    private final ChunkCoordinates c = new ChunkCoordinates(0, 0, 0);
    private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
    private int size = 0;
    private int f = 0;
    private int time = 0;
    private int population = 0;
    private List i = new ArrayList();
    private int j = 0;

    public Village(World world) {
        this.world = world;
    }

    public void tick(int i) {
        this.time = i;
        this.k();
        this.j();
        if (i % 20 == 0) {
            this.i();
        }

        if (i % 30 == 0) {
            this.countPopulation();
        }

        int j = this.population / 16;

        if (this.j < j && this.doors.size() > 20 && this.world.random.nextInt(7000) == 0) {
            Vec3D vec3d = this.a(MathHelper.d((float) this.center.x), MathHelper.d((float) this.center.y), MathHelper.d((float) this.center.z), 2, 4, 2);

            if (vec3d != null) {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.world);

                entityirongolem.setPosition(vec3d.a, vec3d.b, vec3d.c);
                this.world.addEntity(entityirongolem, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE); // CraftBukkit
                ++this.j;
            }
        }
    }

    private Vec3D a(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < 10; ++k1) {
            int l1 = i + this.world.random.nextInt(16) - 8;
            int i2 = j + this.world.random.nextInt(6) - 3;
            int j2 = k + this.world.random.nextInt(16) - 8;

            if (this.a(l1, i2, j2) && this.b(l1, i2, j2, l, i1, j1)) {
                return Vec3D.create((double) l1, (double) i2, (double) j2);
            }
        }

        return null;
    }

    private boolean b(int i, int j, int k, int l, int i1, int j1) {
        if (!this.world.e(i, j - 1, k)) {
            return false;
        } else {
            int k1 = i - l / 2;
            int l1 = k - j1 / 2;

            for (int i2 = k1; i2 < k1 + l; ++i2) {
                for (int j2 = j; j2 < j + i1; ++j2) {
                    for (int k2 = l1; k2 < l1 + j1; ++k2) {
                        if (this.world.e(i2, j2, k2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void countPopulation() {
        List list = this.world.a(EntityIronGolem.class, AxisAlignedBB.b((double) (this.center.x - this.size), (double) (this.center.y - 4), (double) (this.center.z - this.size), (double) (this.center.x + this.size), (double) (this.center.y + 4), (double) (this.center.z + this.size)));

        this.j = list.size();
    }

    private void i() {
        List list = this.world.a(EntityVillager.class, AxisAlignedBB.b((double) (this.center.x - this.size), (double) (this.center.y - 4), (double) (this.center.z - this.size), (double) (this.center.x + this.size), (double) (this.center.y + 4), (double) (this.center.z + this.size)));

        this.population = list.size();
    }

    public ChunkCoordinates getCenter() {
        return this.center;
    }

    public int getSize() {
        return this.size;
    }

    public int getDoorCount() {
        return this.doors.size();
    }

    public int d() {
        return this.time - this.f;
    }

    public int getPopulationCount() {
        return this.population;
    }

    public boolean a(int i, int j, int k) {
        return this.center.c(i, j, k) < (float) (this.size * this.size);
    }

    public List getDoors() {
        return this.doors;
    }

    public VillageDoor b(int i, int j, int k) {
        VillageDoor villagedoor = null;
        int l = Integer.MAX_VALUE;
        Iterator iterator = this.doors.iterator();

        while (iterator.hasNext()) {
            VillageDoor villagedoor1 = (VillageDoor) iterator.next();
            int i1 = villagedoor1.a(i, j, k);

            if (i1 < l) {
                villagedoor = villagedoor1;
                l = i1;
            }
        }

        return villagedoor;
    }

    public VillageDoor c(int i, int j, int k) {
        VillageDoor villagedoor = null;
        int l = Integer.MAX_VALUE;
        Iterator iterator = this.doors.iterator();

        while (iterator.hasNext()) {
            VillageDoor villagedoor1 = (VillageDoor) iterator.next();
            int i1 = villagedoor1.a(i, j, k);

            if (i1 > 256) {
                i1 *= 1000;
            } else {
                i1 = villagedoor1.f();
            }

            if (i1 < l) {
                villagedoor = villagedoor1;
                l = i1;
            }
        }

        return villagedoor;
    }

    public VillageDoor d(int i, int j, int k) {
        if (this.center.c(i, j, k) > (float) (this.size * this.size)) {
            return null;
        } else {
            Iterator iterator = this.doors.iterator();

            VillageDoor villagedoor;

            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                villagedoor = (VillageDoor) iterator.next();
            } while (villagedoor.locX != i || villagedoor.locZ != k || Math.abs(villagedoor.locY - j) > 1);

            return villagedoor;
        }
    }

    public void addDoor(VillageDoor villagedoor) {
        this.doors.add(villagedoor);
        this.c.x += villagedoor.locX;
        this.c.y += villagedoor.locY;
        this.c.z += villagedoor.locZ;
        this.l();
        this.f = villagedoor.addedTime;
    }

    public boolean isAbandoned() {
        return this.doors.isEmpty();
    }

    public void a(EntityLiving entityliving) {
        Iterator iterator = this.i.iterator();

        VillageAgressor villageagressor;

        do {
            if (!iterator.hasNext()) {
                this.i.add(new VillageAgressor(this, entityliving, this.time));
                return;
            }

            villageagressor = (VillageAgressor) iterator.next();
        } while (villageagressor.a != entityliving);

        villageagressor.b = this.time;
    }

    public EntityLiving b(EntityLiving entityliving) {
        double d0 = Double.MAX_VALUE;
        VillageAgressor villageagressor = null;

        for (int i = 0; i < this.i.size(); ++i) {
            VillageAgressor villageagressor1 = (VillageAgressor) this.i.get(i);
            double d1 = villageagressor1.a.j(entityliving);

            if (d1 <= d0) {
                villageagressor = villageagressor1;
                d0 = d1;
            }
        }

        return villageagressor != null ? villageagressor.a : null;
    }

    private void j() {
        Iterator iterator = this.i.iterator();

        while (iterator.hasNext()) {
            VillageAgressor villageagressor = (VillageAgressor) iterator.next();

            if (!villageagressor.a.isAlive() || Math.abs(this.time - villageagressor.b) > 300) {
                iterator.remove();
            }
        }
    }

    private void k() {
        boolean flag = false;
        boolean flag1 = this.world.random.nextInt(50) == 0;
        Iterator iterator = this.doors.iterator();

        while (iterator.hasNext()) {
            VillageDoor villagedoor = (VillageDoor) iterator.next();

            if (flag1) {
                villagedoor.d();
            }

            if (!this.isDoor(villagedoor.locX, villagedoor.locY, villagedoor.locZ) || Math.abs(this.time - villagedoor.addedTime) > 1200) {
                this.c.x -= villagedoor.locX;
                this.c.y -= villagedoor.locY;
                this.c.z -= villagedoor.locZ;
                flag = true;
                villagedoor.g = true;
                iterator.remove();
            }
        }

        if (flag) {
            this.l();
        }
    }

    private boolean isDoor(int i, int j, int k) {
        int l = this.world.getTypeId(i, j, k);

        return l <= 0 ? false : l == Block.WOODEN_DOOR.id;
    }

    private void l() {
        int i = this.doors.size();

        if (i == 0) {
            this.center.a(0, 0, 0);
            this.size = 0;
        } else {
            this.center.a(this.c.x / i, this.c.y / i, this.c.z / i);
            int j = 0;

            VillageDoor villagedoor;

            for (Iterator iterator = this.doors.iterator(); iterator.hasNext(); j = Math.max(villagedoor.a(this.center.x, this.center.y, this.center.z), j)) {
                villagedoor = (VillageDoor) iterator.next();
            }

            this.size = Math.max(32, (int) Math.sqrt((double) j) + 1);
        }
    }
}
