package net.minecraft.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityLightning extends Entity {

    private int lifeTicks;
    public long b;
    private int d;
    public boolean isEffect;
    @Nullable
    private EntityPlayer f;

    public EntityLightning(EntityTypes<? extends EntityLightning> entitytypes, World world) {
        super(entitytypes, world);
        this.Y = true;
        this.lifeTicks = 2;
        this.b = this.random.nextLong();
        this.d = this.random.nextInt(3) + 1;
    }

    public void setEffect(boolean flag) {
        this.isEffect = flag;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }

    public void d(@Nullable EntityPlayer entityplayer) {
        this.f = entityplayer;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.lifeTicks == 2) {
            EnumDifficulty enumdifficulty = this.world.getDifficulty();

            if (enumdifficulty == EnumDifficulty.NORMAL || enumdifficulty == EnumDifficulty.HARD) {
                this.a(4);
            }

            // CraftBukkit start - Use relative location for far away sounds
            // this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            float pitch = 0.8F + this.random.nextFloat() * 0.2F;
            int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
            for (EntityPlayer player : (List<EntityPlayer>) (List) this.world.getPlayers()) {
                double deltaX = this.locX() - player.locX();
                double deltaZ = this.locZ() - player.locZ();
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > viewDistance * viewDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.locX() + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.locZ() + (deltaZ / deltaLength) * viewDistance;
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, relativeX, this.locY(), relativeZ, 10000.0F, pitch));
                } else {
                    player.playerConnection.sendPacket(new PacketPlayOutNamedSoundEffect(SoundEffects.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, this.locX(), this.locY(), this.locZ(), 10000.0F, pitch));
                }
            }
            // CraftBukkit end
            this.world.playSound((EntityHuman) null, this.locX(), this.locY(), this.locZ(), SoundEffects.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            if (this.d == 0) {
                this.die();
            } else if (this.lifeTicks < -this.random.nextInt(10)) {
                --this.d;
                this.lifeTicks = 1;
                this.b = this.random.nextLong();
                this.a(0);
            }
        }

        if (this.lifeTicks >= 0 && !this.isEffect) { // CraftBukkit - add !this.isEffect
            if (!(this.world instanceof WorldServer)) {
                this.world.c(2);
            } else if (!this.isEffect) {
                double d0 = 3.0D;
                List<Entity> list = this.world.getEntities(this, new AxisAlignedBB(this.locX() - 3.0D, this.locY() - 3.0D, this.locZ() - 3.0D, this.locX() + 3.0D, this.locY() + 6.0D + 3.0D, this.locZ() + 3.0D), Entity::isAlive);
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.onLightningStrike((WorldServer) this.world, this);
                }

                if (this.f != null) {
                    CriterionTriggers.E.a(this.f, (Collection) list);
                }
            }
        }

    }

    private void a(int i) {
        if (!this.isEffect && !this.world.isClientSide && this.world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            BlockPosition blockposition = this.getChunkCoordinates();
            IBlockData iblockdata = BlockFireAbstract.a((IBlockAccess) this.world, blockposition);

            if (this.world.getType(blockposition).isAir() && iblockdata.canPlace(this.world, blockposition)) {
                // CraftBukkit start - add "!isEffect"
                if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(world, blockposition, this).isCancelled()) {
                    this.world.setTypeUpdate(blockposition, iblockdata);
                }
                // CraftBukkit end
            }

            for (int j = 0; j < i; ++j) {
                BlockPosition blockposition1 = blockposition.b(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

                iblockdata = BlockFireAbstract.a((IBlockAccess) this.world, blockposition1);
                if (this.world.getType(blockposition1).isAir() && iblockdata.canPlace(this.world, blockposition1)) {
                    // CraftBukkit start - add "!isEffect"
                    if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(world, blockposition1, this).isCancelled()) {
                        this.world.setTypeUpdate(blockposition1, iblockdata);
                    }
                    // CraftBukkit end
                }
            }

        }
    }

    @Override
    protected void initDatawatcher() {}

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {}

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {}

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }
}
