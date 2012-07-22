package net.minecraft.server;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.texture = "/mob/lava.png";
        this.fireProof = true;
        this.al = 0.2F;
    }

    public boolean canSpawn() {
        return this.world.difficulty > 0 && this.world.containsEntity(this.boundingBox) && this.world.getCubes(this, this.boundingBox).size() == 0 && !this.world.containsLiquid(this.boundingBox);
    }

    public int T() {
        return this.getSize() * 3;
    }

    public float b(float f) {
        return 1.0F;
    }

    protected String A() {
        return "flame";
    }

    protected EntitySlime C() {
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

    protected int E() {
        return super.E() * 4;
    }

    protected void F() {
        this.a *= 0.9F;
    }

    protected void ac() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.ce = true;
    }

    protected void a(float f) {}

    protected boolean G() {
        return true;
    }

    protected int H() {
        return super.H() + 2;
    }

    protected String j() {
        return "mob.slime";
    }

    protected String k() {
        return "mob.slime";
    }

    protected String I() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean aV() {
        return false;
    }

    protected boolean K() {
        return true;
    }
}
