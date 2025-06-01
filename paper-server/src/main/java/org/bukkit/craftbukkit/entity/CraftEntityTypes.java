package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.ThrownSplashPotion;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.boat.CraftAcaciaBoat;
import org.bukkit.craftbukkit.entity.boat.CraftAcaciaChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftBambooChestRaft;
import org.bukkit.craftbukkit.entity.boat.CraftBambooRaft;
import org.bukkit.craftbukkit.entity.boat.CraftBirchBoat;
import org.bukkit.craftbukkit.entity.boat.CraftBirchChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftCherryBoat;
import org.bukkit.craftbukkit.entity.boat.CraftCherryChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftDarkOakBoat;
import org.bukkit.craftbukkit.entity.boat.CraftDarkOakChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftJungleBoat;
import org.bukkit.craftbukkit.entity.boat.CraftJungleChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftMangroveBoat;
import org.bukkit.craftbukkit.entity.boat.CraftMangroveChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftOakBoat;
import org.bukkit.craftbukkit.entity.boat.CraftOakChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftPaleOakBoat;
import org.bukkit.craftbukkit.entity.boat.CraftPaleOakChestBoat;
import org.bukkit.craftbukkit.entity.boat.CraftSpruceBoat;
import org.bukkit.craftbukkit.entity.boat.CraftSpruceChestBoat;
import org.bukkit.entity.Allay;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Bogged;
import org.bukkit.entity.Breeze;
import org.bukkit.entity.BreezeWindCharge;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creaking;
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
import org.bukkit.entity.HappyGhast;
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
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.OminousItemSpawner;
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
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Strider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tadpole;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.ThrownExpBottle;
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
import org.bukkit.entity.boat.AcaciaBoat;
import org.bukkit.entity.boat.AcaciaChestBoat;
import org.bukkit.entity.boat.BambooChestRaft;
import org.bukkit.entity.boat.BambooRaft;
import org.bukkit.entity.boat.BirchBoat;
import org.bukkit.entity.boat.BirchChestBoat;
import org.bukkit.entity.boat.CherryBoat;
import org.bukkit.entity.boat.CherryChestBoat;
import org.bukkit.entity.boat.DarkOakBoat;
import org.bukkit.entity.boat.DarkOakChestBoat;
import org.bukkit.entity.boat.JungleBoat;
import org.bukkit.entity.boat.JungleChestBoat;
import org.bukkit.entity.boat.MangroveBoat;
import org.bukkit.entity.boat.MangroveChestBoat;
import org.bukkit.entity.boat.OakBoat;
import org.bukkit.entity.boat.OakChestBoat;
import org.bukkit.entity.boat.PaleOakBoat;
import org.bukkit.entity.boat.PaleOakChestBoat;
import org.bukkit.entity.boat.SpruceBoat;
import org.bukkit.entity.boat.SpruceChestBoat;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.util.Vector;

public final class CraftEntityTypes {

    public record EntityTypeData<E extends Entity, M extends net.minecraft.world.entity.Entity>(EntityType entityType,
                                                                                                Class<E> entityClass,
                                                                                                BiFunction<CraftServer, M, E> convertFunction,
                                                                                                Function<SpawnData, M> spawnFunction) {
    }

    public record SpawnData(WorldGenLevel world, Location location, boolean randomizeData, boolean normalWorld) {
        double x() {
            return this.location().getX();
        }

        double y() {
            return this.location().getY();
        }

        double z() {
            return this.location().getZ();
        }

        float yaw() {
            return this.location().getYaw();
        }

        float pitch() {
            return this.location().getPitch();
        }

        Level minecraftWorld() {
            return this.world().getMinecraftWorld();
        }
    }

    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> POS = (spawnData, entity) -> entity.setPos(spawnData.x(), spawnData.y(), spawnData.z());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> ABS_MOVE = (spawnData, entity) -> {
        entity.absSnapTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
        entity.setYHeadRot(spawnData.yaw()); // SPIGOT-3587
    };
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE = (spawnData, entity) -> entity.snapTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> MOVE_EMPTY_ROT = (spawnData, entity) -> entity.snapTo(spawnData.x(), spawnData.y(), spawnData.z(), 0, 0);
    private static final BiConsumer<SpawnData, AbstractHurtingProjectile> DIRECTION = (spawnData, entity) -> {
        Vector direction = spawnData.location().getDirection();
        entity.assignDirectionalMovement(new Vec3(direction.getX(), direction.getY(), direction.getZ()), 1.0);
    };
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> ROT = (spawnData, entity) -> entity.setRot(spawnData.yaw(), spawnData.pitch()); // Paper
    // Paper start - respect randomizeData
    private static final BiConsumer<SpawnData, net.minecraft.world.entity.Entity> CLEAR_MOVE_IF_NOT_RANDOMIZED = (spawnData, entity) -> {
        if (!spawnData.randomizeData()) {
            entity.setDeltaMovement(net.minecraft.world.phys.Vec3.ZERO);
        }
    };
    // Paper end - respect randomizeData
    private static final Map<Class<?>, EntityTypeData<?, ?>> CLASS_TYPE_DATA = new HashMap<>();
    private static final Map<EntityType, EntityTypeData<?, ?>> ENTITY_TYPE_DATA = new HashMap<>();

    static {
        // Living
        register(new EntityTypeData<>(EntityType.ELDER_GUARDIAN, ElderGuardian.class, CraftElderGuardian::new, createLiving(net.minecraft.world.entity.EntityType.ELDER_GUARDIAN)));
        register(new EntityTypeData<>(EntityType.WITHER_SKELETON, WitherSkeleton.class, CraftWitherSkeleton::new, createLiving(net.minecraft.world.entity.EntityType.WITHER_SKELETON)));
        register(new EntityTypeData<>(EntityType.STRAY, Stray.class, CraftStray::new, createLiving(net.minecraft.world.entity.EntityType.STRAY)));
        register(new EntityTypeData<>(EntityType.BOGGED, Bogged.class, CraftBogged::new, createLiving(net.minecraft.world.entity.EntityType.BOGGED)));
        register(new EntityTypeData<>(EntityType.HUSK, Husk.class, CraftHusk::new, createLiving(net.minecraft.world.entity.EntityType.HUSK)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_VILLAGER, ZombieVillager.class, CraftVillagerZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE_VILLAGER)));
        register(new EntityTypeData<>(EntityType.SKELETON_HORSE, SkeletonHorse.class, CraftSkeletonHorse::new, createLiving(net.minecraft.world.entity.EntityType.SKELETON_HORSE)));
        register(new EntityTypeData<>(EntityType.ZOMBIE_HORSE, ZombieHorse.class, CraftZombieHorse::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE_HORSE)));
        register(new EntityTypeData<>(EntityType.ARMOR_STAND, ArmorStand.class, CraftArmorStand::new, createLiving(net.minecraft.world.entity.EntityType.ARMOR_STAND)));
        register(new EntityTypeData<>(EntityType.DONKEY, Donkey.class, CraftDonkey::new, createLiving(net.minecraft.world.entity.EntityType.DONKEY)));
        register(new EntityTypeData<>(EntityType.MULE, Mule.class, CraftMule::new, createLiving(net.minecraft.world.entity.EntityType.MULE)));
        register(new EntityTypeData<>(EntityType.EVOKER, Evoker.class, CraftEvoker::new, createLiving(net.minecraft.world.entity.EntityType.EVOKER)));
        register(new EntityTypeData<>(EntityType.VEX, Vex.class, CraftVex::new, createLiving(net.minecraft.world.entity.EntityType.VEX)));
        register(new EntityTypeData<>(EntityType.VINDICATOR, Vindicator.class, CraftVindicator::new, createLiving(net.minecraft.world.entity.EntityType.VINDICATOR)));
        register(new EntityTypeData<>(EntityType.ILLUSIONER, Illusioner.class, CraftIllusioner::new, createLiving(net.minecraft.world.entity.EntityType.ILLUSIONER)));
        register(new EntityTypeData<>(EntityType.CREEPER, Creeper.class, CraftCreeper::new, createLiving(net.minecraft.world.entity.EntityType.CREEPER)));
        register(new EntityTypeData<>(EntityType.SKELETON, Skeleton.class, CraftSkeleton::new, createLiving(net.minecraft.world.entity.EntityType.SKELETON)));
        register(new EntityTypeData<>(EntityType.SPIDER, Spider.class, CraftSpider::new, createLiving(net.minecraft.world.entity.EntityType.SPIDER)));
        register(new EntityTypeData<>(EntityType.GIANT, Giant.class, CraftGiant::new, createLiving(net.minecraft.world.entity.EntityType.GIANT)));
        register(new EntityTypeData<>(EntityType.ZOMBIE, Zombie.class, CraftZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIE)));
        register(new EntityTypeData<>(EntityType.SLIME, Slime.class, CraftSlime::new, createLiving(net.minecraft.world.entity.EntityType.SLIME)));
        register(new EntityTypeData<>(EntityType.GHAST, Ghast.class, CraftGhast::new, createLiving(net.minecraft.world.entity.EntityType.GHAST)));
        register(new EntityTypeData<>(EntityType.ZOMBIFIED_PIGLIN, PigZombie.class, CraftPigZombie::new, createLiving(net.minecraft.world.entity.EntityType.ZOMBIFIED_PIGLIN)));
        register(new EntityTypeData<>(EntityType.ENDERMAN, Enderman.class, CraftEnderman::new, createLiving(net.minecraft.world.entity.EntityType.ENDERMAN)));
        register(new EntityTypeData<>(EntityType.CAVE_SPIDER, CaveSpider.class, CraftCaveSpider::new, createLiving(net.minecraft.world.entity.EntityType.CAVE_SPIDER)));
        register(new EntityTypeData<>(EntityType.SILVERFISH, Silverfish.class, CraftSilverfish::new, createLiving(net.minecraft.world.entity.EntityType.SILVERFISH)));
        register(new EntityTypeData<>(EntityType.BLAZE, Blaze.class, CraftBlaze::new, createLiving(net.minecraft.world.entity.EntityType.BLAZE)));
        register(new EntityTypeData<>(EntityType.MAGMA_CUBE, MagmaCube.class, CraftMagmaCube::new, createLiving(net.minecraft.world.entity.EntityType.MAGMA_CUBE)));
        register(new EntityTypeData<>(EntityType.WITHER, Wither.class, CraftWither::new, createLiving(net.minecraft.world.entity.EntityType.WITHER)));
        register(new EntityTypeData<>(EntityType.BAT, Bat.class, CraftBat::new, createLiving(net.minecraft.world.entity.EntityType.BAT)));
        register(new EntityTypeData<>(EntityType.WITCH, Witch.class, CraftWitch::new, createLiving(net.minecraft.world.entity.EntityType.WITCH)));
        register(new EntityTypeData<>(EntityType.ENDERMITE, Endermite.class, CraftEndermite::new, createLiving(net.minecraft.world.entity.EntityType.ENDERMITE)));
        register(new EntityTypeData<>(EntityType.GUARDIAN, Guardian.class, CraftGuardian::new, createLiving(net.minecraft.world.entity.EntityType.GUARDIAN)));
        register(new EntityTypeData<>(EntityType.SHULKER, Shulker.class, CraftShulker::new, createLiving(net.minecraft.world.entity.EntityType.SHULKER)));
        register(new EntityTypeData<>(EntityType.PIG, Pig.class, CraftPig::new, createLiving(net.minecraft.world.entity.EntityType.PIG)));
        register(new EntityTypeData<>(EntityType.SHEEP, Sheep.class, CraftSheep::new, createLiving(net.minecraft.world.entity.EntityType.SHEEP)));
        register(new EntityTypeData<>(EntityType.COW, Cow.class, CraftCow::new, createLiving(net.minecraft.world.entity.EntityType.COW)));
        register(new EntityTypeData<>(EntityType.CHICKEN, Chicken.class, CraftChicken::new, createLiving(net.minecraft.world.entity.EntityType.CHICKEN)));
        register(new EntityTypeData<>(EntityType.SQUID, Squid.class, CraftSquid::new, createLiving(net.minecraft.world.entity.EntityType.SQUID)));
        register(new EntityTypeData<>(EntityType.WOLF, Wolf.class, CraftWolf::new, createLiving(net.minecraft.world.entity.EntityType.WOLF)));
        register(new EntityTypeData<>(EntityType.MOOSHROOM, MushroomCow.class, CraftMushroomCow::new, createLiving(net.minecraft.world.entity.EntityType.MOOSHROOM)));
        register(new EntityTypeData<>(EntityType.SNOW_GOLEM, Snowman.class, CraftSnowman::new, createLiving(net.minecraft.world.entity.EntityType.SNOW_GOLEM)));
        register(new EntityTypeData<>(EntityType.OCELOT, Ocelot.class, CraftOcelot::new, createLiving(net.minecraft.world.entity.EntityType.OCELOT)));
        register(new EntityTypeData<>(EntityType.IRON_GOLEM, IronGolem.class, CraftIronGolem::new, createLiving(net.minecraft.world.entity.EntityType.IRON_GOLEM)));
        register(new EntityTypeData<>(EntityType.HORSE, Horse.class, CraftHorse::new, createLiving(net.minecraft.world.entity.EntityType.HORSE)));
        register(new EntityTypeData<>(EntityType.RABBIT, Rabbit.class, CraftRabbit::new, createLiving(net.minecraft.world.entity.EntityType.RABBIT)));
        register(new EntityTypeData<>(EntityType.POLAR_BEAR, PolarBear.class, CraftPolarBear::new, createLiving(net.minecraft.world.entity.EntityType.POLAR_BEAR)));
        register(new EntityTypeData<>(EntityType.LLAMA, Llama.class, CraftLlama::new, createLiving(net.minecraft.world.entity.EntityType.LLAMA)));
        register(new EntityTypeData<>(EntityType.PARROT, Parrot.class, CraftParrot::new, createLiving(net.minecraft.world.entity.EntityType.PARROT)));
        register(new EntityTypeData<>(EntityType.VILLAGER, Villager.class, CraftVillager::new, createLiving(net.minecraft.world.entity.EntityType.VILLAGER)));
        register(new EntityTypeData<>(EntityType.TURTLE, Turtle.class, CraftTurtle::new, createLiving(net.minecraft.world.entity.EntityType.TURTLE)));
        register(new EntityTypeData<>(EntityType.PHANTOM, Phantom.class, CraftPhantom::new, createLiving(net.minecraft.world.entity.EntityType.PHANTOM)));
        register(new EntityTypeData<>(EntityType.COD, Cod.class, CraftCod::new, createLiving(net.minecraft.world.entity.EntityType.COD)));
        register(new EntityTypeData<>(EntityType.SALMON, Salmon.class, CraftSalmon::new, createLiving(net.minecraft.world.entity.EntityType.SALMON)));
        register(new EntityTypeData<>(EntityType.PUFFERFISH, PufferFish.class, CraftPufferFish::new, createLiving(net.minecraft.world.entity.EntityType.PUFFERFISH)));
        register(new EntityTypeData<>(EntityType.TROPICAL_FISH, TropicalFish.class, CraftTropicalFish::new, createLiving(net.minecraft.world.entity.EntityType.TROPICAL_FISH)));
        register(new EntityTypeData<>(EntityType.DROWNED, Drowned.class, CraftDrowned::new, createLiving(net.minecraft.world.entity.EntityType.DROWNED)));
        register(new EntityTypeData<>(EntityType.DOLPHIN, Dolphin.class, CraftDolphin::new, createLiving(net.minecraft.world.entity.EntityType.DOLPHIN)));
        register(new EntityTypeData<>(EntityType.CAT, Cat.class, CraftCat::new, createLiving(net.minecraft.world.entity.EntityType.CAT)));
        register(new EntityTypeData<>(EntityType.PANDA, Panda.class, CraftPanda::new, createLiving(net.minecraft.world.entity.EntityType.PANDA)));
        register(new EntityTypeData<>(EntityType.PILLAGER, Pillager.class, CraftPillager::new, createLiving(net.minecraft.world.entity.EntityType.PILLAGER)));
        register(new EntityTypeData<>(EntityType.RAVAGER, Ravager.class, CraftRavager::new, createLiving(net.minecraft.world.entity.EntityType.RAVAGER)));
        register(new EntityTypeData<>(EntityType.TRADER_LLAMA, TraderLlama.class, CraftTraderLlama::new, createLiving(net.minecraft.world.entity.EntityType.TRADER_LLAMA)));
        register(new EntityTypeData<>(EntityType.WANDERING_TRADER, WanderingTrader.class, CraftWanderingTrader::new, createLiving(net.minecraft.world.entity.EntityType.WANDERING_TRADER)));
        register(new EntityTypeData<>(EntityType.FOX, Fox.class, CraftFox::new, createLiving(net.minecraft.world.entity.EntityType.FOX)));
        register(new EntityTypeData<>(EntityType.BEE, Bee.class, CraftBee::new, createLiving(net.minecraft.world.entity.EntityType.BEE)));
        register(new EntityTypeData<>(EntityType.HOGLIN, Hoglin.class, CraftHoglin::new, createLiving(net.minecraft.world.entity.EntityType.HOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN, Piglin.class, CraftPiglin::new, createLiving(net.minecraft.world.entity.EntityType.PIGLIN)));
        register(new EntityTypeData<>(EntityType.STRIDER, Strider.class, CraftStrider::new, createLiving(net.minecraft.world.entity.EntityType.STRIDER)));
        register(new EntityTypeData<>(EntityType.ZOGLIN, Zoglin.class, CraftZoglin::new, createLiving(net.minecraft.world.entity.EntityType.ZOGLIN)));
        register(new EntityTypeData<>(EntityType.PIGLIN_BRUTE, PiglinBrute.class, CraftPiglinBrute::new, createLiving(net.minecraft.world.entity.EntityType.PIGLIN_BRUTE)));
        register(new EntityTypeData<>(EntityType.AXOLOTL, Axolotl.class, CraftAxolotl::new, createLiving(net.minecraft.world.entity.EntityType.AXOLOTL)));
        register(new EntityTypeData<>(EntityType.GLOW_SQUID, GlowSquid.class, CraftGlowSquid::new, createLiving(net.minecraft.world.entity.EntityType.GLOW_SQUID)));
        register(new EntityTypeData<>(EntityType.GOAT, Goat.class, CraftGoat::new, createLiving(net.minecraft.world.entity.EntityType.GOAT)));
        register(new EntityTypeData<>(EntityType.ALLAY, Allay.class, CraftAllay::new, createLiving(net.minecraft.world.entity.EntityType.ALLAY)));
        register(new EntityTypeData<>(EntityType.FROG, Frog.class, CraftFrog::new, createLiving(net.minecraft.world.entity.EntityType.FROG)));
        register(new EntityTypeData<>(EntityType.TADPOLE, Tadpole.class, CraftTadpole::new, createLiving(net.minecraft.world.entity.EntityType.TADPOLE)));
        register(new EntityTypeData<>(EntityType.WARDEN, Warden.class, CraftWarden::new, createLiving(net.minecraft.world.entity.EntityType.WARDEN)));
        register(new EntityTypeData<>(EntityType.CAMEL, Camel.class, CraftCamel::new, createLiving(net.minecraft.world.entity.EntityType.CAMEL)));
        register(new EntityTypeData<>(EntityType.SNIFFER, Sniffer.class, CraftSniffer::new, createLiving(net.minecraft.world.entity.EntityType.SNIFFER)));
        register(new EntityTypeData<>(EntityType.BREEZE, Breeze.class, CraftBreeze::new, createLiving(net.minecraft.world.entity.EntityType.BREEZE)));
        register(new EntityTypeData<>(EntityType.ARMADILLO, Armadillo.class, CraftArmadillo::new, createLiving(net.minecraft.world.entity.EntityType.ARMADILLO)));
        register(new EntityTypeData<>(EntityType.CREAKING, Creaking.class, CraftCreaking::new, createLiving(net.minecraft.world.entity.EntityType.CREAKING)));
        register(new EntityTypeData<>(EntityType.HAPPY_GHAST, HappyGhast.class, CraftHappyGhast::new, createLiving(net.minecraft.world.entity.EntityType.HAPPY_GHAST)));

        Function<SpawnData, net.minecraft.world.entity.boss.enderdragon.EnderDragon> dragonFunction = createLiving(net.minecraft.world.entity.EntityType.ENDER_DRAGON);
        register(new EntityTypeData<>(EntityType.ENDER_DRAGON, EnderDragon.class, CraftEnderDragon::new, spawnData -> {
            Preconditions.checkArgument(spawnData.normalWorld(), "Cannot spawn entity %s during world generation", EnderDragon.class.getName());
            return dragonFunction.apply(spawnData);
        }));

        // Fireball
        register(new EntityTypeData<>(EntityType.FIREBALL, LargeFireball.class, CraftLargeFireball::new, createFireball(net.minecraft.world.entity.EntityType.FIREBALL)));
        register(new EntityTypeData<>(EntityType.SMALL_FIREBALL, SmallFireball.class, CraftSmallFireball::new, createFireball(net.minecraft.world.entity.EntityType.SMALL_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WITHER_SKULL, WitherSkull.class, CraftWitherSkull::new, createFireball(net.minecraft.world.entity.EntityType.WITHER_SKULL)));
        register(new EntityTypeData<>(EntityType.DRAGON_FIREBALL, DragonFireball.class, CraftDragonFireball::new, createFireball(net.minecraft.world.entity.EntityType.DRAGON_FIREBALL)));
        register(new EntityTypeData<>(EntityType.WIND_CHARGE, WindCharge.class, CraftWindCharge::new, createFireball(net.minecraft.world.entity.EntityType.WIND_CHARGE)));
        register(new EntityTypeData<>(EntityType.BREEZE_WIND_CHARGE, BreezeWindCharge.class, CraftBreezeWindCharge::new, createFireball(net.minecraft.world.entity.EntityType.BREEZE_WIND_CHARGE)));

        // Hanging
        register(new EntityTypeData<>(EntityType.PAINTING, Painting.class, CraftPainting::new, createHanging(Painting.class, (spawnData, hangingData) -> {
                    if (spawnData.normalWorld && hangingData.randomize()) {
                        // Paper start - if randomizeData fails, force it
                        final net.minecraft.world.entity.decoration.Painting entity = net.minecraft.world.entity.decoration.Painting.create(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()).orElse(null);
                        if (entity != null) {
                            return entity;
                        }
                    } /*else*/ {
                        // Paper end - if randomizeData fails, force it
                        net.minecraft.world.entity.decoration.Painting entity = new net.minecraft.world.entity.decoration.Painting(net.minecraft.world.entity.EntityType.PAINTING, spawnData.minecraftWorld());
                        entity.absSnapTo(spawnData.x(), spawnData.y(), spawnData.z(), spawnData.yaw(), spawnData.pitch());
                        entity.setDirection(hangingData.direction());
                        return entity;
                    }
                }
        )));
        register(new EntityTypeData<>(EntityType.ITEM_FRAME, ItemFrame.class, CraftItemFrame::new, createHanging(ItemFrame.class, (spawnData, hangingData) -> new net.minecraft.world.entity.decoration.ItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));
        register(new EntityTypeData<>(EntityType.GLOW_ITEM_FRAME, GlowItemFrame.class, CraftGlowItemFrame::new, createHanging(GlowItemFrame.class, (spawnData, hangingData) -> new net.minecraft.world.entity.decoration.GlowItemFrame(spawnData.minecraftWorld(), hangingData.position(), hangingData.direction()))));

        // Move no rotation
        register(new EntityTypeData<>(EntityType.ARROW, Arrow.class, CraftArrow::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.ARROW)));
        register(new EntityTypeData<>(EntityType.ENDER_PEARL, EnderPearl.class, CraftEnderPearl::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.ENDER_PEARL)));
        register(new EntityTypeData<>(EntityType.EXPERIENCE_BOTTLE, ThrownExpBottle.class, CraftThrownExpBottle::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.EXPERIENCE_BOTTLE)));
        register(new EntityTypeData<>(EntityType.SPECTRAL_ARROW, SpectralArrow.class, CraftSpectralArrow::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.SPECTRAL_ARROW)));
        register(new EntityTypeData<>(EntityType.END_CRYSTAL, EnderCrystal.class, CraftEnderCrystal::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.END_CRYSTAL)));
        register(new EntityTypeData<>(EntityType.TRIDENT, Trident.class, CraftTrident::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.TRIDENT)));
        register(new EntityTypeData<>(EntityType.LIGHTNING_BOLT, LightningStrike.class, CraftLightningStrike::new, createAndMoveEmptyRot(net.minecraft.world.entity.EntityType.LIGHTNING_BOLT)));

        // Move
        register(new EntityTypeData<>(EntityType.SHULKER_BULLET, ShulkerBullet.class, CraftShulkerBullet::new, createAndMove(net.minecraft.world.entity.EntityType.SHULKER_BULLET)));
        register(new EntityTypeData<>(EntityType.LLAMA_SPIT, LlamaSpit.class, CraftLlamaSpit::new, createAndMove(net.minecraft.world.entity.EntityType.LLAMA_SPIT)));
        register(new EntityTypeData<>(EntityType.OMINOUS_ITEM_SPAWNER, OminousItemSpawner.class, CraftOminousItemSpawner::new, createAndMove(net.minecraft.world.entity.EntityType.OMINOUS_ITEM_SPAWNER)));
        // Move (boats)
        register(new EntityTypeData<>(EntityType.ACACIA_BOAT, AcaciaBoat.class, CraftAcaciaBoat::new, createAndMove(net.minecraft.world.entity.EntityType.ACACIA_BOAT)));
        register(new EntityTypeData<>(EntityType.ACACIA_CHEST_BOAT, AcaciaChestBoat.class, CraftAcaciaChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.ACACIA_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.BAMBOO_RAFT, BambooRaft.class, CraftBambooRaft::new, createAndMove(net.minecraft.world.entity.EntityType.BAMBOO_RAFT)));
        register(new EntityTypeData<>(EntityType.BAMBOO_CHEST_RAFT, BambooChestRaft.class, CraftBambooChestRaft::new, createAndMove(net.minecraft.world.entity.EntityType.BAMBOO_CHEST_RAFT)));
        register(new EntityTypeData<>(EntityType.BIRCH_BOAT, BirchBoat.class, CraftBirchBoat::new, createAndMove(net.minecraft.world.entity.EntityType.BIRCH_BOAT)));
        register(new EntityTypeData<>(EntityType.BIRCH_CHEST_BOAT, BirchChestBoat.class, CraftBirchChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.BIRCH_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.CHERRY_BOAT, CherryBoat.class, CraftCherryBoat::new, createAndMove(net.minecraft.world.entity.EntityType.CHERRY_BOAT)));
        register(new EntityTypeData<>(EntityType.CHERRY_CHEST_BOAT, CherryChestBoat.class, CraftCherryChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.CHERRY_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.DARK_OAK_BOAT, DarkOakBoat.class, CraftDarkOakBoat::new, createAndMove(net.minecraft.world.entity.EntityType.DARK_OAK_BOAT)));
        register(new EntityTypeData<>(EntityType.DARK_OAK_CHEST_BOAT, DarkOakChestBoat.class, CraftDarkOakChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.DARK_OAK_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.JUNGLE_BOAT, JungleBoat.class, CraftJungleBoat::new, createAndMove(net.minecraft.world.entity.EntityType.JUNGLE_BOAT)));
        register(new EntityTypeData<>(EntityType.JUNGLE_CHEST_BOAT, JungleChestBoat.class, CraftJungleChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.JUNGLE_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.MANGROVE_BOAT, MangroveBoat.class, CraftMangroveBoat::new, createAndMove(net.minecraft.world.entity.EntityType.MANGROVE_BOAT)));
        register(new EntityTypeData<>(EntityType.MANGROVE_CHEST_BOAT, MangroveChestBoat.class, CraftMangroveChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.MANGROVE_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.OAK_BOAT, OakBoat.class, CraftOakBoat::new, createAndMove(net.minecraft.world.entity.EntityType.OAK_BOAT)));
        register(new EntityTypeData<>(EntityType.OAK_CHEST_BOAT, OakChestBoat.class, CraftOakChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.OAK_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.PALE_OAK_BOAT, PaleOakBoat.class, CraftPaleOakBoat::new, createAndMove(net.minecraft.world.entity.EntityType.PALE_OAK_BOAT)));
        register(new EntityTypeData<>(EntityType.PALE_OAK_CHEST_BOAT, PaleOakChestBoat.class, CraftPaleOakChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.PALE_OAK_CHEST_BOAT)));
        register(new EntityTypeData<>(EntityType.SPRUCE_BOAT, SpruceBoat.class, CraftSpruceBoat::new, createAndMove(net.minecraft.world.entity.EntityType.SPRUCE_BOAT)));
        register(new EntityTypeData<>(EntityType.SPRUCE_CHEST_BOAT, SpruceChestBoat.class, CraftSpruceChestBoat::new, createAndMove(net.minecraft.world.entity.EntityType.SPRUCE_CHEST_BOAT)));

        // Set pos
        register(new EntityTypeData<>(EntityType.MARKER, Marker.class, CraftMarker::new, createAndSetPos(net.minecraft.world.entity.EntityType.MARKER)));
        register(new EntityTypeData<>(EntityType.BLOCK_DISPLAY, BlockDisplay.class, CraftBlockDisplay::new, combine(createAndSetPos(net.minecraft.world.entity.EntityType.BLOCK_DISPLAY), ROT))); // Paper
        register(new EntityTypeData<>(EntityType.INTERACTION, Interaction.class, CraftInteraction::new, createAndSetPos(net.minecraft.world.entity.EntityType.INTERACTION)));
        register(new EntityTypeData<>(EntityType.ITEM_DISPLAY, ItemDisplay.class, CraftItemDisplay::new, combine(createAndSetPos(net.minecraft.world.entity.EntityType.ITEM_DISPLAY), ROT))); // Paper
        register(new EntityTypeData<>(EntityType.TEXT_DISPLAY, TextDisplay.class, CraftTextDisplay::new, combine(createAndSetPos(net.minecraft.world.entity.EntityType.TEXT_DISPLAY), ROT))); // Paper

        // MISC
        register(new EntityTypeData<>(EntityType.ITEM, Item.class, CraftItem::new, spawnData -> {
            // We use stone instead of empty, to give the plugin developer a visual clue, that the spawn method is working,
            // and that the item stack should probably be changed.
            net.minecraft.world.item.ItemStack itemStack = new net.minecraft.world.item.ItemStack(Items.STONE);
            ItemEntity item = new ItemEntity(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), itemStack);
            item.setPickUpDelay(10);
            CLEAR_MOVE_IF_NOT_RANDOMIZED.accept(spawnData, item); // Paper - respect randomizeData

            return item;
        }));
        register(new EntityTypeData<>(EntityType.EXPERIENCE_ORB, ExperienceOrb.class, CraftExperienceOrb::new,
                combine(combine(spawnData -> new net.minecraft.world.entity.ExperienceOrb(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), 0, org.bukkit.entity.ExperienceOrb.SpawnReason.CUSTOM, null, null), CLEAR_MOVE_IF_NOT_RANDOMIZED), (spawnData, experienceOrb) -> { if (!spawnData.randomizeData()) { experienceOrb.setYRot(0); } }) // Paper - respect randomizeData
        ));
        register(new EntityTypeData<>(EntityType.AREA_EFFECT_CLOUD, AreaEffectCloud.class, CraftAreaEffectCloud::new, createAndMove(net.minecraft.world.entity.EntityType.AREA_EFFECT_CLOUD))); // Paper - set area effect cloud rotation
        register(new EntityTypeData<>(EntityType.EGG, Egg.class, CraftEgg::new, spawnData -> new ThrownEgg(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), new ItemStack(Items.EGG))));
        register(new EntityTypeData<>(EntityType.LEASH_KNOT, LeashHitch.class, CraftLeash::new, spawnData -> new LeashFenceKnotEntity(spawnData.minecraftWorld(), BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z())))); // SPIGOT-5732: LeashHitch has no direction and is always centered at a block
        register(new EntityTypeData<>(EntityType.SNOWBALL, Snowball.class, CraftSnowball::new, spawnData -> new net.minecraft.world.entity.projectile.Snowball(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), new ItemStack(Items.SNOWBALL))));
        register(new EntityTypeData<>(EntityType.EYE_OF_ENDER, EnderSignal.class, CraftEnderSignal::new, spawnData -> new EyeOfEnder(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z())));
        register(new EntityTypeData<>(EntityType.SPLASH_POTION, SplashPotion.class, CraftThrownSplashPotion::new, spawnData -> new ThrownSplashPotion(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), new ItemStack(Items.SPLASH_POTION))));
        register(new EntityTypeData<>(EntityType.LINGERING_POTION, LingeringPotion.class, CraftThrownLingeringPotion::new, spawnData -> new ThrownLingeringPotion(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), new ItemStack(Items.LINGERING_POTION))));
        register(new EntityTypeData<>(EntityType.TNT, TNTPrimed.class, CraftTNTPrimed::new, combine(spawnData -> new PrimedTnt(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), null), CLEAR_MOVE_IF_NOT_RANDOMIZED))); // Paper - respect randomizeData
        register(new EntityTypeData<>(EntityType.FALLING_BLOCK, FallingBlock.class, CraftFallingBlock::new, spawnData -> {
            BlockPos pos = BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z());
            return new FallingBlockEntity(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), spawnData.world().getBlockState(pos)); // Paper - create falling block entities correctly
        }));
        // Paper start - respect randomizeData
        register(new EntityTypeData<>(EntityType.FIREWORK_ROCKET, Firework.class, CraftFirework::new, spawnData -> {
            FireworkRocketEntity entity = new FireworkRocketEntity(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), FireworkRocketEntity.getDefaultItem()); // Paper - pass correct default to rocket for data storage
            if (!spawnData.randomizeData()) {
                // logic below was taken from FireworkRocketEntity constructor
                entity.setDeltaMovement(0, 0.05, 0);
                //noinspection PointlessArithmeticExpression
                entity.lifetime = 10 * 1 + 6;
            }
            return entity;
        }));
        // Paper end - respect randomizeData
        register(new EntityTypeData<>(EntityType.EVOKER_FANGS, EvokerFangs.class, CraftEvokerFangs::new, spawnData -> new net.minecraft.world.entity.projectile.EvokerFangs(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), (float) Math.toRadians(spawnData.yaw()), 0, null)));
        register(new EntityTypeData<>(EntityType.COMMAND_BLOCK_MINECART, CommandMinecart.class, CraftMinecartCommand::new, createMinecart(net.minecraft.world.entity.EntityType.COMMAND_BLOCK_MINECART)));
        register(new EntityTypeData<>(EntityType.MINECART, RideableMinecart.class, CraftMinecartRideable::new, createMinecart(net.minecraft.world.entity.EntityType.MINECART)));
        register(new EntityTypeData<>(EntityType.CHEST_MINECART, StorageMinecart.class, CraftMinecartChest::new, createMinecart(net.minecraft.world.entity.EntityType.CHEST_MINECART)));
        register(new EntityTypeData<>(EntityType.FURNACE_MINECART, PoweredMinecart.class, CraftMinecartFurnace::new, createMinecart(net.minecraft.world.entity.EntityType.FURNACE_MINECART)));
        register(new EntityTypeData<>(EntityType.TNT_MINECART, ExplosiveMinecart.class, CraftMinecartTNT::new, createMinecart(net.minecraft.world.entity.EntityType.TNT_MINECART)));
        register(new EntityTypeData<>(EntityType.HOPPER_MINECART, HopperMinecart.class, CraftMinecartHopper::new, createMinecart(net.minecraft.world.entity.EntityType.HOPPER_MINECART)));
        register(new EntityTypeData<>(EntityType.SPAWNER_MINECART, SpawnerMinecart.class, CraftMinecartMobSpawner::new, createMinecart(net.minecraft.world.entity.EntityType.SPAWNER_MINECART)));

        // None spawn able
        register(new EntityTypeData<>(EntityType.FISHING_BOBBER, FishHook.class, CraftFishHook::new, null)); // Cannot spawn a fish hook
        register(new EntityTypeData<>(EntityType.PLAYER, Player.class, CraftPlayer::new, null)); // Cannot spawn a player
    }

    private static void register(EntityTypeData<?, ?> typeData) {
        EntityTypeData<?, ?> other = CraftEntityTypes.CLASS_TYPE_DATA.put(typeData.entityClass(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for class %s, replacing '%s' with new value '%s'", typeData.entityClass().getName(), other, typeData));
        }

        other = CraftEntityTypes.ENTITY_TYPE_DATA.put(typeData.entityType(), typeData);
        if (other != null) {
            Bukkit.getLogger().warning(String.format("Found multiple entity type data for entity type %s, replacing '%s' with new value '%s'", typeData.entityType().getKey(), other, typeData));
        }
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> fromEntityType(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return spawnData -> entityTypes.create(spawnData.minecraftWorld(), EntitySpawnReason.COMMAND);
    }

    private static <R extends net.minecraft.world.entity.LivingEntity> Function<SpawnData, R> createLiving(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return CraftEntityTypes.combine(CraftEntityTypes.fromEntityType(entityTypes), CraftEntityTypes.ABS_MOVE);
    }

    private static <R extends AbstractHurtingProjectile> Function<SpawnData, R> createFireball(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return CraftEntityTypes.combine(CraftEntityTypes.createAndMove(entityTypes), CraftEntityTypes.DIRECTION);
    }

    private static <R extends AbstractMinecart> Function<SpawnData, R> createMinecart(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return spawnData -> {
            if (spawnData.normalWorld()) {
                return AbstractMinecart.createMinecart(spawnData.minecraftWorld(), spawnData.x(), spawnData.y(), spawnData.z(), entityTypes, EntitySpawnReason.TRIGGERED, ItemStack.EMPTY, null);
            } else {
                return CraftEntityTypes.combine(CraftEntityTypes.fromEntityType(entityTypes), (spawnData2, entity) -> entity.setInitialPos(spawnData.x(), spawnData.y(), spawnData.z())).apply(spawnData);
            }
        };
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMove(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return CraftEntityTypes.combine(CraftEntityTypes.fromEntityType(entityTypes), CraftEntityTypes.MOVE);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndMoveEmptyRot(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return CraftEntityTypes.combine(CraftEntityTypes.fromEntityType(entityTypes), CraftEntityTypes.MOVE_EMPTY_ROT);
    }

    private static <R extends net.minecraft.world.entity.Entity> Function<SpawnData, R> createAndSetPos(net.minecraft.world.entity.EntityType<R> entityTypes) {
        return CraftEntityTypes.combine(CraftEntityTypes.fromEntityType(entityTypes), CraftEntityTypes.POS);
    }

    private record HangingData(boolean randomize, BlockPos position, Direction direction) {
    }

    private static <E extends Hanging, R extends HangingEntity> Function<SpawnData, R> createHanging(Class<E> clazz, BiFunction<SpawnData, HangingData, R> spawnFunction) {
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

            final BlockPos pos = BlockPos.containing(spawnData.x(), spawnData.y(), spawnData.z());
            for (BlockFace dir : faces) {
                BlockState nmsBlock = spawnData.world().getBlockState(pos.relative(CraftBlock.blockFaceToNotch(dir)));
                if (nmsBlock.isSolid() || DiodeBlock.isDiode(nmsBlock)) {
                    boolean taken = false;
                    AABB bb = (ItemFrame.class.isAssignableFrom(clazz))
                            ? net.minecraft.world.entity.decoration.ItemFrame.calculateBoundingBoxStatic(pos, CraftBlock.blockFaceToNotch(dir).getOpposite())
                            : net.minecraft.world.entity.decoration.Painting.calculateBoundingBoxStatic(pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                    if (!spawnData.world.noCollision(bb)) continue; // Paper - add collision check
                    List<net.minecraft.world.entity.Entity> list = spawnData.world().getEntities(null, bb);
                    for (Iterator<net.minecraft.world.entity.Entity> it = list.iterator(); !taken && it.hasNext(); ) {
                        net.minecraft.world.entity.Entity e = it.next();
                        if (e instanceof HangingEntity) {
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

            Direction dir = CraftBlock.blockFaceToNotch(face).getOpposite();
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
        return (EntityTypeData<E, M>) CraftEntityTypes.ENTITY_TYPE_DATA.get(entityType);
    }

    public static <E extends Entity, M extends net.minecraft.world.entity.Entity> EntityTypeData<E, M> getEntityTypeData(Class<E> entityClass) {
        return (EntityTypeData<E, M>) CraftEntityTypes.CLASS_TYPE_DATA.get(entityClass);
    }

    private CraftEntityTypes() {
    }
}
