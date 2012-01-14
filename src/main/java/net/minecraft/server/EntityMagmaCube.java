package net.minecraft.server;

import java.util.List; // CraftBukkit
import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.texture = "/mob/lava.png";
        this.fireProof = true;
        this.al = 0.2F;
    }

    public boolean canSpawn() {
        return this.world.difficulty > 0 && this.world.containsEntity(this.boundingBox) && this.world.a((Entity) this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    public int P() {
        return this.getSize() * 3;
    }

    public float a(float f) {
        return 1.0F;
    }

    protected String v() {
        return "flame";
    }

    protected EntitySlime z() {
        return new EntityMagmaCube(this.world);
    }

    protected int getLootId() {
        return Item.MAGMA_CREAM.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.getLootId();

        if (j > 0 && this.getSize() > 1) {
            int k = this.random.nextInt(4) - 2;

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            loot.add(new org.bukkit.inventory.ItemStack(j, k));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean isBurning() {
        return false;
    }

    protected int B() {
        return super.B() * 4;
    }

    protected void C() {
        this.a *= 0.9F;
    }

    protected void o_() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.ce = true;
    }

    protected void b(float f) {}

    protected boolean D() {
        return true;
    }

    protected int E() {
        return super.E() + 2;
    }

    protected String m() {
        return "mob.slime";
    }

    protected String n() {
        return "mob.slime";
    }

    protected String F() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean aL() {
        return false;
    }

    protected boolean H() {
        return true;
    }
}
