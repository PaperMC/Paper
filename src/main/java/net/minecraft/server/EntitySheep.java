package net.minecraft.server;

import java.util.Random;

public class EntitySheep extends EntityAnimal {

    public static final float[][] a = new float[][] { { 1.0F, 1.0F, 1.0F}, { 0.95F, 0.7F, 0.2F}, { 0.9F, 0.5F, 0.85F}, { 0.6F, 0.7F, 0.95F}, { 0.9F, 0.9F, 0.2F}, { 0.5F, 0.8F, 0.1F}, { 0.95F, 0.7F, 0.8F}, { 0.3F, 0.3F, 0.3F}, { 0.6F, 0.6F, 0.6F}, { 0.3F, 0.6F, 0.7F}, { 0.7F, 0.4F, 0.9F}, { 0.2F, 0.4F, 0.8F}, { 0.5F, 0.4F, 0.3F}, { 0.4F, 0.5F, 0.2F}, { 0.8F, 0.3F, 0.3F}, { 0.1F, 0.1F, 0.1F}};

    public EntitySheep(World world) {
        super(world);
        this.texture = "/mob/sheep.png";
        this.b(0.9F, 1.3F);
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    public boolean damageEntity(Entity entity, int i) {
        return super.damageEntity(entity, i);
    }

    protected void q() {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        if (!this.isSheared()) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOOL, 1, (short) 0, (byte) this.getColor()));
        }

        org.bukkit.World bworld = this.world.getWorld();
        org.bukkit.entity.Entity entity = this.getBukkitEntity();

        org.bukkit.event.entity.EntityDeathEvent event = new org.bukkit.event.entity.EntityDeathEvent(entity, loot);
        this.world.getServer().getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            bworld.dropItemNaturally(entity.getLocation(), stack);
        }
        // CraftBukkit end
    }

    protected int j() {
        return Block.WOOL.id;
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.SHEARS.id && !this.isSheared()) {
            if (!this.world.isStatic) {
                this.setSheared(true);
                int i = 2 + this.random.nextInt(3);

                for (int j = 0; j < i; ++j) {
                    EntityItem entityitem = this.a(new ItemStack(Block.WOOL.id, 1, this.getColor()), 1.0F);

                    entityitem.motY += (double) (this.random.nextFloat() * 0.05F);
                    entityitem.motX += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                    entityitem.motZ += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                }
            }

            itemstack.damage(1, entityhuman);
        }

        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Sheared", this.isSheared());
        nbttagcompound.a("Color", (byte) this.getColor());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSheared(nbttagcompound.m("Sheared"));
        this.setColor(nbttagcompound.c("Color"));
    }

    protected String g() {
        return "mob.sheep";
    }

    protected String h() {
        return "mob.sheep";
    }

    protected String i() {
        return "mob.sheep";
    }

    public int getColor() {
        return this.datawatcher.a(16) & 15;
    }

    public void setColor(int i) {
        byte b0 = this.datawatcher.a(16);

        this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & 240 | i & 15)));
    }

    public boolean isSheared() {
        return (this.datawatcher.a(16) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = this.datawatcher.a(16);

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
}
