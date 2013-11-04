package net.minecraft.server;

import java.util.List;
import java.util.UUID;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityPigZombie extends EntityZombie {

    private static final UUID bq = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier br = (new AttributeModifier(bq, "Attacking speed boost", 0.45D, 0)).a(false);
    public int angerLevel; // CraftBukkit - private -> public
    private int soundDelay;
    private Entity bu;

    public EntityPigZombie(World world) {
        super(world);
        this.fireProof = true;
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(bp).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.5D);
        this.getAttributeInstance(GenericAttributes.e).setValue(5.0D);
    }

    protected boolean bk() {
        return false;
    }

    public void h() {
        if (this.bu != this.target && !this.world.isStatic) {
            AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

            attributeinstance.b(br);
            if (this.target != null) {
                attributeinstance.a(br);
            }
        }

        this.bu = this.target;
        if (this.soundDelay > 0 && --this.soundDelay == 0) {
            this.makeSound("mob.zombiepig.zpigangry", this.bf() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        super.h();
    }

    public boolean canSpawn() {
        return this.world.difficulty != EnumDifficulty.PEACEFUL && this.world.b(this.boundingBox) && this.world.getCubes(this, this.boundingBox).isEmpty() && !this.world.containsLiquid(this.boundingBox);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
    }

    protected Entity findTarget() {
        return this.angerLevel == 0 ? null : super.findTarget();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();

            if (entity instanceof EntityHuman) {
                List list = this.world.getEntities(this, this.boundingBox.grow(32.0D, 32.0D, 32.0D));

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity1 = (Entity) list.get(i);

                    if (entity1 instanceof EntityPigZombie) {
                        EntityPigZombie entitypigzombie = (EntityPigZombie) entity1;

                        entitypigzombie.c(entity);
                    }
                }

                this.c(entity);
            }

            return super.damageEntity(damagesource, f);
        }
    }

    private void c(Entity entity) {
        // CraftBukkit start
        org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

        EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.PIG_ZOMBIE_TARGET);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (event.getTarget() == null) {
            this.target = null;
            return;
        }
        entity = ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
        // CraftBukkit end

        this.target = entity;
        this.angerLevel = 400 + this.random.nextInt(400);
        this.soundDelay = this.random.nextInt(40);
    }

    protected String t() {
        return "mob.zombiepig.zpig";
    }

    protected String aT() {
        return "mob.zombiepig.zpighurt";
    }

    protected String aU() {
        return "mob.zombiepig.zpigdeath";
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(2 + i);

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Items.ROTTEN_FLESH, j));
        }

        j = this.random.nextInt(2 + i);

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Items.GOLD_NUGGET, j));
        }

        // Determine rare item drops and add them to the loot
        if (this.lastDamageByPlayerTime > 0) {
            int k = this.random.nextInt(200) - i;

            if (k < 5) {
                ItemStack itemstack = this.getRareDrop(k <= 0 ? 1 : 0);
                if (itemstack != null) {
                    loot.add(CraftItemStack.asCraftMirror(itemstack));
                }
            }
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean a(EntityHuman entityhuman) {
        return false;
    }

    // CraftBukkit start - Return rare dropped item instead of dropping it
    protected ItemStack getRareDrop(int i) {
        return new ItemStack(Items.GOLD_INGOT, 1, 0);
    }
    // CraftBukkit end

    protected void bA() {
        this.setEquipment(0, new ItemStack(Items.GOLD_SWORD));
    }

    public GroupDataEntity a(GroupDataEntity groupdataentity) {
        super.a(groupdataentity);
        this.setVillager(false);
        return groupdataentity;
    }
}
