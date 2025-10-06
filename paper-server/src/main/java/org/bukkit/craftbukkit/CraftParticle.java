package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.PowerParticleOption;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.core.particles.TrailParticleOption;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.PositionSource;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Vibration;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public abstract class CraftParticle<D> implements Keyed {

    private static final Registry<CraftParticle<?>> CRAFT_PARTICLE_REGISTRY = new CraftParticleRegistry(CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE));

    public static Particle minecraftToBukkit(net.minecraft.core.particles.ParticleType<?> minecraft) {
        Preconditions.checkArgument(minecraft != null);

        net.minecraft.core.Registry<net.minecraft.core.particles.ParticleType<?>> registry = CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE);
        Particle bukkit = Registry.PARTICLE_TYPE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.core.particles.ParticleType<?> bukkitToMinecraft(Particle bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    public static <D> ParticleOptions createParticleParam(Particle particle, D data) {
        Preconditions.checkArgument(particle != null, "particle cannot be null");

        data = CraftParticle.convertLegacy(data);
        if (particle.getDataType() != Void.class) {
            Preconditions.checkArgument(data != null, "missing required data %s", particle.getDataType());
        }
        if (data != null) {
            Preconditions.checkArgument(particle.getDataType().isInstance(data), "data (%s) should be %s", data.getClass(), particle.getDataType());
        }

        CraftParticle<D> craftParticle = (CraftParticle<D>) CraftParticle.CRAFT_PARTICLE_REGISTRY.get(particle.getKey());

        Preconditions.checkArgument(craftParticle != null);

        return craftParticle.createParticleParam(data);
    }

    public static <T> T convertLegacy(T object) {
        if (object instanceof MaterialData mat) {
            return (T) CraftBlockData.fromData(CraftMagicNumbers.getBlock(mat));
        }

        return object;
    }

    private final NamespacedKey key;
    private final net.minecraft.core.particles.ParticleType<?> particle;
    private final Class<D> clazz;

    public CraftParticle(NamespacedKey key, net.minecraft.core.particles.ParticleType<?> particle, Class<D> clazz) {
        this.key = key;
        this.particle = particle;
        this.clazz = clazz;
    }

    public net.minecraft.core.particles.ParticleType<?> getHandle() {
        return this.particle;
    }

    public abstract ParticleOptions createParticleParam(D data);

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    public static class CraftParticleRegistry extends CraftRegistry<CraftParticle<?>, net.minecraft.core.particles.ParticleType<?>> {

        private static final Map<NamespacedKey, BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>>> PARTICLE_MAP = new HashMap<>();

        private static final BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> VOID_FUNCTION = (name, particle) -> new CraftParticle<>(name, particle, Void.class) {
            @Override
            public ParticleOptions createParticleParam(Void data) {
                return (SimpleParticleType) this.getHandle();
            }
        };

        static {
            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> dustOptionsFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.DustOptions.class) {
                @Override
                public ParticleOptions createParticleParam(Particle.DustOptions data) {
                    Color color = data.getColor();
                    return new DustParticleOptions(color.asRGB(), data.getSize());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> itemStackFunction = (name, particle) -> new CraftParticle<>(name, particle, ItemStack.class) {
                @Override
                public ParticleOptions createParticleParam(ItemStack data) {
                    return new ItemParticleOption((net.minecraft.core.particles.ParticleType<ItemParticleOption>) this.getHandle(), CraftItemStack.asNMSCopy(data));
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> blockDataFunction = (name, particle) -> new CraftParticle<>(name, particle, BlockData.class) {
                @Override
                public ParticleOptions createParticleParam(BlockData data) {
                    return new BlockParticleOption((net.minecraft.core.particles.ParticleType<BlockParticleOption>) this.getHandle(), ((CraftBlockData) data).getState());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> dustTransitionFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.DustTransition.class) {
                @Override
                public ParticleOptions createParticleParam(Particle.DustTransition data) {
                    Color from = data.getColor();
                    Color to = data.getToColor();
                    return new DustColorTransitionOptions(from.asRGB(), to.asRGB(), data.getSize());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> vibrationFunction = (name, particle) -> new CraftParticle<>(name, particle, Vibration.class) {
                @Override
                public ParticleOptions createParticleParam(Vibration data) {
                    PositionSource source;
                    if (data.getDestination() instanceof Vibration.Destination.BlockDestination) {
                        Location destination = ((Vibration.Destination.BlockDestination) data.getDestination()).getLocation();
                        source = new BlockPositionSource(CraftLocation.toBlockPosition(destination));
                    } else if (data.getDestination() instanceof Vibration.Destination.EntityDestination) {
                        Entity destination = ((CraftEntity) ((Vibration.Destination.EntityDestination) data.getDestination()).getEntity()).getHandle();
                        source = new EntityPositionSource(destination, destination.getEyeHeight());
                    } else {
                        throw new IllegalArgumentException("Unknown vibration destination " + data.getDestination());
                    }

                    return new VibrationParticleOption(source, data.getArrivalTime());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> sculkFunction = (name, particle) -> new CraftParticle<>(name, particle, Float.class) {
                @Override
                public ParticleOptions createParticleParam(Float data) {
                    return new SculkChargeParticleOptions(data);
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> powerFunction = (name, particle) -> new CraftParticle<>(name, particle, Float.class) {
                @Override
                public ParticleOptions createParticleParam(Float data) {
                    return PowerParticleOption.create((net.minecraft.core.particles.ParticleType<PowerParticleOption>) particle, data);
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> integerFunction = (name, particle) -> new CraftParticle<>(name, particle, Integer.class) {
                @Override
                public ParticleOptions createParticleParam(Integer data) {
                    return new ShriekParticleOption(data);
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> colorFunction = (name, particle) -> new CraftParticle<>(name, particle, Color.class) {
                @Override
                public ParticleOptions createParticleParam(Color color) {
                    return ColorParticleOption.create((net.minecraft.core.particles.ParticleType<ColorParticleOption>) particle, color.asARGB());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> trailFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.Trail.class) {
                @Override
                public ParticleOptions createParticleParam(Particle.Trail data) {
                    return new TrailParticleOption(CraftLocation.toVec3(data.getTarget()), data.getColor().asRGB(), data.getDuration());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> spellFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.Spell.class) {
                @Override
                public ParticleOptions createParticleParam(Particle.Spell data) {
                    Color color = data.getColor();
                    float power = data.getPower();
                    return SpellParticleOption.create((net.minecraft.core.particles.ParticleType<SpellParticleOption>) particle, color.asARGB(), power);
                }
            };

            add("dust", dustOptionsFunction);
            add("item", itemStackFunction);
            add("block", blockDataFunction);
            add("falling_dust", blockDataFunction);
            add("dust_color_transition", dustTransitionFunction);
            add("vibration", vibrationFunction);
            add("sculk_charge", sculkFunction);
            add("dragon_breath", powerFunction);
            add("shriek", integerFunction);
            add("block_marker", blockDataFunction);
            add("entity_effect", colorFunction);
            add("tinted_leaves", colorFunction);
            add("flash", colorFunction);
            add("dust_pillar", blockDataFunction);
            add("block_crumble", blockDataFunction);
            add("trail", trailFunction);
            add("effect", spellFunction);
            add("instant_effect", spellFunction);
        }

        private static void add(String name, BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> function) {
            CraftParticleRegistry.PARTICLE_MAP.put(NamespacedKey.fromString(name), function);
        }

        public CraftParticleRegistry(net.minecraft.core.Registry<net.minecraft.core.particles.ParticleType<?>> minecraftRegistry) {
            super(CraftParticle.class, minecraftRegistry, CraftParticleRegistry::createBukkit, FieldRename.PARTICLE_TYPE_RENAME); // Paper - switch to Holder
        }

        public static CraftParticle<?> createBukkit(NamespacedKey namespacedKey, net.minecraft.core.particles.ParticleType<?> particle) { // Paper - idk why this is a separate implementation, just wrap the function
            if (particle == null) {
                return null;
            }

            BiFunction<NamespacedKey, net.minecraft.core.particles.ParticleType<?>, CraftParticle<?>> function = CraftParticleRegistry.PARTICLE_MAP.getOrDefault(namespacedKey, CraftParticleRegistry.VOID_FUNCTION);

            return function.apply(namespacedKey, particle);
        }
    }
}
