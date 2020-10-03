package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAreaEffectCloud extends Entity {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataWatcherObject<Float> c = DataWatcher.a(EntityAreaEffectCloud.class, DataWatcherRegistry.c);
    private static final DataWatcherObject<Integer> COLOR = DataWatcher.a(EntityAreaEffectCloud.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Boolean> e = DataWatcher.a(EntityAreaEffectCloud.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<ParticleParam> f = DataWatcher.a(EntityAreaEffectCloud.class, DataWatcherRegistry.j);
    private PotionRegistry potionRegistry;
    public List<MobEffect> effects;
    private final Map<Entity, Integer> affectedEntities;
    private int duration;
    public int waitTime;
    public int reapplicationDelay;
    private boolean hasColor;
    public int durationOnUse;
    public float radiusOnUse;
    public float radiusPerTick;
    private EntityLiving ap;
    private UUID aq;

    public EntityAreaEffectCloud(EntityTypes<? extends EntityAreaEffectCloud> entitytypes, World world) {
        super(entitytypes, world);
        this.potionRegistry = Potions.EMPTY;
        this.effects = Lists.newArrayList();
        this.affectedEntities = Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noclip = true;
        this.setRadius(3.0F);
    }

    public EntityAreaEffectCloud(World world, double d0, double d1, double d2) {
        this(EntityTypes.AREA_EFFECT_CLOUD, world);
        this.setPosition(d0, d1, d2);
    }

    @Override
    protected void initDatawatcher() {
        this.getDataWatcher().register(EntityAreaEffectCloud.COLOR, 0);
        this.getDataWatcher().register(EntityAreaEffectCloud.c, 0.5F);
        this.getDataWatcher().register(EntityAreaEffectCloud.e, false);
        this.getDataWatcher().register(EntityAreaEffectCloud.f, Particles.ENTITY_EFFECT);
    }

    public void setRadius(float f) {
        if (!this.world.isClientSide) {
            this.getDataWatcher().set(EntityAreaEffectCloud.c, f);
        }

    }

    @Override
    public void updateSize() {
        double d0 = this.locX();
        double d1 = this.locY();
        double d2 = this.locZ();

        super.updateSize();
        this.setPosition(d0, d1, d2);
    }

    public float getRadius() {
        return (Float) this.getDataWatcher().get(EntityAreaEffectCloud.c);
    }

    public void a(PotionRegistry potionregistry) {
        this.potionRegistry = potionregistry;
        if (!this.hasColor) {
            this.x();
        }

    }

    private void x() {
        if (this.potionRegistry == Potions.EMPTY && this.effects.isEmpty()) {
            this.getDataWatcher().set(EntityAreaEffectCloud.COLOR, 0);
        } else {
            this.getDataWatcher().set(EntityAreaEffectCloud.COLOR, PotionUtil.a((Collection) PotionUtil.a(this.potionRegistry, (Collection) this.effects)));
        }

    }

    public void addEffect(MobEffect mobeffect) {
        this.effects.add(mobeffect);
        if (!this.hasColor) {
            this.x();
        }

    }

    public int getColor() {
        return (Integer) this.getDataWatcher().get(EntityAreaEffectCloud.COLOR);
    }

    public void setColor(int i) {
        this.hasColor = true;
        this.getDataWatcher().set(EntityAreaEffectCloud.COLOR, i);
    }

    public ParticleParam getParticle() {
        return (ParticleParam) this.getDataWatcher().get(EntityAreaEffectCloud.f);
    }

    public void setParticle(ParticleParam particleparam) {
        this.getDataWatcher().set(EntityAreaEffectCloud.f, particleparam);
    }

    protected void a(boolean flag) {
        this.getDataWatcher().set(EntityAreaEffectCloud.e, flag);
    }

    public boolean k() {
        return (Boolean) this.getDataWatcher().get(EntityAreaEffectCloud.e);
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.k();
        float f = this.getRadius();

        if (this.world.isClientSide) {
            ParticleParam particleparam = this.getParticle();
            float f1;
            float f2;
            float f3;
            int i;
            int j;
            int k;

            if (flag) {
                if (this.random.nextBoolean()) {
                    for (int l = 0; l < 2; ++l) {
                        float f4 = this.random.nextFloat() * 6.2831855F;

                        f1 = MathHelper.c(this.random.nextFloat()) * 0.2F;
                        f2 = MathHelper.cos(f4) * f1;
                        f3 = MathHelper.sin(f4) * f1;
                        if (particleparam.getParticle() == Particles.ENTITY_EFFECT) {
                            int i1 = this.random.nextBoolean() ? 16777215 : this.getColor();

                            i = i1 >> 16 & 255;
                            j = i1 >> 8 & 255;
                            k = i1 & 255;
                            this.world.b(particleparam, this.locX() + (double) f2, this.locY(), this.locZ() + (double) f3, (double) ((float) i / 255.0F), (double) ((float) j / 255.0F), (double) ((float) k / 255.0F));
                        } else {
                            this.world.b(particleparam, this.locX() + (double) f2, this.locY(), this.locZ() + (double) f3, 0.0D, 0.0D, 0.0D);
                        }
                    }
                }
            } else {
                float f5 = 3.1415927F * f * f;

                for (int j1 = 0; (float) j1 < f5; ++j1) {
                    f1 = this.random.nextFloat() * 6.2831855F;
                    f2 = MathHelper.c(this.random.nextFloat()) * f;
                    f3 = MathHelper.cos(f1) * f2;
                    float f6 = MathHelper.sin(f1) * f2;

                    if (particleparam.getParticle() == Particles.ENTITY_EFFECT) {
                        i = this.getColor();
                        j = i >> 16 & 255;
                        k = i >> 8 & 255;
                        int k1 = i & 255;

                        this.world.b(particleparam, this.locX() + (double) f3, this.locY(), this.locZ() + (double) f6, (double) ((float) j / 255.0F), (double) ((float) k / 255.0F), (double) ((float) k1 / 255.0F));
                    } else {
                        this.world.b(particleparam, this.locX() + (double) f3, this.locY(), this.locZ() + (double) f6, (0.5D - this.random.nextDouble()) * 0.15D, 0.009999999776482582D, (0.5D - this.random.nextDouble()) * 0.15D);
                    }
                }
            }
        } else {
            if (this.ticksLived >= this.waitTime + this.duration) {
                this.die();
                return;
            }

            boolean flag1 = this.ticksLived < this.waitTime;

            if (flag != flag1) {
                this.a(flag1);
            }

            if (flag1) {
                return;
            }

            if (this.radiusPerTick != 0.0F) {
                f += this.radiusPerTick;
                if (f < 0.5F) {
                    this.die();
                    return;
                }

                this.setRadius(f);
            }

            if (this.ticksLived % 5 == 0) {
                Iterator iterator = this.affectedEntities.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry<Entity, Integer> entry = (Entry) iterator.next();

                    if (this.ticksLived >= (Integer) entry.getValue()) {
                        iterator.remove();
                    }
                }

                List<MobEffect> list = Lists.newArrayList();
                Iterator iterator1 = this.potionRegistry.a().iterator();

                while (iterator1.hasNext()) {
                    MobEffect mobeffect = (MobEffect) iterator1.next();

                    list.add(new MobEffect(mobeffect.getMobEffect(), mobeffect.getDuration() / 4, mobeffect.getAmplifier(), mobeffect.isAmbient(), mobeffect.isShowParticles()));
                }

                list.addAll(this.effects);
                if (list.isEmpty()) {
                    this.affectedEntities.clear();
                } else {
                    List<EntityLiving> list1 = this.world.a(EntityLiving.class, this.getBoundingBox());

                    if (!list1.isEmpty()) {
                        Iterator iterator2 = list1.iterator();

                        while (iterator2.hasNext()) {
                            EntityLiving entityliving = (EntityLiving) iterator2.next();

                            if (!this.affectedEntities.containsKey(entityliving) && entityliving.eg()) {
                                double d0 = entityliving.locX() - this.locX();
                                double d1 = entityliving.locZ() - this.locZ();
                                double d2 = d0 * d0 + d1 * d1;

                                if (d2 <= (double) (f * f)) {
                                    this.affectedEntities.put(entityliving, this.ticksLived + this.reapplicationDelay);
                                    Iterator iterator3 = list.iterator();

                                    while (iterator3.hasNext()) {
                                        MobEffect mobeffect1 = (MobEffect) iterator3.next();

                                        if (mobeffect1.getMobEffect().isInstant()) {
                                            mobeffect1.getMobEffect().applyInstantEffect(this, this.getSource(), entityliving, mobeffect1.getAmplifier(), 0.5D);
                                        } else {
                                            entityliving.addEffect(new MobEffect(mobeffect1));
                                        }
                                    }

                                    if (this.radiusOnUse != 0.0F) {
                                        f += this.radiusOnUse;
                                        if (f < 0.5F) {
                                            this.die();
                                            return;
                                        }

                                        this.setRadius(f);
                                    }

                                    if (this.durationOnUse != 0) {
                                        this.duration += this.durationOnUse;
                                        if (this.duration <= 0) {
                                            this.die();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public void setRadiusOnUse(float f) {
        this.radiusOnUse = f;
    }

    public void setRadiusPerTick(float f) {
        this.radiusPerTick = f;
    }

    public void setWaitTime(int i) {
        this.waitTime = i;
    }

    public void setSource(@Nullable EntityLiving entityliving) {
        this.ap = entityliving;
        this.aq = entityliving == null ? null : entityliving.getUniqueID();
    }

    @Nullable
    public EntityLiving getSource() {
        if (this.ap == null && this.aq != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntity(this.aq);

            if (entity instanceof EntityLiving) {
                this.ap = (EntityLiving) entity;
            }
        }

        return this.ap;
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        this.ticksLived = nbttagcompound.getInt("Age");
        this.duration = nbttagcompound.getInt("Duration");
        this.waitTime = nbttagcompound.getInt("WaitTime");
        this.reapplicationDelay = nbttagcompound.getInt("ReapplicationDelay");
        this.durationOnUse = nbttagcompound.getInt("DurationOnUse");
        this.radiusOnUse = nbttagcompound.getFloat("RadiusOnUse");
        this.radiusPerTick = nbttagcompound.getFloat("RadiusPerTick");
        this.setRadius(nbttagcompound.getFloat("Radius"));
        if (nbttagcompound.b("Owner")) {
            this.aq = nbttagcompound.a("Owner");
        }

        if (nbttagcompound.hasKeyOfType("Particle", 8)) {
            try {
                this.setParticle(ArgumentParticle.b(new StringReader(nbttagcompound.getString("Particle"))));
            } catch (CommandSyntaxException commandsyntaxexception) {
                EntityAreaEffectCloud.LOGGER.warn("Couldn't load custom particle {}", nbttagcompound.getString("Particle"), commandsyntaxexception);
            }
        }

        if (nbttagcompound.hasKeyOfType("Color", 99)) {
            this.setColor(nbttagcompound.getInt("Color"));
        }

        if (nbttagcompound.hasKeyOfType("Potion", 8)) {
            this.a(PotionUtil.c(nbttagcompound));
        }

        if (nbttagcompound.hasKeyOfType("Effects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("Effects", 10);

            this.effects.clear();

            for (int i = 0; i < nbttaglist.size(); ++i) {
                MobEffect mobeffect = MobEffect.b(nbttaglist.getCompound(i));

                if (mobeffect != null) {
                    this.addEffect(mobeffect);
                }
            }
        }

    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Age", this.ticksLived);
        nbttagcompound.setInt("Duration", this.duration);
        nbttagcompound.setInt("WaitTime", this.waitTime);
        nbttagcompound.setInt("ReapplicationDelay", this.reapplicationDelay);
        nbttagcompound.setInt("DurationOnUse", this.durationOnUse);
        nbttagcompound.setFloat("RadiusOnUse", this.radiusOnUse);
        nbttagcompound.setFloat("RadiusPerTick", this.radiusPerTick);
        nbttagcompound.setFloat("Radius", this.getRadius());
        nbttagcompound.setString("Particle", this.getParticle().a());
        if (this.aq != null) {
            nbttagcompound.a("Owner", this.aq);
        }

        if (this.hasColor) {
            nbttagcompound.setInt("Color", this.getColor());
        }

        if (this.potionRegistry != Potions.EMPTY && this.potionRegistry != null) {
            nbttagcompound.setString("Potion", IRegistry.POTION.getKey(this.potionRegistry).toString());
        }

        if (!this.effects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.effects.iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();

                nbttaglist.add(mobeffect.a(new NBTTagCompound()));
            }

            nbttagcompound.set("Effects", nbttaglist);
        }

    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        if (EntityAreaEffectCloud.c.equals(datawatcherobject)) {
            this.updateSize();
        }

        super.a(datawatcherobject);
    }

    @Override
    public EnumPistonReaction getPushReaction() {
        return EnumPistonReaction.IGNORE;
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }

    @Override
    public EntitySize a(EntityPose entitypose) {
        return EntitySize.b(this.getRadius() * 2.0F, 0.5F);
    }
}
