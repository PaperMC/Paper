package net.minecraft.server;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.texture = "/mob/lava.png";
        this.fireProof = true;
        this.aG = 0.2F;
    }

    public boolean canSpawn() {
        return this.world.difficulty > 0 && this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox);
    }

    public int aO() {
        return this.getSize() * 3;
    }

    public float c(float f) {
        return 1.0F;
    }

    protected String i() {
        return "flame";
    }

    protected EntitySlime j() {
        return new EntityMagmaCube(this.world);
    }

    protected int getLootId() {
        return Item.MAGMA_CREAM.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.getLootId();

        if (j > 0 && this.getSize() > 1) {
            int k = this.random.nextInt(4) - 2;

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            if (k > 0) {
                loot.add(new org.bukkit.inventory.ItemStack(j, k));
            }
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean isBurning() {
        return false;
    }

    protected int k() {
        return super.k() * 4;
    }

    protected void l() {
        this.a *= 0.9F;
    }

    protected void aZ() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.al = true;
    }

    protected void a(float f) {}

    protected boolean m() {
        return true;
    }

    protected int n() {
        return super.n() + 2;
    }

    protected String aR() {
        return "mob.slime";
    }

    protected String aS() {
        return "mob.slime";
    }

    protected String o() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean J() {
        return false;
    }

    protected boolean p() {
        return true;
    }
}
