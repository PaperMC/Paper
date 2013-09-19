package net.minecraft.server;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.fireProof = true;
    }

    protected void az() {
        super.az();
        this.getAttributeInstance(GenericAttributes.d).setValue(0.20000000298023224D);
    }

    public boolean canSpawn() {
        return this.world.difficulty > 0 && this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox);
    }

    public int aQ() {
        return this.getSize() * 3;
    }

    public float d(float f) {
        return 1.0F;
    }

    protected String bJ() {
        return "flame";
    }

    protected EntitySlime bK() {
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

    protected int bL() {
        return super.bL() * 4;
    }

    protected void bM() {
        this.h *= 0.9F;
    }

    protected void be() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.an = true;
    }

    protected void b(float f) {}

    protected boolean bN() {
        return true;
    }

    protected int bO() {
        return super.bO() + 2;
    }

    protected String aO() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String aP() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String bP() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean J() {
        return false;
    }

    protected boolean bQ() {
        return true;
    }
}
