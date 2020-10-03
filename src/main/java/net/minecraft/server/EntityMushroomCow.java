package net.minecraft.server;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.tuple.Pair;
// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTransformEvent;
// CraftBukkit end

public class EntityMushroomCow extends EntityCow implements IShearable {

    private static final DataWatcherObject<String> bo = DataWatcher.a(EntityMushroomCow.class, DataWatcherRegistry.d);
    private MobEffectList bp;
    private int bq;
    private UUID br;

    public EntityMushroomCow(EntityTypes<? extends EntityMushroomCow> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return iworldreader.getType(blockposition.down()).a(Blocks.MYCELIUM) ? 10.0F : iworldreader.y(blockposition) - 0.5F;
    }

    public static boolean c(EntityTypes<EntityMushroomCow> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return generatoraccess.getType(blockposition.down()).a(Blocks.MYCELIUM) && generatoraccess.getLightLevel(blockposition, 0) > 8;
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {
        UUID uuid = entitylightning.getUniqueID();

        if (!uuid.equals(this.br)) {
            this.setVariant(this.getVariant() == EntityMushroomCow.Type.RED ? EntityMushroomCow.Type.BROWN : EntityMushroomCow.Type.RED);
            this.br = uuid;
            this.playSound(SoundEffects.ENTITY_MOOSHROOM_CONVERT, 2.0F, 1.0F);
        }

    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityMushroomCow.bo, EntityMushroomCow.Type.RED.c);
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.BOWL && !this.isBaby()) {
            boolean flag = false;
            ItemStack itemstack1;

            if (this.bp != null) {
                flag = true;
                itemstack1 = new ItemStack(Items.SUSPICIOUS_STEW);
                ItemSuspiciousStew.a(itemstack1, this.bp, this.bq);
                this.bp = null;
                this.bq = 0;
            } else {
                itemstack1 = new ItemStack(Items.MUSHROOM_STEW);
            }

            ItemStack itemstack2 = ItemLiquidUtil.a(itemstack, entityhuman, itemstack1, false);

            entityhuman.a(enumhand, itemstack2);
            SoundEffect soundeffect;

            if (flag) {
                soundeffect = SoundEffects.ENTITY_MOOSHROOM_SUSPICIOUS_MILK;
            } else {
                soundeffect = SoundEffects.ENTITY_MOOSHROOM_MILK;
            }

            this.playSound(soundeffect, 1.0F, 1.0F);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (itemstack.getItem() == Items.SHEARS && this.canShear()) {
            // CraftBukkit start
            if (!CraftEventFactory.handlePlayerShearEntityEvent(entityhuman, this, itemstack, enumhand)) {
                return EnumInteractionResult.PASS;
            }
            // CraftBukkit end
            this.shear(SoundCategory.PLAYERS);
            if (!this.world.isClientSide) {
                itemstack.damage(1, entityhuman, (entityhuman1) -> {
                    entityhuman1.broadcastItemBreak(enumhand);
                });
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (this.getVariant() == EntityMushroomCow.Type.BROWN && itemstack.getItem().a((Tag) TagsItem.SMALL_FLOWERS)) {
            if (this.bp != null) {
                for (int i = 0; i < 2; ++i) {
                    this.world.addParticle(Particles.SMOKE, this.locX() + this.random.nextDouble() / 2.0D, this.e(0.5D), this.locZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
                }
            } else {
                Optional<Pair<MobEffectList, Integer>> optional = this.l(itemstack);

                if (!optional.isPresent()) {
                    return EnumInteractionResult.PASS;
                }

                Pair<MobEffectList, Integer> pair = (Pair) optional.get();

                if (!entityhuman.abilities.canInstantlyBuild) {
                    itemstack.subtract(1);
                }

                for (int j = 0; j < 4; ++j) {
                    this.world.addParticle(Particles.EFFECT, this.locX() + this.random.nextDouble() / 2.0D, this.e(0.5D), this.locZ() + this.random.nextDouble() / 2.0D, 0.0D, this.random.nextDouble() / 5.0D, 0.0D);
                }

                this.bp = (MobEffectList) pair.getLeft();
                this.bq = (Integer) pair.getRight();
                this.playSound(SoundEffects.ENTITY_MOOSHROOM_EAT, 2.0F, 1.0F);
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    @Override
    public void shear(SoundCategory soundcategory) {
        this.world.playSound((EntityHuman) null, (Entity) this, SoundEffects.ENTITY_MOOSHROOM_SHEAR, soundcategory, 1.0F, 1.0F);
        if (!this.world.s_()) {
            ((WorldServer) this.world).a(Particles.EXPLOSION, this.locX(), this.e(0.5D), this.locZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            // this.die(); // CraftBukkit - moved down
            EntityCow entitycow = (EntityCow) EntityTypes.COW.a(this.world);

            entitycow.setPositionRotation(this.locX(), this.locY(), this.locZ(), this.yaw, this.pitch);
            entitycow.setHealth(this.getHealth());
            entitycow.aA = this.aA;
            if (this.hasCustomName()) {
                entitycow.setCustomName(this.getCustomName());
                entitycow.setCustomNameVisible(this.getCustomNameVisible());
            }

            if (this.isPersistent()) {
                entitycow.setPersistent();
            }

            entitycow.setInvulnerable(this.isInvulnerable());
            // CraftBukkit start
            if (CraftEventFactory.callEntityTransformEvent(this, entitycow, EntityTransformEvent.TransformReason.SHEARED).isCancelled()) {
                return;
            }
            this.world.addEntity(entitycow, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SHEARED);

            this.die(); // CraftBukkit - from above
            // CraftBukkit end

            for (int i = 0; i < 5; ++i) {
                this.world.addEntity(new EntityItem(this.world, this.locX(), this.e(1.0D), this.locZ(), new ItemStack(this.getVariant().d.getBlock())));
            }
        }

    }

    @Override
    public boolean canShear() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setString("Type", this.getVariant().c);
        if (this.bp != null) {
            nbttagcompound.setByte("EffectId", (byte) MobEffectList.getId(this.bp));
            nbttagcompound.setInt("EffectDuration", this.bq);
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setVariant(EntityMushroomCow.Type.b(nbttagcompound.getString("Type")));
        if (nbttagcompound.hasKeyOfType("EffectId", 1)) {
            this.bp = MobEffectList.fromId(nbttagcompound.getByte("EffectId"));
        }

        if (nbttagcompound.hasKeyOfType("EffectDuration", 3)) {
            this.bq = nbttagcompound.getInt("EffectDuration");
        }

    }

    private Optional<Pair<MobEffectList, Integer>> l(ItemStack itemstack) {
        Item item = itemstack.getItem();

        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();

            if (block instanceof BlockFlowers) {
                BlockFlowers blockflowers = (BlockFlowers) block;

                return Optional.of(Pair.of(blockflowers.c(), blockflowers.d()));
            }
        }

        return Optional.empty();
    }

    public void setVariant(EntityMushroomCow.Type entitymushroomcow_type) {
        this.datawatcher.set(EntityMushroomCow.bo, entitymushroomcow_type.c);
    }

    public EntityMushroomCow.Type getVariant() {
        return EntityMushroomCow.Type.b((String) this.datawatcher.get(EntityMushroomCow.bo));
    }

    @Override
    public EntityMushroomCow createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntityMushroomCow entitymushroomcow = (EntityMushroomCow) EntityTypes.MOOSHROOM.a((World) worldserver);

        entitymushroomcow.setVariant(this.a((EntityMushroomCow) entityageable));
        return entitymushroomcow;
    }

    private EntityMushroomCow.Type a(EntityMushroomCow entitymushroomcow) {
        EntityMushroomCow.Type entitymushroomcow_type = this.getVariant();
        EntityMushroomCow.Type entitymushroomcow_type1 = entitymushroomcow.getVariant();
        EntityMushroomCow.Type entitymushroomcow_type2;

        if (entitymushroomcow_type == entitymushroomcow_type1 && this.random.nextInt(1024) == 0) {
            entitymushroomcow_type2 = entitymushroomcow_type == EntityMushroomCow.Type.BROWN ? EntityMushroomCow.Type.RED : EntityMushroomCow.Type.BROWN;
        } else {
            entitymushroomcow_type2 = this.random.nextBoolean() ? entitymushroomcow_type : entitymushroomcow_type1;
        }

        return entitymushroomcow_type2;
    }

    public static enum Type {

        RED("red", Blocks.RED_MUSHROOM.getBlockData()), BROWN("brown", Blocks.BROWN_MUSHROOM.getBlockData());

        private final String c;
        private final IBlockData d;

        private Type(String s, IBlockData iblockdata) {
            this.c = s;
            this.d = iblockdata;
        }

        private static EntityMushroomCow.Type b(String s) {
            EntityMushroomCow.Type[] aentitymushroomcow_type = values();
            int i = aentitymushroomcow_type.length;

            for (int j = 0; j < i; ++j) {
                EntityMushroomCow.Type entitymushroomcow_type = aentitymushroomcow_type[j];

                if (entitymushroomcow_type.c.equals(s)) {
                    return entitymushroomcow_type;
                }
            }

            return EntityMushroomCow.Type.RED;
        }
    }
}
