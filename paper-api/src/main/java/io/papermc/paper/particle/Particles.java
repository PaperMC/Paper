package io.papermc.paper.particle;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public final class Particles {

    public static final ParticleType.NonValued POOF = unvalued("poof");
    public static final ParticleType.NonValued EXPLOSION = unvalued("explosion");
    public static final ParticleType.NonValued EXPLOSION_EMITTER = unvalued("explosion_emitter");
    public static final ParticleType.NonValued FIREWORK = unvalued("firework");
    public static final ParticleType.NonValued BUBBLE = unvalued("bubble");
    public static final ParticleType.NonValued SPLASH = unvalued("splash");
    public static final ParticleType.NonValued FISHING = unvalued("fishing");
    public static final ParticleType.NonValued UNDERWATER = unvalued("underwater");
    public static final ParticleType.NonValued CRIT = unvalued("crit");
    public static final ParticleType.NonValued ENCHANTED_HIT = unvalued("enchanted_hit");
    public static final ParticleType.NonValued SMOKE = unvalued("smoke");
    public static final ParticleType.NonValued LARGE_SMOKE = unvalued("large_smoke");
    public static final ParticleType.Valued<Particle.Spell> EFFECT = valued("effect");
    public static final ParticleType.Valued<Particle.Spell> INSTANT_EFFECT = valued("instant_effect");
    public static final ParticleType.Valued<Color> ENTITY_EFFECT = valued("entity_effect");
    public static final ParticleType.NonValued WITCH = unvalued("witch");
    public static final ParticleType.NonValued DRIPPING_WATER = unvalued("dripping_water");
    public static final ParticleType.NonValued DRIPPING_LAVA = unvalued("dripping_lava");
    public static final ParticleType.NonValued ANGRY_VILLAGER = unvalued("angry_villager");
    public static final ParticleType.NonValued HAPPY_VILLAGER = unvalued("happy_villager");
    public static final ParticleType.NonValued MYCELIUM = unvalued("mycelium");
    public static final ParticleType.NonValued NOTE = unvalued("note");
    public static final ParticleType.NonValued PORTAL = unvalued("portal");
    public static final ParticleType.NonValued ENCHANT = unvalued("enchant");
    public static final ParticleType.NonValued FLAME = unvalued("flame");
    public static final ParticleType.NonValued LAVA = unvalued("lava");
    public static final ParticleType.NonValued CLOUD = unvalued("cloud");
    public static final ParticleType.Valued<Particle.DustOptions> DUST = valued("dust");
    public static final ParticleType.NonValued ITEM_SNOWBALL = unvalued("item_snowball");
    public static final ParticleType.NonValued ITEM_SLIME = unvalued("item_slime");
    public static final ParticleType.NonValued HEART = unvalued("heart");
    public static final ParticleType.Valued<ItemStack> ITEM = valued("item");
    public static final ParticleType.Valued<BlockData> BLOCK = valued("block");
    public static final ParticleType.NonValued RAIN = unvalued("rain");
    public static final ParticleType.NonValued ELDER_GUARDIAN = unvalued("elder_guardian");
    public static final ParticleType.Valued<Float> DRAGON_BREATH = valued("dragon_breath");
    public static final ParticleType.NonValued END_ROD = unvalued("end_rod");
    public static final ParticleType.NonValued DAMAGE_INDICATOR = unvalued("damage_indicator");
    public static final ParticleType.NonValued SWEEP_ATTACK = unvalued("sweep_attack");
    public static final ParticleType.Valued<BlockData> FALLING_DUST = valued("falling_dust");
    public static final ParticleType.NonValued TOTEM_OF_UNDYING = unvalued("totem_of_undying");
    public static final ParticleType.NonValued SPIT = unvalued("spit");
    public static final ParticleType.NonValued SQUID_INK = unvalued("squid_ink");
    public static final ParticleType.NonValued BUBBLE_POP = unvalued("bubble_pop");
    public static final ParticleType.NonValued CURRENT_DOWN = unvalued("current_down");
    public static final ParticleType.NonValued BUBBLE_COLUMN_UP = unvalued("bubble_column_up");
    public static final ParticleType.NonValued NAUTILUS = unvalued("nautilus");
    public static final ParticleType.NonValued DOLPHIN = unvalued("dolphin");
    public static final ParticleType.NonValued SNEEZE = unvalued("sneeze");
    public static final ParticleType.NonValued CAMPFIRE_COSY_SMOKE = unvalued("campfire_cosy_smoke");
    public static final ParticleType.NonValued CAMPFIRE_SIGNAL_SMOKE = unvalued("campfire_signal_smoke");
    public static final ParticleType.NonValued COMPOSTER = unvalued("composter");
    public static final ParticleType.Valued<Color> FLASH = valued("flash");
    public static final ParticleType.NonValued FALLING_LAVA = unvalued("falling_lava");
    public static final ParticleType.NonValued LANDING_LAVA = unvalued("landing_lava");
    public static final ParticleType.NonValued FALLING_WATER = unvalued("falling_water");
    public static final ParticleType.NonValued DRIPPING_HONEY = unvalued("dripping_honey");
    public static final ParticleType.NonValued FALLING_HONEY = unvalued("falling_honey");
    public static final ParticleType.NonValued LANDING_HONEY = unvalued("landing_honey");
    public static final ParticleType.NonValued FALLING_NECTAR = unvalued("falling_nectar");
    public static final ParticleType.NonValued SOUL_FIRE_FLAME = unvalued("soul_fire_flame");
    public static final ParticleType.NonValued ASH = unvalued("ash");
    public static final ParticleType.NonValued CRIMSON_SPORE = unvalued("crimson_spore");
    public static final ParticleType.NonValued WARPED_SPORE = unvalued("warped_spore");
    public static final ParticleType.NonValued SOUL = unvalued("soul");
    public static final ParticleType.NonValued DRIPPING_OBSIDIAN_TEAR = unvalued("dripping_obsidian_tear");
    public static final ParticleType.NonValued FALLING_OBSIDIAN_TEAR = unvalued("falling_obsidian_tear");
    public static final ParticleType.NonValued LANDING_OBSIDIAN_TEAR = unvalued("landing_obsidian_tear");
    public static final ParticleType.NonValued REVERSE_PORTAL = unvalued("reverse_portal");
    public static final ParticleType.NonValued WHITE_ASH = unvalued("white_ash");
    public static final ParticleType.Valued<Particle.DustTransition> DUST_COLOR_TRANSITION = valued("dust_color_transition");
    public static final ParticleType.Valued<Vibration> VIBRATION = valued("vibration");
    public static final ParticleType.NonValued FALLING_SPORE_BLOSSOM = unvalued("falling_spore_blossom");
    public static final ParticleType.NonValued SPORE_BLOSSOM_AIR = unvalued("spore_blossom_air");
    public static final ParticleType.NonValued SMALL_FLAME = unvalued("small_flame");
    public static final ParticleType.NonValued SNOWFLAKE = unvalued("snowflake");
    public static final ParticleType.NonValued DRIPPING_DRIPSTONE_LAVA = unvalued("dripping_dripstone_lava");
    public static final ParticleType.NonValued FALLING_DRIPSTONE_LAVA = unvalued("falling_dripstone_lava");
    public static final ParticleType.NonValued DRIPPING_DRIPSTONE_WATER = unvalued("dripping_dripstone_water");
    public static final ParticleType.NonValued FALLING_DRIPSTONE_WATER = unvalued("falling_dripstone_water");
    public static final ParticleType.NonValued GLOW_SQUID_INK = unvalued("glow_squid_ink");
    public static final ParticleType.NonValued GLOW = unvalued("glow");
    public static final ParticleType.NonValued WAX_ON = unvalued("wax_on");
    public static final ParticleType.NonValued WAX_OFF = unvalued("wax_off");
    public static final ParticleType.NonValued ELECTRIC_SPARK = unvalued("electric_spark");
    public static final ParticleType.NonValued SCRAPE = unvalued("scrape");
    public static final ParticleType.NonValued SONIC_BOOM = unvalued("sonic_boom");
    public static final ParticleType.NonValued SCULK_SOUL = unvalued("sculk_soul");
    public static final ParticleType.Valued<Float> SCULK_CHARGE = valued("sculk_charge");
    public static final ParticleType.NonValued SCULK_CHARGE_POP = unvalued("sculk_charge_pop");
    public static final ParticleType.Valued<Integer> SHRIEK = valued("shriek");
    public static final ParticleType.NonValued CHERRY_LEAVES = unvalued("cherry_leaves");
    public static final ParticleType.NonValued PALE_OAK_LEAVES = unvalued("pale_oak_leaves");
    public static final ParticleType.Valued<Color> TINTED_LEAVES = valued("tinted_leaves");
    public static final ParticleType.NonValued EGG_CRACK = unvalued("egg_crack");
    public static final ParticleType.NonValued DUST_PLUME = unvalued("dust_plume");
    public static final ParticleType.NonValued WHITE_SMOKE = unvalued("white_smoke");
    public static final ParticleType.NonValued GUST = unvalued("gust");
    public static final ParticleType.NonValued SMALL_GUST = unvalued("small_gust");
    public static final ParticleType.NonValued GUST_EMITTER_LARGE = unvalued("gust_emitter_large");
    public static final ParticleType.NonValued GUST_EMITTER_SMALL = unvalued("gust_emitter_small");
    public static final ParticleType.NonValued TRIAL_SPAWNER_DETECTION = unvalued("trial_spawner_detection");
    public static final ParticleType.NonValued TRIAL_SPAWNER_DETECTION_OMINOUS = unvalued("trial_spawner_detection_ominous");
    public static final ParticleType.NonValued VAULT_CONNECTION = unvalued("vault_connection");
    public static final ParticleType.NonValued INFESTED = unvalued("infested");
    public static final ParticleType.NonValued ITEM_COBWEB = unvalued("item_cobweb");
    public static final ParticleType.Valued<BlockData> DUST_PILLAR = valued("dust_pillar");
    public static final ParticleType.Valued<BlockData> BLOCK_CRUMBLE = valued("block_crumble");
    public static final ParticleType.NonValued FIREFLY = unvalued("firefly");
    public static final ParticleType.Valued<Particle.Trail> TRAIL = valued("trail");
    public static final ParticleType.NonValued OMINOUS_SPAWNING = unvalued("ominous_spawning");
    public static final ParticleType.NonValued RAID_OMEN = unvalued("raid_omen");
    public static final ParticleType.NonValued TRIAL_OMEN = unvalued("trial_omen");
    public static final ParticleType.Valued<BlockData> BLOCK_MARKER = valued("block_marker");
    public static final ParticleType.NonValued COPPER_FIRE_FLAME = unvalued("copper_fire_flame");

    private static ParticleType.NonValued unvalued(final @KeyPattern.Value String name) {
        final ParticleType memoryKey = Registry.PARTICLE_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (memoryKey instanceof ParticleType.NonValued) {
            return (ParticleType.NonValued) memoryKey;
        }
        throw new IllegalStateException(name + " is not a valid unvalued type, it is a " + memoryKey.getClass().getTypeName());
    }

    @SuppressWarnings("unchecked")
    private static <T> ParticleType.Valued<T> valued(final @KeyPattern.Value String name) {
        final ParticleType memoryKey = Registry.PARTICLE_TYPE.getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, name));
        if (memoryKey instanceof ParticleType.Valued) {
            return (ParticleType.Valued<T>) memoryKey;
        }
        throw new IllegalStateException(name + " is not a valid valued type, it is a " + memoryKey.getClass().getTypeName());
    }

    private Particles() {
    }
}
