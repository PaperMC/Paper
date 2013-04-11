package net.minecraft.server;

public class MobEffect {

    private int effectId;
    private int duration;
    private int amplification;
    private boolean splash;
    private boolean ambient;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit

    public MobEffect(int i, int j) {
        this(i, j, 0);
    }

    public MobEffect(int i, int j, int k) {
        this(i, j, k, false);
    }

    public MobEffect(int i, int j, int k, boolean flag) {
        this.effectId = i;
        this.duration = j;
        this.amplification = k;
        this.ambient = flag;
    }

    public MobEffect(MobEffect mobeffect) {
        this.effectId = mobeffect.effectId;
        this.duration = mobeffect.duration;
        this.amplification = mobeffect.amplification;
    }

    public void a(MobEffect mobeffect) {
        if (this.effectId != mobeffect.effectId) {
            System.err.println("This method should only be called for matching effects!");
        }

        if (mobeffect.amplification > this.amplification) {
            this.amplification = mobeffect.amplification;
            this.duration = mobeffect.duration;
        } else if (mobeffect.amplification == this.amplification && this.duration < mobeffect.duration) {
            this.duration = mobeffect.duration;
        } else if (!mobeffect.ambient && this.ambient) {
            this.ambient = mobeffect.ambient;
        }
    }

    public int getEffectId() {
        return this.effectId;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplification;
    }

    public boolean isSplash() {
        return this.splash;
    }

    public void setSplash(boolean flag) {
        this.splash = flag;
    }

    public boolean isAmbient() {
        return this.ambient;
    }

    public boolean tick(EntityLiving entityliving) {
        if (this.duration > 0) {
            if (MobEffectList.byId[this.effectId].a(this.duration, this.amplification)) {
                this.b(entityliving);
            }

            this.h();
        }

        return this.duration > 0;
    }

    private int h() {
        // CraftBukkit start - Use wall time instead of ticks for potion effects
        int elapsedTicks = Math.max(1, MinecraftServer.currentTick - this.lastTick);
        this.lastTick = MinecraftServer.currentTick;
        this.duration -= elapsedTicks;

        return this.duration;
        // CraftBukkit end
    }

    public void b(EntityLiving entityliving) {
        if (this.duration > 0) {
            MobEffectList.byId[this.effectId].tick(entityliving, this.amplification);
        }
    }

    public String f() {
        return MobEffectList.byId[this.effectId].a();
    }

    public int hashCode() {
        return this.effectId;
    }

    public String toString() {
        String s = "";

        if (this.getAmplifier() > 0) {
            s = this.f() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration();
        } else {
            s = this.f() + ", Duration: " + this.getDuration();
        }

        if (this.splash) {
            s = s + ", Splash: true";
        }

        return MobEffectList.byId[this.effectId].i() ? "(" + s + ")" : s;
    }

    public boolean equals(Object object) {
        if (!(object instanceof MobEffect)) {
            return false;
        } else {
            MobEffect mobeffect = (MobEffect) object;

            return this.effectId == mobeffect.effectId && this.amplification == mobeffect.amplification && this.duration == mobeffect.duration && this.splash == mobeffect.splash && this.ambient == mobeffect.ambient;
        }
    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Id", (byte) this.getEffectId());
        nbttagcompound.setByte("Amplifier", (byte) this.getAmplifier());
        nbttagcompound.setInt("Duration", this.getDuration());
        nbttagcompound.setBoolean("Ambient", this.isAmbient());
        return nbttagcompound;
    }

    public static MobEffect b(NBTTagCompound nbttagcompound) {
        byte b0 = nbttagcompound.getByte("Id");
        byte b1 = nbttagcompound.getByte("Amplifier");
        int i = nbttagcompound.getInt("Duration");
        boolean flag = nbttagcompound.getBoolean("Ambient");

        return new MobEffect(b0, i, b1, flag);
    }
}
