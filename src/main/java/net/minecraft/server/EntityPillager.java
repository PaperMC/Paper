package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;

public class EntityPillager extends EntityIllagerAbstract implements ICrossbow {

    private static final DataWatcherObject<Boolean> b = DataWatcher.a(EntityPillager.class, DataWatcherRegistry.i);
    public final InventorySubcontainer inventory = new InventorySubcontainer(5);

    public EntityPillager(EntityTypes<? extends EntityPillager> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new EntityRaider.a(this, 10.0F));
        this.goalSelector.a(3, new PathfinderGoalCrossbowAttack<>(this, 1.0D, 8.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 15.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 15.0F));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a(new Class[0])); // CraftBukkit - decompile error
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, false));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
    }

    public static AttributeProvider.Builder eK() {
        return EntityMonster.eR().a(GenericAttributes.MOVEMENT_SPEED, 0.3499999940395355D).a(GenericAttributes.MAX_HEALTH, 24.0D).a(GenericAttributes.ATTACK_DAMAGE, 5.0D).a(GenericAttributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityPillager.b, false);
    }

    @Override
    public boolean a(ItemProjectileWeapon itemprojectileweapon) {
        return itemprojectileweapon == Items.CROSSBOW;
    }

    @Override
    public void b(boolean flag) {
        this.datawatcher.set(EntityPillager.b, flag);
    }

    @Override
    public void U_() {
        this.ticksFarFromPlayer = 0;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);

            if (!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new NBTTagCompound()));
            }
        }

        nbttagcompound.set("Inventory", nbttaglist);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);

        for (int i = 0; i < nbttaglist.size(); ++i) {
            ItemStack itemstack = ItemStack.a(nbttaglist.getCompound(i));

            if (!itemstack.isEmpty()) {
                this.inventory.a(itemstack);
            }
        }

        this.setCanPickupLoot(true);
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        IBlockData iblockdata = iworldreader.getType(blockposition.down());

        return !iblockdata.a(Blocks.GRASS_BLOCK) && !iblockdata.a(Blocks.SAND) ? 0.5F - iworldreader.y(blockposition) : 10.0F;
    }

    @Override
    public int getMaxSpawnGroup() {
        return 1;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.a(difficultydamagescaler);
        this.b(difficultydamagescaler);
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    @Override
    protected void w(float f) {
        super.w(f);
        if (this.random.nextInt(300) == 0) {
            ItemStack itemstack = this.getItemInMainHand();

            if (itemstack.getItem() == Items.CROSSBOW) {
                Map<Enchantment, Integer> map = EnchantmentManager.a(itemstack);

                map.putIfAbsent(Enchantments.PIERCING, 1);
                EnchantmentManager.a(map, itemstack);
                this.setSlot(EnumItemSlot.MAINHAND, itemstack);
            }
        }

    }

    @Override
    public boolean r(Entity entity) {
        return super.r(entity) ? true : (entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == EnumMonsterType.ILLAGER ? this.getScoreboardTeam() == null && entity.getScoreboardTeam() == null : false);
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_PILLAGER_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_PILLAGER_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_PILLAGER_HURT;
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        this.b(this, 1.6F);
    }

    @Override
    public void a(EntityLiving entityliving, ItemStack itemstack, IProjectile iprojectile, float f) {
        this.a(this, entityliving, iprojectile, f, 1.6F);
    }

    @Override
    protected void b(EntityItem entityitem) {
        ItemStack itemstack = entityitem.getItemStack();

        if (itemstack.getItem() instanceof ItemBanner) {
            super.b(entityitem);
        } else {
            Item item = itemstack.getItem();

            if (this.b(item)) {
                this.a(entityitem);
                ItemStack itemstack1 = this.inventory.a(itemstack);

                if (itemstack1.isEmpty()) {
                    entityitem.die();
                } else {
                    itemstack.setCount(itemstack1.getCount());
                }
            }
        }

    }

    private boolean b(Item item) {
        return this.fb() && item == Items.WHITE_BANNER;
    }

    @Override
    public boolean a_(int i, ItemStack itemstack) {
        if (super.a_(i, itemstack)) {
            return true;
        } else {
            int j = i - 300;

            if (j >= 0 && j < this.inventory.getSize()) {
                this.inventory.setItem(j, itemstack);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void a(int i, boolean flag) {
        Raid raid = this.fa();
        boolean flag1 = this.random.nextFloat() <= raid.w();

        if (flag1) {
            ItemStack itemstack = new ItemStack(Items.CROSSBOW);
            Map<Enchantment, Integer> map = Maps.newHashMap();

            if (i > raid.a(EnumDifficulty.NORMAL)) {
                map.put(Enchantments.QUICK_CHARGE, 2);
            } else if (i > raid.a(EnumDifficulty.EASY)) {
                map.put(Enchantments.QUICK_CHARGE, 1);
            }

            map.put(Enchantments.MULTISHOT, 1);
            EnchantmentManager.a((Map) map, itemstack);
            this.setSlot(EnumItemSlot.MAINHAND, itemstack);
        }

    }

    @Override
    public SoundEffect eL() {
        return SoundEffects.ENTITY_PILLAGER_CELEBRATE;
    }
}
