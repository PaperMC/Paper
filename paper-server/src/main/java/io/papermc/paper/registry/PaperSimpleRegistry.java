package io.papermc.paper.registry;

import java.util.function.Predicate;
import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.Keyed;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperSimpleRegistry<T extends Enum<T> & Keyed, M> extends Registry.SimpleRegistry<T> {

    static Registry<EntityType> entityType() {
        return new PaperSimpleRegistry<>(EntityType.class, entity -> entity != EntityType.UNKNOWN, BuiltInRegistries.ENTITY_TYPE);
    }

    static Registry<Particle> particleType() {
        return new PaperSimpleRegistry<>(Particle.class, BuiltInRegistries.PARTICLE_TYPE);
    }

    static Registry<PotionType> potion() {
        return new PaperSimpleRegistry<>(PotionType.class, BuiltInRegistries.POTION);
    }

    private final net.minecraft.core.Registry<M> nmsRegistry;

    protected PaperSimpleRegistry(final Class<T> type, final net.minecraft.core.Registry<M> nmsRegistry) {
        super(type);
        this.nmsRegistry = nmsRegistry;
    }

    public PaperSimpleRegistry(final Class<T> type, final Predicate<T> predicate, final net.minecraft.core.Registry<M> nmsRegistry) {
        super(type, predicate);
        this.nmsRegistry = nmsRegistry;
    }
}
