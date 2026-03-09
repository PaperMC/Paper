package io.papermc.paper.world.attribute;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import io.papermc.paper.registry.typed.PaperTypedDataCollector;
import io.papermc.paper.util.converter.Converter;
import io.papermc.paper.util.converter.Converters;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.TriState;
import net.minecraft.util.Util;
import net.minecraft.world.attribute.AttributeType;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;
import org.bukkit.Color;

import static io.papermc.paper.util.converter.Converter.direct;
import static io.papermc.paper.util.converter.Converters.sameOrder;

public final class PaperEnvironmentalAttributeType<A, M> extends HolderableBase<EnvironmentAttribute<M>> implements EnvironmentalAttributeType<A> {

    @SuppressWarnings("RedundantTypeArguments")
    private static final PaperTypedDataAdapters<EnvironmentAttribute<?>> ADAPTERS = PaperTypedDataAdapters.<EnvironmentAttribute<?>, PaperTypedDataCollector<EnvironmentAttribute<?>>>create(
        BuiltInRegistries.ENVIRONMENT_ATTRIBUTE,
        PaperTypedDataCollector::new,
        collector -> {
            final Converter<Integer, Color> intAsColor = direct(Color::fromARGB, Color::asARGB);
            final Converter<Integer, Color> intAsOpaqueColor = direct(
                color -> Color.fromRGB(color & 0x00FFFFFF), color -> ARGB.opaque(color.asRGB())
            );
            final Converter<TriState, net.kyori.adventure.util.TriState> triState = direct(PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            final Converter<MoonPhase, io.papermc.paper.world.MoonPhase> moonPhase = sameOrder(io.papermc.paper.world.MoonPhase.class, MoonPhase.class);

            final Map<AttributeType<?>, Converter<?, ?>> converters = Util.make(new IdentityHashMap<>(), map -> {
                map.put(AttributeTypes.ARGB_COLOR, intAsColor);
                map.put(AttributeTypes.RGB_COLOR, intAsOpaqueColor);
                map.put(AttributeTypes.TRI_STATE, triState);
                map.put(AttributeTypes.MOON_PHASE, moonPhase);
            });

            collector.dispatch(attribute -> {
                final Converter<?, ?> converter = converters.get(attribute.type());
                if (converter == null) {
                    throw new UnsupportedOperationException("Unknown attribute type: " + BuiltInRegistries.ATTRIBUTE_TYPE.getKey(attribute.type()));
                }
                return converter;
            }).add(
                EnvironmentAttributes.EYEBLOSSOM_OPEN,
                EnvironmentAttributes.MOON_PHASE,
                EnvironmentAttributes.CLOUD_COLOR,
                EnvironmentAttributes.FOG_COLOR,
                EnvironmentAttributes.SKY_COLOR,
                EnvironmentAttributes.SKY_LIGHT_COLOR,
                EnvironmentAttributes.SUNRISE_SUNSET_COLOR,
                EnvironmentAttributes.WATER_FOG_COLOR
                // EnvironmentAttributes.VILLAGER_ACTIVITY
                // EnvironmentAttributes.AMBIENT_PARTICLES
                // EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE
                // EnvironmentAttributes.AMBIENT_SOUNDS
                // EnvironmentAttributes.BACKGROUND_MUSIC
                // EnvironmentAttributes.BABY_VILLAGER_ACTIVITY
                // EnvironmentAttributes.BED_RULE
            );
            collector.dispatch(type -> Converter.identity(type.valueCodec())).add(
                EnvironmentAttributes.FIREFLY_BUSH_SOUNDS,
                EnvironmentAttributes.MUSIC_VOLUME,
                EnvironmentAttributes.BEES_STAY_IN_HIVE,
                EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN,
                EnvironmentAttributes.CAN_START_RAID,
                EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE,
                EnvironmentAttributes.CREAKING_ACTIVE,
                EnvironmentAttributes.FAST_LAVA,
                EnvironmentAttributes.INCREASED_FIRE_BURNOUT,
                EnvironmentAttributes.MONSTERS_BURN,
                EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS,
                EnvironmentAttributes.PIGLINS_ZOMBIFY,
                EnvironmentAttributes.RESPAWN_ANCHOR_WORKS,
                EnvironmentAttributes.SKY_LIGHT_LEVEL,
                EnvironmentAttributes.SNOW_GOLEM_MELTS,
                EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE,
                EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE,
                EnvironmentAttributes.WATER_EVAPORATES,
                EnvironmentAttributes.CLOUD_FOG_END_DISTANCE,
                EnvironmentAttributes.CLOUD_HEIGHT,
                EnvironmentAttributes.FOG_END_DISTANCE,
                EnvironmentAttributes.FOG_START_DISTANCE,
                EnvironmentAttributes.MOON_ANGLE,
                EnvironmentAttributes.SKY_FOG_END_DISTANCE,
                EnvironmentAttributes.SKY_LIGHT_FACTOR,
                EnvironmentAttributes.STAR_ANGLE,
                EnvironmentAttributes.STAR_BRIGHTNESS,
                EnvironmentAttributes.SUN_ANGLE,
                EnvironmentAttributes.WATER_FOG_END_DISTANCE,
                EnvironmentAttributes.WATER_FOG_START_DISTANCE
            );
        }
    );

    private final Converter<M, A> converter;

    private PaperEnvironmentalAttributeType(final Holder<EnvironmentAttribute<M>> holder, final Converter<M, A> converter) {
        super(holder);
        this.converter = converter;
    }

    @SuppressWarnings("unchecked")
    public static <M> EnvironmentalAttributeType<?> of(final Holder<?> holder) {
        final Holder.Reference<EnvironmentAttribute<M>> reference = (Holder.Reference<EnvironmentAttribute<M>>) holder;
        final Converter<M, ?> converter = PaperEnvironmentalAttributeType.ADAPTERS.get(reference.key());
        if (converter == Converters.unvalued()) {
             throw new IllegalStateException("Non-valued converter is not supported for environmental attribute type: " + reference.key());
        }
        return new PaperEnvironmentalAttributeType<>(reference, converter);
    }

    @Override
    public A getDefaultValue() {
        return this.getConverter().fromVanilla(this.getHandle().defaultValue());
    }

    public Converter<M, A> getConverter() {
        return this.converter;
    }
}
