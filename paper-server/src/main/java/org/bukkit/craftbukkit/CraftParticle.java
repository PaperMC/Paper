package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import net.minecraft.core.IRegistry;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.core.particles.ParticleParamBlock;
import net.minecraft.core.particles.ParticleParamItem;
import net.minecraft.core.particles.ParticleParamRedstone;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
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
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.joml.Vector3f;

public abstract class CraftParticle<D> implements Keyed {

    private static final Registry<CraftParticle<?>> CRAFT_PARTICLE_REGISTRY = new CraftParticleRegistry(CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE));

    public static Particle minecraftToBukkit(net.minecraft.core.particles.Particle<?> minecraft) {
        Preconditions.checkArgument(minecraft != null);

        IRegistry<net.minecraft.core.particles.Particle<?>> registry = CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE);
        Particle bukkit = Registry.PARTICLE_TYPE.get(CraftNamespacedKey.fromMinecraft(registry.getResourceKey(minecraft).orElseThrow().location()));

        Preconditions.checkArgument(bukkit != null);

        return bukkit;
    }

    public static net.minecraft.core.particles.Particle<?> bukkitToMinecraft(Particle bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return CraftRegistry.getMinecraftRegistry(Registries.PARTICLE_TYPE)
                .getOptional(CraftNamespacedKey.toMinecraft(bukkit.getKey())).orElseThrow();
    }

    public static <D> ParticleParam createParticleParam(Particle particle, D data) {
        Preconditions.checkArgument(particle != null);

        CraftParticle<D> craftParticle = (CraftParticle<D>) CRAFT_PARTICLE_REGISTRY.get(particle.getKey());

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
    private final net.minecraft.core.particles.Particle<?> particle;
    private final Class<D> clazz;

    public CraftParticle(NamespacedKey key, net.minecraft.core.particles.Particle<?> particle, Class<D> clazz) {
        this.key = key;
        this.particle = particle;
        this.clazz = clazz;
    }

    public net.minecraft.core.particles.Particle<?> getHandle() {
        return particle;
    }

    public abstract ParticleParam createParticleParam(D data);

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public static class CraftParticleRegistry extends CraftRegistry<CraftParticle<?>, net.minecraft.core.particles.Particle<?>> {

        private static final Map<NamespacedKey, BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>>> PARTICLE_MAP = new HashMap<>();

        private static final BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> VOID_FUNCTION = (name, particle) -> new CraftParticle<>(name, particle, Void.class) {
            @Override
            public ParticleParam createParticleParam(Void data) {
                return (ParticleType) getHandle();
            }
        };

        static {
            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> dustOptionsFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.DustOptions.class) {
                @Override
                public ParticleParam createParticleParam(Particle.DustOptions data) {
                    Color color = data.getColor();
                    return new ParticleParamRedstone(new Vector3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f), data.getSize());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> itemStackFunction = (name, particle) -> new CraftParticle<>(name, particle, ItemStack.class) {
                @Override
                public ParticleParam createParticleParam(ItemStack data) {
                    return new ParticleParamItem((net.minecraft.core.particles.Particle<ParticleParamItem>) getHandle(), CraftItemStack.asNMSCopy(data));
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> blockDataFunction = (name, particle) -> new CraftParticle<>(name, particle, BlockData.class) {
                @Override
                public ParticleParam createParticleParam(BlockData data) {
                    return new ParticleParamBlock((net.minecraft.core.particles.Particle<ParticleParamBlock>) getHandle(), ((CraftBlockData) data).getState());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> dustTransitionFunction = (name, particle) -> new CraftParticle<>(name, particle, Particle.DustTransition.class) {
                @Override
                public ParticleParam createParticleParam(Particle.DustTransition data) {
                    Color from = data.getColor();
                    Color to = data.getToColor();
                    return new DustColorTransitionOptions(new Vector3f(from.getRed() / 255.0f, from.getGreen() / 255.0f, from.getBlue() / 255.0f), new Vector3f(to.getRed() / 255.0f, to.getGreen() / 255.0f, to.getBlue() / 255.0f), data.getSize());
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> vibrationFunction = (name, particle) -> new CraftParticle<>(name, particle, Vibration.class) {
                @Override
                public ParticleParam createParticleParam(Vibration data) {
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

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> floatFunction = (name, particle) -> new CraftParticle<>(name, particle, Float.class) {
                @Override
                public ParticleParam createParticleParam(Float data) {
                    return new SculkChargeParticleOptions(data);
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> integerFunction = (name, particle) -> new CraftParticle<>(name, particle, Integer.class) {
                @Override
                public ParticleParam createParticleParam(Integer data) {
                    return new ShriekParticleOption(data);
                }
            };

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> colorFunction = (name, particle) -> new CraftParticle<>(name, particle, Color.class) {
                @Override
                public ParticleParam createParticleParam(Color color) {
                    return ColorParticleOption.create((net.minecraft.core.particles.Particle<ColorParticleOption>) particle, color.asARGB());
                }
            };

            add("dust", dustOptionsFunction);
            add("item", itemStackFunction);
            add("block", blockDataFunction);
            add("falling_dust", blockDataFunction);
            add("dust_color_transition", dustTransitionFunction);
            add("vibration", vibrationFunction);
            add("sculk_charge", floatFunction);
            add("shriek", integerFunction);
            add("block_marker", blockDataFunction);
            add("entity_effect", colorFunction);
            add("dust_pillar", blockDataFunction);
        }

        private static void add(String name, BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> function) {
            PARTICLE_MAP.put(NamespacedKey.fromString(name), function);
        }

        public CraftParticleRegistry(IRegistry<net.minecraft.core.particles.Particle<?>> minecraftRegistry) {
            super(CraftParticle.class, minecraftRegistry, null);
        }

        @Override
        public CraftParticle<?> createBukkit(NamespacedKey namespacedKey, net.minecraft.core.particles.Particle<?> particle) {
            if (particle == null) {
                return null;
            }

            BiFunction<NamespacedKey, net.minecraft.core.particles.Particle<?>, CraftParticle<?>> function = PARTICLE_MAP.getOrDefault(namespacedKey, VOID_FUNCTION);

            return function.apply(namespacedKey, particle);
        }
    }
}
