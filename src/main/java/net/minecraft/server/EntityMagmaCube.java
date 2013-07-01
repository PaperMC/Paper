package net.minecraft.server;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.fireProof = true;
    }

    protected void ax() {
        super.ax();
        this.a(GenericAttributes.d).a(0.20000000298023224D);
    }

    public boolean canSpawn() {
        return this.world.difficulty > 0 && this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox);
    }

    public int aM() {
        return this.getSize() * 3;
    }

    public float d(float f) {
        return 1.0F;
    }

    protected String bF() {
        return "flame";
    }

    protected EntitySlime bG() {
        return new EntityMagmaCube(this.world);
    }

    protected int getLootId() {
        return Item.MAGMA_CREAM.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - Whole method
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

    protected int bH() {
        return super.bH() * 4;
    }

    protected void bI() {
        this.h *= 0.9F;
    }

    protected void ba() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.an = true;
    }

    protected void b(float f) {}

    protected boolean bJ() {
        return true;
    }

    protected int bK() {
        return super.bK() + 2;
    }

    protected String aK() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String aL() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String bL() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean I() {
        return false;
    }

    protected boolean bM() {
        return true;
    }
}
