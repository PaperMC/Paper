package net.minecraft.server;

// CraftBukkit start
import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
// CraftBukkit end

public class EntityMushroomCow extends EntityCow {

    public EntityMushroomCow(World world) {
        super(world);
        this.texture = "/mob/redcow.png";
        this.b(0.9F, 1.3F);
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.BOWL.id && this.getAge() >= 0) {
            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, new ItemStack(Item.MUSHROOM_SOUP));
            return true;
        } else if (itemstack != null && itemstack.id == Item.SHEARS.id && this.getAge() >= 0) {
            // CraftBukkit start
            List<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
            for (int i = 0; i < 5; ++i) {
                drops.add(new CraftItemStack(Block.RED_MUSHROOM.id, 1));
            }
            org.bukkit.event.player.PlayerShearEntityEvent event = new org.bukkit.event.player.PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity(), drops);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            this.die();
            this.world.a("largeexplode", this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D);
            if (!this.world.isStatic) {
                EntityCow entitycow = new EntityCow(this.world);

                entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
                entitycow.setHealth(this.getHealth());
                entitycow.V = this.V;
                this.world.addEntity(entitycow);

                /* CraftBukkit start - This logic moved up to before the event is fired ...
                for (int i = 0; i < 5; ++i) {
                    this.world.addEntity(new EntityItem(this.world, this.locX, this.locY + (double) this.length, this.locZ, new ItemStack(Block.RED_MUSHROOM)));
                }
                // ... and replaced by this logic */
                for (org.bukkit.inventory.ItemStack stack : drops) {
                    this.world.addEntity(new EntityItem(this.world, this.locX, this.locY + this.length, this.locZ, CraftItemStack.createNMSItemStack(stack)));
                }
                // CraftBukkit end
            }

            return true;
        } else {
            return super.b(entityhuman);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityMushroomCow(this.world);
    }
}
