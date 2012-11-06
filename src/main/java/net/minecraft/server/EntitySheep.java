package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
// CraftBukkit end

public class EntitySheep extends EntityAnimal {

    private final InventoryCrafting e = new InventoryCrafting(new ContainerSheepBreed(this), 2, 1);
    public static final float[][] d = new float[][] { { 1.0F, 1.0F, 1.0F}, { 0.85F, 0.5F, 0.2F}, { 0.7F, 0.3F, 0.85F}, { 0.4F, 0.6F, 0.85F}, { 0.9F, 0.9F, 0.2F}, { 0.5F, 0.8F, 0.1F}, { 0.95F, 0.5F, 0.65F}, { 0.3F, 0.3F, 0.3F}, { 0.6F, 0.6F, 0.6F}, { 0.3F, 0.5F, 0.6F}, { 0.5F, 0.25F, 0.7F}, { 0.2F, 0.3F, 0.7F}, { 0.4F, 0.3F, 0.2F}, { 0.4F, 0.5F, 0.2F}, { 0.6F, 0.2F, 0.2F}, { 0.1F, 0.1F, 0.1F}};
    private int f;
    private PathfinderGoalEatTile g = new PathfinderGoalEatTile(this);

    public EntitySheep(World world) {
        super(world);
        this.texture = "/mob/sheep.png";
        this.a(0.9F, 1.3F);
        float f = 0.23F;

        this.getNavigation().a(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, f));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 0.25F, Item.WHEAT.id, false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 0.25F));
        this.goalSelector.a(5, this.g);
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, f));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.e.setItem(0, new ItemStack(Item.INK_SACK, 1, 0));
        this.e.setItem(1, new ItemStack(Item.INK_SACK, 1, 0));
    }

    protected boolean be() {
        return true;
    }

    protected void bl() {
        this.f = this.g.f();
        super.bl();
    }

    public void c() {
        if (this.world.isStatic) {
            this.f = Math.max(0, this.f - 1);
        }

        super.c();
    }

    public int getMaxHealth() {
        return 8;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        if (!this.isSheared()) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOOL, 1, (short) 0, (byte) this.getColor()));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected int getLootId() {
        return Block.WOOL.id;
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.SHEARS.id && !this.isSheared() && !this.isBaby()) {
            if (!this.world.isStatic) {
                // CraftBukkit start
                PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
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
            this.makeSound("mob.sheep.shear", 1.0F, 1.0F);
        }

        return super.a(entityhuman);
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

    protected String aY() {
        return "mob.sheep.say";
    }

    protected String aZ() {
        return "mob.sheep.say";
    }

    protected String ba() {
        return "mob.sheep.say";
    }

    protected void a(int i, int j, int k, int l) {
        this.makeSound("mob.sheep.step", 0.15F, 1.0F);
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

    public EntitySheep b(EntityAgeable entityageable) {
        EntitySheep entitysheep = (EntitySheep) entityageable;
        EntitySheep entitysheep1 = new EntitySheep(this.world);
        int i = this.a(this, entitysheep);

        entitysheep1.setColor(15 - i);
        return entitysheep1;
    }

    public void aH() {
        // CraftBukkit start
        SheepRegrowWoolEvent event = new SheepRegrowWoolEvent((org.bukkit.entity.Sheep) this.getBukkitEntity());
        this.world.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.setSheared(false);
        }
        // CraftBukkit end

        if (this.isBaby()) {
            int i = this.getAge() + 1200;

            if (i > 0) {
                i = 0;
            }

            this.setAge(i);
        }
    }

    public void bG() {
        this.setColor(a(this.world.random));
    }

    private int a(EntityAnimal entityanimal, EntityAnimal entityanimal1) {
        int i = this.b(entityanimal);
        int j = this.b(entityanimal1);

        this.e.getItem(0).setData(i);
        this.e.getItem(1).setData(j);
        ItemStack itemstack = CraftingManager.getInstance().craft(this.e, ((EntitySheep) entityanimal).world);
        int k;

        if (itemstack != null && itemstack.getItem().id == Item.INK_SACK.id) {
            k = itemstack.getData();
        } else {
            k = this.world.random.nextBoolean() ? i : j;
        }

        return k;
    }

    private int b(EntityAnimal entityanimal) {
        return 15 - ((EntitySheep) entityanimal).getColor();
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
