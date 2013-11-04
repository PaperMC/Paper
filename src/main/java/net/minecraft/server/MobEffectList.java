package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.minecraft.util.com.google.common.collect.Maps;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
// CraftBukkit end

public class MobEffectList {

    public static final MobEffectList[] byId = new MobEffectList[32];
    public static final MobEffectList b = null;
    public static final MobEffectList FASTER_MOVEMENT = (new MobEffectList(1, false, 8171462)).b("potion.moveSpeed").b(0, 0).a(GenericAttributes.d, "91AEAA56-376B-4498-935B-2F7F68070635", 0.20000000298023224D, 2);
    public static final MobEffectList SLOWER_MOVEMENT = (new MobEffectList(2, true, 5926017)).b("potion.moveSlowdown").b(1, 0).a(GenericAttributes.d, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15000000596046448D, 2);
    public static final MobEffectList FASTER_DIG = (new MobEffectList(3, false, 14270531)).b("potion.digSpeed").b(2, 0).a(1.5D);
    public static final MobEffectList SLOWER_DIG = (new MobEffectList(4, true, 4866583)).b("potion.digSlowDown").b(3, 0);
    public static final MobEffectList INCREASE_DAMAGE = (new MobEffectAttackDamage(5, false, 9643043)).b("potion.damageBoost").b(4, 0).a(GenericAttributes.e, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 3.0D, 2);
    public static final MobEffectList HEAL = (new InstantMobEffect(6, false, 16262179)).b("potion.heal");
    public static final MobEffectList HARM = (new InstantMobEffect(7, true, 4393481)).b("potion.harm");
    public static final MobEffectList JUMP = (new MobEffectList(8, false, 7889559)).b("potion.jump").b(2, 1);
    public static final MobEffectList CONFUSION = (new MobEffectList(9, true, 5578058)).b("potion.confusion").b(3, 1).a(0.25D);
    public static final MobEffectList REGENERATION = (new MobEffectList(10, false, 13458603)).b("potion.regeneration").b(7, 0).a(0.25D);
    public static final MobEffectList RESISTANCE = (new MobEffectList(11, false, 10044730)).b("potion.resistance").b(6, 1);
    public static final MobEffectList FIRE_RESISTANCE = (new MobEffectList(12, false, 14981690)).b("potion.fireResistance").b(7, 1);
    public static final MobEffectList WATER_BREATHING = (new MobEffectList(13, false, 3035801)).b("potion.waterBreathing").b(0, 2);
    public static final MobEffectList INVISIBILITY = (new MobEffectList(14, false, 8356754)).b("potion.invisibility").b(0, 1);
    public static final MobEffectList BLINDNESS = (new MobEffectList(15, true, 2039587)).b("potion.blindness").b(5, 1).a(0.25D);
    public static final MobEffectList NIGHT_VISION = (new MobEffectList(16, false, 2039713)).b("potion.nightVision").b(4, 1);
    public static final MobEffectList HUNGER = (new MobEffectList(17, true, 5797459)).b("potion.hunger").b(1, 1);
    public static final MobEffectList WEAKNESS = (new MobEffectAttackDamage(18, true, 4738376)).b("potion.weakness").b(5, 0).a(GenericAttributes.e, "22653B89-116E-49DC-9B6B-9971489B5BE5", 2.0D, 0);
    public static final MobEffectList POISON = (new MobEffectList(19, true, 5149489)).b("potion.poison").b(6, 0).a(0.25D);
    public static final MobEffectList WITHER = (new MobEffectList(20, true, 3484199)).b("potion.wither").b(1, 2).a(0.25D);
    public static final MobEffectList HEALTH_BOOST = (new MobEffectHealthBoost(21, false, 16284963)).b("potion.healthBoost").b(2, 2).a(GenericAttributes.a, "5D6F0BA2-1186-46AC-B896-C61C5CEE99CC", 4.0D, 0);
    public static final MobEffectList ABSORPTION = (new MobEffectAbsorption(22, false, 2445989)).b("potion.absorption").b(2, 2);
    public static final MobEffectList SATURATION = (new InstantMobEffect(23, false, 16262179)).b("potion.saturation");
    public static final MobEffectList z = null;
    public static final MobEffectList A = null;
    public static final MobEffectList B = null;
    public static final MobEffectList C = null;
    public static final MobEffectList D = null;
    public static final MobEffectList E = null;
    public static final MobEffectList F = null;
    public static final MobEffectList G = null;
    public final int id;
    private final Map I = Maps.newHashMap();
    private final boolean J;
    private final int K;
    private String L = "";
    private int M = -1;
    private double N;
    private boolean O;

    protected MobEffectList(int i, boolean flag, int j) {
        this.id = i;
        byId[i] = this;
        this.J = flag;
        if (flag) {
            this.N = 0.5D;
        } else {
            this.N = 1.0D;
        }

        this.K = j;

        org.bukkit.potion.PotionEffectType.registerPotionEffectType(new org.bukkit.craftbukkit.potion.CraftPotionEffectType(this)); // CraftBukkit
    }

    protected MobEffectList b(int i, int j) {
        this.M = i + j * 8;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public void tick(EntityLiving entityliving, int i) {
        if (this.id == REGENERATION.id) {
            if (entityliving.getHealth() < entityliving.getMaxHealth()) {
                entityliving.heal(1.0F, RegainReason.MAGIC_REGEN); // CraftBukkit
            }
        } else if (this.id == POISON.id) {
            if (entityliving.getHealth() > 1.0F) {
                entityliving.damageEntity(CraftEventFactory.POISON, 1.0F); // CraftBukkit - DamageSource.MAGIC -> CraftEventFactory.POISON
            }
        } else if (this.id == WITHER.id) {
            entityliving.damageEntity(DamageSource.WITHER, 1.0F);
        } else if (this.id == HUNGER.id && entityliving instanceof EntityHuman) {
            ((EntityHuman) entityliving).a(0.025F * (float) (i + 1));
        } else if (this.id == SATURATION.id && entityliving instanceof EntityHuman) {
            if (!entityliving.world.isStatic) {
                ((EntityHuman) entityliving).getFoodData().eat(i + 1, 1.0F);
            }
        } else if ((this.id != HEAL.id || entityliving.aR()) && (this.id != HARM.id || !entityliving.aR())) {
            if (this.id == HARM.id && !entityliving.aR() || this.id == HEAL.id && entityliving.aR()) {
                entityliving.damageEntity(DamageSource.MAGIC, (float) (6 << i));
            }
        } else {
            entityliving.heal((float) Math.max(4 << i, 0), RegainReason.MAGIC); // CraftBukkit
        }
    }

    public void applyInstantEffect(EntityLiving entityliving, EntityLiving entityliving1, int i, double d0) {
        // CraftBukkit start - Delegate; we need EntityPotion
        applyInstantEffect(entityliving, entityliving1, i, d0, null);
    }

    public void applyInstantEffect(EntityLiving entityliving, EntityLiving entityliving1, int i, double d0, EntityPotion potion) {
        // CraftBukkit end
        int j;

        if ((this.id != HEAL.id || entityliving1.aR()) && (this.id != HARM.id || !entityliving1.aR())) {
            if (this.id == HARM.id && !entityliving1.aR() || this.id == HEAL.id && entityliving1.aR()) {
                j = (int) (d0 * (double) (6 << i) + 0.5D);
                if (entityliving == null) {
                    entityliving1.damageEntity(DamageSource.MAGIC, (float) j);
                } else {
                    // CraftBukkit - The "damager" needs to be the potion
                    entityliving1.damageEntity(DamageSource.b(potion != null ? potion : entityliving1, entityliving), (float) j);
                }
            }
        } else {
            j = (int) (d0 * (double) (4 << i) + 0.5D);
            entityliving1.heal((float) j, RegainReason.MAGIC); // CraftBukkit
        }
    }

    public boolean isInstant() {
        return false;
    }

    public boolean a(int i, int j) {
        int k;

        if (this.id == REGENERATION.id) {
            k = 50 >> j;
            return k > 0 ? i % k == 0 : true;
        } else if (this.id == POISON.id) {
            k = 25 >> j;
            return k > 0 ? i % k == 0 : true;
        } else if (this.id == WITHER.id) {
            k = 40 >> j;
            return k > 0 ? i % k == 0 : true;
        } else {
            return this.id == HUNGER.id;
        }
    }

    public MobEffectList b(String s) {
        this.L = s;
        return this;
    }

    public String a() {
        return this.L;
    }

    protected MobEffectList a(double d0) {
        this.N = d0;
        return this;
    }

    public double getDurationModifier() {
        return this.N;
    }

    public boolean i() {
        return this.O;
    }

    public int j() {
        return this.K;
    }

    public MobEffectList a(IAttribute iattribute, String s, double d0, int i) {
        AttributeModifier attributemodifier = new AttributeModifier(UUID.fromString(s), this.a(), d0, i);

        this.I.put(iattribute, attributemodifier);
        return this;
    }

    public void a(EntityLiving entityliving, AttributeMapBase attributemapbase, int i) {
        Iterator iterator = this.I.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            AttributeInstance attributeinstance = attributemapbase.a((IAttribute) entry.getKey());

            if (attributeinstance != null) {
                attributeinstance.b((AttributeModifier) entry.getValue());
            }
        }
    }

    public void b(EntityLiving entityliving, AttributeMapBase attributemapbase, int i) {
        Iterator iterator = this.I.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            AttributeInstance attributeinstance = attributemapbase.a((IAttribute) entry.getKey());

            if (attributeinstance != null) {
                AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();

                attributeinstance.b(attributemodifier);
                attributeinstance.a(new AttributeModifier(attributemodifier.a(), this.a() + " " + i, this.a(i, attributemodifier), attributemodifier.c()));
            }
        }
    }

    public double a(int i, AttributeModifier attributemodifier) {
        return attributemodifier.d() * (double) (i + 1);
    }
}
