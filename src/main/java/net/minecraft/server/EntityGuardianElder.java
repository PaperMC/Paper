package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class EntityGuardianElder extends EntityGuardian {

    public static final float b = EntityTypes.ELDER_GUARDIAN.j() / EntityTypes.GUARDIAN.j();

    public EntityGuardianElder(EntityTypes<? extends EntityGuardianElder> entitytypes, World world) {
        super(entitytypes, world);
        this.setPersistent();
        if (this.goalRandomStroll != null) {
            this.goalRandomStroll.setTimeBetweenMovement(400);
        }

    }

    public static AttributeProvider.Builder m() {
        return EntityGuardian.eM().a(GenericAttributes.MOVEMENT_SPEED, 0.30000001192092896D).a(GenericAttributes.ATTACK_DAMAGE, 8.0D).a(GenericAttributes.MAX_HEALTH, 80.0D);
    }

    @Override
    public int eK() {
        return 60;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return this.aG() ? SoundEffects.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEffects.ENTITY_ELDER_GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return this.aG() ? SoundEffects.ENTITY_ELDER_GUARDIAN_HURT : SoundEffects.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return this.aG() ? SoundEffects.ENTITY_ELDER_GUARDIAN_DEATH : SoundEffects.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }

    @Override
    protected SoundEffect getSoundFlop() {
        return SoundEffects.ENTITY_ELDER_GUARDIAN_FLOP;
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        boolean flag = true;

        if ((this.ticksLived + this.getId()) % 1200 == 0) {
            MobEffectList mobeffectlist = MobEffects.SLOWER_DIG;
            List<EntityPlayer> list = ((WorldServer) this.world).a((entityplayer) -> {
                return this.h((Entity) entityplayer) < 2500.0D && entityplayer.playerInteractManager.d();
            });
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();

                if (!entityplayer.hasEffect(mobeffectlist) || entityplayer.getEffect(mobeffectlist).getAmplifier() < 2 || entityplayer.getEffect(mobeffectlist).getDuration() < 1200) {
                    entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.k, this.isSilent() ? 0.0F : 1.0F));
                    entityplayer.addEffect(new MobEffect(mobeffectlist, 6000, 2), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.ATTACK); // CraftBukkit
                }
            }
        }

        if (!this.ez()) {
            this.a(this.getChunkCoordinates(), 16);
        }

    }
}
