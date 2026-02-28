package io.papermc.paper.world.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;

public final class PaperEnvironmentalAttributeType<API, NMS> extends HolderableBase<EnvironmentAttribute<NMS>> implements EnvironmentalAttributeType<API> {

    private static final PaperTypedDataAdapters<EnvironmentAttribute<?>> ADAPTERS = PaperTypedDataAdapters.create(
        BuiltInRegistries.ENVIRONMENT_ATTRIBUTE,
        PaperEnvironmentalAttributeTypeCollector::new,
        collector -> {
            // Audio
            // collector.register(EnvironmentAttributes.AMBIENT_SOUNDS, toApi, toNms);
            // collector.register(EnvironmentAttributes.BACKGROUND_MUSIC, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS);
            collector.registerIdentity(EnvironmentAttributes.MUSIC_VOLUME);

            // Gameplay
            // collector.register(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, toApi, toNms);
            // collector.register(EnvironmentAttributes.BED_RULE, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.BEES_STAY_IN_HIVE);
            collector.registerIdentity(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN);
            collector.registerIdentity(EnvironmentAttributes.CAN_START_RAID);
            collector.registerIdentity(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE);
            collector.registerIdentity(EnvironmentAttributes.CREAKING_ACTIVE);
            collector.register(EnvironmentAttributes.EYEBLOSSOM_OPEN, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.registerIdentity(EnvironmentAttributes.FAST_LAVA);
            collector.registerIdentity(EnvironmentAttributes.INCREASED_FIRE_BURNOUT);
            collector.registerIdentity(EnvironmentAttributes.MONSTERS_BURN);
            collector.registerIdentity(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS);
            collector.registerIdentity(EnvironmentAttributes.PIGLINS_ZOMBIFY);
            collector.registerIdentity(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS);
            collector.registerIdentity(EnvironmentAttributes.SKY_LIGHT_LEVEL);
            collector.registerIdentity(EnvironmentAttributes.SNOW_GOLEM_MELTS);
            collector.registerIdentity(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE);
            collector.registerIdentity(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE);
            // collector.register(EnvironmentAttributes.VILLAGER_ACTIVITY, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.WATER_EVAPORATES);

            // Visual
            // collector.register(EnvironmentAttributes.AMBIENT_PARTICLES, toApi, toNms);
            collector.registerIntAsColor(EnvironmentAttributes.CLOUD_COLOR);
            collector.registerIdentity(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE);
            collector.registerIdentity(EnvironmentAttributes.CLOUD_HEIGHT);
            // collector.register(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, toApi, toNms);
            collector.registerIntAsColor(EnvironmentAttributes.FOG_COLOR);
            collector.registerIdentity(EnvironmentAttributes.FOG_END_DISTANCE);
            collector.registerIdentity(EnvironmentAttributes.FOG_START_DISTANCE);
            collector.registerIdentity(EnvironmentAttributes.MOON_ANGLE);
            collector.register(EnvironmentAttributes.MOON_PHASE, moonPhase -> io.papermc.paper.world.MoonPhase.values()[moonPhase.ordinal()], moonPhase -> MoonPhase.values()[moonPhase.ordinal()]);
            collector.registerIntAsColor(EnvironmentAttributes.SKY_COLOR);
            collector.registerIdentity(EnvironmentAttributes.SKY_FOG_END_DISTANCE);
            collector.registerIntAsColor(EnvironmentAttributes.SKY_LIGHT_COLOR);
            collector.registerIdentity(EnvironmentAttributes.SKY_LIGHT_FACTOR);
            collector.registerIdentity(EnvironmentAttributes.STAR_ANGLE);
            collector.registerIdentity(EnvironmentAttributes.STAR_BRIGHTNESS);
            collector.registerIdentity(EnvironmentAttributes.SUN_ANGLE);
            collector.registerIntAsColor(EnvironmentAttributes.SUNRISE_SUNSET_COLOR);
            collector.registerIntAsColor(EnvironmentAttributes.WATER_FOG_COLOR);
            collector.registerIdentity(EnvironmentAttributes.WATER_FOG_END_DISTANCE);
            collector.registerIdentity(EnvironmentAttributes.WATER_FOG_START_DISTANCE);
        }
    );

    private final PaperTypedDataAdapter<API, NMS> adapter;

    private PaperEnvironmentalAttributeType(Holder<EnvironmentAttribute<NMS>> holder, PaperTypedDataAdapter<API, NMS> adapter) {
        super(holder);

        this.adapter = adapter;
    }

    @SuppressWarnings("unchecked")
    public static <NMS> EnvironmentalAttributeType<?> of(final Holder<?> holder) {
        final Holder.Reference<EnvironmentAttribute<NMS>> reference = (Holder.Reference<EnvironmentAttribute<NMS>>) holder;
        final PaperTypedDataAdapter<?, NMS> adapter = PaperEnvironmentalAttributeType.ADAPTERS.get(reference.key());
        return new PaperEnvironmentalAttributeType<>(reference, adapter);
    }

    @Override
    public API getDefaultValue() {
        return this.getAdapter().fromVanilla(this.getHandle().defaultValue());
    }

    public PaperTypedDataAdapter<API, NMS> getAdapter() {
        return this.adapter;
    }
}
