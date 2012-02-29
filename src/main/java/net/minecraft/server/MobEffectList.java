package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
// CraftBukkit end

public class MobEffectList {

    public static final MobEffectList[] byId = new MobEffectList[32];
    public static final MobEffectList b = null;
    public static final MobEffectList FASTER_MOVEMENT = (new MobEffectList(1, false, 8171462)).a("potion.moveSpeed").a(0, 0);
    public static final MobEffectList SLOWER_MOVEMENT = (new MobEffectList(2, true, 5926017)).a("potion.moveSlowdown").a(1, 0);
    public static final MobEffectList FASTER_DIG = (new MobEffectList(3, false, 14270531)).a("potion.digSpeed").a(2, 0).a(1.5D);
    public static final MobEffectList SLOWER_DIG = (new MobEffectList(4, true, 4866583)).a("potion.digSlowDown").a(3, 0);
    public static final MobEffectList INCREASE_DAMAGE = (new MobEffectList(5, false, 9643043)).a("potion.damageBoost").a(4, 0);
    public static final MobEffectList HEAL = (new InstantMobEffect(6, false, 16262179)).a("potion.heal");
    public static final MobEffectList HARM = (new InstantMobEffect(7, true, 4393481)).a("potion.harm");
    public static final MobEffectList JUMP = (new MobEffectList(8, false, 7889559)).a("potion.jump").a(2, 1);
    public static final MobEffectList CONFUSION = (new MobEffectList(9, true, 5578058)).a("potion.confusion").a(3, 1).a(0.25D);
    public static final MobEffectList REGENERATION = (new MobEffectList(10, false, 13458603)).a("potion.regeneration").a(7, 0).a(0.25D);
    public static final MobEffectList RESISTANCE = (new MobEffectList(11, false, 10044730)).a("potion.resistance").a(6, 1);
    public static final MobEffectList FIRE_RESISTANCE = (new MobEffectList(12, false, 14981690)).a("potion.fireResistance").a(7, 1);
    public static final MobEffectList WATER_BREATHING = (new MobEffectList(13, false, 3035801)).a("potion.waterBreathing").a(0, 2);
    public static final MobEffectList INVISIBILITY = (new MobEffectList(14, false, 8356754)).a("potion.invisibility").a(0, 1).e();
    public static final MobEffectList BLINDNESS = (new MobEffectList(15, true, 2039587)).a("potion.blindness").a(5, 1).a(0.25D);
    public static final MobEffectList NIGHT_VISION = (new MobEffectList(16, false, 2039713)).a("potion.nightVision").a(4, 1).e();
    public static final MobEffectList HUNGER = (new MobEffectList(17, true, 5797459)).a("potion.hunger").a(1, 1);
    public static final MobEffectList WEAKNESS = (new MobEffectList(18, true, 4738376)).a("potion.weakness").a(5, 0);
    public static final MobEffectList POISON = (new MobEffectList(19, true, 5149489)).a("potion.poison").a(6, 0).a(0.25D);
    public static final MobEffectList v = null;
    public static final MobEffectList w = null;
    public static final MobEffectList x = null;
    public static final MobEffectList y = null;
    public static final MobEffectList z = null;
    public static final MobEffectList A = null;
    public static final MobEffectList B = null;
    public static final MobEffectList C = null;
    public static final MobEffectList D = null;
    public static final MobEffectList E = null;
    public static final MobEffectList F = null;
    public static final MobEffectList G = null;
    public final int id;
    private String I = "";
    private int J = -1;
    private final boolean K;
    private double L;
    private boolean M;
    private final int N;

    protected MobEffectList(int i, boolean flag, int j) {
        this.id = i;
        byId[i] = this;
        this.K = flag;
        if (flag) {
            this.L = 0.5D;
        } else {
            this.L = 1.0D;
        }

        this.N = j;

        PotionEffectType.registerPotionEffectType(new CraftPotionEffectType(this)); // CraftBukkit
    }

    protected MobEffectList a(int i, int j) {
        this.J = i + j * 8;
        return this;
    }

    public int getId() {
        return this.id;
    }

    public void tick(EntityLiving entityliving, int i) {
        if (this.id == REGENERATION.id) {
            if (entityliving.getHealth() < entityliving.getMaxHealth()) {
                entityliving.heal(1, RegainReason.MAGIC_REGEN); // CraftBukkit
            }
        } else if (this.id == POISON.id) {
            if (entityliving.getHealth() > 1) {
                // CraftBukkit start
                EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(null, entityliving, DamageCause.POISON, 1);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled() && event.getDamage() > 0) {
                    entityliving.damageEntity(DamageSource.MAGIC, event.getDamage());
                }
                // CraftBukkit end
            }
        } else if (this.id == HUNGER.id && entityliving instanceof EntityHuman) {
            ((EntityHuman) entityliving).c(0.025F * (float) (i + 1));
        } else if ((this.id != HEAL.id || entityliving.aE()) && (this.id != HARM.id || !entityliving.aE())) {
            if (this.id == HARM.id && !entityliving.aE() || this.id == HEAL.id && entityliving.aE()) {
                // CraftBukkit start
                EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(null, entityliving, DamageCause.MAGIC, 6 << i);
                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled() && event.getDamage() > 0) {
                    entityliving.damageEntity(DamageSource.MAGIC, event.getDamage());
                }
                // CraftBukkit end
            }
        } else {
            entityliving.heal(6 << i, RegainReason.MAGIC); // CraftBukkit
        }
    }

    public void applyInstantEffect(EntityLiving entityliving, EntityLiving entityliving1, int i, double d0) {
        // CraftBukkit start - delegate; we need EntityPotion
        applyInstantEffect(entityliving, entityliving1, i, d0, null);
    }

    public void applyInstantEffect(EntityLiving entityliving, EntityLiving entityliving1, int i, double d0, EntityPotion potion) {
        // CraftBukkit end
        int j;

        if ((this.id != HEAL.id || entityliving1.aE()) && (this.id != HARM.id || !entityliving1.aE())) {
            if (this.id == HARM.id && !entityliving1.aE() || this.id == HEAL.id && entityliving1.aE()) {
                j = (int) (d0 * (double) (6 << i) + 0.5D);

                // CraftBukkit start
                EntityDamageEvent event = CraftEventFactory.callEntityDamageEvent(potion != null ? potion : entityliving, entityliving1, DamageCause.MAGIC, j);
                j = event.getDamage();
                if (event.isCancelled() || j == 0) {
                    return;
                }
                // CraftBukkit end

                if (entityliving == null) {
                    entityliving1.damageEntity(DamageSource.MAGIC, j);
                } else {
                    entityliving1.damageEntity(DamageSource.b(entityliving1, entityliving), j);
                }
            }
        } else {
            j = (int) (d0 * (double) (6 << i) + 0.5D);
            entityliving1.heal(j, RegainReason.MAGIC); // CraftBukkit
        }
    }

    public boolean isInstant() {
        return false;
    }

    public boolean b(int i, int j) {
        if (this.id != REGENERATION.id && this.id != POISON.id) {
            return this.id == HUNGER.id;
        } else {
            int k = 25 >> j;

            return k > 0 ? i % k == 0 : true;
        }
    }

    public MobEffectList a(String s) {
        this.I = s;
        return this;
    }

    public String c() {
        return this.I;
    }

    protected MobEffectList a(double d0) {
        this.L = d0;
        return this;
    }

    public double getDurationModifier() {
        return this.L;
    }

    public MobEffectList e() {
        this.M = true;
        return this;
    }

    public boolean f() {
        return this.M;
    }

    public int g() {
        return this.N;
    }
}
