package io.papermc.paper.entity.meta;

import io.papermc.paper.generated.GeneratedFrom;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.OminousItemSpawner;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.Donkey;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.animal.horse.Mule;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.entity.vehicle.MinecartTNT;

@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@GeneratedFrom("1.21")
public final class EntityMetaWatcher {
    private static final Map<Class<? extends Entity>, Map<Long, EntityDataSerializer<?>>> VALID_ENTITY_META_MAP = initialize();

    private static final Map<Long, EntityDataSerializer<?>> cow() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartTNT() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> panda() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.INT);
        result.put(19L, EntityDataSerializers.INT);
        result.put(20L, EntityDataSerializers.BYTE);
        result.put(21L, EntityDataSerializers.BYTE);
        result.put(22L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> pig() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> vindicator() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> thrownEnderpearl() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> lightningBolt() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> warden() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> spectralArrow() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> llama() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(19L, EntityDataSerializers.INT);
        result.put(20L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> phantom() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> thrownPotion() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> squid() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> evokerFangs() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> guardian() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartSpawner() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> armadillo() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.ARMADILLO_STATE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> stray() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> axolotl() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> mule() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> itemEntity() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> blockDisplay() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(23L, EntityDataSerializers.BLOCK_STATE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.INT);
        result.put(11L, EntityDataSerializers.VECTOR3);
        result.put(12L, EntityDataSerializers.VECTOR3);
        result.put(13L, EntityDataSerializers.QUATERNION);
        result.put(14L, EntityDataSerializers.QUATERNION);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.FLOAT);
        result.put(18L, EntityDataSerializers.FLOAT);
        result.put(19L, EntityDataSerializers.FLOAT);
        result.put(20L, EntityDataSerializers.FLOAT);
        result.put(21L, EntityDataSerializers.FLOAT);
        result.put(22L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> sheep() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> breezeWindCharge() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> dragonFireball() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> endCrystal() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(9L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> glowItemFrame() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(9L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> wolf() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(20L, EntityDataSerializers.INT);
        result.put(21L, EntityDataSerializers.INT);
        result.put(22L, EntityDataSerializers.WOLF_VARIANT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.OPTIONAL_UUID);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> leashFenceKnotEntity() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> fallingBlockEntity() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.BLOCK_POS);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> turtle() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BLOCK_POS);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(20L, EntityDataSerializers.BLOCK_POS);
        result.put(21L, EntityDataSerializers.BOOLEAN);
        result.put(22L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> serverPlayer() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.FLOAT);
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BYTE);
        result.put(19L, EntityDataSerializers.COMPOUND_TAG);
        result.put(20L, EntityDataSerializers.COMPOUND_TAG);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> endermite() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> fox() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BYTE);
        result.put(19L, EntityDataSerializers.OPTIONAL_UUID);
        result.put(20L, EntityDataSerializers.OPTIONAL_UUID);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> zombieVillager() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(20L, EntityDataSerializers.VILLAGER_DATA);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> mushroomCow() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.STRING);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> boat() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.BOOLEAN);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        result.put(14L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> windCharge() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> allay() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> thrownEgg() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> witherBoss() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.INT);
        result.put(19L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> zoglin() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> donkey() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> bee() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> zombieHorse() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> cat() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(19L, EntityDataSerializers.CAT_VARIANT);
        result.put(20L, EntityDataSerializers.BOOLEAN);
        result.put(21L, EntityDataSerializers.BOOLEAN);
        result.put(22L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.OPTIONAL_UUID);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> cod() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> pufferfish() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> eyeOfEnder() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> areaEffectCloud() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.FLOAT);
        result.put(9L, EntityDataSerializers.BOOLEAN);
        result.put(10L, EntityDataSerializers.PARTICLE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> zombifiedPiglin() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> fishingHook() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> ominousItemSpawner() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> enderDragon() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> thrownTrident() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(10L, EntityDataSerializers.BYTE);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> bat() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> marker() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> wanderingTrader() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> blaze() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> spider() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> strider() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> shulkerBullet() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> witherSkeleton() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> piglinBrute() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> hoglin() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> slime() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> itemFrame() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(9L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> salmon() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> tropicalFish() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> dolphin() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BLOCK_POS);
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> interaction() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.FLOAT);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> llamaSpit() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> chicken() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> glowSquid() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecart() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartHopper() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> itemDisplay() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(23L, EntityDataSerializers.ITEM_STACK);
        result.put(24L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.INT);
        result.put(11L, EntityDataSerializers.VECTOR3);
        result.put(12L, EntityDataSerializers.VECTOR3);
        result.put(13L, EntityDataSerializers.QUATERNION);
        result.put(14L, EntityDataSerializers.QUATERNION);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.FLOAT);
        result.put(18L, EntityDataSerializers.FLOAT);
        result.put(19L, EntityDataSerializers.FLOAT);
        result.put(20L, EntityDataSerializers.FLOAT);
        result.put(21L, EntityDataSerializers.FLOAT);
        result.put(22L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> creeper() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> painting() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.PAINTING_VARIANT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> textDisplay() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(23L, EntityDataSerializers.COMPONENT);
        result.put(24L, EntityDataSerializers.INT);
        result.put(25L, EntityDataSerializers.INT);
        result.put(26L, EntityDataSerializers.BYTE);
        result.put(27L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.INT);
        result.put(11L, EntityDataSerializers.VECTOR3);
        result.put(12L, EntityDataSerializers.VECTOR3);
        result.put(13L, EntityDataSerializers.QUATERNION);
        result.put(14L, EntityDataSerializers.QUATERNION);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.INT);
        result.put(17L, EntityDataSerializers.FLOAT);
        result.put(18L, EntityDataSerializers.FLOAT);
        result.put(19L, EntityDataSerializers.FLOAT);
        result.put(20L, EntityDataSerializers.FLOAT);
        result.put(21L, EntityDataSerializers.FLOAT);
        result.put(22L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> drowned() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> snowball() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> rabbit() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> thrownExperienceBottle() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> traderLlama() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.INT);
        result.put(20L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> largeFireball() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> horse() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(18L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> ocelot() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> pillager() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartChest() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> witch() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> sniffer() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.SNIFFER_STATE);
        result.put(18L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> zombie() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> villager() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(18L, EntityDataSerializers.VILLAGER_DATA);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> camel() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.LONG);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> chestBoat() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.BOOLEAN);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        result.put(14L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> silverfish() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> snowGolem() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> evoker() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> giant() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> arrow() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(10L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> elderGuardian() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> shulker() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.DIRECTION);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> experienceOrb() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> piglin() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> primedTnt() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.BLOCK_STATE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> fireworkRocketEntity() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        result.put(9L, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        result.put(10L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> tadpole() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartFurnace() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(14L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> ghast() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> enderMan() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> smallFireball() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.ITEM_STACK);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> parrot() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(19L, EntityDataSerializers.INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        result.put(18L, EntityDataSerializers.OPTIONAL_UUID);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> polarBear() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> skeletonHorse() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> bogged() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> enderDragonPart() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> caveSpider() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> ironGolem() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> goat() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.BOOLEAN);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        result.put(19L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> witherSkull() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(8L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> frog() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(17L, EntityDataSerializers.FROG_VARIANT);
        result.put(18L, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> breeze() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> illusioner() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> minecartCommandBlock() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(14L, EntityDataSerializers.STRING);
        result.put(15L, EntityDataSerializers.COMPONENT);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.INT);
        result.put(9L, EntityDataSerializers.INT);
        result.put(10L, EntityDataSerializers.FLOAT);
        result.put(11L, EntityDataSerializers.INT);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> vex() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BYTE);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> armorStand() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.ROTATIONS);
        result.put(17L, EntityDataSerializers.ROTATIONS);
        result.put(18L, EntityDataSerializers.ROTATIONS);
        result.put(19L, EntityDataSerializers.ROTATIONS);
        result.put(20L, EntityDataSerializers.ROTATIONS);
        result.put(21L, EntityDataSerializers.ROTATIONS);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> ravager() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> skeleton() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> husk() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.BOOLEAN);
        result.put(17L, EntityDataSerializers.INT);
        result.put(18L, EntityDataSerializers.BOOLEAN);
        return Map.copyOf(result);
    }

    private static final Map<Long, EntityDataSerializer<?>> magmaCube() {
        Map<Long, EntityDataSerializer<?>> result = new HashMap<>();
        result.put(0L, EntityDataSerializers.BYTE);
        result.put(1L, EntityDataSerializers.INT);
        result.put(2L, EntityDataSerializers.OPTIONAL_COMPONENT);
        result.put(3L, EntityDataSerializers.BOOLEAN);
        result.put(4L, EntityDataSerializers.BOOLEAN);
        result.put(5L, EntityDataSerializers.BOOLEAN);
        result.put(6L, EntityDataSerializers.POSE);
        result.put(7L, EntityDataSerializers.INT);
        result.put(8L, EntityDataSerializers.BYTE);
        result.put(9L, EntityDataSerializers.FLOAT);
        result.put(10L, EntityDataSerializers.PARTICLES);
        result.put(11L, EntityDataSerializers.BOOLEAN);
        result.put(12L, EntityDataSerializers.INT);
        result.put(13L, EntityDataSerializers.INT);
        result.put(14L, EntityDataSerializers.OPTIONAL_BLOCK_POS);
        result.put(15L, EntityDataSerializers.BYTE);
        result.put(16L, EntityDataSerializers.INT);
        return Map.copyOf(result);
    }

    private static final Map<Class<? extends Entity>, Map<Long, EntityDataSerializer<?>>> initialize(
            ) {
        Map<Class<? extends Entity>, Map<Long, EntityDataSerializer<?>>> result = new HashMap<>();
        result.put(Cow.class, cow());
        result.put(MinecartTNT.class, minecartTNT());
        result.put(Panda.class, panda());
        result.put(Pig.class, pig());
        result.put(Vindicator.class, vindicator());
        result.put(ThrownEnderpearl.class, thrownEnderpearl());
        result.put(LightningBolt.class, lightningBolt());
        result.put(Warden.class, warden());
        result.put(SpectralArrow.class, spectralArrow());
        result.put(Llama.class, llama());
        result.put(Phantom.class, phantom());
        result.put(ThrownPotion.class, thrownPotion());
        result.put(Squid.class, squid());
        result.put(EvokerFangs.class, evokerFangs());
        result.put(Guardian.class, guardian());
        result.put(MinecartSpawner.class, minecartSpawner());
        result.put(Armadillo.class, armadillo());
        result.put(Stray.class, stray());
        result.put(Axolotl.class, axolotl());
        result.put(Mule.class, mule());
        result.put(ItemEntity.class, itemEntity());
        result.put(Display.BlockDisplay.class, blockDisplay());
        result.put(Sheep.class, sheep());
        result.put(BreezeWindCharge.class, breezeWindCharge());
        result.put(DragonFireball.class, dragonFireball());
        result.put(EndCrystal.class, endCrystal());
        result.put(GlowItemFrame.class, glowItemFrame());
        result.put(Wolf.class, wolf());
        result.put(LeashFenceKnotEntity.class, leashFenceKnotEntity());
        result.put(FallingBlockEntity.class, fallingBlockEntity());
        result.put(Turtle.class, turtle());
        result.put(ServerPlayer.class, serverPlayer());
        result.put(Endermite.class, endermite());
        result.put(Fox.class, fox());
        result.put(ZombieVillager.class, zombieVillager());
        result.put(MushroomCow.class, mushroomCow());
        result.put(Boat.class, boat());
        result.put(WindCharge.class, windCharge());
        result.put(Allay.class, allay());
        result.put(ThrownEgg.class, thrownEgg());
        result.put(WitherBoss.class, witherBoss());
        result.put(Zoglin.class, zoglin());
        result.put(Donkey.class, donkey());
        result.put(Bee.class, bee());
        result.put(ZombieHorse.class, zombieHorse());
        result.put(Cat.class, cat());
        result.put(Cod.class, cod());
        result.put(Pufferfish.class, pufferfish());
        result.put(EyeOfEnder.class, eyeOfEnder());
        result.put(AreaEffectCloud.class, areaEffectCloud());
        result.put(ZombifiedPiglin.class, zombifiedPiglin());
        result.put(FishingHook.class, fishingHook());
        result.put(OminousItemSpawner.class, ominousItemSpawner());
        result.put(EnderDragon.class, enderDragon());
        result.put(ThrownTrident.class, thrownTrident());
        result.put(Bat.class, bat());
        result.put(Marker.class, marker());
        result.put(WanderingTrader.class, wanderingTrader());
        result.put(Blaze.class, blaze());
        result.put(Spider.class, spider());
        result.put(Strider.class, strider());
        result.put(ShulkerBullet.class, shulkerBullet());
        result.put(WitherSkeleton.class, witherSkeleton());
        result.put(PiglinBrute.class, piglinBrute());
        result.put(Hoglin.class, hoglin());
        result.put(Slime.class, slime());
        result.put(ItemFrame.class, itemFrame());
        result.put(Salmon.class, salmon());
        result.put(TropicalFish.class, tropicalFish());
        result.put(Dolphin.class, dolphin());
        result.put(Interaction.class, interaction());
        result.put(LlamaSpit.class, llamaSpit());
        result.put(Chicken.class, chicken());
        result.put(GlowSquid.class, glowSquid());
        result.put(Minecart.class, minecart());
        result.put(MinecartHopper.class, minecartHopper());
        result.put(Display.ItemDisplay.class, itemDisplay());
        result.put(Creeper.class, creeper());
        result.put(Painting.class, painting());
        result.put(Display.TextDisplay.class, textDisplay());
        result.put(Drowned.class, drowned());
        result.put(Snowball.class, snowball());
        result.put(Rabbit.class, rabbit());
        result.put(ThrownExperienceBottle.class, thrownExperienceBottle());
        result.put(TraderLlama.class, traderLlama());
        result.put(LargeFireball.class, largeFireball());
        result.put(Horse.class, horse());
        result.put(Ocelot.class, ocelot());
        result.put(Pillager.class, pillager());
        result.put(MinecartChest.class, minecartChest());
        result.put(Witch.class, witch());
        result.put(Sniffer.class, sniffer());
        result.put(Zombie.class, zombie());
        result.put(Villager.class, villager());
        result.put(Camel.class, camel());
        result.put(ChestBoat.class, chestBoat());
        result.put(Silverfish.class, silverfish());
        result.put(SnowGolem.class, snowGolem());
        result.put(Evoker.class, evoker());
        result.put(Giant.class, giant());
        result.put(Arrow.class, arrow());
        result.put(ElderGuardian.class, elderGuardian());
        result.put(Shulker.class, shulker());
        result.put(ExperienceOrb.class, experienceOrb());
        result.put(Piglin.class, piglin());
        result.put(PrimedTnt.class, primedTnt());
        result.put(FireworkRocketEntity.class, fireworkRocketEntity());
        result.put(Tadpole.class, tadpole());
        result.put(MinecartFurnace.class, minecartFurnace());
        result.put(Ghast.class, ghast());
        result.put(EnderMan.class, enderMan());
        result.put(SmallFireball.class, smallFireball());
        result.put(Parrot.class, parrot());
        result.put(PolarBear.class, polarBear());
        result.put(SkeletonHorse.class, skeletonHorse());
        result.put(Bogged.class, bogged());
        result.put(EnderDragonPart.class, enderDragonPart());
        result.put(CaveSpider.class, caveSpider());
        result.put(IronGolem.class, ironGolem());
        result.put(Goat.class, goat());
        result.put(WitherSkull.class, witherSkull());
        result.put(Frog.class, frog());
        result.put(Breeze.class, breeze());
        result.put(Illusioner.class, illusioner());
        result.put(MinecartCommandBlock.class, minecartCommandBlock());
        result.put(Vex.class, vex());
        result.put(ArmorStand.class, armorStand());
        result.put(Ravager.class, ravager());
        result.put(Skeleton.class, skeleton());
        result.put(Husk.class, husk());
        result.put(MagmaCube.class, magmaCube());
        return Map.copyOf(result);
    }

    public static final boolean isValidForClass(Class<? extends Entity> clazz,
            EntityDataAccessor accessor) {
        Map<Long, EntityDataSerializer<?>> serializerMap = VALID_ENTITY_META_MAP.get(clazz);
        if(serializerMap == null) {
            return false;
        }
        var serializer = serializerMap.get(accessor.id());
        return serializer != null && serializer == accessor.serializer();
    }
}
