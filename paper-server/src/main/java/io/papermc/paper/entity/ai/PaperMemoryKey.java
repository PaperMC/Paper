package io.papermc.paper.entity.ai;

import com.destroystokyo.paper.entity.PaperPathfinder;
import com.google.common.primitives.Primitives;
import io.leangen.geantyref.GenericTypeReflector;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import io.papermc.paper.registry.typed.PaperTypedDataCollector;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.util.converter.Converter;
import io.papermc.paper.util.converter.Converters;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftAgeable;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftHoglin;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.craftbukkit.entity.CraftPiglinAbstract;
import org.bukkit.craftbukkit.util.CraftLocation;

import static io.papermc.paper.util.converter.Converter.direct;
import static io.papermc.paper.util.converter.Converters.entity;
import static io.papermc.paper.util.converter.Converters.wrapper;

public class PaperMemoryKey<A, M> extends HolderableBase<MemoryModuleType<M>> implements MemoryKey {

    @SuppressWarnings("RedundantTypeArguments")
    private static final PaperTypedDataAdapters<MemoryModuleType<?>> ADAPTERS = PaperTypedDataAdapters.<MemoryModuleType<?>, PaperTypedDataCollector<MemoryModuleType<?>>>create(
        BuiltInRegistries.MEMORY_MODULE_TYPE,
        PaperTypedDataCollector::new,
        collector -> {
            final ClassValue<Converter<?, ?>> converters = new ClassValue<>() {
                @Override
                protected Converter<?, ?> computeValue(final Class<?> type) {
                    if (Primitives.isWrapperType(type) || type == UUID.class) {
                        return Converter.identity();
                    }
                    if (type == Unit.class) {
                        return Converters.unvalued();
                    }
                    if (type == GlobalPos.class) {
                        return direct(CraftLocation::fromGlobalPos, CraftLocation::toGlobalPos);
                    }
                    if (type == Entity.class) {
                        return entity(Entity.class, CraftEntity.class);
                    }
                    if (type == LivingEntity.class) {
                        return entity(LivingEntity.class, CraftLivingEntity.class);
                    }
                    if (type == Mob.class) {
                        return entity(Mob.class, CraftMob.class);
                    }
                    if (type == AgeableMob.class) {
                        return entity(AgeableMob.class, CraftAgeable.class);
                    }
                    if (type == ItemEntity.class) {
                        return entity(ItemEntity.class, CraftItem.class);
                    }
                    if (type == Hoglin.class) {
                        return entity(Hoglin.class, CraftHoglin.class);
                    }
                    if (type == AbstractPiglin.class) {
                        return entity(AbstractPiglin.class, CraftPiglinAbstract.class);
                    }
                    if (type == Player.class) {
                        return entity(Player.class, CraftHumanEntity.class);
                    }
                    if (type == Path.class) {
                        return direct(
                            path -> new PaperPathfinder.PaperPathResult(path, null),
                            PaperPathfinder.PaperPathResult::getHandle
                        );
                    }
                    if (type == BlockPos.class) {
                        return direct(MCUtil::toPosition, MCUtil::toBlockPos);
                    }
                    if (type == DamageSource.class) {
                        return wrapper(CraftDamageSource::new);
                    }
                    if (type == Vec3.class) {
                        return direct(MCUtil::toPosition, MCUtil::toVec3);
                    }
                    return null;
                }
            };

            collector.dispatchClass(valueType -> {
                final Class<?> valueClass = GenericTypeReflector.erase(valueType);
                if (valueType instanceof ParameterizedType paramType) {
                    Converter<?, ?> elementConverter = converters.get((Class<?>) paramType.getActualTypeArguments()[0]);
                    if (elementConverter == null) {
                        return Converters.unimplemented();
                    }
                    if (List.class.isAssignableFrom(valueClass)) {
                        return elementConverter.list();
                    }
                    if (Set.class.isAssignableFrom(valueClass)) {
                        return elementConverter.set();
                    }
                    if (Collection.class.isAssignableFrom(valueClass)) {
                        return elementConverter.collection();
                    }
                }

                return Objects.requireNonNullElseGet(converters.get(valueClass), Converters::unimplemented);
            }).scanConstants(MemoryModuleType.class, field -> (MemoryModuleType<?>) field.get(null), (type, field) -> {
                if (field.getGenericType() instanceof ParameterizedType paramType) {
                    Type[] args = paramType.getActualTypeArguments();
                    if (args.length == 1) {
                        return args[0];
                    }
                }
                return null;
            });
            //collector.register(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, fromApi, toApi);
            //collector.register(MemoryModuleType.WALK_TARGET, fromApi, toApi);
            //collector.register(MemoryModuleType.LOOK_TARGET, fromApi, toApi);
            //collector.register(MemoryModuleType.SPEAR_STATUS, fromApi, toApi);
        }
    );

    private final Converter<M, A> converter;

    private PaperMemoryKey(final Holder<MemoryModuleType<M>> holder, final Converter<M, A> converter) {
        super(holder);
        this.converter = converter;
    }

    public Converter<M, A> getConverter() {
        return this.converter;
    }

    @SuppressWarnings("unchecked")
    public static <M> MemoryKey of(final Holder<?> holder) {
        final Holder.Reference<MemoryModuleType<M>> reference = (Holder.Reference<MemoryModuleType<M>>) holder;
        final Converter<M, ?> converter = PaperMemoryKey.ADAPTERS.get(reference.key());
        if (converter == Converters.unimplemented()) {
            return new Unimplemented<>(reference, converter);
        } else if (converter == Converters.unvalued()) {
            return new NonValuedImpl<>(reference, converter);
        } else {
            return new ValuedImpl<>(reference, converter);
        }
    }

    public static final class NonValuedImpl<A, M> extends PaperMemoryKey<A, M> implements MemoryKey.NonValued {

        NonValuedImpl(
            final Holder<MemoryModuleType<M>> holder,
            final Converter<M, A> converter
        ) {
            super(holder, converter);
        }
    }

    public static final class ValuedImpl<A, M> extends PaperMemoryKey<A, M> implements MemoryKey.Valued<A> {

        ValuedImpl(
            final Holder<MemoryModuleType<M>> holder,
            final Converter<M, A> converter
        ) {
            super(holder, converter);
        }
    }

    public static final class Unimplemented<A, M> extends PaperMemoryKey<A, M> {

        public Unimplemented(
            final Holder<MemoryModuleType<M>> holder,
            final Converter<M, A> converter
        ) {
            super(holder, converter);
        }
    }

    public static MemoryKey minecraftToBukkit(MemoryModuleType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MEMORY_MODULE_TYPE);
    }

    public static MemoryModuleType<?> bukkitToMinecraft(MemoryKey bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }
}
