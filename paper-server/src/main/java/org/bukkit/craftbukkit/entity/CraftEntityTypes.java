package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderDragon;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.entity.decoration.EntityLeash;
import net.minecraft.world.entity.decoration.EntityPainting;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityEnderSignal;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import net.minecraft.world.entity.vehicle.EntityMinecartCommandBlock;
import net.minecraft.world.entity.vehicle.EntityMinecartFurnace;
import net.minecraft.world.entity.vehicle.EntityMinecartHopper;
import net.minecraft.world.entity.vehicle.EntityMinecartMobSpawner;
import net.minecraft.world.entity.vehicle.EntityMinecartRideable;
import net.minecraft.world.entity.vehicle.EntityMinecartTNT;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.BlockDiodeAbstract;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Allay;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Strider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tadpole;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Trident;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Warden;
import org.bukkit.entity.WindCharge;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public final class CraftEntityTypes {

    public record EntityTypeData<E extends Entity, M extends net.minecraft.world.entity.Entity>(EntityType entityType,
                                                                                                Class<E> entityClass,
                                                                                                BiFunction<CraftServer, M, CraftEntity> convertFunction,
                                                                                                Function<SpawnData, M> spawnFunction) {
    }

    public record SpawnData(GeneratorAccessSeed world, Location location, boolean randomizeData, boolean normalWorld) {
        double x() {
            return location().getX();
        }

        double y() {
            return location().getY();
        }

        double z() {
            return location().getZ();
        }

        float yaw() {
            return location().getYaw();
        }

        float pitch() {
            return location().getPitch();
        }

        World minecraftWorld() {
            return world().getMinecraftWorld();
        }
    }

    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> POS = (spawnData, entity) -> entity.setPos(spawnData.x(), spawnData.y(), spawnData.z());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> ABS_MOVE = (spawnData, entity) -> {
        entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
        entity.setYHeadRot(spawnData.yaw()); // SPIGOT-3587
    };
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE_EMPTY_ROT = (spawnData, entity) -> entity.moveTo(spawnData.x(), spawnData.y(), spawnData.z(), 0, 0);
    private static final BiConsumer<SpawnData, EntityFireball> DIRECTION = (spawnData, entity) -> {
        Vector direction = spawnData.location().getDirection().multiply(10);
        entity.setDirection(direction.getX(), direction.getY(), direction.getZ());
    };
    private static final Map<Class<?>, EntityTypeData<?, ?>> CLASS_TYPE_DATA = new HashMap<>();
    private static final Map<EntityType, EntityTypeData<?, ?>> ENTITY_TYPE_DATA = new HashMap<>();

    static {
        // Living
        register(new EntityTypeData<>(EntityType.ELDER_GUARDIAN, ElderGuardian.class, CraftElderGuardian::new, createLiving(EntityTypes.ELDER_GUARDIAN)));
        register(new EntityTypeData<>(EntityType.WITHER_SKELETON, WitherSkeleton.class, CraftWitherSkeleton::new, createLiving(EntityTypes.WITHER_SKELETON)));
        register(new EntityTypeData<>(EntityType.STRAY, Stray.class, CraftStray::new, createLiving(EntityTypes.STRAY)));
        register(new EntityTypeData<>(EntityType.HUSK, Husk.class, CraftHusk::new, createLiving(EntityTypes.HUSK)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_VILLAGER, ZombieVillager.class, CraftVillagerZombie::new, createLiving(EntityTypes.ZOMBIE_VILLAGER)));
        register(new EntityTypeData<>(EntityType.SKELETON_HORSE, SkeletonHorse.class, CraftSkeletonHorse::new, createLiving(EntityTypes.SKELETON_HORSE)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_HORSE, ZombieHorse.class, CraftZombieHorse::new, createLiving(EntityTypes.ZOMBIE_HORSE)));
        register(new EntityTypeData<>(EntityType.ARMOR_STAND, ArmorStand.class, CraftArmorStand::new, createLiving(EntityTypes.ARMOR_STAND)));
        register(new EntityTypeData<>(EntityType.DONKEY, Donkey.class, CraftDonkey::new, createLiving(EntityTypes.DONKEY)));
        register(new EntityTypeData<>(EntityType.MULE, Mule.class, CraftMule::new, createLiving(EntityTypes.MULE)));
        register(new EntityTypeData<>(EntityType.EVOKER, Evoker.class, CraftEvoker::new, createLiving(EntityTypes.EVOKER)));
        register(new EntityTypeData<>(EntityType.VEX, Vex.class, CraftVex::new, createLiving(EntityTypes.VEX)));
        register(new EntityTypeData<>(EntityType.VINDICATOR, Vindicator.class, CraftVindicator::new, createLiving(EntityTypes.VINDICATOR)));
        register(new EntityTypeData<>(EntityType.ILLUSIONER, Illusioner.class, CraftIllusioner::new, createLiving(EntityTypes.ILLUSIONER)));
        register(new EntityTypeData<>(EntityType.CREEPER, Creeper.class, CraftCreeper::new, createLiving(EntityTypes.CREEPER)));
        register(new EntityTypeData<>(EntityType.SKELETON, Skeleton.class, CraftSkeleton::new, createLiving(EntityTypes.SKELETON)));
        register(new EntityTypeData<>(EntityType.SPIDER, Spider.class, CraftSpider::new, createLiving(EntityTypes.SPIDER)));
        register(new EntityTypeData<>(EntityType.GIANT, Giant.class, CraftGiant::new, createLiving(EntityTypes.GIANT)));
        register(new EntityTypeData<>(EntityType.ZOMBIE, Zombie.class, CraftZombie::new, createLiving(EntityTypes.ZOMBIE)));
        register(new EntityTypeData<>(EntityType.SLIME, Slime.class, CraftSlime::new, createLiving(EntityTypes.SLIME)));
        register(new EntityTypeData<>(EntityType.GHAST, Ghast.class, CraftGhast::new, createLiving(EntityTypes.GHAST)));
        register(new EntityTypeData<>(EntityType.ZOMBIFIED_PIGLIN, PigZombie.class, CraftPigZombie::new, createLiving(EntityTypes.ZOMBIFIED_PIGLIN)));
        register(new EntityTypeData<>(EntityType.ENDERMAN, Enderman.class, CraftEnderman::new, createLiving(EntityTypes.ENDERMAN)));
        register(new EntityTypeData<>(EntityType.CAVE_SPIDER, CaveSpider.class, CraftCaveSpider::new, createLiving(EntityTypes.CAVE_SPIDER)));
        register(new EntityTypeData<>(EntityType.SILVERFISH, Silverfish.class, CraftSilverfish::new, createLiving(EntityTypes.SILVERFISH)));
        register(new EntityTypeData<>(EntityType.BLAZE, Blaze.class, CraftBlaze::new, createLiving(EntityTypes.BLAZE)));
        register(new EntityTypeData<>(EntityType.MAGMA_CUBE, MagmaCube.class, CraftMagmaCube::new, createLiving(EntityTypes.MAGMA_CUBE)));
        register(new EntityTypeData<>(EntityType.WITHER, Wither.class, CraftWither::new, createLiving(EntityTypes.WITHER)));
        register(new EntityTypeData<>(EntityType.BAT, Bat.class, CraftBat::new, createLiving(EntityTypes.BAT)));
        register(new EntityTypeData<>(EntityType.WITCH, Witch.class, CraftWitch::new, createLiving(EntityTypes.WITCH)));
        register(new EntityTypeData<>(EntityType.ENDERMITE, Endermite.class, CraftEndermite::new, createLiving(EntityTypes.ENDERMITE)));
        register(new EntityTypeData<>(EntityType.GUARDIAN, Guardian.class, CraftGuardian::new, createLiving(EntityTypes.GUARDIAN)));
        register(new EntityTypeData<>(EntityType.SHULKER, Shulker.class, CraftShulker::new, createLiving(EntityTypes.SHULKER)));
        register(new EntityTypeData<>(EntityType.PIG, Pig.class, CraftPig::new, createLiving(EntityTypes.PIG)));
        register(new EntityTypeData<>(EntityType.SHEEP, Sheep.class, CraftSheep::new, createLiving(EntityTypes.SHEEP)));
        register(new EntityTypeData<>(EntityType.COW, Cow.class, CraftCow::new, createLiving(EntityTypes.COW)));
        register(new EntityTypeData<>(EntityType.CHICKEN, Chicken.class, CraftChicken::new, createLiving(EntityTypes.CHICKEN)));
        register(new EntityTypeData<>(EntityType.SQUID, Squid.class, CraftSquid::new, createLiving(EntityTypes.SQUID)));
        register(new EntityTypeData<>(EntityType.WOLF, Wolf.class, CraftWolf::new, createLiving(EntityTypes.WOLF)));
        register(new EntityTypeData<>(EntityType.MUSHROOM_COW, MushroomCow.class, CraftMushroomCow::new, createLiving(EntityTypes.MOOSHROOM)));
        register(new EntityTypeData<>(EntityType.SNOWMAN, Snowman.class, CraftSnowman::new, createLiving(EntityTypes.SNOW_GOLEM)));
        register(new EntityTypeData<>(EntityType.OCELOT, Ocelot.class, CraftOcelot::new, createLiving(EntityTypes.OCELOT)));
        register(new EntityTypeData<>(EntityType.IRON_GOLEM, IronGolem.class, CraftIronGolem::new, createLiving(EntityTypes.IRON_GOLEM)));
        register(new EntityTypeData<>(EntityType.HORSE, Horse.class, CraftHorse::new, createLiving(EntityTypes.HORSE)));
        register(new EntityTypeData<>(EntityType.RABBIT, Rabbit.class, CraftRabbit::new, createLiving(EntityTypes.RABBIT)));
        register(new EntityTypeData<>(EntityType.POLAR_BEAR, PolarBear.class, CraftPolarBear::new, createLiving(EntityTypes.POLAR_BEAR)));
        register(new EntityTypeData<>(EntityType.LLAMA, Llama.class, CraftLlama::new, createLiving(EntityTypes.LLAMA)));
        register(new EntityTypeData<>(EntityType.PARROT, Parrot.class, CraftParrot::new, createLiving(EntityTypes.PARROT)));
        register(new EntityTypeData<>(EntityType.VILLAGER, Villager.class, CraftVillager::new, createLiving(EntityTypes.VILLAGER)));
        register(new EntityTypeData<>(EntityType.TURTLE, Turtle.class, CraftTurtle::new, createLiving(EntityTypes.TURTLE)));
        register(new EntityTypeData<>(EntityType.PHANTOM, Phantom.class, CraftPhantom::new, createLiving(EntityTypes.PHANTOM)));
        register(new EntityTypeData<>(EntityType.COD, Cod.class, CraftCod::new, createLiving(EntityTypes.COD)));
        register(new EntityTypeData<>(EntityType.SALMON, Salmon.class, CraftSalmon::new, createLiving(EntityTypes.SALMON)));
        register(new EntityTypeData<>(EntityType.PUFFERFISH, PufferFish.class, CraftPufferFish::new, createLiving(EntityTypes.PUFFERFISH)));
        register(new EntityTypeData<>(EntityType.TROPICAL_FISH, TropicalFish.class, CraftTropicalFish::new, createLiving(EntityTypes.TROPICAL_FISH)));
        register(new EntityTypeData<>(EntityType.DROWNED, Drowned.class, CraftDrowned::new, createLiving(EntityTypes.DROWNED)));
        register(new EntityTypeData<>(EntityType.DOLPHIN, Dolphin.class, CraftDolphin::new, createLiving(EntityTypes.DOLPHIN)));
        register(new EntityTypeData<>(EntityType.CAT, Cat.class, CraftCat::new, createLiving(EntityTypes.CAT)));
        register(new EntityTypeData<>(EntityType.PANDA, Panda.class, CraftPanda::new, createLiving(EntityTypes.PANDA)));
        register(new EntityTypeData<>(EntityType.PILLAGER, Pillager.class, CraftPillager::new, createLiving(EntityTypes.PILLAGER)));
        register(new EntityTypeData<>(EntityType.RAVAGER, Ravager.class, CraftRavager::new, createLiving(EntityTypes.RAVAGER)));
        register(new EntityTypeData<>(EntityType.TRADER_LLAMA, TraderLlama.class, CraftTraderLlama::new, createLiving(EntityTypes.TRADER_LLAMA)));
        register(new EntityTypeData<>(EntityType.WANDERING_TRADER, WanderingTrader.class, CraftWanderingTrader::new, createLiving(EntityTypes.WANDERING_TRADER)));
        register(new EntityTypeData<>(EntityType.FOX, Fox.class, CraftFox::new, createLiving(EntityTypes.FOX)));
        register(new EntityTypeData<>(EntityType.BEE, Bee.class, CraftBee::new, createLiving(EntityTypes.BEE)));
        register(new EntityTypeData<>(EntityType.HOGLIN, Hoglin.class, CraftHoglin::new, createLiving(EntityTypes.HOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN, Piglin.class, CraftPiglin::new, createLiving(EntityTypes.PIGLIN)));
        register(new EntityTypeData<>(EntityType.STRIDER, Strider.class, CraftStrider::new, createLiving(EntityTypes.STRIDER)));
        register(new EntityTypeData<>(EntityType.ZOGLIN, Zoglin.class, CraftZoglin::new, createLiving(EntityTypes.ZOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN_BRUTE, PiglinBrute.class, CraftPiglinBrute::new, createLiving(EntityTypes.PIGLIN_BRUTE)));
        register(new EntityTypeData<>(EntityType.AXOLOTL, Axolotl.class, CraftAxolotl::new, createLiving(EntityTypes.AXOLOTL)));
        register(new EntityTypeData<>(EntityType.GLOW_SQUID, GlowSquid.class, CraftGlowSquid::new, createLiving(EntityTypes.GLOW_SQUID)));
        register(new EntityTypeData<>(EntityType.GOAT, Goat.class, CraftGoat::new, createLiving(EntityTypes.GOAT)));
        register(new EntityTypeData<>(EntityType.ALLAY, Allay.class, CraftAllay::new, createLiving(EntityTypes.ALLAY)));
        register(new EntityTypeData<>(EntityType.FROG, Frog.class, CraftFrog::new, createLiving(EntityTypes.FROG)));
        register(new EntityTypeData<>(EntityType.TADPOLE, Tadpole.class, CraftTadpole::new, createLiving(EntityTypes.TADPOLE)));
        register(new EntityTypeData<>(EntityType.WARDEN, Warden.class, CraftWarden::new, createLiving(EntityTypes.WARDEN)));
        register(new EntityTypeData<>(EntityType.CAMEL, Camel.class, CraftCamel::new, createLiving(EntityTypes.CAMEL)));
        register(new EntityTypeData<>(EntityType.SNIFFER, Sniffer.class, CraftSniffer::new, createLiving(EntityTypes.SNIFFER)));
        register(new EntityTypeData<>(EntityType.BREEZE, Breeze.class, CraftBreeze::new, createLiving(EntityTypes.BREEZE)));

        Function<SpawnData, EntityEnderDragon> dragonFunction = createLiving(EntityTypes.ENDER_DRAGON);
        register(new EntityTypeData<>(EntityType.ENDER_DRAGON, EnderDragon.class, CraftEnderDragon::new, spawnData -> {
            Preconditions.checkArgument(spawnData.normalWorld(), "Cannot spawn entity %s during world generation", EnderDragon.class.getName());
            return dragonFunction.apply(spawnData);
        }));

        // Fireball
        register(new EntityTypeData<>(EntityType.FIREBALL, LargeFireball.class, CraftLargeFireball::new, createFireball(EntityTypes.FIREBALL)));
        register(new EntityTypeData<>(EntityType.SMALL_FIREBALL, SmallFireball.class, CraftSmallFireball::new, createFireball(EntityTypes.SMALL_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WITHER_SKULL, WitherSkull.class, CraftWitherSkull::new, createFireball(EntityTypes.WITHER_SKULL)));
        register(new EntityTypeData<>(EntityType.DRAGON_FIREBALL, DragonFireball.class, CraftDragonFireball::new, createFireball(EntityTypes.DRAGON_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WIND_CHARGE, WindCharge.class, CraftWindCharge::new, createFireball(EntityTypes.WIND_CHARGE)));

        // Hanging
        register(new EntityTypeData<>(EntityType.PAINTING, Painting.class, CraftPainting::new, createHanging(Painting.class, (spawnData, hangingData) -> {
                    if (spawnData.normalWorld && hangingData.randomize()) {
                        return EntityPainting.create(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()).orElse(null);
                    } else {
                        EntityPainting entity = new EntityPainting(EntityTypes.PAINTING, spawnData.minecraftWorld());
                        entity.absMoveTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
                        entity.setDirection(hangingData.direction());
                        return entity;
                    }
                }
        )));
        register(new EntityTypeData<>(EntityType.ITEM_FRAME, ItemFrame.class, CraftItemFrame::new, createHanging(ItemFrame.class, (spawnData, hangingData) -> new EntityItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));
        register(new EntityTypeData<>(EntityType.GLOW_ITEM_FRAME, GlowItemFrame.class, CraftGlowItemFrame::new, createHanging(GlowItemFrame.class, (spawnData, hangingData) -> new net.minecraft.world.entity.decoration.GlowItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));

        // Move no rotation
        register(new EntityTypeData<>(EntityType.ARROW, Arrow.class, CraftArrow::new, createAndMoveEmptyRot(EntityTypes.ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_PEARL, EnderPearl.class, CraftEnderPearl::new, createAndMoveEmptyRot(EntityTypes.ENDER_PEARL)));
        register(new EntityTypeData<>(EntityType.THROWN_EXP_BOTTLE, ThrownExpBottle.class, CraftThrownExpBottle::new, createAndMoveEmptyRot(EntityTypes.EXPERIENCE_BOTTLE)));
        register(new EntityTypeData<>(EntityType.SPECTRAL_ARROW, SpectralArrow.class, CraftSpectralArrow::new, createAndMoveEmptyRot(EntityTypes.SPECTRAL_ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_CRYSTAL, EnderCrystal.class, CraftEnderCrystal::new, createAndMoveEmptyRot(EntityTypes.END_CRYSTAL)));
        register(new EntityTypeData<>(EntityType.TRIDENT, Trident.class, CraftTrident::new, createAndMoveEmptyRot(EntityTypes.TRIDENT)));
        register(new EntityTypeData<>(EntityType.LIGHTNING, LightningStrike.class, CraftLightningStrike::new, createAndMoveEmptyRot(EntityTypes.LIGHTNING_BOLT)));

        // Move
        register(new EntityTypeData<>(EntityType.SHULKER_BULLET, ShulkerBullet.class, CraftShulkerBullet::new, createAndMove(EntityTypes.SHULKER_BULLET)));
        register(new EntityTypeData<>(EntityType.BOAT, Boat.class, CraftBoat::new, createAndMove(EntityTypes.BOAT)));
        register(new EntityTypeData<>(EntityType.LLAMA_SPIT, LlamaSpit.class, CraftLlamaSpit::new, createAndMove(EntityTypes.LLAMA_SPIT)));
        register(new EntityTypeData<>(EntityType.CHEST_BOAT, ChestBoat.class, CraftChestBoat::new, createAndMove(EntityTypes.CHEST_BOAT)));

        // Set pos
        register(new EntityTypeData<>(EntityType.MARKER, Marker.class, CraftMarker::new, createAndSetPos(EntityTypes.MARKER)));
        register(new EntityTypeData<>(EntityType.BLOCK_DISPLAY, BlockDisplay.class, CraftBlockDisplay::new, createAndSetPos(EntityTypes.BLOCK_DISPLAY)));
        register(new EntityTypeData<>(EntityType.INTERACTION, Interaction.class, CraftInteraction::new, createAndSetPos(EntityTypes.INTERACTION)));
        register(new EntityTypeData<>(EntityType.ITEM_DISPLAY, ItemDisplay.class, CraftItemDisplay::new, createAndSetPos(EntityTypes.ITEM_DISPLAY)));
        register(new EntityTypeData<>(EntityType.TEXT_DISPLAY, TextDisplay.class, CraftTextDisplay::new, createAndSetPos(EntityTypes.TEXT_DISPLAY)));

        // MISC
        register(new EntityTypeData<>(EntityType.DROPPED_ITEM, Item.class, CraftItem::new, spawnData -> {
            // We use stone instead of empty, to give the plugin developer a visual clue, that the spawn method is working,
            // and that the item stack should probably be changed.
            net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.STONE);
            EntityItem item = new EntityItem(spawnData.minecraftWorld(), spawnData.x(), spawnData.z(), spawnData.z(), itemStack);
            item.setPickUpDelay(10);

            return item;
        }));
        register(new EntityTypeData<>(EntityType.EXPERIENCE_ORB, ExperienceOrb.class, CraftExperienceOrb::new,
                spawnData -> new EntityExperienceOrb(spawnData.minecraftWorld(), spawnData.x(), spawnData.z(), spawnData.z(), 0)
        ));
        register(new EntityTypeData<>(EntityType.AREA_EFFECT_CLOUD, AreaEffectCloud.class, CraftAreaEffectCloud::new, spawnData -> new EntityAreaEffectCloud(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.EGG, Egg.class, CraftEgg::new, spawnData -> new EntityEgg(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.LEASH_HITCH, LeashHitch.class, CraftLeash::new, spawnData -> new EntityLeash(spawnData.minecraftWorld(), BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z())))); // SPIGOT-5732: LeashHitch has no direction and is always centered at a block
        register(new EntityTypeData<>(EntityType.SNOWBALL, Snowball.class, CraftSnowball::new, spawnData -> new EntitySnowball(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.ENDER_SIGNAL, EnderSignal.class, CraftEnderSignal::new, spawnData -> new EntityEnderSignal(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.SPLASH_POTION, ThrownPotion.class, CraftThrownPotion::new, spawnData -> {
            EntityPotion entity = new EntityPotion(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z());
            entity.setItem(CraftItemStack.asNMSCopy(new ItemStack(Material.SPLASH_POTION, 1)));
            return entity;
        }));
        register(new EntityTypeData<>(EntityType.PRIMED_TNT, TNTPrimed.class, CraftTNTPrimed::new, spawnData -> new EntityTNTPrimed(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), null)));
        register(new EntityTypeData<>(EntityType.FALLING_BLOCK, FallingBlock.class, CraftFallingBlock::new, spawnData -> {
            BlockPosition pos = BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z());
            return EntityFallingBlock.fall(spawnData.minecraftWorld(), pos, spawnData.world().getBlockState(pos));
        }));
        register(new EntityTypeData<>(EntityType.FIREWORK, Firework.class, CraftFirework::new, spawnData -> new EntityFireworks(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), net.minecraft.world.item.ItemStack.EMPTY)));
        register(new EntityTypeData<>(EntityType.EVOKER_FANGS, EvokerFangs.class, CraftEvokerFangs::new, spawnData -> new EntityEvokerFangs(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), (float) Math.toRadians(spawnData.yaw()), 0, null)));
        register(new EntityTypeData<>(EntityType.MINECART_COMMAND, CommandMinecart.class, CraftMinecartCommand::new, spawnData -> new EntityMinecartCommandBlock(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART, RideableMinecart.class, CraftMinecartRideable::new, spawnData -> new EntityMinecartRideable(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_CHEST, StorageMinecart.class, CraftMinecartChest::new, spawnData -> new EntityMinecartChest(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_FURNACE, PoweredMinecart.class, CraftMinecartFurnace::new, spawnData -> new EntityMinecartFurnace(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_TNT, ExplosiveMinecart.class, CraftMinecartTNT::new, spawnData -> new EntityMinecartTNT(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_HOPPER, HopperMinecart.class, CraftMinecartHopper::new, spawnData -> new EntityMinecartHopper(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.MINECART_MOB_SPAWNER, SpawnerMinecart.class, CraftMinecartMobSpawner::new, spawnData -> new EntityMinecartMobSpawner(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));

        // None spawn able
        register(new EntityTypeData<>(EntityType.FISHING_HOOK, FishHook.class, CraftFishHook::new, null)); // Cannot spawn a fish hook
        register(new EntityTypeData<>(EntityType.PLAYER, Player.class, CraftPlayer::new, null)); // Cannot spawn a player
    }

    private static void register(EntityTypeData<?, ?> typeData) {
        EntityTypeData<?, ?> other = CLASS_TYPE_DATA.put(typeData.entityClass(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for class %s, replacing '%s' with new value '%s'", typeData.entityClass().getName(), other, typeData));
        }

        other = ENTITY_TYPE_DATA.put(typeData.entityType(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for entity type %s, replacing '%s' with new value '%s'", typeData.entityType().getKey(), other, typeData));
        }
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> fromEntityType(EntityTypes<R> entityTypes) {
        return spawnData -> entityTypes.create(spawnData.minecraftWorld());
    }

    private static <R extends net.minecraft.world.entity.EntityLiving> Function<SpawnData, R> createLiving(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), ABS_MOVE);
    }

    private static <R extends EntityFireball> Function<SpawnData, R> createFireball(EntityTypes<R> entityTypes) {
        return combine(createAndMove(entityTypes), DIRECTION);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMove(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMoveEmptyRot(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), MOVE_EMPTY_ROT);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndSetPos(EntityTypes<R> entityTypes) {
        return combine(fromEntityType(entityTypes), POS);
    }

    private record HangingData(boolean randomize, BlockPosition position, EnumDirection direction) {
    }

    private static <E extends Hanging, R extends EntityHanging> Function<SpawnData, R> createHanging(Class<E> clazz, BiFunction<SpawnData, HangingData, R> spawnFunction) {
        return spawnData -> {
            boolean randomizeData = spawnData.randomizeData();
            BlockFace face = BlockFace.SELF;
            BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};

            int width = 16; // 1 full block, also painting smallest size.
            int height = 16; // 1 full block, also painting smallest size.

            if (ItemFrame.class.isAssignableFrom(clazz)) {
                width = 12;
                height = 12;
                faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
            }

            final BlockPosition pos = BlockPosition.containing(spawnData.x(), spawnData.y(), spawnData.z());
            for (BlockFace dir : faces) {
                IBlockData nmsBlock = spawnData.world().getBlockState(pos.relative(CraftBlock.blockFaceToNotch(dir)));
                if (nmsBlock.isSolid() || BlockDiodeAbstract.isDiode(nmsBlock)) {
                    boolean taken = false;
                    AxisAlignedBB bb = (ItemFrame.class.isAssignableFrom(clazz))
                            ? EntityItemFrame.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height)
                            : EntityHanging.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    List<net.minecraft.world.entity.Entity> list = spawnData.world().getEntities(null, bb);
                    for (Iterator<net.minecraft.world.entity.Entity> it = list.iterator(); !taken && it.hasNext(); ) {
                        net.minecraft.world.entity.Entity e = it.next();
                        if (e instanceof EntityHanging) {
                            taken = true; // Hanging entities do not like hanging entities which intersect them.
                        }
                    }

                    if (!taken) {
                        face = dir;
                        break;
                    }
                }
            }

            // No valid face found
            if (face == BlockFace.SELF) {
                // SPIGOT-6387: Allow hanging entities to be placed in midair
                face = BlockFace.SOUTH;
                randomizeData = false; // Don't randomize if no valid face is found, prevents null painting
            }

            EnumDirection dir = CraftBlock.blockFaceToNotch(face).getOpposite();
            return spawnFunction.apply(spawnData, new HangingData(randomizeData, pos, dir));
        };
    }

    private static <T, R> Function<T, R> combine(Function<T, R> before, BiConsumer<T, ? super R> after) {
        return (t) -> {
            R r = before.apply(t);
            after.accept(t, r);
            return r;
        };
    }

    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(EntityType entityType) {
        return (EntityTypeData<E, M>) ENTITY_TYPE_DATA.get(entityType);
    }

    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(Class<E> entityClass) {
        return (EntityTypeData<E, M>) CLASS_TYPE_DATA.get(entityClass);
    }

    private CraftEntityTypes() {
    }
}
