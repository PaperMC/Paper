package net.minecraft.server;

import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTypes<T extends Entity> {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final EntityTypes<EntityAreaEffectCloud> AREA_EFFECT_CLOUD = a("area_effect_cloud", EntityTypes.Builder.a(EntityAreaEffectCloud::new, EnumCreatureType.MISC).c().a(6.0F, 0.5F).trackingRange(10).updateInterval(10)); // CraftBukkit - SPIGOT-3729: track area effect clouds
    public static final EntityTypes<EntityArmorStand> ARMOR_STAND = a("armor_stand", EntityTypes.Builder.a(EntityArmorStand::new, EnumCreatureType.MISC).a(0.5F, 1.975F).trackingRange(10));
    public static final EntityTypes<EntityTippedArrow> ARROW = a("arrow", EntityTypes.Builder.a(EntityTippedArrow::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(4).updateInterval(20));
    public static final EntityTypes<EntityBat> BAT = a("bat", EntityTypes.Builder.a(EntityBat::new, EnumCreatureType.AMBIENT).a(0.5F, 0.9F).trackingRange(5));
    public static final EntityTypes<EntityBee> BEE = a("bee", EntityTypes.Builder.a(EntityBee::new, EnumCreatureType.CREATURE).a(0.7F, 0.6F).trackingRange(8));
    public static final EntityTypes<EntityBlaze> BLAZE = a("blaze", EntityTypes.Builder.a(EntityBlaze::new, EnumCreatureType.MONSTER).c().a(0.6F, 1.8F).trackingRange(8));
    public static final EntityTypes<EntityBoat> BOAT = a("boat", EntityTypes.Builder.a(EntityBoat::new, EnumCreatureType.MISC).a(1.375F, 0.5625F).trackingRange(10));
    public static final EntityTypes<EntityCat> CAT = a("cat", EntityTypes.Builder.a(EntityCat::new, EnumCreatureType.CREATURE).a(0.6F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityCaveSpider> CAVE_SPIDER = a("cave_spider", EntityTypes.Builder.a(EntityCaveSpider::new, EnumCreatureType.MONSTER).a(0.7F, 0.5F).trackingRange(8));
    public static final EntityTypes<EntityChicken> CHICKEN = a("chicken", EntityTypes.Builder.a(EntityChicken::new, EnumCreatureType.CREATURE).a(0.4F, 0.7F).trackingRange(10));
    public static final EntityTypes<EntityCod> COD = a("cod", EntityTypes.Builder.a(EntityCod::new, EnumCreatureType.WATER_AMBIENT).a(0.5F, 0.3F).trackingRange(4));
    public static final EntityTypes<EntityCow> COW = a("cow", EntityTypes.Builder.a(EntityCow::new, EnumCreatureType.CREATURE).a(0.9F, 1.4F).trackingRange(10));
    public static final EntityTypes<EntityCreeper> CREEPER = a("creeper", EntityTypes.Builder.a(EntityCreeper::new, EnumCreatureType.MONSTER).a(0.6F, 1.7F).trackingRange(8));
    public static final EntityTypes<EntityDolphin> DOLPHIN = a("dolphin", EntityTypes.Builder.a(EntityDolphin::new, EnumCreatureType.WATER_CREATURE).a(0.9F, 0.6F));
    public static final EntityTypes<EntityHorseDonkey> DONKEY = a("donkey", EntityTypes.Builder.a(EntityHorseDonkey::new, EnumCreatureType.CREATURE).a(1.3964844F, 1.5F).trackingRange(10));
    public static final EntityTypes<EntityDragonFireball> DRAGON_FIREBALL = a("dragon_fireball", EntityTypes.Builder.a(EntityDragonFireball::new, EnumCreatureType.MISC).a(1.0F, 1.0F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityDrowned> DROWNED = a("drowned", EntityTypes.Builder.a(EntityDrowned::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityGuardianElder> ELDER_GUARDIAN = a("elder_guardian", EntityTypes.Builder.a(EntityGuardianElder::new, EnumCreatureType.MONSTER).a(1.9975F, 1.9975F).trackingRange(10));
    public static final EntityTypes<EntityEnderCrystal> END_CRYSTAL = a("end_crystal", EntityTypes.Builder.a(EntityEnderCrystal::new, EnumCreatureType.MISC).a(2.0F, 2.0F).trackingRange(16).updateInterval(Integer.MAX_VALUE));
    public static final EntityTypes<EntityEnderDragon> ENDER_DRAGON = a("ender_dragon", EntityTypes.Builder.a(EntityEnderDragon::new, EnumCreatureType.MONSTER).c().a(16.0F, 8.0F).trackingRange(10));
    public static final EntityTypes<EntityEnderman> ENDERMAN = a("enderman", EntityTypes.Builder.a(EntityEnderman::new, EnumCreatureType.MONSTER).a(0.6F, 2.9F).trackingRange(8));
    public static final EntityTypes<EntityEndermite> ENDERMITE = a("endermite", EntityTypes.Builder.a(EntityEndermite::new, EnumCreatureType.MONSTER).a(0.4F, 0.3F).trackingRange(8));
    public static final EntityTypes<EntityEvoker> EVOKER = a("evoker", EntityTypes.Builder.a(EntityEvoker::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityEvokerFangs> EVOKER_FANGS = a("evoker_fangs", EntityTypes.Builder.a(EntityEvokerFangs::new, EnumCreatureType.MISC).a(0.5F, 0.8F).trackingRange(6).updateInterval(2));
    public static final EntityTypes<EntityExperienceOrb> EXPERIENCE_ORB = a("experience_orb", EntityTypes.Builder.a(EntityExperienceOrb::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(6).updateInterval(20));
    public static final EntityTypes<EntityEnderSignal> EYE_OF_ENDER = a("eye_of_ender", EntityTypes.Builder.a(EntityEnderSignal::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(4));
    public static final EntityTypes<EntityFallingBlock> FALLING_BLOCK = a("falling_block", EntityTypes.Builder.a(EntityFallingBlock::new, EnumCreatureType.MISC).a(0.98F, 0.98F).trackingRange(10).updateInterval(20));
    public static final EntityTypes<EntityFireworks> FIREWORK_ROCKET = a("firework_rocket", EntityTypes.Builder.a(EntityFireworks::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityFox> FOX = a("fox", EntityTypes.Builder.a(EntityFox::new, EnumCreatureType.CREATURE).a(0.6F, 0.7F).trackingRange(8).a(Blocks.SWEET_BERRY_BUSH));
    public static final EntityTypes<EntityGhast> GHAST = a("ghast", EntityTypes.Builder.a(EntityGhast::new, EnumCreatureType.MONSTER).c().a(4.0F, 4.0F).trackingRange(10));
    public static final EntityTypes<EntityGiantZombie> GIANT = a("giant", EntityTypes.Builder.a(EntityGiantZombie::new, EnumCreatureType.MONSTER).a(3.6F, 12.0F).trackingRange(10));
    public static final EntityTypes<EntityGuardian> GUARDIAN = a("guardian", EntityTypes.Builder.a(EntityGuardian::new, EnumCreatureType.MONSTER).a(0.85F, 0.85F).trackingRange(8));
    public static final EntityTypes<EntityHoglin> HOGLIN = a("hoglin", EntityTypes.Builder.a(EntityHoglin::new, EnumCreatureType.MONSTER).a(1.3964844F, 1.4F).trackingRange(8));
    public static final EntityTypes<EntityHorse> HORSE = a("horse", EntityTypes.Builder.a(EntityHorse::new, EnumCreatureType.CREATURE).a(1.3964844F, 1.6F).trackingRange(10));
    public static final EntityTypes<EntityZombieHusk> HUSK = a("husk", EntityTypes.Builder.a(EntityZombieHusk::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityIllagerIllusioner> ILLUSIONER = a("illusioner", EntityTypes.Builder.a(EntityIllagerIllusioner::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityIronGolem> IRON_GOLEM = a("iron_golem", EntityTypes.Builder.a(EntityIronGolem::new, EnumCreatureType.MISC).a(1.4F, 2.7F).trackingRange(10));
    public static final EntityTypes<EntityItem> ITEM = a("item", EntityTypes.Builder.a(EntityItem::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(6).updateInterval(20));
    public static final EntityTypes<EntityItemFrame> ITEM_FRAME = a("item_frame", EntityTypes.Builder.a(EntityItemFrame::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(10).updateInterval(Integer.MAX_VALUE));
    public static final EntityTypes<EntityLargeFireball> FIREBALL = a("fireball", EntityTypes.Builder.a(EntityLargeFireball::new, EnumCreatureType.MISC).a(1.0F, 1.0F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityLeash> LEASH_KNOT = a("leash_knot", EntityTypes.Builder.a(EntityLeash::new, EnumCreatureType.MISC).b().a(0.5F, 0.5F).trackingRange(10).updateInterval(Integer.MAX_VALUE));
    public static final EntityTypes<EntityLightning> LIGHTNING_BOLT = a("lightning_bolt", EntityTypes.Builder.a(EntityLightning::new, EnumCreatureType.MISC).b().a(0.0F, 0.0F).trackingRange(16).updateInterval(Integer.MAX_VALUE));
    public static final EntityTypes<EntityLlama> LLAMA = a("llama", EntityTypes.Builder.a(EntityLlama::new, EnumCreatureType.CREATURE).a(0.9F, 1.87F).trackingRange(10));
    public static final EntityTypes<EntityLlamaSpit> LLAMA_SPIT = a("llama_spit", EntityTypes.Builder.a(EntityLlamaSpit::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityMagmaCube> MAGMA_CUBE = a("magma_cube", EntityTypes.Builder.a(EntityMagmaCube::new, EnumCreatureType.MONSTER).c().a(2.04F, 2.04F).trackingRange(8));
    public static final EntityTypes<EntityMinecartRideable> MINECART = a("minecart", EntityTypes.Builder.a(EntityMinecartRideable::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartChest> CHEST_MINECART = a("chest_minecart", EntityTypes.Builder.a(EntityMinecartChest::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartCommandBlock> COMMAND_BLOCK_MINECART = a("command_block_minecart", EntityTypes.Builder.a(EntityMinecartCommandBlock::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartFurnace> FURNACE_MINECART = a("furnace_minecart", EntityTypes.Builder.a(EntityMinecartFurnace::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartHopper> HOPPER_MINECART = a("hopper_minecart", EntityTypes.Builder.a(EntityMinecartHopper::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartMobSpawner> SPAWNER_MINECART = a("spawner_minecart", EntityTypes.Builder.a(EntityMinecartMobSpawner::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityMinecartTNT> TNT_MINECART = a("tnt_minecart", EntityTypes.Builder.a(EntityMinecartTNT::new, EnumCreatureType.MISC).a(0.98F, 0.7F).trackingRange(8));
    public static final EntityTypes<EntityHorseMule> MULE = a("mule", EntityTypes.Builder.a(EntityHorseMule::new, EnumCreatureType.CREATURE).a(1.3964844F, 1.6F).trackingRange(8));
    public static final EntityTypes<EntityMushroomCow> MOOSHROOM = a("mooshroom", EntityTypes.Builder.a(EntityMushroomCow::new, EnumCreatureType.CREATURE).a(0.9F, 1.4F).trackingRange(10));
    public static final EntityTypes<EntityOcelot> OCELOT = a("ocelot", EntityTypes.Builder.a(EntityOcelot::new, EnumCreatureType.CREATURE).a(0.6F, 0.7F).trackingRange(10));
    public static final EntityTypes<EntityPainting> PAINTING = a("painting", EntityTypes.Builder.a(EntityPainting::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(10).updateInterval(Integer.MAX_VALUE));
    public static final EntityTypes<EntityPanda> PANDA = a("panda", EntityTypes.Builder.a(EntityPanda::new, EnumCreatureType.CREATURE).a(1.3F, 1.25F).trackingRange(10));
    public static final EntityTypes<EntityParrot> PARROT = a("parrot", EntityTypes.Builder.a(EntityParrot::new, EnumCreatureType.CREATURE).a(0.5F, 0.9F).trackingRange(8));
    public static final EntityTypes<EntityPhantom> PHANTOM = a("phantom", EntityTypes.Builder.a(EntityPhantom::new, EnumCreatureType.MONSTER).a(0.9F, 0.5F).trackingRange(8));
    public static final EntityTypes<EntityPig> PIG = a("pig", EntityTypes.Builder.a(EntityPig::new, EnumCreatureType.CREATURE).a(0.9F, 0.9F).trackingRange(10));
    public static final EntityTypes<EntityPiglin> PIGLIN = a("piglin", EntityTypes.Builder.a(EntityPiglin::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityPiglinBrute> PIGLIN_BRUTE = a("piglin_brute", EntityTypes.Builder.a(EntityPiglinBrute::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityPillager> PILLAGER = a("pillager", EntityTypes.Builder.a(EntityPillager::new, EnumCreatureType.MONSTER).d().a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityPolarBear> POLAR_BEAR = a("polar_bear", EntityTypes.Builder.a(EntityPolarBear::new, EnumCreatureType.CREATURE).a(1.4F, 1.4F).trackingRange(10));
    public static final EntityTypes<EntityTNTPrimed> TNT = a("tnt", EntityTypes.Builder.a(EntityTNTPrimed::new, EnumCreatureType.MISC).c().a(0.98F, 0.98F).trackingRange(10).updateInterval(10));
    public static final EntityTypes<EntityPufferFish> PUFFERFISH = a("pufferfish", EntityTypes.Builder.a(EntityPufferFish::new, EnumCreatureType.WATER_AMBIENT).a(0.7F, 0.7F).trackingRange(4));
    public static final EntityTypes<EntityRabbit> RABBIT = a("rabbit", EntityTypes.Builder.a(EntityRabbit::new, EnumCreatureType.CREATURE).a(0.4F, 0.5F).trackingRange(8));
    public static final EntityTypes<EntityRavager> RAVAGER = a("ravager", EntityTypes.Builder.a(EntityRavager::new, EnumCreatureType.MONSTER).a(1.95F, 2.2F).trackingRange(10));
    public static final EntityTypes<EntitySalmon> SALMON = a("salmon", EntityTypes.Builder.a(EntitySalmon::new, EnumCreatureType.WATER_AMBIENT).a(0.7F, 0.4F).trackingRange(4));
    public static final EntityTypes<EntitySheep> SHEEP = a("sheep", EntityTypes.Builder.a(EntitySheep::new, EnumCreatureType.CREATURE).a(0.9F, 1.3F).trackingRange(10));
    public static final EntityTypes<EntityShulker> SHULKER = a("shulker", EntityTypes.Builder.a(EntityShulker::new, EnumCreatureType.MONSTER).c().d().a(1.0F, 1.0F).trackingRange(10));
    public static final EntityTypes<EntityShulkerBullet> SHULKER_BULLET = a("shulker_bullet", EntityTypes.Builder.a(EntityShulkerBullet::new, EnumCreatureType.MISC).a(0.3125F, 0.3125F).trackingRange(8));
    public static final EntityTypes<EntitySilverfish> SILVERFISH = a("silverfish", EntityTypes.Builder.a(EntitySilverfish::new, EnumCreatureType.MONSTER).a(0.4F, 0.3F).trackingRange(8));
    public static final EntityTypes<EntitySkeleton> SKELETON = a("skeleton", EntityTypes.Builder.a(EntitySkeleton::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F).trackingRange(8));
    public static final EntityTypes<EntityHorseSkeleton> SKELETON_HORSE = a("skeleton_horse", EntityTypes.Builder.a(EntityHorseSkeleton::new, EnumCreatureType.CREATURE).a(1.3964844F, 1.6F).trackingRange(10));
    public static final EntityTypes<EntitySlime> SLIME = a("slime", EntityTypes.Builder.a(EntitySlime::new, EnumCreatureType.MONSTER).a(2.04F, 2.04F).trackingRange(10));
    public static final EntityTypes<EntitySmallFireball> SMALL_FIREBALL = a("small_fireball", EntityTypes.Builder.a(EntitySmallFireball::new, EnumCreatureType.MISC).a(0.3125F, 0.3125F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntitySnowman> SNOW_GOLEM = a("snow_golem", EntityTypes.Builder.a(EntitySnowman::new, EnumCreatureType.MISC).a(0.7F, 1.9F).trackingRange(8));
    public static final EntityTypes<EntitySnowball> SNOWBALL = a("snowball", EntityTypes.Builder.a(EntitySnowball::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntitySpectralArrow> SPECTRAL_ARROW = a("spectral_arrow", EntityTypes.Builder.a(EntitySpectralArrow::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(4).updateInterval(20));
    public static final EntityTypes<EntitySpider> SPIDER = a("spider", EntityTypes.Builder.a(EntitySpider::new, EnumCreatureType.MONSTER).a(1.4F, 0.9F).trackingRange(8));
    public static final EntityTypes<EntitySquid> SQUID = a("squid", EntityTypes.Builder.a(EntitySquid::new, EnumCreatureType.WATER_CREATURE).a(0.8F, 0.8F).trackingRange(8));
    public static final EntityTypes<EntitySkeletonStray> STRAY = a("stray", EntityTypes.Builder.a(EntitySkeletonStray::new, EnumCreatureType.MONSTER).a(0.6F, 1.99F).trackingRange(8));
    public static final EntityTypes<EntityStrider> STRIDER = a("strider", EntityTypes.Builder.a(EntityStrider::new, EnumCreatureType.CREATURE).c().a(0.9F, 1.7F).trackingRange(10));
    public static final EntityTypes<EntityEgg> EGG = a("egg", EntityTypes.Builder.a(EntityEgg::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityEnderPearl> ENDER_PEARL = a("ender_pearl", EntityTypes.Builder.a(EntityEnderPearl::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityThrownExpBottle> EXPERIENCE_BOTTLE = a("experience_bottle", EntityTypes.Builder.a(EntityThrownExpBottle::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityPotion> POTION = a("potion", EntityTypes.Builder.a(EntityPotion::new, EnumCreatureType.MISC).a(0.25F, 0.25F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityThrownTrident> TRIDENT = a("trident", EntityTypes.Builder.a(EntityThrownTrident::new, EnumCreatureType.MISC).a(0.5F, 0.5F).trackingRange(4).updateInterval(20));
    public static final EntityTypes<EntityLlamaTrader> TRADER_LLAMA = a("trader_llama", EntityTypes.Builder.a(EntityLlamaTrader::new, EnumCreatureType.CREATURE).a(0.9F, 1.87F).trackingRange(10));
    public static final EntityTypes<EntityTropicalFish> TROPICAL_FISH = a("tropical_fish", EntityTypes.Builder.a(EntityTropicalFish::new, EnumCreatureType.WATER_AMBIENT).a(0.5F, 0.4F).trackingRange(4));
    public static final EntityTypes<EntityTurtle> TURTLE = a("turtle", EntityTypes.Builder.a(EntityTurtle::new, EnumCreatureType.CREATURE).a(1.2F, 0.4F).trackingRange(10));
    public static final EntityTypes<EntityVex> VEX = a("vex", EntityTypes.Builder.a(EntityVex::new, EnumCreatureType.MONSTER).c().a(0.4F, 0.8F).trackingRange(8));
    public static final EntityTypes<EntityVillager> VILLAGER = a("villager", EntityTypes.Builder.a(EntityVillager::new, EnumCreatureType.MISC).a(0.6F, 1.95F).trackingRange(10));
    public static final EntityTypes<EntityVindicator> VINDICATOR = a("vindicator", EntityTypes.Builder.a(EntityVindicator::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityVillagerTrader> WANDERING_TRADER = a("wandering_trader", EntityTypes.Builder.a(EntityVillagerTrader::new, EnumCreatureType.CREATURE).a(0.6F, 1.95F).trackingRange(10));
    public static final EntityTypes<EntityWitch> WITCH = a("witch", EntityTypes.Builder.a(EntityWitch::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityWither> WITHER = a("wither", EntityTypes.Builder.a(EntityWither::new, EnumCreatureType.MONSTER).c().a(Blocks.WITHER_ROSE).a(0.9F, 3.5F).trackingRange(10));
    public static final EntityTypes<EntitySkeletonWither> WITHER_SKELETON = a("wither_skeleton", EntityTypes.Builder.a(EntitySkeletonWither::new, EnumCreatureType.MONSTER).c().a(Blocks.WITHER_ROSE).a(0.7F, 2.4F).trackingRange(8));
    public static final EntityTypes<EntityWitherSkull> WITHER_SKULL = a("wither_skull", EntityTypes.Builder.a(EntityWitherSkull::new, EnumCreatureType.MISC).a(0.3125F, 0.3125F).trackingRange(4).updateInterval(10));
    public static final EntityTypes<EntityWolf> WOLF = a("wolf", EntityTypes.Builder.a(EntityWolf::new, EnumCreatureType.CREATURE).a(0.6F, 0.85F).trackingRange(10));
    public static final EntityTypes<EntityZoglin> ZOGLIN = a("zoglin", EntityTypes.Builder.a(EntityZoglin::new, EnumCreatureType.MONSTER).c().a(1.3964844F, 1.4F).trackingRange(8));
    public static final EntityTypes<EntityZombie> ZOMBIE = a("zombie", EntityTypes.Builder.a(EntityZombie::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityHorseZombie> ZOMBIE_HORSE = a("zombie_horse", EntityTypes.Builder.a(EntityHorseZombie::new, EnumCreatureType.CREATURE).a(1.3964844F, 1.6F).trackingRange(10));
    public static final EntityTypes<EntityZombieVillager> ZOMBIE_VILLAGER = a("zombie_villager", EntityTypes.Builder.a(EntityZombieVillager::new, EnumCreatureType.MONSTER).a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityPigZombie> ZOMBIFIED_PIGLIN = a("zombified_piglin", EntityTypes.Builder.a(EntityPigZombie::new, EnumCreatureType.MONSTER).c().a(0.6F, 1.95F).trackingRange(8));
    public static final EntityTypes<EntityHuman> PLAYER = a("player", EntityTypes.Builder.a(EnumCreatureType.MISC).b().a().a(0.6F, 1.8F).trackingRange(32).updateInterval(2));
    public static final EntityTypes<EntityFishingHook> FISHING_BOBBER = a("fishing_bobber", EntityTypes.Builder.a(EnumCreatureType.MISC).b().a().a(0.25F, 0.25F).trackingRange(4).updateInterval(5));
    private final EntityTypes.b<T> bf;
    private final EnumCreatureType bg;
    private final ImmutableSet<Block> bh;
    private final boolean bi;
    private final boolean bj;
    private final boolean bk;
    private final boolean bl;
    private final int bm;
    private final int bn;
    @Nullable
    private String bo;
    @Nullable
    private IChatBaseComponent bp;
    @Nullable
    private MinecraftKey bq;
    private final EntitySize br;

    private static <T extends Entity> EntityTypes<T> a(String s, EntityTypes.Builder entitytypes_builder) { // CraftBukkit - decompile error
        return (EntityTypes) IRegistry.a((IRegistry) IRegistry.ENTITY_TYPE, s, (Object) entitytypes_builder.a(s));
    }

    public static MinecraftKey getName(EntityTypes<?> entitytypes) {
        return IRegistry.ENTITY_TYPE.getKey(entitytypes);
    }

    public static Optional<EntityTypes<?>> a(String s) {
        return IRegistry.ENTITY_TYPE.getOptional(MinecraftKey.a(s));
    }

    public EntityTypes(EntityTypes.b<T> entitytypes_b, EnumCreatureType enumcreaturetype, boolean flag, boolean flag1, boolean flag2, boolean flag3, ImmutableSet<Block> immutableset, EntitySize entitysize, int i, int j) {
        this.bf = entitytypes_b;
        this.bg = enumcreaturetype;
        this.bl = flag3;
        this.bi = flag;
        this.bj = flag1;
        this.bk = flag2;
        this.bh = immutableset;
        this.br = entitysize;
        this.bm = i;
        this.bn = j;
    }

    @Nullable
    public Entity spawnCreature(WorldServer worldserver, @Nullable ItemStack itemstack, @Nullable EntityHuman entityhuman, BlockPosition blockposition, EnumMobSpawn enummobspawn, boolean flag, boolean flag1) {
        return this.spawnCreature(worldserver, itemstack == null ? null : itemstack.getTag(), itemstack != null && itemstack.hasName() ? itemstack.getName() : null, entityhuman, blockposition, enummobspawn, flag, flag1);
    }

    @Nullable
    public T spawnCreature(WorldServer worldserver, @Nullable NBTTagCompound nbttagcompound, @Nullable IChatBaseComponent ichatbasecomponent, @Nullable EntityHuman entityhuman, BlockPosition blockposition, EnumMobSpawn enummobspawn, boolean flag, boolean flag1) {
        // CraftBukkit start
        return this.spawnCreature(worldserver, nbttagcompound, ichatbasecomponent, entityhuman, blockposition, enummobspawn, flag, flag1, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }

    @Nullable
    public T spawnCreature(WorldServer worldserver, @Nullable NBTTagCompound nbttagcompound, @Nullable IChatBaseComponent ichatbasecomponent, @Nullable EntityHuman entityhuman, BlockPosition blockposition, EnumMobSpawn enummobspawn, boolean flag, boolean flag1, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
        T t0 = this.createCreature(worldserver, nbttagcompound, ichatbasecomponent, entityhuman, blockposition, enummobspawn, flag, flag1);

        if (t0 != null) {
            return worldserver.addAllEntities(t0, spawnReason) ? t0 : null; // Don't return an entity when CreatureSpawnEvent is canceled
            // CraftBukkit end
        }

        return t0;
    }

    @Nullable
    public T createCreature(WorldServer worldserver, @Nullable NBTTagCompound nbttagcompound, @Nullable IChatBaseComponent ichatbasecomponent, @Nullable EntityHuman entityhuman, BlockPosition blockposition, EnumMobSpawn enummobspawn, boolean flag, boolean flag1) {
        T t0 = this.a((World) worldserver);

        if (t0 == null) {
            return null;
        } else {
            double d0;

            if (flag) {
                t0.setPosition((double) blockposition.getX() + 0.5D, (double) (blockposition.getY() + 1), (double) blockposition.getZ() + 0.5D);
                d0 = a(worldserver, blockposition, flag1, t0.getBoundingBox());
            } else {
                d0 = 0.0D;
            }

            t0.setPositionRotation((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + d0, (double) blockposition.getZ() + 0.5D, MathHelper.g(worldserver.random.nextFloat() * 360.0F), 0.0F);
            if (t0 instanceof EntityInsentient) {
                EntityInsentient entityinsentient = (EntityInsentient) t0;

                entityinsentient.aC = entityinsentient.yaw;
                entityinsentient.aA = entityinsentient.yaw;
                entityinsentient.prepare(worldserver, worldserver.getDamageScaler(entityinsentient.getChunkCoordinates()), enummobspawn, (GroupDataEntity) null, nbttagcompound);
                entityinsentient.F();
            }

            if (ichatbasecomponent != null && t0 instanceof EntityLiving) {
                t0.setCustomName(ichatbasecomponent);
            }

            try { a((World) worldserver, entityhuman, t0, nbttagcompound); } catch (Throwable t) { LOGGER.warn("Error loading spawn egg NBT", t); } // CraftBukkit - SPIGOT-5665
            return t0;
        }
    }

    protected static double a(IWorldReader iworldreader, BlockPosition blockposition, boolean flag, AxisAlignedBB axisalignedbb) {
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(blockposition);

        if (flag) {
            axisalignedbb1 = axisalignedbb1.b(0.0D, -1.0D, 0.0D);
        }

        Stream<VoxelShape> stream = iworldreader.d((Entity) null, axisalignedbb1, (entity) -> {
            return true;
        });

        return 1.0D + VoxelShapes.a(EnumDirection.EnumAxis.Y, axisalignedbb, stream, flag ? -2.0D : -1.0D);
    }

    public static void a(World world, @Nullable EntityHuman entityhuman, @Nullable Entity entity, @Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("EntityTag", 10)) {
            MinecraftServer minecraftserver = world.getMinecraftServer();

            if (minecraftserver != null && entity != null) {
                if (world.isClientSide || !entity.ci() || entityhuman != null && minecraftserver.getPlayerList().isOp(entityhuman.getProfile())) {
                    NBTTagCompound nbttagcompound1 = entity.save(new NBTTagCompound());
                    UUID uuid = entity.getUniqueID();

                    nbttagcompound1.a(nbttagcompound.getCompound("EntityTag"));
                    entity.a_(uuid);
                    entity.load(nbttagcompound1);
                }
            }
        }
    }

    public boolean a() {
        return this.bi;
    }

    public boolean b() {
        return this.bj;
    }

    public boolean c() {
        return this.bk;
    }

    public boolean d() {
        return this.bl;
    }

    public EnumCreatureType e() {
        return this.bg;
    }

    public String f() {
        if (this.bo == null) {
            this.bo = SystemUtils.a("entity", IRegistry.ENTITY_TYPE.getKey(this));
        }

        return this.bo;
    }

    public IChatBaseComponent g() {
        if (this.bp == null) {
            this.bp = new ChatMessage(this.f());
        }

        return this.bp;
    }

    public String toString() {
        return this.f();
    }

    public MinecraftKey i() {
        if (this.bq == null) {
            MinecraftKey minecraftkey = IRegistry.ENTITY_TYPE.getKey(this);

            this.bq = new MinecraftKey(minecraftkey.getNamespace(), "entities/" + minecraftkey.getKey());
        }

        return this.bq;
    }

    public float j() {
        return this.br.width;
    }

    public float k() {
        return this.br.height;
    }

    @Nullable
    public T a(World world) {
        return this.bf.create(this, world);
    }

    public static Optional<Entity> a(NBTTagCompound nbttagcompound, World world) {
        return SystemUtils.a(a(nbttagcompound).map((entitytypes) -> {
            return entitytypes.a(world);
        }), (entity) -> {
            entity.load(nbttagcompound);
        }, () -> {
            EntityTypes.LOGGER.warn("Skipping Entity with id {}", nbttagcompound.getString("id"));
        });
    }

    public AxisAlignedBB a(double d0, double d1, double d2) {
        float f = this.j() / 2.0F;

        return new AxisAlignedBB(d0 - (double) f, d1, d2 - (double) f, d0 + (double) f, d1 + (double) this.k(), d2 + (double) f);
    }

    public boolean a(IBlockData iblockdata) {
        return this.bh.contains(iblockdata.getBlock()) ? false : (!this.bk && (iblockdata.a((Tag) TagsBlock.FIRE) || iblockdata.a(Blocks.MAGMA_BLOCK) || BlockCampfire.g(iblockdata) || iblockdata.a(Blocks.LAVA)) ? true : iblockdata.a(Blocks.WITHER_ROSE) || iblockdata.a(Blocks.SWEET_BERRY_BUSH) || iblockdata.a(Blocks.CACTUS));
    }

    public EntitySize l() {
        return this.br;
    }

    public static Optional<EntityTypes<?>> a(NBTTagCompound nbttagcompound) {
        return IRegistry.ENTITY_TYPE.getOptional(new MinecraftKey(nbttagcompound.getString("id")));
    }

    @Nullable
    public static Entity a(NBTTagCompound nbttagcompound, World world, Function<Entity, Entity> function) {
        return (Entity) b(nbttagcompound, world).map(function).map((entity) -> {
            if (nbttagcompound.hasKeyOfType("Passengers", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getList("Passengers", 10);

                for (int i = 0; i < nbttaglist.size(); ++i) {
                    Entity entity1 = a(nbttaglist.getCompound(i), world, function);

                    if (entity1 != null) {
                        entity1.a(entity, true);
                    }
                }
            }

            return entity;
        }).orElse(null); // CraftBukkit - decompile error
    }

    private static Optional<Entity> b(NBTTagCompound nbttagcompound, World world) {
        try {
            return a(nbttagcompound, world);
        } catch (RuntimeException runtimeexception) {
            EntityTypes.LOGGER.warn("Exception loading entity: ", runtimeexception);
            return Optional.empty();
        }
    }

    public int getChunkRange() {
        return this.bm;
    }

    public int getUpdateInterval() {
        return this.bn;
    }

    public boolean isDeltaTracking() {
        return this != EntityTypes.PLAYER && this != EntityTypes.LLAMA_SPIT && this != EntityTypes.WITHER && this != EntityTypes.BAT && this != EntityTypes.ITEM_FRAME && this != EntityTypes.LEASH_KNOT && this != EntityTypes.PAINTING && this != EntityTypes.END_CRYSTAL && this != EntityTypes.EVOKER_FANGS;
    }

    public boolean a(Tag<EntityTypes<?>> tag) {
        return tag.isTagged(this);
    }

    public interface b<T extends Entity> {

        T create(EntityTypes<T> entitytypes, World world);
    }

    public static class Builder<T extends Entity> {

        private final EntityTypes.b<T> a;
        private final EnumCreatureType b;
        private ImmutableSet<Block> c = ImmutableSet.of();
        private boolean d = true;
        private boolean e = true;
        private boolean f;
        private boolean g;
        private int h = 5;
        private int i = 3;
        private EntitySize j = EntitySize.b(0.6F, 1.8F);

        private Builder(EntityTypes.b<T> entitytypes_b, EnumCreatureType enumcreaturetype) {
            this.a = entitytypes_b;
            this.b = enumcreaturetype;
            this.g = enumcreaturetype == EnumCreatureType.CREATURE || enumcreaturetype == EnumCreatureType.MISC;
        }

        public static <T extends Entity> EntityTypes.Builder<T> a(EntityTypes.b entitytypes_b, EnumCreatureType enumcreaturetype) { // CraftBukkit - decompile error
            return new EntityTypes.Builder<>(entitytypes_b, enumcreaturetype);
        }

        public static <T extends Entity> EntityTypes.Builder<T> a(EnumCreatureType enumcreaturetype) {
            return new EntityTypes.Builder<>((entitytypes, world) -> {
                return null;
            }, enumcreaturetype);
        }

        public EntityTypes.Builder<T> a(float f, float f1) {
            this.j = EntitySize.b(f, f1);
            return this;
        }

        public EntityTypes.Builder<T> a() {
            this.e = false;
            return this;
        }

        public EntityTypes.Builder<T> b() {
            this.d = false;
            return this;
        }

        public EntityTypes.Builder<T> c() {
            this.f = true;
            return this;
        }

        public EntityTypes.Builder<T> a(Block... ablock) {
            this.c = ImmutableSet.copyOf(ablock);
            return this;
        }

        public EntityTypes.Builder<T> d() {
            this.g = true;
            return this;
        }

        public EntityTypes.Builder<T> trackingRange(int i) {
            this.h = i;
            return this;
        }

        public EntityTypes.Builder<T> updateInterval(int i) {
            this.i = i;
            return this;
        }

        public EntityTypes<T> a(String s) {
            if (this.d) {
                SystemUtils.a(DataConverterTypes.ENTITY_TREE, s);
            }

            return new EntityTypes<>(this.a, this.b, this.d, this.e, this.f, this.g, this.c, this.j, this.h, this.i);
        }
    }
}
