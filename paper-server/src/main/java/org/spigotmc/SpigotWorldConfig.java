package org.spigotmc;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotWorldConfig {

    private final String worldName;
    private final YamlConfiguration config;
    private boolean verbose;

    public SpigotWorldConfig(String worldName) {
        this.worldName = worldName;
        this.config = SpigotConfig.config;
        this.init();
    }

    public void init() {
        this.verbose = this.getBoolean("verbose", false); // Paper

        this.log("-------- World Settings For [" + this.worldName + "] --------");
        SpigotConfig.readConfig(SpigotWorldConfig.class, this);
    }

    private void log(String s) {
        if (this.verbose) {
            Bukkit.getLogger().info(s);
        }
    }

    private void set(String path, Object val) {
        this.config.set("world-settings.default." + path, val);
    }

    public boolean getBoolean(String path, boolean def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getBoolean("world-settings." + this.worldName + "." + path, this.config.getBoolean("world-settings.default." + path));
    }

    public double getDouble(String path, double def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getDouble("world-settings." + this.worldName + "." + path, this.config.getDouble("world-settings.default." + path));
    }

    public int getInt(String path) {
        return this.config.getInt("world-settings." + this.worldName + "." + path);
    }

    public int getInt(String path, int def) {
        // Paper start - get int without setting default
        return this.getInt(path, def, true);
    }

    public int getInt(String path, int def, boolean setDef) {
        if (setDef) this.config.addDefault("world-settings.default." + path, def);
        return this.config.getInt("world-settings." + this.worldName + "." + path, this.config.getInt("world-settings.default." + path, def));
        // Paper end
    }

    public <T> List getList(String path, T def) {
        this.config.addDefault("world-settings.default." + path, def);
        return (List<T>) this.config.getList("world-settings." + this.worldName + "." + path, this.config.getList("world-settings.default." + path));
    }

    public String getString(String path, String def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.getString("world-settings." + this.worldName + "." + path, this.config.getString("world-settings.default." + path));
    }

    private Object get(String path, Object def) {
        this.config.addDefault("world-settings.default." + path, def);
        return this.config.get("world-settings." + this.worldName + "." + path, this.config.get("world-settings.default." + path));
    }

    // Crop growth rates
    public int cactusModifier;
    public int caneModifier;
    public int melonModifier;
    public int mushroomModifier;
    public int pumpkinModifier;
    public int saplingModifier;
    public int beetrootModifier;
    public int carrotModifier;
    public int potatoModifier;
    public int torchFlowerModifier;
    public int wheatModifier;
    public int wartModifier;
    public int vineModifier;
    public int cocoaModifier;
    public int bambooModifier;
    public int sweetBerryModifier;
    public int kelpModifier;
    public int twistingVinesModifier;
    public int weepingVinesModifier;
    public int caveVinesModifier;
    public int glowBerryModifier;
    public int pitcherPlantModifier;

    private int getAndValidateGrowth(String crop) {
        int modifier = this.getInt("growth." + crop.toLowerCase(java.util.Locale.ENGLISH) + "-modifier", 100);
        if (modifier == 0) {
            this.log("Cannot set " + crop + " growth to zero, defaulting to 100");
            modifier = 100;
        }
        this.log(crop + " Growth Modifier: " + modifier + "%");

        return modifier;
    }

    private void growthModifiers() {
        this.cactusModifier = this.getAndValidateGrowth("Cactus");
        this.caneModifier = this.getAndValidateGrowth("Cane");
        this.melonModifier = this.getAndValidateGrowth("Melon");
        this.mushroomModifier = this.getAndValidateGrowth("Mushroom");
        this.pumpkinModifier = this.getAndValidateGrowth("Pumpkin");
        this.saplingModifier = this.getAndValidateGrowth("Sapling");
        this.beetrootModifier = this.getAndValidateGrowth("Beetroot");
        this.carrotModifier = this.getAndValidateGrowth("Carrot");
        this.potatoModifier = this.getAndValidateGrowth("Potato");
        this.torchFlowerModifier = this.getAndValidateGrowth("TorchFlower");
        this.wheatModifier = this.getAndValidateGrowth("Wheat");
        this.wartModifier = this.getAndValidateGrowth("NetherWart");
        this.vineModifier = this.getAndValidateGrowth("Vine");
        this.cocoaModifier = this.getAndValidateGrowth("Cocoa");
        this.bambooModifier = this.getAndValidateGrowth("Bamboo");
        this.sweetBerryModifier = this.getAndValidateGrowth("SweetBerry");
        this.kelpModifier = this.getAndValidateGrowth("Kelp");
        this.twistingVinesModifier = this.getAndValidateGrowth("TwistingVines");
        this.weepingVinesModifier = this.getAndValidateGrowth("WeepingVines");
        this.caveVinesModifier = this.getAndValidateGrowth("CaveVines");
        this.glowBerryModifier = this.getAndValidateGrowth("GlowBerry");
        this.pitcherPlantModifier = this.getAndValidateGrowth("PitcherPlant");
    }

    public double itemMerge;
    private void itemMerge() {
        this.itemMerge = this.getDouble("merge-radius.item", 0.5);
        this.log("Item Merge Radius: " + this.itemMerge);
    }

    public double expMerge;
    private void expMerge() {
        this.expMerge = this.getDouble("merge-radius.exp", -1);
        this.log("Experience Merge Radius: " + this.expMerge);
    }

    public int viewDistance;
    private void viewDistance() {
        if (SpigotConfig.version < 12) {
            this.set("view-distance", null);
        }

        Object viewDistanceObject = this.get("view-distance", "default");
        this.viewDistance = (viewDistanceObject) instanceof Number ? ((Number) viewDistanceObject).intValue() : -1;
        if (this.viewDistance <= 0) {
            this.viewDistance = Bukkit.getViewDistance();
        }

        this.viewDistance = Math.max(Math.min(this.viewDistance, 32), 3);
        this.log("View Distance: " + this.viewDistance);
    }

    public int simulationDistance;
    private void simulationDistance() {
        Object simulationDistanceObject = this.get("simulation-distance", "default");
        this.simulationDistance = (simulationDistanceObject) instanceof Number ? ((Number) simulationDistanceObject).intValue() : -1;
        if (this.simulationDistance <= 0) {
            this.simulationDistance = Bukkit.getSimulationDistance();
        }

        this.log("Simulation Distance: " + this.simulationDistance);
    }

    public byte mobSpawnRange;
    private void mobSpawnRange() {
        this.mobSpawnRange = (byte) getInt("mob-spawn-range", 8); // Paper - Vanilla
        this.log("Mob Spawn Range: " + this.mobSpawnRange);
    }

    public int itemDespawnRate;
    private void itemDespawnRate() {
        this.itemDespawnRate = this.getInt("item-despawn-rate", 6000);
        this.log("Item Despawn Rate: " + this.itemDespawnRate);
    }

    public int animalActivationRange = 32;
    public int monsterActivationRange = 32;
    public int raiderActivationRange = 64;
    public int miscActivationRange = 16;
    public int flyingMonsterActivationRange = 32;
    public int waterActivationRange = 16;
    public int villagerActivationRange = 32;
    public int wakeUpInactiveAnimals = 4;
    public int wakeUpInactiveAnimalsEvery = 60 * 20;
    public int wakeUpInactiveAnimalsFor = 5 * 20;
    public int wakeUpInactiveMonsters = 8;
    public int wakeUpInactiveMonstersEvery = 20 * 20;
    public int wakeUpInactiveMonstersFor = 5 * 20;
    public int wakeUpInactiveVillagers = 4;
    public int wakeUpInactiveVillagersEvery = 30 * 20;
    public int wakeUpInactiveVillagersFor = 5 * 20;
    public int wakeUpInactiveFlying = 8;
    public int wakeUpInactiveFlyingEvery = 10 * 20;
    public int wakeUpInactiveFlyingFor = 5 * 20;
    public int villagersWorkImmunityAfter = 5 * 20;
    public int villagersWorkImmunityFor = 20;
    public boolean villagersActiveForPanic = true;
    public boolean tickInactiveVillagers = true;
    public boolean ignoreSpectatorActivation = false;

    private void activationRange() {
        boolean hasAnimalsConfig = config.getInt("entity-activation-range.animals", this.animalActivationRange) != this.animalActivationRange; // Paper
        this.animalActivationRange = this.getInt("entity-activation-range.animals", this.animalActivationRange);
        this.monsterActivationRange = this.getInt("entity-activation-range.monsters", this.monsterActivationRange);
        this.raiderActivationRange = this.getInt("entity-activation-range.raiders", this.raiderActivationRange);
        this.miscActivationRange = this.getInt("entity-activation-range.misc", this.miscActivationRange);
        this.waterActivationRange = this.getInt("entity-activation-range.water", this.waterActivationRange);
        this.villagerActivationRange = this.getInt("entity-activation-range.villagers", hasAnimalsConfig ? this.animalActivationRange : this.villagerActivationRange);
        this.flyingMonsterActivationRange = this.getInt("entity-activation-range.flying-monsters", this.flyingMonsterActivationRange);

        this.wakeUpInactiveAnimals = this.getInt("entity-activation-range.wake-up-inactive.animals-max-per-tick", this.wakeUpInactiveAnimals);
        this.wakeUpInactiveAnimalsEvery = this.getInt("entity-activation-range.wake-up-inactive.animals-every", this.wakeUpInactiveAnimalsEvery);
        this.wakeUpInactiveAnimalsFor = this.getInt("entity-activation-range.wake-up-inactive.animals-for", this.wakeUpInactiveAnimalsFor);

        this.wakeUpInactiveMonsters = this.getInt("entity-activation-range.wake-up-inactive.monsters-max-per-tick", this.wakeUpInactiveMonsters);
        this.wakeUpInactiveMonstersEvery = this.getInt("entity-activation-range.wake-up-inactive.monsters-every", this.wakeUpInactiveMonstersEvery);
        this.wakeUpInactiveMonstersFor = this.getInt("entity-activation-range.wake-up-inactive.monsters-for", this.wakeUpInactiveMonstersFor);

        this.wakeUpInactiveVillagers = this.getInt("entity-activation-range.wake-up-inactive.villagers-max-per-tick", this.wakeUpInactiveVillagers);
        this.wakeUpInactiveVillagersEvery = this.getInt("entity-activation-range.wake-up-inactive.villagers-every", this.wakeUpInactiveVillagersEvery);
        this.wakeUpInactiveVillagersFor = this.getInt("entity-activation-range.wake-up-inactive.villagers-for", this.wakeUpInactiveVillagersFor);

        this.wakeUpInactiveFlying = this.getInt("entity-activation-range.wake-up-inactive.flying-monsters-max-per-tick", this.wakeUpInactiveFlying);
        this.wakeUpInactiveFlyingEvery = this.getInt("entity-activation-range.wake-up-inactive.flying-monsters-every", this.wakeUpInactiveFlyingEvery);
        this.wakeUpInactiveFlyingFor = this.getInt("entity-activation-range.wake-up-inactive.flying-monsters-for", this.wakeUpInactiveFlyingFor);

        this.villagersWorkImmunityAfter = this.getInt("entity-activation-range.villagers-work-immunity-after", this.villagersWorkImmunityAfter);
        this.villagersWorkImmunityFor = this.getInt("entity-activation-range.villagers-work-immunity-for", this.villagersWorkImmunityFor);
        this.villagersActiveForPanic = this.getBoolean("entity-activation-range.villagers-active-for-panic", this.villagersActiveForPanic);
        this.tickInactiveVillagers = this.getBoolean("entity-activation-range.tick-inactive-villagers", this.tickInactiveVillagers);
        this.ignoreSpectatorActivation = this.getBoolean("entity-activation-range.ignore-spectators", this.ignoreSpectatorActivation);
        this.log("Entity Activation Range: An " + this.animalActivationRange + " / Mo " + this.monsterActivationRange + " / Ra " + this.raiderActivationRange + " / Mi " + this.miscActivationRange + " / Tiv " + this.tickInactiveVillagers + " / Isa " + this.ignoreSpectatorActivation);
    }

    public int playerTrackingRange = 128;
    public int animalTrackingRange = 96;
    public int monsterTrackingRange = 96;
    public int miscTrackingRange = 96;
    public int displayTrackingRange = 128;
    public int otherTrackingRange = 64;
    private void trackingRange() {
        this.playerTrackingRange = this.getInt("entity-tracking-range.players", this.playerTrackingRange);
        this.animalTrackingRange = this.getInt("entity-tracking-range.animals", this.animalTrackingRange);
        this.monsterTrackingRange = this.getInt("entity-tracking-range.monsters", this.monsterTrackingRange);
        this.miscTrackingRange = this.getInt("entity-tracking-range.misc", this.miscTrackingRange);
        this.displayTrackingRange = this.getInt("entity-tracking-range.display", this.displayTrackingRange);
        this.otherTrackingRange = this.getInt("entity-tracking-range.other", this.otherTrackingRange);
        this.log("Entity Tracking Range: Pl " + this.playerTrackingRange + " / An " + this.animalTrackingRange + " / Mo " + this.monsterTrackingRange + " / Mi " + this.miscTrackingRange + " / Di " + this.displayTrackingRange + " / Other " + this.otherTrackingRange);
    }

    public int hopperTransfer;
    public int hopperCheck;
    public int hopperAmount;
    public boolean hopperCanLoadChunks;
    private void hoppers() {
        // Set the tick delay between hopper item movements
        this.hopperTransfer = this.getInt("ticks-per.hopper-transfer", 8);
        if (SpigotConfig.version < 11) {
            this.set("ticks-per.hopper-check", 1);
        }
        this.hopperCheck = this.getInt("ticks-per.hopper-check", 1);
        this.hopperAmount = this.getInt("hopper-amount", 1);
        this.hopperCanLoadChunks = this.getBoolean("hopper-can-load-chunks", false);
        this.log("Hopper Transfer: " + this.hopperTransfer + " Hopper Check: " + this.hopperCheck + " Hopper Amount: " + this.hopperAmount + " Hopper Can Load Chunks: " + this.hopperCanLoadChunks);
    }

    public int arrowDespawnRate;
    public int tridentDespawnRate;
    private void arrowDespawnRate() {
        this.arrowDespawnRate = this.getInt("arrow-despawn-rate", 1200);
        this.tridentDespawnRate = this.getInt("trident-despawn-rate", this.arrowDespawnRate);
        this.log("Arrow Despawn Rate: " + this.arrowDespawnRate + " Trident Respawn Rate:" + this.tridentDespawnRate);
    }

    public boolean zombieAggressiveTowardsVillager;
    private void zombieAggressiveTowardsVillager() {
        this.zombieAggressiveTowardsVillager = this.getBoolean("zombie-aggressive-towards-villager", true);
        this.log("Zombie Aggressive Towards Villager: " + this.zombieAggressiveTowardsVillager);
    }

    public boolean nerfSpawnerMobs;
    private void nerfSpawnerMobs() {
        this.nerfSpawnerMobs = this.getBoolean("nerf-spawner-mobs", false);
        this.log("Nerfing mobs spawned from spawners: " + this.nerfSpawnerMobs);
    }

    public boolean enableZombiePigmenPortalSpawns;
    private void enableZombiePigmenPortalSpawns() {
        this.enableZombiePigmenPortalSpawns = this.getBoolean("enable-zombie-pigmen-portal-spawns", true);
        this.log("Allow Zombie Pigmen to spawn from portal blocks: " + this.enableZombiePigmenPortalSpawns);
    }

    public int dragonDeathSoundRadius;
    private void keepDragonDeathPerWorld() {
        this.dragonDeathSoundRadius = this.getInt("dragon-death-sound-radius", 0);
    }

    public int witherSpawnSoundRadius;
    private void witherSpawnSoundRadius() {
        this.witherSpawnSoundRadius = this.getInt("wither-spawn-sound-radius", 0);
    }

    public int endPortalSoundRadius;
    private void endPortalSoundRadius() {
        this.endPortalSoundRadius = this.getInt("end-portal-sound-radius", 0);
    }

    public int villageSeed;
    public int desertSeed;
    public int iglooSeed;
    public int jungleSeed;
    public int swampSeed;
    public int monumentSeed;
    public int oceanSeed;
    public int outpostSeed;
    public int shipwreckSeed;
    public int slimeSeed;
    public int endCitySeed;
    public int netherSeed;
    public int mansionSeed;
    public int fossilSeed;
    public int portalSeed;
    public int ancientCitySeed;
    public int trailRuinsSeed;
    public int trialChambersSeed;
    public int buriedTreasureSeed;
    public Integer mineshaftSeed;
    public Long strongholdSeed;

    private <N extends Number> N getSeed(String path, java.util.function.Function<String, N> toNumberFunc) {
        final String value = this.getString(path, "default");
        return org.apache.commons.lang3.math.NumberUtils.isParsable(value) ? toNumberFunc.apply(value) : null;
    }

    private void initWorldGenSeeds() {
        this.villageSeed = this.getInt("seed-village", 10387312);
        this.desertSeed = this.getInt("seed-desert", 14357617);
        this.iglooSeed = this.getInt("seed-igloo", 14357618);
        this.jungleSeed = this.getInt("seed-jungle", 14357619);
        this.swampSeed = this.getInt("seed-swamp", 14357620);
        this.monumentSeed = this.getInt("seed-monument", 10387313);
        this.shipwreckSeed = this.getInt("seed-shipwreck", 165745295);
        this.oceanSeed = this.getInt("seed-ocean", 14357621);
        this.outpostSeed = this.getInt("seed-outpost", 165745296);
        this.endCitySeed = this.getInt("seed-endcity", 10387313);
        this.slimeSeed = this.getInt("seed-slime", 987234911);
        this.netherSeed = this.getInt("seed-nether", 30084232);
        this.mansionSeed = this.getInt("seed-mansion", 10387319);
        this.fossilSeed = this.getInt("seed-fossil", 14357921);
        this.portalSeed = this.getInt("seed-portal", 34222645);
        this.ancientCitySeed = this.getInt("seed-ancientcity", 20083232);
        this.trailRuinsSeed = this.getInt("seed-trailruins", 83469867);
        this.trialChambersSeed = this.getInt("seed-trialchambers", 94251327);
        this.buriedTreasureSeed = this.getInt("seed-buriedtreasure", 10387320); // StructurePlacement#HIGHLY_ARBITRARY_RANDOM_SALT
        this.mineshaftSeed = this.getSeed("seed-mineshaft", Integer::parseInt);
        this.strongholdSeed = this.getSeed("seed-stronghold", Long::parseLong);
        this.log("Custom Map Seeds:  Village: " + this.villageSeed + " Desert: " + this.desertSeed + " Igloo: " + this.iglooSeed + " Jungle: " + this.jungleSeed + " Swamp: " + this.swampSeed + " Monument: " + this.monumentSeed
            + " Ocean: " + this.oceanSeed + " Shipwreck: " + this.shipwreckSeed + " End City: " + this.endCitySeed + " Slime: " + this.slimeSeed + " Nether: " + this.netherSeed + " Mansion: " + this.mansionSeed + " Fossil: " + this.fossilSeed + " Portal: " + this.portalSeed);
    }

    public float jumpWalkExhaustion;
    public float jumpSprintExhaustion;
    public float combatExhaustion;
    public float regenExhaustion;
    public float swimMultiplier;
    public float sprintMultiplier;
    public float otherMultiplier;
    private void initHunger() {
        if (SpigotConfig.version < 10) {
            this.set("hunger.walk-exhaustion", null);
            this.set("hunger.sprint-exhaustion", null);
            this.set("hunger.combat-exhaustion", 0.1);
            this.set("hunger.regen-exhaustion", 6.0);
        }

        this.jumpWalkExhaustion = (float) this.getDouble("hunger.jump-walk-exhaustion", 0.05);
        this.jumpSprintExhaustion = (float) this.getDouble("hunger.jump-sprint-exhaustion", 0.2);
        this.combatExhaustion = (float) this.getDouble("hunger.combat-exhaustion", 0.1);
        this.regenExhaustion = (float) this.getDouble("hunger.regen-exhaustion", 6.0);
        this.swimMultiplier = (float) this.getDouble("hunger.swim-multiplier", 0.01);
        this.sprintMultiplier = (float) this.getDouble("hunger.sprint-multiplier", 0.1);
        this.otherMultiplier = (float) this.getDouble("hunger.other-multiplier", 0.0);
    }

    public int currentPrimedTnt = 0;
    public int maxTntTicksPerTick;
    private void maxTntPerTick() {
        if (SpigotConfig.version < 7) {
            this.set("max-tnt-per-tick", 100);
        }
        this.maxTntTicksPerTick = this.getInt("max-tnt-per-tick", 100);
        this.log("Max TNT Explosions: " + this.maxTntTicksPerTick);
    }

    public int hangingTickFrequency;
    private void hangingTickFrequency() {
        this.hangingTickFrequency = this.getInt("hanging-tick-frequency", 100);
    }

    public int tileMaxTickTime;
    public int entityMaxTickTime;
    private void maxTickTimes() {
        this.tileMaxTickTime = this.getInt("max-tick-time.tile", 50);
        this.entityMaxTickTime = this.getInt("max-tick-time.entity", 50);
        this.log("Tile Max Tick Time: " + this.tileMaxTickTime + "ms Entity max Tick Time: " + this.entityMaxTickTime + "ms");
    }

    public int thunderChance;
    private void thunderChance() {
        this.thunderChance = this.getInt("thunder-chance", 100000);
    }

    public boolean belowZeroGenerationInExistingChunks;
    private void belowZeroGenerationInExistingChunks() {
        this.belowZeroGenerationInExistingChunks = this.getBoolean("below-zero-generation-in-existing-chunks", true);
    }

    public boolean unloadFrozenChunks;
    private void unloadFrozenChunks() {
        this.unloadFrozenChunks = this.getBoolean("unload-frozen-chunks", false);
    }
}
