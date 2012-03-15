package net.minecraft.server;

public class PlayerAbilities {

    public boolean isInvulnerable = false;
    public boolean isFlying = false;
    public boolean canFly = false;
    public boolean canInstantlyBuild = false;

    public PlayerAbilities() {}

    public void a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.setBoolean("invulnerable", this.isInvulnerable);
        nbttagcompound1.setBoolean("flying", this.isInvulnerable);
        nbttagcompound1.setBoolean("mayfly", this.canFly);
        nbttagcompound1.setBoolean("instabuild", this.canInstantlyBuild);
        nbttagcompound.set("abilities", nbttagcompound1);
    }

    public void b(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("abilities")) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("abilities");

            this.isInvulnerable = nbttagcompound1.getBoolean("invulnerable");
            this.isFlying = nbttagcompound1.getBoolean("flying");
            this.canFly = nbttagcompound1.getBoolean("mayfly");
            this.canInstantlyBuild = nbttagcompound1.getBoolean("instabuild");
        }
    }
}
