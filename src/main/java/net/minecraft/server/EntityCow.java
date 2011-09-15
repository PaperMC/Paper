package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketFillEvent;
// CraftBukkit end

public class EntityCow extends EntityAnimal {

    public EntityCow(World world) {
        super(world);
        this.texture = "/mob/cow.png";
        this.b(0.9F, 1.3F);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected String h() {
        return "mob.cow";
    }

    protected String i() {
        return "mob.cowhurt";
    }

    protected String j() {
        return "mob.cowhurt";
    }

    protected float l() {
        return 0.4F;
    }

    protected int k() {
        return Item.LEATHER.id;
    }

    protected void a(boolean flag) {
        int i = this.random.nextInt(3);

        int j;

        for (j = 0; j < i; ++j) {
            this.b(Item.LEATHER.id, 1);
        }

        i = this.random.nextInt(3) + 1;

        for (j = 0; j < i; ++j) {
            if (this.fireTicks > 0) {
                this.b(Item.COOKED_BEEF.id, 1);
            } else {
                this.b(Item.RAW_BEEF.id, 1);
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.BUCKET.id) {
            // CraftBukkit start - got milk?
            Location loc = this.getBukkitEntity().getLocation();
            PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), -1, itemstack, Item.MILK_BUCKET);

            if (event.isCancelled()) {
                return false;
            }

            CraftItemStack itemInHand = (CraftItemStack) event.getItemStack();
            byte data = itemInHand.getData() == null ? (byte) 0 : itemInHand.getData().getData();
            itemstack = new ItemStack(itemInHand.getTypeId(), itemInHand.getAmount(), data);

            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, itemstack);
            // CraftBukkit end

            return true;
        } else {
            return false;
        }
    }
}
