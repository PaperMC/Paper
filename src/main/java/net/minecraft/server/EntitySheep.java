package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.Material;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityChangeBlockEvent;
// CraftBukkit end

public class EntitySheep extends EntityAnimal {

    public static final float[][] a = new float[][] { { 1.0F, 1.0F, 1.0F}, { 0.95F, 0.7F, 0.2F}, { 0.9F, 0.5F, 0.85F}, { 0.6F, 0.7F, 0.95F}, { 0.9F, 0.9F, 0.2F}, { 0.5F, 0.8F, 0.1F}, { 0.95F, 0.7F, 0.8F}, { 0.3F, 0.3F, 0.3F}, { 0.6F, 0.6F, 0.6F}, { 0.3F, 0.6F, 0.7F}, { 0.7F, 0.4F, 0.9F}, { 0.2F, 0.4F, 0.8F}, { 0.5F, 0.4F, 0.3F}, { 0.4F, 0.5F, 0.2F}, { 0.8F, 0.3F, 0.3F}, { 0.1F, 0.1F, 0.1F}};
    private int b;

    public EntitySheep(World world) {
        super(world);
        this.texture = "/mob/sheep.png";
        this.b(0.9F, 1.3F);
    }

    public int getMaxHealth() {
        return 8;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        if (!this.isSheared()) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOOL, 1, (short) 0, (byte) this.getColor()));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected int getLootId() {
        return Block.WOOL.id;
    }

    public void d() {
        super.d();
        if (this.b > 0) {
            --this.b;
        }
    }

    protected void o_() {
        if (this.b <= 0) {
            super.o_();
        }
    }

    protected void m_() {
        super.m_();
        int i;
        int j;
        int k;

        if (!this.E() && this.b <= 0 && (this.isBaby() && this.random.nextInt(50) == 0 || this.random.nextInt(1000) == 0)) {
            i = MathHelper.floor(this.locX);
            j = MathHelper.floor(this.locY);
            k = MathHelper.floor(this.locZ);
            if (this.world.getTypeId(i, j, k) == Block.LONG_GRASS.id && this.world.getData(i, j, k) == 1 || this.world.getTypeId(i, j - 1, k) == Block.GRASS.id) {
                this.b = 40;
                this.world.a(this, (byte) 10);
            }
        } else if (this.b == 4) {
            i = MathHelper.floor(this.locX);
            j = MathHelper.floor(this.locY);
            k = MathHelper.floor(this.locZ);
            boolean flag = false;

            if (this.world.getTypeId(i, j, k) == Block.LONG_GRASS.id) {
                // CraftBukkit start
                org.bukkit.World bworld = this.world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(i, j, k);

                EntityChangeBlockEvent event = new EntityChangeBlockEvent(this.getBukkitEntity(), bblock, Material.AIR);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.f(2001, i, j, k, Block.LONG_GRASS.id + 256);
                    this.world.setTypeId(i, j, k, 0);
                    flag = true;
                }
                // CraftBukkit end
            } else if (this.world.getTypeId(i, j - 1, k) == Block.GRASS.id) {
                // CraftBukkit start
                org.bukkit.World bworld = this.world.getWorld();
                org.bukkit.block.Block bblock = bworld.getBlockAt(i, j - 1, k);

                EntityChangeBlockEvent event = new EntityChangeBlockEvent(this.getBukkitEntity(), bblock, Material.DIRT);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.f(2001, i, j - 1, k, Block.GRASS.id);
                    this.world.setTypeId(i, j - 1, k, Block.DIRT.id);
                    flag = true;
                }
                // CraftBukkit end
            }

            if (flag) {
               // CraftBukkit start
                if (!this.isBaby()) {
                    org.bukkit.event.entity.SheepRegrowWoolEvent event = new org.bukkit.event.entity.SheepRegrowWoolEvent(this.getBukkitEntity());
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.setSheared(false);
                    }
                }
                // CraftBukkit end

                if (this.isBaby()) {
                    int l = this.getAge() + 1200;

                    if (l > 0) {
                        l = 0;
                    }

                    this.setAge(l);
                }
            }
        }
    }

    protected boolean v() {
        return this.b > 0;
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.SHEARS.id && !this.isSheared() && !this.isBaby()) {
            if (!this.world.isStatic) {
                // CraftBukkit start
                org.bukkit.event.player.PlayerShearEntityEvent event = new org.bukkit.event.player.PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end

                this.setSheared(true);
                int i = 1 + this.random.nextInt(3);

                for (int j = 0; j < i; ++j) {
                    EntityItem entityitem = this.a(new ItemStack(Block.WOOL.id, 1, this.getColor()), 1.0F);

                    entityitem.motY += (double) (this.random.nextFloat() * 0.05F);
                    entityitem.motX += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                    entityitem.motZ += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                }
            }

            itemstack.damage(1, entityhuman);
        }

        return super.b(entityhuman);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Sheared", this.isSheared());
        nbttagcompound.setByte("Color", (byte) this.getColor());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSheared(nbttagcompound.getBoolean("Sheared"));
        this.setColor(nbttagcompound.getByte("Color"));
    }

    protected String c_() {
        return "mob.sheep";
    }

    protected String m() {
        return "mob.sheep";
    }

    protected String n() {
        return "mob.sheep";
    }

    public int getColor() {
        return this.datawatcher.getByte(16) & 15;
    }

    public void setColor(int i) {
        byte b0 = this.datawatcher.getByte(16);

        this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & 240 | i & 15)));
    }

    public boolean isSheared() {
        return (this.datawatcher.getByte(16) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 16)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -17)));
        }
    }

    public static int a(Random random) {
        int i = random.nextInt(100);

        return i < 5 ? 15 : (i < 10 ? 7 : (i < 15 ? 8 : (i < 18 ? 12 : (random.nextInt(500) == 0 ? 6 : 0))));
    }

    protected EntityAnimal createChild(EntityAnimal entityanimal) {
        EntitySheep entitysheep = (EntitySheep) entityanimal;
        EntitySheep entitysheep1 = new EntitySheep(this.world);

        if (this.random.nextBoolean()) {
            entitysheep1.setColor(this.getColor());
        } else {
            entitysheep1.setColor(entitysheep.getColor());
        }

        return entitysheep1;
    }
}
