package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class EntitySheep extends EntityAnimal implements IShearable {

    private static final DataWatcherObject<Byte> bo = DataWatcher.a(EntitySheep.class, DataWatcherRegistry.a);
    private static final Map<EnumColor, IMaterial> bp = (Map) SystemUtils.a((Object) Maps.newEnumMap(EnumColor.class), (enummap) -> {
        enummap.put(EnumColor.WHITE, Blocks.WHITE_WOOL);
        enummap.put(EnumColor.ORANGE, Blocks.ORANGE_WOOL);
        enummap.put(EnumColor.MAGENTA, Blocks.MAGENTA_WOOL);
        enummap.put(EnumColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        enummap.put(EnumColor.YELLOW, Blocks.YELLOW_WOOL);
        enummap.put(EnumColor.LIME, Blocks.LIME_WOOL);
        enummap.put(EnumColor.PINK, Blocks.PINK_WOOL);
        enummap.put(EnumColor.GRAY, Blocks.GRAY_WOOL);
        enummap.put(EnumColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        enummap.put(EnumColor.CYAN, Blocks.CYAN_WOOL);
        enummap.put(EnumColor.PURPLE, Blocks.PURPLE_WOOL);
        enummap.put(EnumColor.BLUE, Blocks.BLUE_WOOL);
        enummap.put(EnumColor.BROWN, Blocks.BROWN_WOOL);
        enummap.put(EnumColor.GREEN, Blocks.GREEN_WOOL);
        enummap.put(EnumColor.RED, Blocks.RED_WOOL);
        enummap.put(EnumColor.BLACK, Blocks.BLACK_WOOL);
    });
    private static final Map<EnumColor, float[]> bq = Maps.newEnumMap((Map) Arrays.stream(EnumColor.values()).collect(Collectors.toMap((enumcolor) -> {
        return enumcolor;
    }, EntitySheep::c)));
    private int br;
    private PathfinderGoalEatTile bs;

    private static float[] c(EnumColor enumcolor) {
        if (enumcolor == EnumColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = enumcolor.getColor();
            float f = 0.75F;

            return new float[]{afloat[0] * 0.75F, afloat[1] * 0.75F, afloat[2] * 0.75F};
        }
    }

    public EntitySheep(EntityTypes<? extends EntitySheep> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        this.bs = new PathfinderGoalEatTile(this);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 1.1D, RecipeItemStack.a(Items.WHEAT), false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.1D));
        this.goalSelector.a(5, this.bs);
        this.goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    }

    @Override
    protected void mobTick() {
        this.br = this.bs.g();
        super.mobTick();
    }

    @Override
    public void movementTick() {
        if (this.world.isClientSide) {
            this.br = Math.max(0, this.br - 1);
        }

        super.movementTick();
    }

    public static AttributeProvider.Builder eK() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 8.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.23000000417232513D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntitySheep.bo, (byte) 0);
    }

    @Override
    public MinecraftKey getDefaultLootTable() {
        if (this.isSheared()) {
            return this.getEntityType().i();
        } else {
            switch (this.getColor()) {
                case WHITE:
                default:
                    return LootTables.Q;
                case ORANGE:
                    return LootTables.R;
                case MAGENTA:
                    return LootTables.S;
                case LIGHT_BLUE:
                    return LootTables.T;
                case YELLOW:
                    return LootTables.U;
                case LIME:
                    return LootTables.V;
                case PINK:
                    return LootTables.W;
                case GRAY:
                    return LootTables.X;
                case LIGHT_GRAY:
                    return LootTables.Y;
                case CYAN:
                    return LootTables.Z;
                case PURPLE:
                    return LootTables.aa;
                case BLUE:
                    return LootTables.ab;
                case BROWN:
                    return LootTables.ac;
                case GREEN:
                    return LootTables.ad;
                case RED:
                    return LootTables.ae;
                case BLACK:
                    return LootTables.af;
            }
        }
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (itemstack.getItem() == Items.SHEARS) {
            if (!this.world.isClientSide && this.canShear()) {
                this.shear(SoundCategory.PLAYERS);
                itemstack.damage(1, entityhuman, (entityhuman1) -> {
                    entityhuman1.broadcastItemBreak(enumhand);
                });
                return EnumInteractionResult.SUCCESS;
            } else {
                return EnumInteractionResult.CONSUME;
            }
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    @Override
    public void shear(SoundCategory soundcategory) {
        this.world.playSound((EntityHuman) null, (Entity) this, SoundEffects.ENTITY_SHEEP_SHEAR, soundcategory, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 1 + this.random.nextInt(3);

        for (int j = 0; j < i; ++j) {
            EntityItem entityitem = this.a((IMaterial) EntitySheep.bp.get(this.getColor()), 1);

            if (entityitem != null) {
                entityitem.setMot(entityitem.getMot().add((double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double) (this.random.nextFloat() * 0.05F), (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    @Override
    public boolean canShear() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("Sheared", this.isSheared());
        nbttagcompound.setByte("Color", (byte) this.getColor().getColorIndex());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.setSheared(nbttagcompound.getBoolean("Sheared"));
        this.setColor(EnumColor.fromColorIndex(nbttagcompound.getByte("Color")));
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_SHEEP_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_SHEEP_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_SHEEP_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_SHEEP_STEP, 0.15F, 1.0F);
    }

    public EnumColor getColor() {
        return EnumColor.fromColorIndex((Byte) this.datawatcher.get(EntitySheep.bo) & 15);
    }

    public void setColor(EnumColor enumcolor) {
        byte b0 = (Byte) this.datawatcher.get(EntitySheep.bo);

        this.datawatcher.set(EntitySheep.bo, (byte) (b0 & 240 | enumcolor.getColorIndex() & 15));
    }

    public boolean isSheared() {
        return ((Byte) this.datawatcher.get(EntitySheep.bo) & 16) != 0;
    }

    public void setSheared(boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntitySheep.bo);

        if (flag) {
            this.datawatcher.set(EntitySheep.bo, (byte) (b0 | 16));
        } else {
            this.datawatcher.set(EntitySheep.bo, (byte) (b0 & -17));
        }

    }

    public static EnumColor a(Random random) {
        int i = random.nextInt(100);

        return i < 5 ? EnumColor.BLACK : (i < 10 ? EnumColor.GRAY : (i < 15 ? EnumColor.LIGHT_GRAY : (i < 18 ? EnumColor.BROWN : (random.nextInt(500) == 0 ? EnumColor.PINK : EnumColor.WHITE))));
    }

    @Override
    public EntitySheep createChild(WorldServer worldserver, EntityAgeable entityageable) {
        EntitySheep entitysheep = (EntitySheep) entityageable;
        EntitySheep entitysheep1 = (EntitySheep) EntityTypes.SHEEP.a((World) worldserver);

        entitysheep1.setColor(this.a((EntityAnimal) this, (EntityAnimal) entitysheep));
        return entitysheep1;
    }

    @Override
    public void blockEaten() {
        this.setSheared(false);
        if (this.isBaby()) {
            this.setAge(60);
        }

    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setColor(a(worldaccess.getRandom()));
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    private EnumColor a(EntityAnimal entityanimal, EntityAnimal entityanimal1) {
        EnumColor enumcolor = ((EntitySheep) entityanimal).getColor();
        EnumColor enumcolor1 = ((EntitySheep) entityanimal1).getColor();
        InventoryCrafting inventorycrafting = a(enumcolor, enumcolor1);
        Optional optional = this.world.getCraftingManager().craft(Recipes.CRAFTING, inventorycrafting, this.world).map((recipecrafting) -> {
            return recipecrafting.a(inventorycrafting);
        }).map(ItemStack::getItem);

        ItemDye.class.getClass();
        optional = optional.filter(ItemDye.class::isInstance);
        ItemDye.class.getClass();
        return (EnumColor) optional.map(ItemDye.class::cast).map(ItemDye::d).orElseGet(() -> {
            return this.world.random.nextBoolean() ? enumcolor : enumcolor1;
        });
    }

    private static InventoryCrafting a(EnumColor enumcolor, EnumColor enumcolor1) {
        InventoryCrafting inventorycrafting = new InventoryCrafting(new Container((Containers) null, -1) {
            @Override
            public boolean canUse(EntityHuman entityhuman) {
                return false;
            }
        }, 2, 1);

        inventorycrafting.setItem(0, new ItemStack(ItemDye.a(enumcolor)));
        inventorycrafting.setItem(1, new ItemStack(ItemDye.a(enumcolor1)));
        return inventorycrafting;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.95F * entitysize.height;
    }
}
