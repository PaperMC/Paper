package net.minecraft.server;

// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.event.CraftEventFactory;
// CraftBukkit end

public class EntityChicken extends EntityAnimal {

    public boolean a = false;
    public float b = 0.0F;
    public float c = 0.0F;
    public float g;
    public float h;
    public float i = 1.0F;
    public int j;

    public EntityChicken(World world) {
        super(world);
        this.texture = "/mob/chicken.png";
        this.b(0.3F, 0.7F);
        this.health = 4;
        this.j = this.random.nextInt(6000) + 6000;
    }

    public void s() {
        super.s();
        this.h = this.b;
        this.g = this.c;
        this.c = (float) ((double) this.c + (double) (this.onGround ? -1 : 4) * 0.3D);
        if (this.c < 0.0F) {
            this.c = 0.0F;
        }

        if (this.c > 1.0F) {
            this.c = 1.0F;
        }

        if (!this.onGround && this.i < 1.0F) {
            this.i = 1.0F;
        }

        this.i = (float) ((double) this.i * 0.9D);
        if (!this.onGround && this.motY < 0.0D) {
            this.motY *= 0.6D;
        }

        this.b += this.i * 2.0F;
        if (!this.world.isStatic && --this.j <= 0) {
            this.world.makeSound(this, "mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.b(Item.EGG.id, 1);
            this.j = this.random.nextInt(6000) + 6000;
        }
    }

    protected void a(float f) {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected String h() {
        return "mob.chicken";
    }

    protected String i() {
        return "mob.chickenhurt";
    }

    protected String j() {
        return "mob.chickenhurt";
    }

    protected int k() {
        return Item.FEATHER.id;
    }

    protected void a(boolean flag) {
        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int count = this.random.nextInt(3);

        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(Item.FEATHER.id, count));
            loot.add(new org.bukkit.inventory.ItemStack(this.fireTicks > 0 ? Item.COOKED_CHICKEN.id : Item.RAW_CHICKEN.id, 1));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }
}
