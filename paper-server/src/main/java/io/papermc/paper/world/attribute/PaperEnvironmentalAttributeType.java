package io.papermc.paper.world.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapter;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapters;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;

public final class PaperEnvironmentalAttributeType<API, NMS> extends HolderableBase<EnvironmentAttribute<NMS>> implements EnvironmentalAttributeType<API> {

    private static final PaperTypedDataAdapters ADAPTERS = PaperTypedDataAdapters.create(
        BuiltInRegistries.ENVIRONMENT_ATTRIBUTE,
        PaperEnvironmentalAttributeTypeCollector::new,
        collector -> {
            // Audio
            // collector.register(EnvironmentAttributes.AMBIENT_SOUNDS, toApi, toNms);
            // collector.register(EnvironmentAttributes.BACKGROUND_MUSIC, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.MUSIC_VOLUME, EnvironmentAttribute::valueCodec);

            // Gameplay
            // collector.register(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, toApi, toNms);
            // collector.register(EnvironmentAttributes.BED_RULE, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.BEES_STAY_IN_HIVE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CAN_START_RAID, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CREAKING_ACTIVE, EnvironmentAttribute::valueCodec);
            collector.register(EnvironmentAttributes.EYEBLOSSOM_OPEN, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.registerIdentity(EnvironmentAttributes.FAST_LAVA, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.MONSTERS_BURN, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.PIGLINS_ZOMBIFY, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SKY_LIGHT_LEVEL, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SNOW_GOLEM_MELTS, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, EnvironmentAttribute::valueCodec);
            // collector.register(EnvironmentAttributes.VILLAGER_ACTIVITY, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.WATER_EVAPORATES, EnvironmentAttribute::valueCodec);

            // Visual
            // collector.register(EnvironmentAttributes.AMBIENT_PARTICLES, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.CLOUD_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.CLOUD_HEIGHT, EnvironmentAttribute::valueCodec);
            // collector.register(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, toApi, toNms);
            collector.registerIdentity(EnvironmentAttributes.FOG_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.FOG_END_DISTANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.FOG_START_DISTANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.MOON_ANGLE, EnvironmentAttribute::valueCodec);
            collector.register(EnvironmentAttributes.MOON_PHASE, moonPhase -> io.papermc.paper.world.MoonPhase.values()[moonPhase.ordinal()], moonPhase -> MoonPhase.values()[moonPhase.ordinal()]);
            collector.registerIdentity(EnvironmentAttributes.SKY_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SKY_FOG_END_DISTANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SKY_LIGHT_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SKY_LIGHT_FACTOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.STAR_ANGLE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.STAR_BRIGHTNESS, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SUN_ANGLE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.WATER_FOG_COLOR, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.WATER_FOG_END_DISTANCE, EnvironmentAttribute::valueCodec);
            collector.registerIdentity(EnvironmentAttributes.WATER_FOG_START_DISTANCE, EnvironmentAttribute::valueCodec);
        }
    );

    private final PaperTypedDataAdapter<API, NMS> adapter;

    private PaperEnvironmentalAttributeType(Holder<EnvironmentAttribute<NMS>> holder, PaperTypedDataAdapter<API, NMS> adapter) {
        super(holder);

        this.adapter = adapter;
    }

    @SuppressWarnings("unchecked")
    public static <NMS> EnvironmentalAttributeType<?> of(final Holder<?> holder) {
        final PaperTypedDataAdapter<?, NMS> adapter = PaperEnvironmentalAttributeType.ADAPTERS.getAdapter(holder.unwrapKey().orElseThrow());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for " + holder);
        }
        if (!adapter.isValued()) {
            throw new IllegalStateException("Non-valued adapter is not supported for environmental attribute types: " + holder);
        }
        return new PaperEnvironmentalAttributeType<>((Holder<EnvironmentAttribute<NMS>>) holder, adapter);
    }

    @Override
    public API getDefaultValue() {
        return this.getAdapter().fromVanilla(this.getHandle().defaultValue());
    }

    public PaperTypedDataAdapter<API, NMS> getAdapter() {
        return this.adapter;
    }
}
