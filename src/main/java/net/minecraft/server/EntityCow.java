package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
// CraftBukkit end

public class EntityCow extends EntityAnimal {

    public EntityCow(World world) {
        super(world);
        this.a(0.9F, 1.3F);
        this.getNavigation().a(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 2.0D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.25D, Items.WHEAT, false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.25D));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    public boolean bk() {
        return true;
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.a).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.20000000298023224D);
    }

    protected String t() {
        return "mob.cow.say";
    }

    protected String aT() {
        return "mob.cow.hurt";
    }

    protected String aU() {
        return "mob.cow.hurt";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.cow.step", 0.15F, 1.0F);
    }

    protected float bf() {
        return 0.4F;
    }

    protected Item getLoot() {
        return Items.LEATHER;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - Whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        int k;

        if (j > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(CraftMagicNumbers.getMaterial(Items.LEATHER), j));
        }

        j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);

        if (j > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(this.isBurning() ? CraftMagicNumbers.getMaterial(Items.COOKED_BEEF) : CraftMagicNumbers.getMaterial(Items.RAW_BEEF), j));
        }

        CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.getItem() == Items.BUCKET && !entityhuman.abilities.canInstantlyBuild) {
            // CraftBukkit start - Got milk?
            org.bukkit.Location loc = this.getBukkitEntity().getLocation();
            org.bukkit.event.player.PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), -1, itemstack, Items.MILK_BUCKET);

            if (event.isCancelled()) {
                return false;
            }

            if (--itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, CraftItemStack.asNMSCopy(event.getItemStack()));
            } else if (!entityhuman.inventory.pickup(new ItemStack(Items.MILK_BUCKET))) {
                entityhuman.drop(CraftItemStack.asNMSCopy(event.getItemStack()), false);
            }
            // CraftBukkit end

            return true;
        } else {
            return super.a(entityhuman);
        }
    }

    public EntityCow b(EntityAgeable entityageable) {
        return new EntityCow(this.world);
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
