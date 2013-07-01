package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

public class Village {

    private World world;
    private final List doors = new ArrayList();
    private final ChunkCoordinates c = new ChunkCoordinates(0, 0, 0);
    private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
    private int size;
    private int f;
    private int time;
    private int population;
    private int noBreedTicks;
    private TreeMap playerStandings = new TreeMap();
    private List aggressors = new ArrayList();
    private int ironGolemCount;

    public Village() {}

    public Village(World world) {
        this.world = world;
    }

    public void a(World world) {
        this.world = world;
    }

    public void tick(int i) {
        this.time = i;
        this.m();
        this.l();
        if (i % 20 == 0) {
            this.k();
        }

        if (i % 30 == 0) {
            this.countPopulation();
        }

        int j = this.population / 10;

        if (this.ironGolemCount < j && this.doors.size() > 20 && this.world.random.nextInt(7000) == 0) {
            Vec3D vec3d = this.a(MathHelper.d((float) this.center.x), MathHelper.d((float) this.center.y), MathHelper.d((float) this.center.z), 2, 4, 2);

            if (vec3d != null) {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.world);

                entityirongolem.setPosition(vec3d.c, vec3d.d, vec3d.e);
                this.world.addEntity(entityirongolem, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE); // CraftBukkit
                ++this.ironGolemCount;
            }
        }
    }

    private Vec3D a(int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = 0; k1 < 10; ++k1) {
            int l1 = i + this.world.random.nextInt(16) - 8;
            int i2 = j + this.world.random.nextInt(6) - 3;
            int j2 = k + this.world.random.nextInt(16) - 8;

            if (this.a(l1, i2, j2) && this.b(l1, i2, j2, l, i1, j1)) {
                return this.world.getVec3DPool().create((double) l1, (double) i2, (double) j2);
            }
        }

        return null;
    }

    private boolean b(int i, int j, int k, int l, int i1, int j1) {
        if (!this.world.w(i, j - 1, k)) {
            return false;
        } else {
            int k1 = i - l / 2;
            int l1 = k - j1 / 2;

            for (int i2 = k1; i2 < k1 + l; ++i2) {
                for (int j2 = j; j2 < j + i1; ++j2) {
                    for (int k2 = l1; k2 < l1 + j1; ++k2) {
                        if (this.world.u(i2, j2, k2)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void countPopulation() {
        List list = this.world.a(EntityIronGolem.class, AxisAlignedBB.a().a((double) (this.center.x - this.size), (double) (this.center.y - 4), (double) (this.center.z - this.size), (double) (this.center.x + this.size), (double) (this.center.y + 4), (double) (this.center.z + this.size)));

        this.ironGolemCount = list.size();
    }

    private void k() {
        List list = this.world.a(EntityVillager.class, AxisAlignedBB.a().a((double) (this.center.x - this.size), (double) (this.center.y - 4), (double) (this.center.z - this.size), (double) (this.center.x + this.size), (double) (this.center.y + 4), (double) (this.center.z + this.size)));

        this.population = list.size();
        if (this.population == 0) {
            this.playerStandings.clear();
        }
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
        return this.center.e(i, j, k) < (float) (this.size * this.size);
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
            int i1 = villagedoor1.b(i, j, k);

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
            int i1 = villagedoor1.b(i, j, k);

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

    public VillageDoor e(int i, int j, int k) {
        if (this.center.e(i, j, k) > (float) (this.size * this.size)) {
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
        this.n();
        this.f = villagedoor.addedTime;
    }

    public boolean isAbandoned() {
        return this.doors.isEmpty();
    }

    public void a(EntityLiving entityliving) {
        Iterator iterator = this.aggressors.iterator();

        VillageAggressor villageaggressor;

        do {
            if (!iterator.hasNext()) {
                this.aggressors.add(new VillageAggressor(this, entityliving, this.time));
                return;
            }

            villageaggressor = (VillageAggressor) iterator.next();
        } while (villageaggressor.a != entityliving);

        villageaggressor.b = this.time;
    }

    public EntityLiving b(EntityLiving entityliving) {
        double d0 = Double.MAX_VALUE;
        VillageAggressor villageaggressor = null;

        for (int i = 0; i < this.aggressors.size(); ++i) {
            VillageAggressor villageaggressor1 = (VillageAggressor) this.aggressors.get(i);
            double d1 = villageaggressor1.a.e(entityliving);

            if (d1 <= d0) {
                villageaggressor = villageaggressor1;
                d0 = d1;
            }
        }

        return villageaggressor != null ? villageaggressor.a : null;
    }

    public EntityHuman c(EntityLiving entityliving) {
        double d0 = Double.MAX_VALUE;
        EntityHuman entityhuman = null;
        Iterator iterator = this.playerStandings.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (this.d(s)) {
                EntityHuman entityhuman1 = this.world.a(s);

                if (entityhuman1 != null) {
                    double d1 = entityhuman1.e(entityliving);

                    if (d1 <= d0) {
                        entityhuman = entityhuman1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityhuman;
    }

    private void l() {
        Iterator iterator = this.aggressors.iterator();

        while (iterator.hasNext()) {
            VillageAggressor villageaggressor = (VillageAggressor) iterator.next();

            if (!villageaggressor.a.isAlive() || Math.abs(this.time - villageaggressor.b) > 300) {
                iterator.remove();
            }
        }
    }

    private void m() {
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
                villagedoor.removed = true;
                iterator.remove();
            }
        }

        if (flag) {
            this.n();
        }
    }

    private boolean isDoor(int i, int j, int k) {
        int l = this.world.getTypeId(i, j, k);

        return l <= 0 ? false : l == Block.WOODEN_DOOR.id;
    }

    private void n() {
        int i = this.doors.size();

        if (i == 0) {
            this.center.b(0, 0, 0);
            this.size = 0;
        } else {
            this.center.b(this.c.x / i, this.c.y / i, this.c.z / i);
            int j = 0;

            VillageDoor villagedoor;

            for (Iterator iterator = this.doors.iterator(); iterator.hasNext(); j = Math.max(villagedoor.b(this.center.x, this.center.y, this.center.z), j)) {
                villagedoor = (VillageDoor) iterator.next();
            }

            this.size = Math.max(32, (int) Math.sqrt((double) j) + 1);
        }
    }

    public int a(String s) {
        Integer integer = (Integer) this.playerStandings.get(s);

        return integer != null ? integer.intValue() : 0;
    }

    public int a(String s, int i) {
        int j = this.a(s);
        int k = MathHelper.a(j + i, -30, 10);

        this.playerStandings.put(s, Integer.valueOf(k));
        return k;
    }

    public boolean d(String s) {
        return this.a(s) <= -15;
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.population = nbttagcompound.getInt("PopSize");
        this.size = nbttagcompound.getInt("Radius");
        this.ironGolemCount = nbttagcompound.getInt("Golems");
        this.f = nbttagcompound.getInt("Stable");
        this.time = nbttagcompound.getInt("Tick");
        this.noBreedTicks = nbttagcompound.getInt("MTick");
        this.center.x = nbttagcompound.getInt("CX");
        this.center.y = nbttagcompound.getInt("CY");
        this.center.z = nbttagcompound.getInt("CZ");
        this.c.x = nbttagcompound.getInt("ACX");
        this.c.y = nbttagcompound.getInt("ACY");
        this.c.z = nbttagcompound.getInt("ACZ");
        NBTTagList nbttaglist = nbttagcompound.getList("Doors");

        for (int i = 0; i < nbttaglist.size(); ++i) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
            VillageDoor villagedoor = new VillageDoor(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"), nbttagcompound1.getInt("IDX"), nbttagcompound1.getInt("IDZ"), nbttagcompound1.getInt("TS"));

            this.doors.add(villagedoor);
        }

        NBTTagList nbttaglist1 = nbttagcompound.getList("Players");

        for (int j = 0; j < nbttaglist1.size(); ++j) {
            NBTTagCompound nbttagcompound2 = (NBTTagCompound) nbttaglist1.get(j);

            this.playerStandings.put(nbttagcompound2.getString("Name"), Integer.valueOf(nbttagcompound2.getInt("S")));
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("PopSize", this.population);
        nbttagcompound.setInt("Radius", this.size);
        nbttagcompound.setInt("Golems", this.ironGolemCount);
        nbttagcompound.setInt("Stable", this.f);
        nbttagcompound.setInt("Tick", this.time);
        nbttagcompound.setInt("MTick", this.noBreedTicks);
        nbttagcompound.setInt("CX", this.center.x);
        nbttagcompound.setInt("CY", this.center.y);
        nbttagcompound.setInt("CZ", this.center.z);
        nbttagcompound.setInt("ACX", this.c.x);
        nbttagcompound.setInt("ACY", this.c.y);
        nbttagcompound.setInt("ACZ", this.c.z);
        NBTTagList nbttaglist = new NBTTagList("Doors");
        Iterator iterator = this.doors.iterator();

        while (iterator.hasNext()) {
            VillageDoor villagedoor = (VillageDoor) iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound("Door");

            nbttagcompound1.setInt("X", villagedoor.locX);
            nbttagcompound1.setInt("Y", villagedoor.locY);
            nbttagcompound1.setInt("Z", villagedoor.locZ);
            nbttagcompound1.setInt("IDX", villagedoor.d);
            nbttagcompound1.setInt("IDZ", villagedoor.e);
            nbttagcompound1.setInt("TS", villagedoor.addedTime);
            nbttaglist.add(nbttagcompound1);
        }

        nbttagcompound.set("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList("Players");
        Iterator iterator1 = this.playerStandings.keySet().iterator();

        while (iterator1.hasNext()) {
            String s = (String) iterator1.next();
            NBTTagCompound nbttagcompound2 = new NBTTagCompound(s);

            nbttagcompound2.setString("Name", s);
            nbttagcompound2.setInt("S", ((Integer) this.playerStandings.get(s)).intValue());
            nbttaglist1.add(nbttagcompound2);
        }

        nbttagcompound.set("Players", nbttaglist1);
    }

    public void h() {
        this.noBreedTicks = this.time;
    }

    public boolean i() {
        return this.noBreedTicks == 0 || this.time - this.noBreedTicks >= 3600;
    }

    public void b(int i) {
        Iterator iterator = this.playerStandings.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.a(s, i);
        }
    }
}
