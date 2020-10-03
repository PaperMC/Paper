package net.minecraft.server;

import javax.annotation.Nullable;

public abstract class EntityAgeable extends EntityCreature {

    private static final DataWatcherObject<Boolean> bo = DataWatcher.a(EntityAgeable.class, DataWatcherRegistry.i);
    protected int b;
    protected int c;
    protected int d;
    public boolean ageLocked; // CraftBukkit

    protected EntityAgeable(EntityTypes<? extends EntityAgeable> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(true);
        }

        EntityAgeable.a entityageable_a = (EntityAgeable.a) groupdataentity;

        if (entityageable_a.c() && entityageable_a.a() > 0 && this.random.nextFloat() <= entityageable_a.d()) {
            this.setAgeRaw(-24000);
        }

        entityageable_a.b();
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    @Nullable
    public abstract EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable);

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityAgeable.bo, false);
    }

    public boolean canBreed() {
        return false;
    }

    public int getAge() {
        return this.world.isClientSide ? ((Boolean) this.datawatcher.get(EntityAgeable.bo) ? -1 : 1) : this.b;
    }

    public void setAge(int i, boolean flag) {
        int j = this.getAge();
        int k = j;

        j += i * 20;
        if (j > 0) {
            j = 0;
        }

        int l = j - k;

        this.setAgeRaw(j);
        if (flag) {
            this.c += l;
            if (this.d == 0) {
                this.d = 40;
            }
        }

        if (this.getAge() == 0) {
            this.setAgeRaw(this.c);
        }

    }

    public void setAge(int i) {
        this.setAge(i, false);
    }

    public void setAgeRaw(int i) {
        int j = this.b;

        this.b = i;
        if (j < 0 && i >= 0 || j >= 0 && i < 0) {
            this.datawatcher.set(EntityAgeable.bo, i < 0);
            this.m();
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Age", this.getAge());
        nbttagcompound.setInt("ForcedAge", this.c);
        nbttagcompound.setBoolean("AgeLocked", this.ageLocked); // CraftBukkit
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setAgeRaw(nbttagcompound.getInt("Age"));
        this.c = nbttagcompound.getInt("ForcedAge");
        this.ageLocked = nbttagcompound.getBoolean("AgeLocked"); // CraftBukkit
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityAgeable.bo.equals(datawatcherobject)) {
            this.updateSize();
        }

        super.a(datawatcherobject);
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.world.isClientSide || ageLocked) { // CraftBukkit
            if (this.d > 0) {
                if (this.d % 4 == 0) {
                    this.world.addParticle(Particles.HAPPY_VILLAGER, this.d(1.0D), this.cE() + 0.5D, this.g(1.0D), 0.0D, 0.0D, 0.0D);
                }

                --this.d;
            }
        } else if (this.isAlive()) {
            int i = this.getAge();

            if (i < 0) {
                ++i;
                this.setAgeRaw(i);
            } else if (i > 0) {
                --i;
                this.setAgeRaw(i);
            }
        }

    }

    protected void m() {}

    @Override
    public boolean isBaby() {
        return this.getAge() < 0;
    }

    @Override
    public void setBaby(boolean flag) {
        this.setAgeRaw(flag ? -24000 : 0);
    }

    public static class a implements GroupDataEntity {

        private int a;
        private final boolean b;
        private final float c;

        private a(boolean flag, float f) {
            this.b = flag;
            this.c = f;
        }

        public a(boolean flag) {
            this(flag, 0.05F);
        }

        public a(float f) {
            this(true, f);
        }

        public int a() {
            return this.a;
        }

        public void b() {
            ++this.a;
        }

        public boolean c() {
            return this.b;
        }

        public float d() {
            return this.c;
        }
    }
}
