package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftAnimals;
// CraftBukkit end

public abstract class EntityAnimal extends EntityCreature implements IAnimal {

    public EntityAnimal(World world) {
        super(world);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftAnimals(server, this);
        // CraftBukkit end
    }

    protected float a(int i, int j, int k) {
        return this.world.getTypeId(i, j - 1, k) == Block.GRASS.id ? 10.0F : this.world.l(i, j, k) - 0.5F;
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public boolean b() {
        int i = MathHelper.b(this.locX);
        int j = MathHelper.b(this.boundingBox.b);
        int k = MathHelper.b(this.locZ);

        return this.world.getTypeId(i, j - 1, k) == Block.GRASS.id && this.world.j(i, j, k) > 8 && super.b();
    }

    public int c() {
        return 120;
    }
}
