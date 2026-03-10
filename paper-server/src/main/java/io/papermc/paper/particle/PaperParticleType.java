package io.papermc.paper.particle;

import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import io.papermc.paper.registry.typed.PaperTypedDataCollector;
import io.papermc.paper.registry.typed.TypedDataCollector;
import io.papermc.paper.util.converter.Converter;
import io.papermc.paper.util.converter.Converters;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.inventory.ItemStack;

import static io.papermc.paper.util.converter.Converter.direct;

public abstract class PaperParticleType<A, M extends ParticleOptions> extends HolderableBase<ParticleType<M>> implements io.papermc.paper.particle.ParticleType {

    private static <P, V> Function<P, V> unsupportedOperation() {
        final class Holder {
            private static final Function<?, ?> UNSUPPORTED_OPERATION = $ -> {
                throw new UnsupportedOperationException();
            };
        }

        //noinspection unchecked
        return (Function<P, V>) Holder.UNSUPPORTED_OPERATION; // todo we probably want to support this at some point in the future
    }

    // this is a hack around generics limitations to prevent
    // having to define generics on each register call as seen below, making collectors easier to read:
    //   collector.<T, A>register(...)
    private static <M extends ParticleOptions, A> void register(final TypedDataCollector<ParticleType<?>> collector, final ParticleType<M> type, Function<ParticleType<M>, Converter<M, A>> converter) {
        collector.register(type, converter.apply(type).serializable(type.codec().codec()));
    }

    @SuppressWarnings("RedundantTypeArguments")
    private static final PaperTypedDataAdapters<ParticleType<?>> ADAPTERS = PaperTypedDataAdapters.<ParticleType<?>, PaperTypedDataCollector<ParticleType<?>>>create(
        BuiltInRegistries.PARTICLE_TYPE,
        PaperTypedDataCollector::new,
        collector -> {
            collector.dispatch($ -> Converters.unvalued()).add(
                ParticleTypes.POOF,
                ParticleTypes.EXPLOSION,
                ParticleTypes.EXPLOSION_EMITTER,
                ParticleTypes.FIREWORK,
                ParticleTypes.BUBBLE,
                ParticleTypes.SPLASH,
                ParticleTypes.FISHING,
                ParticleTypes.UNDERWATER,
                ParticleTypes.CRIT,
                ParticleTypes.ENCHANTED_HIT,
                ParticleTypes.SMOKE,
                ParticleTypes.LARGE_SMOKE,
                ParticleTypes.WITCH,
                ParticleTypes.DRIPPING_WATER,
                ParticleTypes.DRIPPING_LAVA,
                ParticleTypes.ANGRY_VILLAGER,
                ParticleTypes.HAPPY_VILLAGER,
                ParticleTypes.MYCELIUM,
                ParticleTypes.NOTE,
                ParticleTypes.PORTAL,
                ParticleTypes.ENCHANT,
                ParticleTypes.FLAME,
                ParticleTypes.LAVA,
                ParticleTypes.CLOUD,
                ParticleTypes.ITEM_SNOWBALL,
                ParticleTypes.ITEM_SLIME,
                ParticleTypes.HEART,
                ParticleTypes.RAIN,
                ParticleTypes.ELDER_GUARDIAN,
                ParticleTypes.END_ROD,
                ParticleTypes.DAMAGE_INDICATOR,
                ParticleTypes.SWEEP_ATTACK,
                ParticleTypes.TOTEM_OF_UNDYING,
                ParticleTypes.SPIT,
                ParticleTypes.SQUID_INK,
                ParticleTypes.BUBBLE_POP,
                ParticleTypes.CURRENT_DOWN,
                ParticleTypes.BUBBLE_COLUMN_UP,
                ParticleTypes.NAUTILUS,
                ParticleTypes.DOLPHIN,
                ParticleTypes.SNEEZE,
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                ParticleTypes.COMPOSTER,
                ParticleTypes.FALLING_LAVA,
                ParticleTypes.LANDING_LAVA,
                ParticleTypes.FALLING_WATER,
                ParticleTypes.DRIPPING_HONEY,
                ParticleTypes.FALLING_HONEY,
                ParticleTypes.LANDING_HONEY,
                ParticleTypes.FALLING_NECTAR,
                ParticleTypes.SOUL_FIRE_FLAME,
                ParticleTypes.ASH,
                ParticleTypes.CRIMSON_SPORE,
                ParticleTypes.WARPED_SPORE,
                ParticleTypes.SOUL,
                ParticleTypes.DRIPPING_OBSIDIAN_TEAR,
                ParticleTypes.FALLING_OBSIDIAN_TEAR,
                ParticleTypes.LANDING_OBSIDIAN_TEAR,
                ParticleTypes.REVERSE_PORTAL,
                ParticleTypes.WHITE_ASH,
                ParticleTypes.FALLING_SPORE_BLOSSOM,
                ParticleTypes.SPORE_BLOSSOM_AIR,
                ParticleTypes.SMALL_FLAME,
                ParticleTypes.SNOWFLAKE,
                ParticleTypes.DRIPPING_DRIPSTONE_LAVA,
                ParticleTypes.FALLING_DRIPSTONE_LAVA,
                ParticleTypes.DRIPPING_DRIPSTONE_WATER,
                ParticleTypes.FALLING_DRIPSTONE_WATER,
                ParticleTypes.GLOW_SQUID_INK,
                ParticleTypes.GLOW,
                ParticleTypes.WAX_ON,
                ParticleTypes.WAX_OFF,
                ParticleTypes.ELECTRIC_SPARK,
                ParticleTypes.SCRAPE,
                ParticleTypes.SONIC_BOOM,
                ParticleTypes.SCULK_SOUL,
                ParticleTypes.SCULK_CHARGE_POP,
                ParticleTypes.CHERRY_LEAVES,
                ParticleTypes.PALE_OAK_LEAVES,
                ParticleTypes.EGG_CRACK,
                ParticleTypes.DUST_PLUME,
                ParticleTypes.WHITE_SMOKE,
                ParticleTypes.GUST,
                ParticleTypes.SMALL_GUST,
                ParticleTypes.GUST_EMITTER_LARGE,
                ParticleTypes.GUST_EMITTER_SMALL,
                ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS,
                ParticleTypes.VAULT_CONNECTION,
                ParticleTypes.INFESTED,
                ParticleTypes.ITEM_COBWEB,
                ParticleTypes.FIREFLY,
                ParticleTypes.OMINOUS_SPAWNING,
                ParticleTypes.RAID_OMEN,
                ParticleTypes.TRIAL_OMEN,
                ParticleTypes.COPPER_FIRE_FLAME
            );

            interface ConverterGetter<M extends ParticleOptions, A> {
                M forType(ParticleType<M> type, A value);

                default Converter<M, A> get(ParticleType<M> type) {
                    return direct(unsupportedOperation(), value -> this.forType(type, value));
                }
            }

            final ConverterGetter<ItemParticleOption, ItemStack> item = (type, value) -> {
                return new ItemParticleOption(type, CraftItemStack.asNMSCopy(value));
            };
            final ConverterGetter<BlockParticleOption, BlockData> block = (type, value) -> {
                return new BlockParticleOption(type, ((CraftBlockData) value).getState());
            };
            final ConverterGetter<PowerParticleOption, Float> power = PowerParticleOption::create;
            final ConverterGetter<SpellParticleOption, Particle.Spell> spell = (type, value) -> {
                return SpellParticleOption.create(type, value.getColor().asARGB(), value.getPower());
            };
            final ConverterGetter<ColorParticleOption, Color> color = (type, value) -> ColorParticleOption.create(type, value.asARGB());
            final ConverterGetter<DustParticleOptions, Particle.DustOptions> dust = (type, value) -> {
                return new DustParticleOptions(value.getColor().asRGB(), value.getSize());
            };
            final ConverterGetter<DustColorTransitionOptions, Particle.DustTransition> dustTransition = (type, value) -> {
                return new DustColorTransitionOptions(value.getColor().asRGB(), value.getToColor().asRGB(), value.getSize());
            };
            final ConverterGetter<VibrationParticleOption, Vibration> vibration = (type, value) -> {
                final PositionSource source;
                if (value.getDestination() instanceof Vibration.Destination.BlockDestination blockDestination) {
                    Location destination = blockDestination.getLocation();
                    source = new BlockPositionSource(CraftLocation.toBlockPosition(destination));
                } else if (value.getDestination() instanceof Vibration.Destination.EntityDestination entityDestination) {
                    Entity destination = ((CraftEntity) entityDestination.getEntity()).getHandle();
                    source = new EntityPositionSource(destination, destination.getEyeHeight());
                } else {
                    throw new IllegalStateException("Unknown vibration destination " + value.getDestination());
                }

                return new VibrationParticleOption(source, value.getArrivalTime());
            };
            final ConverterGetter<SculkChargeParticleOptions, Float> sculkCharge = (type, value) -> new SculkChargeParticleOptions(value);
            final ConverterGetter<ShriekParticleOption, Integer> shriek = (type, value) -> new ShriekParticleOption(value);
            final ConverterGetter<TrailParticleOption, Particle.Trail> trail = (type, value) -> {
                return new TrailParticleOption(CraftLocation.toVec3(value.getTarget()), value.getColor().asRGB(), value.getDuration());
            };

            register(collector, ParticleTypes.EFFECT, spell::get);
            register(collector, ParticleTypes.INSTANT_EFFECT, spell::get);
            register(collector, ParticleTypes.ENTITY_EFFECT, color::get);
            register(collector, ParticleTypes.DUST, dust::get);
            register(collector, ParticleTypes.ITEM, item::get);
            register(collector, ParticleTypes.BLOCK, block::get);
            register(collector, ParticleTypes.DRAGON_BREATH, power::get);
            register(collector, ParticleTypes.FALLING_DUST, block::get);
            register(collector, ParticleTypes.FLASH, color::get);
            register(collector, ParticleTypes.DUST_COLOR_TRANSITION, dustTransition::get);
            register(collector, ParticleTypes.VIBRATION, vibration::get);
            register(collector, ParticleTypes.SCULK_CHARGE, sculkCharge::get);
            register(collector, ParticleTypes.SHRIEK, shriek::get);
            register(collector, ParticleTypes.TINTED_LEAVES, color::get);
            register(collector, ParticleTypes.DUST_PILLAR, block::get);
            register(collector, ParticleTypes.BLOCK_CRUMBLE, block::get);
            register(collector, ParticleTypes.TRAIL, trail::get);
            register(collector, ParticleTypes.BLOCK_MARKER, block::get);
        }
    );

    public static ParticleType<? extends ParticleOptions> bukkitToMinecraft(final io.papermc.paper.particle.ParticleType type) {
        return CraftRegistry.bukkitToMinecraft(type);
    }

    public static io.papermc.paper.particle.ParticleType minecraftToBukkit(final ParticleType<?> type) {
        return CraftRegistry.minecraftToBukkit(type, Registries.PARTICLE_TYPE);
    }

    private final Converter<M, A> converter;

    private PaperParticleType(final Holder<ParticleType<M>> holder, final Converter<M, A> converter) {
        super(holder);
        this.converter = converter;
    }

    public Converter<M, A> getConverter() {
        return this.converter;
    }

    @SuppressWarnings("unchecked")
    public static <M extends ParticleOptions> io.papermc.paper.particle.ParticleType of(final Holder<?> holder) {
        final Holder.Reference<ParticleType<M>> reference = (Holder.Reference<ParticleType<M>>) holder;
        final Converter<M, ?> converter = PaperParticleType.ADAPTERS.get(reference.key());
        if (converter.equals(Converters.unvalued())) {
            return new NonValuedImpl<>(reference, converter);
        } else {
            return new ValuedImpl<>(reference, converter);
        }
    }

    public static final class NonValuedImpl<A, M extends ParticleOptions> extends PaperParticleType<A, M> implements NonValued {

        NonValuedImpl(
            final Holder<ParticleType<M>> holder,
            final Converter<M, A> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class ValuedImpl<A, M extends ParticleOptions> extends PaperParticleType<A, M> implements Valued<A> {

        ValuedImpl(
            final Holder<ParticleType<M>> holder,
            final Converter<M, A> adapter
        ) {
            super(holder, adapter);
        }
    }
}
