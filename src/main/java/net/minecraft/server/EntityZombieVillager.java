package net.minecraft.server;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class EntityZombieVillager extends EntityZombie implements VillagerDataHolder {

    public static final DataWatcherObject<Boolean> CONVERTING = DataWatcher.a(EntityZombieVillager.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<VillagerData> c = DataWatcher.a(EntityZombieVillager.class, DataWatcherRegistry.q);
    public int conversionTime;
    public UUID conversionPlayer;
    private NBTBase bp;
    private NBTTagCompound bq;
    private int br;

    public EntityZombieVillager(EntityTypes<? extends EntityZombieVillager> entitytypes, World world) {
        super(entitytypes, world);
        this.setVillagerData(this.getVillagerData().withProfession((VillagerProfession) IRegistry.VILLAGER_PROFESSION.a(this.random)));
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityZombieVillager.CONVERTING, false);
        this.datawatcher.register(EntityZombieVillager.c, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        DataResult dataresult = VillagerData.a.encodeStart(DynamicOpsNBT.a, this.getVillagerData());
        Logger logger = EntityZombieVillager.LOGGER;

        logger.getClass();
        dataresult.resultOrPartial(logger::error).ifPresent((nbtbase) -> {
            nbttagcompound.set("VillagerData", nbtbase);
        });
        if (this.bq != null) {
            nbttagcompound.set("Offers", this.bq);
        }

        if (this.bp != null) {
            nbttagcompound.set("Gossips", this.bp);
        }

        nbttagcompound.setInt("ConversionTime", this.isConverting() ? this.conversionTime : -1);
        if (this.conversionPlayer != null) {
            nbttagcompound.a("ConversionPlayer", this.conversionPlayer);
        }

        nbttagcompound.setInt("Xp", this.br);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("VillagerData", 10)) {
            DataResult<VillagerData> dataresult = VillagerData.a.parse(new Dynamic(DynamicOpsNBT.a, nbttagcompound.get("VillagerData")));
            Logger logger = EntityZombieVillager.LOGGER;

            logger.getClass();
            dataresult.resultOrPartial(logger::error).ifPresent(this::setVillagerData);
        }

        if (nbttagcompound.hasKeyOfType("Offers", 10)) {
            this.bq = nbttagcompound.getCompound("Offers");
        }

        if (nbttagcompound.hasKeyOfType("Gossips", 10)) {
            this.bp = nbttagcompound.getList("Gossips", 10);
        }

        if (nbttagcompound.hasKeyOfType("ConversionTime", 99) && nbttagcompound.getInt("ConversionTime") > -1) {
            this.startConversion(nbttagcompound.b("ConversionPlayer") ? nbttagcompound.a("ConversionPlayer") : null, nbttagcompound.getInt("ConversionTime"));
        }

        if (nbttagcompound.hasKeyOfType("Xp", 3)) {
            this.br = nbttagcompound.getInt("Xp");
        }

    }

    @Override
    public void tick() {
        if (!this.world.isClientSide && this.isAlive() && this.isConverting()) {
            int i = this.getConversionProgress();

            this.conversionTime -= i;
            if (this.conversionTime <= 0) {
                this.c((WorldServer) this.world);
            }
        }

        super.tick();
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.GOLDEN_APPLE) {
            if (this.hasEffect(MobEffects.WEAKNESS)) {
                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }

                if (!this.world.isClientSide) {
                    this.startConversion(entityhuman.getUniqueID(), this.random.nextInt(2401) + 3600);
                }

                return EnumInteractionResult.SUCCESS;
            } else {
                return EnumInteractionResult.CONSUME;
            }
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    @Override
    protected boolean eN() {
        return false;
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return !this.isConverting() && this.br == 0;
    }

    public boolean isConverting() {
        return (Boolean) this.getDataWatcher().get(EntityZombieVillager.CONVERTING);
    }

    public void startConversion(@Nullable UUID uuid, int i) {
        this.conversionPlayer = uuid;
        this.conversionTime = i;
        this.getDataWatcher().set(EntityZombieVillager.CONVERTING, true);
        this.removeEffect(MobEffects.WEAKNESS);
        this.addEffect(new MobEffect(MobEffects.INCREASE_DAMAGE, i, Math.min(this.world.getDifficulty().a() - 1, 0)));
        this.world.broadcastEntityEffect(this, (byte) 16);
    }

    private void c(WorldServer worldserver) {
        EntityVillager entityvillager = (EntityVillager) this.a(EntityTypes.VILLAGER, false);
        EnumItemSlot[] aenumitemslot = EnumItemSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];
            ItemStack itemstack = this.getEquipment(enumitemslot);

            if (!itemstack.isEmpty()) {
                if (EnchantmentManager.d(itemstack)) {
                    entityvillager.a_(enumitemslot.b() + 300, itemstack);
                } else {
                    double d0 = (double) this.e(enumitemslot);

                    if (d0 > 1.0D) {
                        this.a(itemstack);
                    }
                }
            }
        }

        entityvillager.setVillagerData(this.getVillagerData());
        if (this.bp != null) {
            entityvillager.a(this.bp);
        }

        if (this.bq != null) {
            entityvillager.b(new MerchantRecipeList(this.bq));
        }

        entityvillager.setExperience(this.br);
        entityvillager.prepare(worldserver, worldserver.getDamageScaler(entityvillager.getChunkCoordinates()), EnumMobSpawn.CONVERSION, (GroupDataEntity) null, (NBTTagCompound) null);
        if (this.conversionPlayer != null) {
            EntityHuman entityhuman = worldserver.b(this.conversionPlayer);

            if (entityhuman instanceof EntityPlayer) {
                CriterionTriggers.r.a((EntityPlayer) entityhuman, (EntityZombie) this, entityvillager);
                worldserver.a(ReputationEvent.a, (Entity) entityhuman, (ReputationHandler) entityvillager);
            }
        }

        entityvillager.addEffect(new MobEffect(MobEffects.CONFUSION, 200, 0));
        if (!this.isSilent()) {
            worldserver.a((EntityHuman) null, 1027, this.getChunkCoordinates(), 0);
        }

    }

    private int getConversionProgress() {
        int i = 1;

        if (this.random.nextFloat() < 0.01F) {
            int j = 0;
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

            for (int k = (int) this.locX() - 4; k < (int) this.locX() + 4 && j < 14; ++k) {
                for (int l = (int) this.locY() - 4; l < (int) this.locY() + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.locZ() - 4; i1 < (int) this.locZ() + 4 && j < 14; ++i1) {
                        Block block = this.world.getType(blockposition_mutableblockposition.d(k, l, i1)).getBlock();

                        if (block == Blocks.IRON_BARS || block instanceof BlockBed) {
                            if (this.random.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    @Override
    protected float dG() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 2.0F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_ZOMBIE_VILLAGER_AMBIENT;
    }

    @Override
    public SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_ZOMBIE_VILLAGER_HURT;
    }

    @Override
    public SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_ZOMBIE_VILLAGER_DEATH;
    }

    @Override
    public SoundEffect getSoundStep() {
        return SoundEffects.ENTITY_ZOMBIE_VILLAGER_STEP;
    }

    @Override
    protected ItemStack eM() {
        return ItemStack.b;
    }

    public void setOffers(NBTTagCompound nbttagcompound) {
        this.bq = nbttagcompound;
    }

    public void a(NBTBase nbtbase) {
        this.bp = nbtbase;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setVillagerData(this.getVillagerData().withType(VillagerType.a(worldaccess.i(this.getChunkCoordinates()))));
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    public void setVillagerData(VillagerData villagerdata) {
        VillagerData villagerdata1 = this.getVillagerData();

        if (villagerdata1.getProfession() != villagerdata.getProfession()) {
            this.bq = null;
        }

        this.datawatcher.set(EntityZombieVillager.c, villagerdata);
    }

    @Override
    public VillagerData getVillagerData() {
        return (VillagerData) this.datawatcher.get(EntityZombieVillager.c);
    }

    public void a(int i) {
        this.br = i;
    }
}
