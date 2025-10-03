package io.papermc.generator.rewriter.types.simple;

import com.google.gson.internal.Primitives;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.ClassHelper;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import static io.papermc.generator.utils.Formatting.quoted;

@Deprecated
public class MemoryKeyRewriter extends RegistryFieldRewriter<MemoryModuleType<?>> {

    private static final Map<ResourceKey<MemoryModuleType<?>>, Class<?>> MEMORY_GENERIC_TYPES = RegistryEntries.byRegistryKey(Registries.MEMORY_MODULE_TYPE).getFields(field -> {
        if (field.getGenericType() instanceof ParameterizedType complexType && complexType.getActualTypeArguments().length == 1) {
            return ClassHelper.eraseType(complexType.getActualTypeArguments()[0]);
        }
        return null;
    });

    public MemoryKeyRewriter() {
        super(Registries.MEMORY_MODULE_TYPE, null);
    }

    // this api is not implemented and is not backed by a proper registry
    private static final Set<Class<?>> IGNORED_TYPES = Set.of(
        NearestVisibleLivingEntities.class,
        WalkTarget.class,
        PositionTracker.class,
        Path.class,
        DamageSource.class,
        Vec3.class,
        BlockPos.class,
        Unit.class,
        Void.class
    );

    private static final Set<Class<?>> IGNORED_SUB_TYPES = Set.of(
        Iterable.class,
        Map.class,
        Entity.class
    );

    private static final Map<Class<?>, Class<?>> API_BRIDGE = Map.of(
        GlobalPos.class, Location.class
    );

    private static final Map<String, String> FIELD_RENAMES = Map.of(
        "LIKED_NOTEBLOCK", "LIKED_NOTEBLOCK_POSITION"
    );

    @Override
    protected boolean canPrintField(Holder.Reference<MemoryModuleType<?>> reference) {
        Class<?> memoryType = MEMORY_GENERIC_TYPES.get(reference.key());
        if (IGNORED_TYPES.contains(memoryType)) {
            return false;
        }
        for (Class<?> subType : IGNORED_SUB_TYPES) {
            if (subType.isAssignableFrom(memoryType)) {
                return false;
            }
        }

        return true;
    }

    private @MonotonicNonNull Class<?> apiMemoryType;

    @Override
    protected String rewriteFieldType(Holder.Reference<MemoryModuleType<?>> reference) {
        Class<?> memoryType = MEMORY_GENERIC_TYPES.get(reference.key());

        if (!Primitives.isWrapperType(memoryType) && API_BRIDGE.containsKey(memoryType)) {
            this.apiMemoryType = API_BRIDGE.get(memoryType);
        } else {
            this.apiMemoryType = memoryType;
        }

        return "%s<%s>".formatted(this.registryEntry.apiClass().getSimpleName(), this.importCollector.getShortName(this.apiMemoryType));
    }

    @Override
    protected String rewriteFieldName(Holder.Reference<MemoryModuleType<?>> reference) {
        String keyedName = super.rewriteFieldName(reference);
        return FIELD_RENAMES.getOrDefault(keyedName, keyedName);
    }

    @Override
    protected String rewriteFieldValue(Holder.Reference<MemoryModuleType<?>> reference) {
        return "new %s<>(%s.minecraft(%s), %s.class)".formatted(
            this.registryEntry.apiClass().getSimpleName(),
            NamespacedKey.class.getSimpleName(),
            quoted(reference.key().location().getPath()),
            this.apiMemoryType.getSimpleName() // assume the type is already import (see above in rewriteFieldType)
        );
    }
}
