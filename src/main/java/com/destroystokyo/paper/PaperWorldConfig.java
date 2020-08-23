package com.destroystokyo.paper;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray.EngineMode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.SpigotWorldConfig;

import static com.destroystokyo.paper.PaperConfig.log;
import static com.destroystokyo.paper.PaperConfig.logError;

public class PaperWorldConfig {

    private final String worldName;
    private final SpigotWorldConfig spigotConfig;
    private final YamlConfiguration config;
    private boolean verbose;

    public PaperWorldConfig(String worldName, SpigotWorldConfig spigotConfig) {
        this.worldName = worldName;
        this.spigotConfig = spigotConfig;
        this.config = PaperConfig.config;
        init();
    }

    public void init() {
        log("-------- World Settings For [" + worldName + "] --------");
        PaperConfig.readConfig(PaperWorldConfig.class, this);
    }

    private void set(String path, Object val) {
        config.set("world-settings.default." + path, val);
        if (config.get("world-settings." + worldName + "." + path) != null) {
            config.set("world-settings." + worldName + "." + path, val);
        }
    }

    private boolean getBoolean(String path, boolean def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getBoolean("world-settings." + worldName + "." + path, config.getBoolean("world-settings.default." + path));
    }

    private double getDouble(String path, double def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getDouble("world-settings." + worldName + "." + path, config.getDouble("world-settings.default." + path));
    }

    private int getInt(String path, int def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getInt("world-settings." + worldName + "." + path, config.getInt("world-settings.default." + path));
    }

    private float getFloat(String path, float def) {
        // TODO: Figure out why getFloat() always returns the default value.
        return (float) getDouble(path, (double) def);
    }

    private <T> List<T> getList(String path, List<T> def) {
        config.addDefault("world-settings.default." + path, def);
        return (List<T>) config.getList("world-settings." + worldName + "." + path, config.getList("world-settings.default." + path));
    }

    private String getString(String path, String def) {
        config.addDefault("world-settings.default." + path, def);
        return config.getString("world-settings." + worldName + "." + path, config.getString("world-settings.default." + path));
    }

    public int cactusMaxHeight;
    public int reedMaxHeight;
    private void blockGrowthHeight() {
        cactusMaxHeight = getInt("max-growth-height.cactus", 3);
        reedMaxHeight = getInt("max-growth-height.reeds", 3);
        log("Max height for cactus growth " + cactusMaxHeight + ". Max height for reed growth " + reedMaxHeight);

    }

    public double babyZombieMovementModifier;
    private void babyZombieMovementModifier() {
        babyZombieMovementModifier = getDouble("baby-zombie-movement-modifier", 0.5D);
        if (PaperConfig.version < 20) {
            babyZombieMovementModifier = getDouble("baby-zombie-movement-speed", 0.5D);
            set("baby-zombie-movement-modifier", babyZombieMovementModifier);
        }

        log("Baby zombies will move at the speed of " + babyZombieMovementModifier);
    }

    public int fishingMinTicks;
    public int fishingMaxTicks;
    private void fishingTickRange() {
        fishingMinTicks = getInt("fishing-time-range.MinimumTicks", 100);
        fishingMaxTicks = getInt("fishing-time-range.MaximumTicks", 600);
        log("Fishing time ranges are between " + fishingMinTicks +" and " + fishingMaxTicks + " ticks");
    }

    public boolean nerfedMobsShouldJump;
    private void nerfedMobsShouldJump() {
        nerfedMobsShouldJump = getBoolean("spawner-nerfed-mobs-should-jump", false);
    }

    public int softDespawnDistance;
    public int hardDespawnDistance;
    private void despawnDistances() {
        softDespawnDistance = getInt("despawn-ranges.soft", 32); // 32^2 = 1024, Minecraft Default
        hardDespawnDistance = getInt("despawn-ranges.hard", 128); // 128^2 = 16384, Minecraft Default

        if (softDespawnDistance > hardDespawnDistance) {
            softDespawnDistance = hardDespawnDistance;
        }

        log("Living Entity Despawn Ranges:  Soft: " + softDespawnDistance + " Hard: " + hardDespawnDistance);

        softDespawnDistance = softDespawnDistance*softDespawnDistance;
        hardDespawnDistance = hardDespawnDistance*hardDespawnDistance;
    }

    public boolean keepSpawnInMemory;
    private void keepSpawnInMemory() {
        keepSpawnInMemory = getBoolean("keep-spawn-loaded", true);
        log("Keep spawn chunk loaded: " + keepSpawnInMemory);
    }

    public int fallingBlockHeightNerf;
    public int entityTNTHeightNerf;
    private void heightNerfs() {
        fallingBlockHeightNerf = getInt("falling-block-height-nerf", 0);
        entityTNTHeightNerf = getInt("tnt-entity-height-nerf", 0);

        if (fallingBlockHeightNerf != 0) log("Falling Block Height Limit set to Y: " + fallingBlockHeightNerf);
        if (entityTNTHeightNerf != 0) log("TNT Entity Height Limit set to Y: " + entityTNTHeightNerf);
    }

    public int netherVoidTopDamageHeight;
    public boolean doNetherTopVoidDamage() { return netherVoidTopDamageHeight > 0; }
    private void netherVoidTopDamageHeight() {
        netherVoidTopDamageHeight = getInt("nether-ceiling-void-damage-height", 0);
        log("Top of the nether void damage height: " + netherVoidTopDamageHeight);

        if (PaperConfig.version < 18) {
            boolean legacy = getBoolean("nether-ceiling-void-damage", false);
            if (legacy) {
                netherVoidTopDamageHeight = 128;
                set("nether-ceiling-void-damage-height", netherVoidTopDamageHeight);
            }
        }
    }

    public boolean disableEndCredits;
    private void disableEndCredits() {
        disableEndCredits = getBoolean("game-mechanics.disable-end-credits", false);
        log("End credits disabled: " + disableEndCredits);
    }

    public boolean optimizeExplosions;
    private void optimizeExplosions() {
        optimizeExplosions = getBoolean("optimize-explosions", false);
        log("Optimize explosions: " + optimizeExplosions);
    }

    public boolean disableExplosionKnockback;
    private void disableExplosionKnockback(){
        disableExplosionKnockback = getBoolean("disable-explosion-knockback", false);
    }

    public boolean disableThunder;
    private void disableThunder() {
        disableThunder = getBoolean("disable-thunder", false);
    }

    public boolean disableIceAndSnow;
    private void disableIceAndSnow(){
        disableIceAndSnow = getBoolean("disable-ice-and-snow", false);
    }

    public int mobSpawnerTickRate;
    private void mobSpawnerTickRate() {
        mobSpawnerTickRate = getInt("mob-spawner-tick-rate", 1);
    }

    public int containerUpdateTickRate;
    private void containerUpdateTickRate() {
        containerUpdateTickRate = getInt("container-update-tick-rate", 1);
    }

    public boolean disableChestCatDetection;
    private void disableChestCatDetection() {
        disableChestCatDetection = getBoolean("game-mechanics.disable-chest-cat-detection", false);
    }

    public boolean disablePlayerCrits;
    private void disablePlayerCrits() {
        disablePlayerCrits = getBoolean("game-mechanics.disable-player-crits", false);
    }

    public boolean allChunksAreSlimeChunks;
    private void allChunksAreSlimeChunks() {
        allChunksAreSlimeChunks = getBoolean("all-chunks-are-slime-chunks", false);
    }

    public int portalSearchRadius;
    public int portalCreateRadius;
    private void portalSearchRadius() {
        portalSearchRadius = getInt("portal-search-radius", 128);
        portalCreateRadius = getInt("portal-create-radius", 16);
    }

    public boolean disableTeleportationSuffocationCheck;
    private void disableTeleportationSuffocationCheck() {
        disableTeleportationSuffocationCheck = getBoolean("disable-teleportation-suffocation-check", false);
    }

    public boolean nonPlayerEntitiesOnScoreboards = false;
    private void nonPlayerEntitiesOnScoreboards() {
        nonPlayerEntitiesOnScoreboards = getBoolean("allow-non-player-entities-on-scoreboards", false);
    }

    public int nonPlayerArrowDespawnRate = -1;
    public int creativeArrowDespawnRate = -1;
    private void nonPlayerArrowDespawnRate() {
        nonPlayerArrowDespawnRate = getInt("non-player-arrow-despawn-rate", -1);
        if (nonPlayerArrowDespawnRate == -1) {
            nonPlayerArrowDespawnRate = spigotConfig.arrowDespawnRate;
        }
        creativeArrowDespawnRate = getInt("creative-arrow-despawn-rate", -1);
        if (creativeArrowDespawnRate == -1) {
            creativeArrowDespawnRate = spigotConfig.arrowDespawnRate;
        }
        log("Non Player Arrow Despawn Rate: " + nonPlayerArrowDespawnRate);
        log("Creative Arrow Despawn Rate: " + creativeArrowDespawnRate);
    }

    public double skeleHorseSpawnChance;
    private void skeleHorseSpawnChance() {
        skeleHorseSpawnChance = getDouble("skeleton-horse-thunder-spawn-chance", 0.01D);
        if (skeleHorseSpawnChance < 0) {
            skeleHorseSpawnChance = 0.01D; // Vanilla value
        }
    }

    public int fixedInhabitedTime;
    private void fixedInhabitedTime() {
        if (PaperConfig.version < 16) {
            if (!config.getBoolean("world-settings.default.use-chunk-inhabited-timer", true)) config.set("world-settings.default.fixed-chunk-inhabited-time", 0);
            if (!config.getBoolean("world-settings." + worldName + ".use-chunk-inhabited-timer", true)) config.set("world-settings." + worldName + ".fixed-chunk-inhabited-time", 0);
            set("use-chunk-inhabited-timer", null);
        }
        fixedInhabitedTime = getInt("fixed-chunk-inhabited-time", -1);
    }

    public int grassUpdateRate = 1;
    private void grassUpdateRate() {
        grassUpdateRate = Math.max(0, getInt("grass-spread-tick-rate", grassUpdateRate));
        log("Grass Spread Tick Rate: " + grassUpdateRate);
    }

    public boolean useVanillaScoreboardColoring;
    private void useVanillaScoreboardColoring() {
        useVanillaScoreboardColoring = getBoolean("use-vanilla-world-scoreboard-name-coloring", false);
    }

    public boolean frostedIceEnabled = true;
    public int frostedIceDelayMin = 20;
    public int frostedIceDelayMax = 40;
    private void frostedIce() {
        this.frostedIceEnabled = this.getBoolean("frosted-ice.enabled", this.frostedIceEnabled);
        this.frostedIceDelayMin = this.getInt("frosted-ice.delay.min", this.frostedIceDelayMin);
        this.frostedIceDelayMax = this.getInt("frosted-ice.delay.max", this.frostedIceDelayMax);
        log("Frosted Ice: " + (this.frostedIceEnabled ? "enabled" : "disabled") + " / delay: min=" + this.frostedIceDelayMin + ", max=" + this.frostedIceDelayMax);
    }

    public boolean autoReplenishLootables;
    public boolean restrictPlayerReloot;
    public boolean changeLootTableSeedOnFill;
    public int maxLootableRefills;
    public int lootableRegenMin;
    public int lootableRegenMax;
    private void enhancedLootables() {
        autoReplenishLootables = getBoolean("lootables.auto-replenish", false);
        restrictPlayerReloot = getBoolean("lootables.restrict-player-reloot", true);
        changeLootTableSeedOnFill = getBoolean("lootables.reset-seed-on-fill", true);
        maxLootableRefills = getInt("lootables.max-refills", -1);
        lootableRegenMin = PaperConfig.getSeconds(getString("lootables.refresh-min", "12h"));
        lootableRegenMax = PaperConfig.getSeconds(getString("lootables.refresh-max", "2d"));
        if (autoReplenishLootables) {
            log("Lootables: Replenishing every " +
                PaperConfig.timeSummary(lootableRegenMin) + " to " +
                PaperConfig.timeSummary(lootableRegenMax) +
                (restrictPlayerReloot ? " (restricting reloot)" : "")
            );
        }
    }

    public boolean preventTntFromMovingInWater;
    private void preventTntFromMovingInWater() {
        if (PaperConfig.version < 13) {
            boolean oldVal = getBoolean("enable-old-tnt-cannon-behaviors", false);
            set("prevent-tnt-from-moving-in-water", oldVal);
        }
        preventTntFromMovingInWater = getBoolean("prevent-tnt-from-moving-in-water", false);
        log("Prevent TNT from moving in water: " + preventTntFromMovingInWater);
    }

    public boolean removeCorruptTEs = false;
    private void removeCorruptTEs() {
        removeCorruptTEs = getBoolean("remove-corrupt-tile-entities", false);
    }

    public boolean filterNBTFromSpawnEgg = true;
    private void fitlerNBTFromSpawnEgg() {
        filterNBTFromSpawnEgg = getBoolean("filter-nbt-data-from-spawn-eggs-and-related", true);
        if (!filterNBTFromSpawnEgg) {
            Bukkit.getLogger().warning("Spawn Egg and Armor Stand NBT filtering disabled, this is a potential security risk");
        }
    }

    public boolean enableTreasureMaps = true;
    public boolean treasureMapsAlreadyDiscovered = false;
    private void treasureMapsAlreadyDiscovered() {
        enableTreasureMaps = getBoolean("enable-treasure-maps", true);
        treasureMapsAlreadyDiscovered = getBoolean("treasure-maps-return-already-discovered", false);
        if (treasureMapsAlreadyDiscovered) {
            log("Treasure Maps will return already discovered locations");
        }
    }

    public int maxCollisionsPerEntity;
    private void maxEntityCollision() {
        maxCollisionsPerEntity = getInt( "max-entity-collisions", this.spigotConfig.getInt("max-entity-collisions", 8) );
        log( "Max Entity Collisions: " + maxCollisionsPerEntity );
    }

    public boolean parrotsHangOnBetter;
    private void parrotsHangOnBetter() {
        parrotsHangOnBetter = getBoolean("parrots-are-unaffected-by-player-movement", false);
        log("Parrots are unaffected by player movement: " + parrotsHangOnBetter);
    }

    public boolean disableCreeperLingeringEffect;
    private void setDisableCreeperLingeringEffect() {
        disableCreeperLingeringEffect = getBoolean("disable-creeper-lingering-effect", false);
        log("Creeper lingering effect: " + disableCreeperLingeringEffect);
    }

    public int expMergeMaxValue;
    private void expMergeMaxValue() {
        expMergeMaxValue = getInt("experience-merge-max-value", -1);
        log("Experience Merge Max Value: " + expMergeMaxValue);
    }

    public double squidMaxSpawnHeight;
    private void squidMaxSpawnHeight() {
        squidMaxSpawnHeight = getDouble("squid-spawn-height.maximum", 0.0D);
    }

    public boolean disableSprintInterruptionOnAttack;
    private void disableSprintInterruptionOnAttack() {
        disableSprintInterruptionOnAttack = getBoolean("game-mechanics.disable-sprint-interruption-on-attack", false);
    }

    public boolean disableEnderpearlExploit = true;
    private void disableEnderpearlExploit() {
        disableEnderpearlExploit = getBoolean("game-mechanics.disable-unloaded-chunk-enderpearl-exploit", disableEnderpearlExploit);
        log("Disable Unloaded Chunk Enderpearl Exploit: " + (disableEnderpearlExploit ? "enabled" : "disabled"));
    }

    public int shieldBlockingDelay = 5;
    private void shieldBlockingDelay() {
        shieldBlockingDelay = getInt("game-mechanics.shield-blocking-delay", 5);
    }

    public boolean scanForLegacyEnderDragon = true;
    private void scanForLegacyEnderDragon() {
        scanForLegacyEnderDragon = getBoolean("game-mechanics.scan-for-legacy-ender-dragon", true);
    }

    public boolean ironGolemsCanSpawnInAir = false;
    private void ironGolemsCanSpawnInAir() {
        ironGolemsCanSpawnInAir = getBoolean("iron-golems-can-spawn-in-air", ironGolemsCanSpawnInAir);
    }

    public boolean armorStandEntityLookups = true;
    private void armorStandEntityLookups() {
        armorStandEntityLookups = getBoolean("armor-stands-do-collision-entity-lookups", true);
    }

    public boolean armorStandTick = true;
    private void armorStandTick() {
        this.armorStandTick = this.getBoolean("armor-stands-tick", this.armorStandTick);
        log("ArmorStand ticking is " + (this.armorStandTick ? "enabled" : "disabled") + " by default");
    }

    public int waterOverLavaFlowSpeed;
    private void waterOverLavaFlowSpeed() {
        waterOverLavaFlowSpeed = getInt("water-over-lava-flow-speed", 5);
        log("Water over lava flow speed: " + waterOverLavaFlowSpeed);
    }

    public boolean preventMovingIntoUnloadedChunks = false;
    private void preventMovingIntoUnloadedChunks() {
        preventMovingIntoUnloadedChunks = getBoolean("prevent-moving-into-unloaded-chunks", false);
    }

    public enum DuplicateUUIDMode {
        SAFE_REGEN, DELETE, NOTHING, WARN
    }
    public DuplicateUUIDMode duplicateUUIDMode = DuplicateUUIDMode.SAFE_REGEN;
    public int duplicateUUIDDeleteRange = 32;
    private void repairDuplicateUUID() {
        String desiredMode = getString("duplicate-uuid-resolver", "saferegen").toLowerCase().trim();
        duplicateUUIDDeleteRange = getInt("duplicate-uuid-saferegen-delete-range", duplicateUUIDDeleteRange);
        switch (desiredMode.toLowerCase()) {
            case "regen":
            case "regenerate":
            case "saferegen":
            case "saferegenerate":
                duplicateUUIDMode = DuplicateUUIDMode.SAFE_REGEN;
                log("Duplicate UUID Resolve: Regenerate New UUID if distant (Delete likely duplicates within " + duplicateUUIDDeleteRange + " blocks)");
                break;
            case "remove":
            case "delete":
                duplicateUUIDMode = DuplicateUUIDMode.DELETE;
                log("Duplicate UUID Resolve: Delete Entity");
                break;
            case "silent":
            case "nothing":
                duplicateUUIDMode = DuplicateUUIDMode.NOTHING;
                logError("Duplicate UUID Resolve: Do Nothing (no logs) - Warning, may lose indication of bad things happening");
                break;
            case "log":
            case "warn":
                duplicateUUIDMode = DuplicateUUIDMode.WARN;
                log("Duplicate UUID Resolve: Warn (do nothing but log it happened, may be spammy)");
                break;
            default:
                duplicateUUIDMode = DuplicateUUIDMode.WARN;
                logError("Warning: Invalid duplicate-uuid-resolver config " + desiredMode + " - must be one of: regen, delete, nothing, warn");
                log("Duplicate UUID Resolve: Warn (do nothing but log it happened, may be spammy)");
                break;
        }
    }

    public short keepLoadedRange;
    private void keepLoadedRange() {
        keepLoadedRange = (short) (getInt("keep-spawn-loaded-range", Math.min(spigotConfig.viewDistance, 10)) * 16);
        log( "Keep Spawn Loaded Range: " + (keepLoadedRange/16));
    }

    public int autoSavePeriod = -1;
    private void autoSavePeriod() {
        autoSavePeriod = getInt("auto-save-interval", -1);
        if (autoSavePeriod > 0) {
            log("Auto Save Interval: " +autoSavePeriod + " (" + (autoSavePeriod / 20) + "s)");
        } else if (autoSavePeriod < 0) {
            autoSavePeriod = net.minecraft.server.MinecraftServer.getServer().autosavePeriod;
        }
    }

    public int maxAutoSaveChunksPerTick = 24;
    private void maxAutoSaveChunksPerTick() {
        maxAutoSaveChunksPerTick = getInt("max-auto-save-chunks-per-tick", 24);
    }

    public boolean countAllMobsForSpawning = false;
    private void countAllMobsForSpawning() {
        countAllMobsForSpawning = getBoolean("count-all-mobs-for-spawning", false);
        if (countAllMobsForSpawning) {
            log("Counting all mobs for spawning. Mob farms may reduce natural spawns elsewhere in world.");
        } else {
            log("Using improved mob spawn limits (Only Natural Spawns impact spawn limits for more natural spawns)");
        }
    }

    public boolean antiXray;
    public EngineMode engineMode;
    public int maxChunkSectionIndex;
    public int updateRadius;
    public List<String> hiddenBlocks;
    public List<String> replacementBlocks;
    private void antiXray() {
        antiXray = getBoolean("anti-xray.enabled", false);
        engineMode = EngineMode.getById(getInt("anti-xray.engine-mode", EngineMode.HIDE.getId()));
        engineMode = engineMode == null ? EngineMode.HIDE : engineMode;
        maxChunkSectionIndex = getInt("anti-xray.max-chunk-section-index", 3);
        maxChunkSectionIndex = maxChunkSectionIndex > 15 ? 15 : maxChunkSectionIndex;
        updateRadius = getInt("anti-xray.update-radius", 2);
        hiddenBlocks = getList("anti-xray.hidden-blocks", Arrays.asList("gold_ore", "iron_ore", "coal_ore", "lapis_ore", "mossy_cobblestone", "obsidian", "chest", "diamond_ore", "redstone_ore", "clay", "emerald_ore", "ender_chest"));
        replacementBlocks = getList("anti-xray.replacement-blocks", Arrays.asList("stone", "oak_planks"));
        if (PaperConfig.version < 19) {
            hiddenBlocks.remove("lit_redstone_ore");
            int index = replacementBlocks.indexOf("planks");
            if (index != -1) {
                replacementBlocks.set(index, "oak_planks");
            }
            set("anti-xray.hidden-blocks", hiddenBlocks);
            set("anti-xray.replacement-blocks", replacementBlocks);
        }
        log("Anti-Xray: " + (antiXray ? "enabled" : "disabled") + " / Engine Mode: " + engineMode.getDescription() + " / Up to " + ((maxChunkSectionIndex + 1) * 16) + " blocks / Update Radius: " + updateRadius);
    }

    public boolean disableRelativeProjectileVelocity;
    private void disableRelativeProjectileVelocity() {
        disableRelativeProjectileVelocity = getBoolean("game-mechanics.disable-relative-projectile-velocity", false);
    }

    public boolean altItemDespawnRateEnabled;
    public Map<Material, Integer> altItemDespawnRateMap;
    private void altItemDespawnRate() {
        String path = "alt-item-despawn-rate";

        altItemDespawnRateEnabled = getBoolean(path + ".enabled", false);

        Map<Material, Integer> altItemDespawnRateMapDefault = new EnumMap<>(Material.class);
        altItemDespawnRateMapDefault.put(Material.COBBLESTONE, 300);
        for (Material key : altItemDespawnRateMapDefault.keySet()) {
            config.addDefault("world-settings.default." + path + ".items." + key, altItemDespawnRateMapDefault.get(key));
        }

        Map<String, Integer> rawMap = new HashMap<>();
        try {
            ConfigurationSection mapSection = config.getConfigurationSection("world-settings." + worldName + "." + path + ".items");
            if (mapSection == null) {
                mapSection = config.getConfigurationSection("world-settings.default." + path + ".items");
            }
            for (String key : mapSection.getKeys(false)) {
                int val = mapSection.getInt(key);
                rawMap.put(key, val);
            }
        }
        catch (Exception e) {
            logError("alt-item-despawn-rate was malformatted");
            altItemDespawnRateEnabled = false;
        }

        altItemDespawnRateMap = new EnumMap<>(Material.class);
        if (!altItemDespawnRateEnabled) {
            return;
        }

        for(String key : rawMap.keySet()) {
            try {
                altItemDespawnRateMap.put(Material.valueOf(key), rawMap.get(key));
            } catch (Exception e) {
                logError("Could not add item " + key + " to altItemDespawnRateMap: " + e.getMessage());
            }
        }
        if(altItemDespawnRateEnabled) {
            for(Material key : altItemDespawnRateMap.keySet()) {
                log("Alternative item despawn rate of " + key + ": " + altItemDespawnRateMap.get(key));
            }
        }
    }

    public boolean perPlayerMobSpawns = false;
    private void perPlayerMobSpawns() {
        perPlayerMobSpawns = getBoolean("per-player-mob-spawns", false);
    }

    public boolean generateFlatBedrock;
    private void generatorSettings() {
        generateFlatBedrock = getBoolean("generator-settings.flat-bedrock", false);
    }

    public boolean disablePillagerPatrols = false;
    public double patrolSpawnChance = 0.2;
    public boolean patrolPerPlayerDelay = false;
    public int patrolDelay = 12000;
    public boolean patrolPerPlayerStart = false;
    public int patrolStartDay = 5;
    private void pillagerSettings() {
        disablePillagerPatrols = getBoolean("game-mechanics.disable-pillager-patrols", disablePillagerPatrols);
        patrolSpawnChance = getDouble("game-mechanics.pillager-patrols.spawn-chance", patrolSpawnChance);
        patrolPerPlayerDelay = getBoolean("game-mechanics.pillager-patrols.spawn-delay.per-player", patrolPerPlayerDelay);
        patrolDelay = getInt("game-mechanics.pillager-patrols.spawn-delay.ticks", patrolDelay);
        patrolPerPlayerStart = getBoolean("game-mechanics.pillager-patrols.start.per-player", patrolPerPlayerStart);
        patrolStartDay = getInt("game-mechanics.pillager-patrols.start.day", patrolStartDay);
    }


    public boolean entitiesTargetWithFollowRange = false;
    private void entitiesTargetWithFollowRange() {
        entitiesTargetWithFollowRange = getBoolean("entities-target-with-follow-range", entitiesTargetWithFollowRange);
    }

    public boolean cooldownHopperWhenFull = true;
    public boolean disableHopperMoveEvents = false;
    private void hopperOptimizations() {
        cooldownHopperWhenFull = getBoolean("hopper.cooldown-when-full", cooldownHopperWhenFull);
        log("Cooldown Hoppers when Full: " + (cooldownHopperWhenFull ? "enabled" : "disabled"));
        disableHopperMoveEvents = getBoolean("hopper.disable-move-event", disableHopperMoveEvents);
        log("Hopper Move Item Events: " + (disableHopperMoveEvents ? "disabled" : "enabled"));
    }

    public boolean nerfNetherPortalPigmen = false;
    private void nerfNetherPortalPigmen() {
        nerfNetherPortalPigmen = getBoolean("game-mechanics.nerf-pigmen-from-nether-portals", nerfNetherPortalPigmen);
    }

    public double zombieVillagerInfectionChance = -1.0;
    private void zombieVillagerInfectionChance() {
        zombieVillagerInfectionChance = getDouble("zombie-villager-infection-chance", zombieVillagerInfectionChance);
    }

    public int lightQueueSize = 20;
    private void lightQueueSize() {
        lightQueueSize = getInt("light-queue-size", lightQueueSize);
    }

    public boolean phantomIgnoreCreative = true;
    public boolean phantomOnlyAttackInsomniacs = true;
    private void phantomSettings() {
        phantomIgnoreCreative = getBoolean("phantoms-do-not-spawn-on-creative-players", phantomIgnoreCreative);
        phantomOnlyAttackInsomniacs = getBoolean("phantoms-only-attack-insomniacs", phantomOnlyAttackInsomniacs);
    }

    public int noTickViewDistance;
    private void viewDistance() {
        this.noTickViewDistance = this.getInt("viewdistances.no-tick-view-distance", -1);
    }

    public long delayChunkUnloadsBy;
    private void delayChunkUnloadsBy() {
        delayChunkUnloadsBy = PaperConfig.getSeconds(getString("delay-chunk-unloads-by", "10s"));
        if (delayChunkUnloadsBy > 0) {
            log("Delaying chunk unloads by " + delayChunkUnloadsBy + " seconds");
            delayChunkUnloadsBy *= 20;
        }
    }

    public double sqrMaxThunderDistance;
    public double sqrMaxLightningImpactSoundDistance;
    public double maxLightningFlashDistance;
    private void lightningStrikeDistanceLimit() {
        sqrMaxThunderDistance = getInt("lightning-strike-distance-limit.sound", -1);
        if (sqrMaxThunderDistance > 0) {
            sqrMaxThunderDistance *= sqrMaxThunderDistance;
        }

        sqrMaxLightningImpactSoundDistance = getInt("lightning-strike-distance-limit.impact-sound", -1);
        if (sqrMaxLightningImpactSoundDistance < 0) {
            sqrMaxLightningImpactSoundDistance = 32 * 32; //Vanilla value
        } else {
            sqrMaxLightningImpactSoundDistance *= sqrMaxLightningImpactSoundDistance;
        }

        maxLightningFlashDistance = getInt("lightning-strike-distance-limit.flash", -1);
        if (maxLightningFlashDistance < 0) {
            maxLightningFlashDistance = 512; // Vanilla value
        }
    }

    public boolean zombiesTargetTurtleEggs = true;
    private void zombiesTargetTurtleEggs() {
        zombiesTargetTurtleEggs = getBoolean("zombies-target-turtle-eggs", zombiesTargetTurtleEggs);
    }
}
