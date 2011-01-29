package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftCow;
// CraftBukkit end

public class EntityCow extends EntityAnimal {

    public EntityCow(World world) {
        super(world);
        this.texture = "/mob/cow.png";
        this.a(0.9F, 1.3F);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftCow(server, this);
        // CraftBukkit end
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected String e() {
        return "mob.cow";
    }

    protected String f() {
        return "mob.cowhurt";
    }

    protected String g() {
        return "mob.cowhurt";
    }

    protected float i() {
        return 0.4F;
    }

    protected int h() {
        return Item.LEATHER.id;
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.e();

        if (itemstack != null && itemstack.id == Item.BUCKET.id) {
            entityhuman.inventory.a(entityhuman.inventory.c, new ItemStack(Item.MILK_BUCKET));
            return true;
        } else {
            return false;
        }
    }
}
