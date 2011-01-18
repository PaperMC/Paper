package net.minecraft.server;

//CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftCow;
//CraftBukkit end

public class EntityCow extends EntityAnimals {

    public EntityCow(World world) {
        super(world);
        aP = "/mob/cow.png";
        a(0.9F, 1.3F);
        //CraftBukkit start
        CraftServer server = ((WorldServer) this.l).getServer();
        this.bukkitEntity = new CraftCow(server, this);
        //CraftBukkit end
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
        return Item.aD.ba;
    }

    public boolean a(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.an.e();

        if (itemstack != null && itemstack.c == Item.au.ba) {
            entityplayer.an.a(entityplayer.an.c, new ItemStack(Item.aE));
            return true;
        } else {
            return false;
        }
    }
}
