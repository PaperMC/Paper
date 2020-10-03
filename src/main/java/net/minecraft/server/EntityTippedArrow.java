package net.minecraft.server;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class EntityTippedArrow extends EntityArrow {

    private static final DataWatcherObject<Integer> COLOR = DataWatcher.a(EntityTippedArrow.class, DataWatcherRegistry.b);
    private PotionRegistry potionRegistry;
    public final Set<MobEffect> effects;
    private boolean hasColor;

    public EntityTippedArrow(EntityTypes<? extends EntityTippedArrow> entitytypes, World world) {
        super(entitytypes, world);
        this.potionRegistry = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, double d0, double d1, double d2) {
        super(EntityTypes.ARROW, d0, d1, d2, world);
        this.potionRegistry = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, EntityLiving entityliving) {
        super(EntityTypes.ARROW, entityliving, world);
        this.potionRegistry = Potions.EMPTY;
        this.effects = Sets.newHashSet();
    }

    public void b(ItemStack itemstack) {
        if (itemstack.getItem() == Items.TIPPED_ARROW) {
            this.potionRegistry = PotionUtil.d(itemstack);
            Collection<MobEffect> collection = PotionUtil.b(itemstack);

            if (!collection.isEmpty()) {
                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {
                    MobEffect mobeffect = (MobEffect) iterator.next();

                    this.effects.add(new MobEffect(mobeffect));
                }
            }

            int i = c(itemstack);

            if (i == -1) {
                this.z();
            } else {
                this.setColor(i);
            }
        } else if (itemstack.getItem() == Items.ARROW) {
            this.potionRegistry = Potions.EMPTY;
            this.effects.clear();
            this.datawatcher.set(EntityTippedArrow.COLOR, -1);
        }

    }

    public static int c(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        return nbttagcompound != null && nbttagcompound.hasKeyOfType("CustomPotionColor", 99) ? nbttagcompound.getInt("CustomPotionColor") : -1;
    }

    private void z() {
        this.hasColor = false;
        if (this.potionRegistry == Potions.EMPTY && this.effects.isEmpty()) {
            this.datawatcher.set(EntityTippedArrow.COLOR, -1);
        } else {
            this.datawatcher.set(EntityTippedArrow.COLOR, PotionUtil.a((Collection) PotionUtil.a(this.potionRegistry, (Collection) this.effects)));
        }

    }

    public void addEffect(MobEffect mobeffect) {
        this.effects.add(mobeffect);
        this.getDataWatcher().set(EntityTippedArrow.COLOR, PotionUtil.a((Collection) PotionUtil.a(this.potionRegistry, (Collection) this.effects)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityTippedArrow.COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide) {
            if (this.inGround) {
                if (this.c % 5 == 0) {
                    this.b(1);
                }
            } else {
                this.b(2);
            }
        } else if (this.inGround && this.c != 0 && !this.effects.isEmpty() && this.c >= 600) {
            this.world.broadcastEntityEffect(this, (byte) 0);
            this.potionRegistry = Potions.EMPTY;
            this.effects.clear();
            this.datawatcher.set(EntityTippedArrow.COLOR, -1);
        }

    }

    private void b(int i) {
        int j = this.getColor();

        if (j != -1 && i > 0) {
            double d0 = (double) (j >> 16 & 255) / 255.0D;
            double d1 = (double) (j >> 8 & 255) / 255.0D;
            double d2 = (double) (j >> 0 & 255) / 255.0D;

            for (int k = 0; k < i; ++k) {
                this.world.addParticle(Particles.ENTITY_EFFECT, this.d(0.5D), this.cE(), this.g(0.5D), d0, d1, d2);
            }

        }
    }

    public int getColor() {
        return (Integer) this.datawatcher.get(EntityTippedArrow.COLOR);
    }

    public void setColor(int i) {
        this.hasColor = true;
        this.datawatcher.set(EntityTippedArrow.COLOR, i);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.potionRegistry != Potions.EMPTY && this.potionRegistry != null) {
            nbttagcompound.setString("Potion", IRegistry.POTION.getKey(this.potionRegistry).toString());
        }

        if (this.hasColor) {
            nbttagcompound.setInt("Color", this.getColor());
        }

        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                nbttaglist.add(mobeffect.a(new NBTTagCompound()));
            }

            nbttagcompound.set("CustomPotionEffects", nbttaglist);
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("Potion", 8)) {
            this.potionRegistry = PotionUtil.c(nbttagcompound);
        }

        Iterator iterator = PotionUtil.b(nbttagcompound).iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            this.addEffect(mobeffect);
        }

        if (nbttagcompound.hasKeyOfType("Color", 99)) {
            this.setColor(nbttagcompound.getInt("Color"));
        } else {
            this.z();
        }

    }

    @Override
    protected void a(EntityLiving entityliving) {
        super.a(entityliving);
        Iterator iterator = this.potionRegistry.a().iterator();

        MobEffect mobeffect;

        while (iterator.hasNext()) {
            mobeffect = (MobEffect) iterator.next();
            entityliving.addEffect(new MobEffect(mobeffect.getMobEffect(), Math.max(mobeffect.getDuration() / 8, 1), mobeffect.getAmplifier(), mobeffect.isAmbient(), mobeffect.isShowParticles()));
        }

        if (!this.effects.isEmpty()) {
            iterator = this.effects.iterator();

            while (iterator.hasNext()) {
                mobeffect = (MobEffect) iterator.next();
                entityliving.addEffect(mobeffect);
            }
        }

    }

    @Override
    protected ItemStack getItemStack() {
        if (this.effects.isEmpty() && this.potionRegistry == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);

            PotionUtil.a(itemstack, this.potionRegistry);
            PotionUtil.a(itemstack, (Collection) this.effects);
            if (this.hasColor) {
                itemstack.getOrCreateTag().setInt("CustomPotionColor", this.getColor());
            }

            return itemstack;
        }
    }
}
